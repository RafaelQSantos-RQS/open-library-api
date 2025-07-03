package br.com.openlibrary.open_library.service.reservation;

import br.com.openlibrary.open_library.dto.loan.LoanResponseDto;
import br.com.openlibrary.open_library.dto.reservation.ReservationCreateDto;
import br.com.openlibrary.open_library.dto.reservation.ReservationResponseDto;

public interface ReservationService {

    /**
     * Creates a new reservation for an unavailable item.
     *
     * @param createDto DTO containing the user and item IDs.
     * @return DTO of the created reservation.
     */
    ReservationResponseDto createReservation(ReservationCreateDto createDto);

    /**
     * Expires all reservations that are older than 30 days.
     * <p>
     * This method is meant to be called by a scheduler to periodically clean up
     * reservations that have expired.
     */
    void expireOldReservations();

    /**
     * Fullfills a reservation by creating a new loan for the reserved item.
     *
     * @param reservationId ID of the reservation to be fullfilled.
     * @return DTO of the created loan.
     */
    LoanResponseDto fullfillReservation(Long reservationId);
}
