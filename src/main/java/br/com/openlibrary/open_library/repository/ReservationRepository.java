package br.com.openlibrary.open_library.repository;

import br.com.openlibrary.open_library.model.Item;
import br.com.openlibrary.open_library.model.Reservation;
import br.com.openlibrary.open_library.model.ReservationStatus;
import br.com.openlibrary.open_library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository  extends JpaRepository<Reservation, Long> {
    /**
     * Checks if a reservation for the given user with the given item and status exists.
     *
     * @param user   the user to check for a reservation
     * @param item   the item to check for a reservation
     * @param status the status of the reservation to check for
     * @return true if a reservation exists, false otherwise
     */
    boolean existsByUserAndItemAndStatus(User user, Item item, ReservationStatus status);
    /**
     * Counts the number of reservations for the given item with the given status.
     *
     * @param item   the item to count reservations for
     * @param status the status of the reservations to count
     * @return the number of reservations
     */
    long countByItemAndStatus(Item item, ReservationStatus status);
    /**
     * Retrieves a list of all reservations with the given status created before the given date.
     *
     * @param status the status of the reservations to retrieve
     * @param date   the date to check if the reservations' creation date is before
     * @return the list of reservations
     */
    List<Reservation> findAllByStatusAndCreatedAtBefore(ReservationStatus status, LocalDateTime date);
}
