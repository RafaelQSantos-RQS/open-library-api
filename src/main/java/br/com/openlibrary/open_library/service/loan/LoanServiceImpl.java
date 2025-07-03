package br.com.openlibrary.open_library.service.loan;

import br.com.openlibrary.open_library.dto.loan.LoanCreateDTO;
import br.com.openlibrary.open_library.dto.loan.LoanResponseDTO;
import br.com.openlibrary.open_library.mapper.LoanMapper;
import br.com.openlibrary.open_library.model.Item;
import br.com.openlibrary.open_library.model.Loan;
import br.com.openlibrary.open_library.model.LoanStatus;
import br.com.openlibrary.open_library.model.User;
import br.com.openlibrary.open_library.repository.ItemRepository;
import br.com.openlibrary.open_library.repository.LoanRepository;
import br.com.openlibrary.open_library.repository.UserRepository;
import br.com.openlibrary.open_library.service.strategy.due_date.DueDateStrategy;
import br.com.openlibrary.open_library.service.strategy.due_date.EmployeeDueDateStrategy;
import br.com.openlibrary.open_library.service.strategy.due_date.StudentDueDateStrategy;
import br.com.openlibrary.open_library.service.strategy.due_date.TeacherDueDateStrategy;
import br.com.openlibrary.open_library.service.strategy.loan_limit.LoanLimitStrategy;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final LoanMapper loanMapper;
    private final DueDateStrategy studentDueDateStrategy;
    private final DueDateStrategy teacherDueDateStrategy;
    private final DueDateStrategy employeeDueDateStrategy;
    private final LoanLimitStrategy studentLoanLimitStrategy;
    private final LoanLimitStrategy teacherLoanLimitStrategy;
    private final LoanLimitStrategy employeeLoanLimitStrategy;

    public LoanServiceImpl(LoanRepository loanRepository,
                           UserRepository userRepository,
                           ItemRepository itemRepository,
                           LoanMapper loanMapper,
                           @Qualifier("studentStrategy") StudentDueDateStrategy studentDueDateStrategy,
                           @Qualifier("teacherStrategy") TeacherDueDateStrategy teacherDueDateStrategy,
                           @Qualifier("employeeStrategy") EmployeeDueDateStrategy employeeDueDateStrategy,
                           @Qualifier("studentLoanLimitStrategy") LoanLimitStrategy studentLoanLimitStrategy,
                           @Qualifier("teacherLoanLimitStrategy") LoanLimitStrategy teacherLoanLimitStrategy,
                           @Qualifier("employeeLoanLimitStrategy") LoanLimitStrategy employeeLoanLimitStrategy) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.loanMapper = loanMapper;
        this.studentDueDateStrategy = studentDueDateStrategy;
        this.teacherDueDateStrategy = teacherDueDateStrategy;
        this.employeeDueDateStrategy = employeeDueDateStrategy;
        this.studentLoanLimitStrategy = studentLoanLimitStrategy;
        this.teacherLoanLimitStrategy = teacherLoanLimitStrategy;
        this.employeeLoanLimitStrategy = employeeLoanLimitStrategy;
    }

    @Override
    @Transactional
    public LoanResponseDTO createLoan(LoanCreateDTO loanCreateDTO) {
        // Search for user and item
        User user = userRepository.findById(loanCreateDTO.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found! Please check the user id."));

        Item item = itemRepository.findById(loanCreateDTO.itemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found! Please check the item id."));

        // Business rules
        // Item is already loaned by the user
        List<LoanStatus> activeOrOverdue = List.of(LoanStatus.ACTIVE, LoanStatus.OVERDUE);
        if (loanRepository.existsByUserAndItemAndStatusIn(user, item, activeOrOverdue))
            throw new IllegalStateException("User already has an active or overdue loan for this item!");

        // User has item overdued
        if (loanRepository.existsByUserAndDueDateBeforeAndStatus(user, LocalDate.now(), LoanStatus.OVERDUE))
            throw new IllegalStateException("User has an overdue loan!");

        // Item is not available to loan
        if (item.getAvailableQuantity() < 0)
            throw new IllegalArgumentException("No more items available for loan!");

        // User has reached the maximum loan limit
        Long activeLoanCount = loanRepository.countByUserAndStatusIn(user, activeOrOverdue);
        LoanLimitStrategy selectedLimitStrategy;
        switch (user.getUserType()) { // Select strategy based on user type
            case STUDENT -> selectedLimitStrategy = studentLoanLimitStrategy;
            case TEACHER -> selectedLimitStrategy = teacherLoanLimitStrategy;
            case EMPLOYEE -> selectedLimitStrategy = employeeLoanLimitStrategy;
            default -> throw new IllegalArgumentException("User type not supported!");
        }
        selectedLimitStrategy.verifyLimit(activeLoanCount);

        // Calculate due date
        DueDateStrategy selectedStrategy;
        switch (user.getUserType()) { // Select strategy based on user type
            case STUDENT -> selectedStrategy = studentDueDateStrategy;
            case TEACHER -> selectedStrategy = teacherDueDateStrategy;
            case EMPLOYEE -> selectedStrategy = employeeDueDateStrategy;
            default -> throw new IllegalArgumentException("User type not supported!");
        }
        LocalDate dueDate = selectedStrategy.calculateDueDate(LocalDate.now(),item);

        // Create loan Entity
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setItem(item);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(dueDate);
        loan.setStatus(LoanStatus.ACTIVE);

        // Update item quantity
        item.setAvailableQuantity(item.getAvailableQuantity() - 1);
        itemRepository.save(item);

        // Return loan response
        Loan savedLoan = loanRepository.save(loan);
        return loanMapper.toResponseDto(savedLoan);
    }

    @Override
    @Transactional
    public void updateOverdueLoansStatus() {
        // Find all active loans that are overdue
        List<Loan> overdueLoans = loanRepository.findAllByStatusAndDueDateBefore(LoanStatus.ACTIVE, LocalDate.now());

        // If there are no overdue loans, do nothing
        if (overdueLoans.isEmpty())
            return;

        // Set the status of all overdue loans to OVERDUE
        overdueLoans.forEach(loan -> loan.setStatus(LoanStatus.OVERDUE));

        // Save all overdue loans
        loanRepository.saveAll(overdueLoans);
    }

    @Override
    @Transactional
    public LoanResponseDTO returnLoan(Long loanId) {
        // Search for loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found! Please check the loan id."));

        // Check if loan is already returned
        if (loan.getStatus() == LoanStatus.RETURNED)
            throw new IllegalStateException("Loan is already returned!");

        // Update loan
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());

        // Update item
        Item item = loan.getItem();
        item.setAvailableQuantity(item.getAvailableQuantity() + 1);

        // Persist
        Loan savedLoan = loanRepository.save(loan);
        itemRepository.save(item);

        return loanMapper.toResponseDto(savedLoan);
    }
}
