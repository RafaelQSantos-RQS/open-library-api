package br.com.openlibrary.open_library.service.reservation;

import br.com.openlibrary.open_library.dto.reservation.ReservationCreateDTO;
import br.com.openlibrary.open_library.dto.reservation.ReservationResponseDTO;
import br.com.openlibrary.open_library.mapper.ReservationMapper;
import br.com.openlibrary.open_library.model.*;
import br.com.openlibrary.open_library.repository.ItemRepository;
import br.com.openlibrary.open_library.repository.LoanRepository;
import br.com.openlibrary.open_library.repository.ReservationRepository;
import br.com.openlibrary.open_library.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final LoanRepository loanRepository;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            ItemRepository itemRepository,
            LoanRepository loanRepository,
            ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.loanRepository = loanRepository;
        this.reservationMapper = reservationMapper;
    }

    @Override
    @Transactional
    public ReservationResponseDTO createReservation(ReservationCreateDTO createDto) {
        // Check if user and item exist
        User user = userRepository.findById(createDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + createDto.userId()));
        Item item = itemRepository.findById(createDto.itemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + createDto.itemId()));

        // Business rules
        // Check if item is available
        if (item.getAvailableQuantity()>0)
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
}
