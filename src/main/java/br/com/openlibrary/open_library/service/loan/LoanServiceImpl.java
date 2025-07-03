package br.com.openlibrary.open_library.service.loan;

import br.com.openlibrary.open_library.dto.loan.LoanCreateDto;
import br.com.openlibrary.open_library.dto.loan.LoanHistoryItemDto;
import br.com.openlibrary.open_library.dto.loan.LoanResponseDto;
import br.com.openlibrary.open_library.dto.page.PageDto;
import br.com.openlibrary.open_library.mapper.LoanMapper;
import br.com.openlibrary.open_library.model.*;
import br.com.openlibrary.open_library.repository.*;
import br.com.openlibrary.open_library.service.fine_policy.FinePolicyService;
import br.com.openlibrary.open_library.service.strategy.due_date.DueDateStrategy;
import br.com.openlibrary.open_library.service.strategy.loan_limit.LoanLimitStrategy;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ReservationRepository reservationRepository;
    private final FinePolicyService finePolicyService;
    private final LoanMapper loanMapper;
    private final DueDateStrategy studentDueDateStrategy;
    private final DueDateStrategy teacherDueDateStrategy;
    private final DueDateStrategy employeeDueDateStrategy;
    private final LoanLimitStrategy studentLoanLimitStrategy;
    private final LoanLimitStrategy teacherLoanLimitStrategy;
    private final LoanLimitStrategy employeeLoanLimitStrategy;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository,
                           UserRepository userRepository,
                           ItemRepository itemRepository,
                           FinePolicyService finePolicyService,
                           LoanMapper loanMapper,
                           ReservationRepository reservationRepository,
                           @Qualifier("studentStrategy") DueDateStrategy studentDueDateStrategy,
                           @Qualifier("teacherStrategy") DueDateStrategy teacherDueDateStrategy,
                           @Qualifier("employeeStrategy") DueDateStrategy employeeDueDateStrategy,
                           @Qualifier("studentLoanLimitStrategy") LoanLimitStrategy studentLoanLimitStrategy,
                           @Qualifier("teacherLoanLimitStrategy") LoanLimitStrategy teacherLoanLimitStrategy,
                           @Qualifier("employeeLoanLimitStrategy") LoanLimitStrategy employeeLoanLimitStrategy) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.reservationRepository = reservationRepository;
        this.finePolicyService = finePolicyService;
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
    public LoanResponseDto createLoan(LoanCreateDto loanCreateDTO) {
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
        if (item.getAvailableQuantity() < 1)
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
        DueDateStrategy selectedStrategy = getDueDateStrategy(user);
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
    public LoanResponseDto returnLoan(Long loanId) {
        // Search for loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found! Please check the loan id."));

        // Check if loan is already returned
        if (loan.getStatus() == LoanStatus.RETURNED)
            throw new IllegalStateException("Loan is already returned!");

        // Calculate fine
        LocalDate returnDate = LocalDate.now();
        if (returnDate.isAfter(loan.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);

            BigDecimal finePerDay = finePolicyService.getCurrentFineAmountPerDay();

            BigDecimal totalFine = finePerDay.multiply(new BigDecimal(daysOverdue));
            loan.setFineAmount(totalFine);
        }

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

    @Override
    public PageDto<LoanHistoryItemDto> findLoansByUserId(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("User not found! Please check the user id.");

        Page<Loan> loanPage = loanRepository.findByUserId(userId, pageable);

        List<LoanHistoryItemDto> historyItemDTOS = loanPage
                .getContent()
                .stream()
                .map(loanMapper::toHistoryItemDto)
                .toList();

        return new PageDto<>(
                historyItemDTOS,
                loanPage.getNumber(),
                loanPage.getSize(),
                loanPage.getTotalElements(),
                loanPage.getTotalPages()
        );
    }

    @Override
    public Optional<LoanResponseDto> renewLoan(Long loanId) {
        return loanRepository.findById(loanId)
                .map(loan -> {
                    if (loan.getStatus() != LoanStatus.ACTIVE)
                        throw new IllegalStateException("Loan cannot be renewed because its status is not ACTIVE.");

                    long activeReservationCount = reservationRepository.countByItemAndStatus(loan.getItem(), ReservationStatus.ACTIVE);
                    if (activeReservationCount > 0)
                        throw new IllegalStateException("Loan cannot be renewed because there is an active reservation for this item.");

                    DueDateStrategy selectedStrategy = getDueDateStrategy(loan.getUser());

                    LocalDate newDueDate = selectedStrategy.calculateDueDate(LocalDate.now(),loan.getItem());
                    loan.setDueDate(newDueDate);

                    Loan savedLoan = loanRepository.save(loan);
                    return loanMapper.toResponseDto(savedLoan);
                });
    }

    private DueDateStrategy getDueDateStrategy(User loan) {
        DueDateStrategy selectedStrategy;
        switch (loan.getUserType()) {
            case STUDENT -> selectedStrategy = studentDueDateStrategy;
            case TEACHER -> selectedStrategy = teacherDueDateStrategy;
            case EMPLOYEE -> selectedStrategy = employeeDueDateStrategy;
            default -> throw new IllegalArgumentException("User type not supported!");
        }
        return selectedStrategy;
    }
}
