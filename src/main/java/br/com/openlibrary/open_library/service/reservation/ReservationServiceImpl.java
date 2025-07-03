package br.com.openlibrary.open_library.service.reservation;

import br.com.openlibrary.open_library.dto.loan.LoanResponseDto;
import br.com.openlibrary.open_library.dto.reservation.ReservationCreateDto;
import br.com.openlibrary.open_library.dto.reservation.ReservationResponseDto;
import br.com.openlibrary.open_library.mapper.LoanMapper;
import br.com.openlibrary.open_library.mapper.ReservationMapper;
import br.com.openlibrary.open_library.model.*;
import br.com.openlibrary.open_library.repository.ItemRepository;
import br.com.openlibrary.open_library.repository.LoanRepository;
import br.com.openlibrary.open_library.repository.ReservationRepository;
import br.com.openlibrary.open_library.repository.UserRepository;
import br.com.openlibrary.open_library.service.strategy.due_date.DueDateStrategy;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final LoanRepository loanRepository;
    private final ReservationMapper reservationMapper;
    private final LoanMapper loanMapper;
    private final DueDateStrategy studentStrategy;
    private final DueDateStrategy teacherStrategy;
    private final DueDateStrategy employeeStrategy;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            ItemRepository itemRepository,
            LoanRepository loanRepository,
            ReservationMapper reservationMapper,
            LoanMapper loanMapper,
            @Qualifier("studentStrategy") DueDateStrategy studentStrategy,
            @Qualifier("teacherStrategy") DueDateStrategy teacherStrategy,
            @Qualifier("employeeStrategy") DueDateStrategy employeeStrategy) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.loanRepository = loanRepository;
        this.reservationMapper = reservationMapper;
        this.loanMapper = loanMapper;
        this.studentStrategy = studentStrategy;
        this.teacherStrategy = teacherStrategy;
        this.employeeStrategy = employeeStrategy;
    }

    @Override
    @Transactional
    public ReservationResponseDto createReservation(ReservationCreateDto createDto) {
        // Check if user and item exist
        User user = userRepository.findById(createDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + createDto.userId()));
        Item item = itemRepository.findById(createDto.itemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + createDto.itemId()));

        // Business rules
        // Check if item is available
        if (item.getAvailableQuantity() > 0)
            throw new IllegalStateException("Cannot reserve an item that is currently available.");

        // Check if user already has an active or overdue loan for this item
        List<LoanStatus> activeLoanStatuses = List.of(LoanStatus.ACTIVE, LoanStatus.OVERDUE);
        if (loanRepository.existsByUserAndItemAndStatusIn(user, item, activeLoanStatuses))
            throw new IllegalStateException("Cannot reserve an item that is currently loaned.");

        // Check if user already has a reservation for this item
        if (reservationRepository.existsByUserAndItemAndStatus(user, item, ReservationStatus.ACTIVE))
            throw new IllegalStateException("User already has an active reservation for this item.");

        // Create reservation
        Reservation newReservation = new Reservation();
        newReservation.setUser(user);
        newReservation.setItem(item);
        newReservation.setStatus(ReservationStatus.ACTIVE);

        // Persist
        Reservation savedReservation = reservationRepository.save(newReservation);
        return reservationMapper.toResponseDto(savedReservation);
    }

    @Override
    public void expireOldReservations() {
        // Find all active reservations that are older than 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Reservation> oldReservations = reservationRepository.findAllByStatusAndCreatedAtBefore(ReservationStatus.ACTIVE, thirtyDaysAgo);

        // If there are no old reservations, do nothing
        if (oldReservations.isEmpty())
            return;

        // Update the status of all old reservations to EXPIRED
        oldReservations.forEach(reservation -> reservation.setStatus(ReservationStatus.EXPIRED));

        // Save all expired reservations
        reservationRepository.saveAll(oldReservations);
    }

    @Override
    @Transactional
    public LoanResponseDto fullfillReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + reservationId));

        User user = reservation.getUser();
        Item item = reservation.getItem();

        if (reservation.getStatus() != ReservationStatus.ACTIVE) throw new IllegalStateException("Reservation is not active and cannot be fulfilled.");
        if (item.getAvailableQuantity() <= 0) throw new IllegalStateException("Item is not available to be picked up.");

        reservation.setStatus(ReservationStatus.FULFILLED);
        reservationRepository.save(reservation);

        item.setAvailableQuantity(item.getAvailableQuantity() - 1);
        itemRepository.save(item);

        DueDateStrategy selectedStrategy = switch (user.getUserType()) {
            case STUDENT -> studentStrategy;
            case TEACHER -> teacherStrategy;
            case EMPLOYEE -> employeeStrategy;
            default -> throw new IllegalStateException("Unknown user type.");
        };
        LocalDate dueDate = selectedStrategy.calculateDueDate(LocalDate.now(), item);

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setItem(item);
        loan.setDueDate(dueDate);
        loan.setStatus(LoanStatus.ACTIVE);

        Loan savedLoan = loanRepository.save(loan);
        return loanMapper.toResponseDto(savedLoan);
    }
}
