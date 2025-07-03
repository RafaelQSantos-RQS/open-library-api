package br.com.openlibrary.open_library.service.reservation;

import br.com.openlibrary.open_library.dto.reservation.ReservationCreateDTO;
import br.com.openlibrary.open_library.dto.reservation.ReservationResponseDTO;

public interface ReservationService {

    /**
     * Creates a new reservation for an unavailable item.
     * @param createDto DTO containing the user and item IDs.
     * @return DTO of the created reservation.
     */
    ReservationResponseDTO createReservation(ReservationCreateDTO createDto);
    /**
     * Expires all reservations that are older than 30 days.
     * <p>
     * This method is meant to be called by a scheduler to periodically clean up
     * reservations that have expired.
     */
    void expireOldReservations();
}
