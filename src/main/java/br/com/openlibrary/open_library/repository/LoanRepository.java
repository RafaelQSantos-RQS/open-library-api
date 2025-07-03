package br.com.openlibrary.open_library.repository;

import br.com.openlibrary.open_library.model.Item;
import br.com.openlibrary.open_library.model.Loan;
import br.com.openlibrary.open_library.model.LoanStatus;
import br.com.openlibrary.open_library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    /**
     * Returns the count of {@link Loan} for the given {@link User} with a status contained in the given list of {@link LoanStatus}.
     *
     * @param user     the {@link User}
     * @param statuses the list of {@link LoanStatus} to query
     * @return the count of {@link Loan}
     */
    Long countByUserAndStatusIn(User user, List<LoanStatus> statuses);
    /**
     * Checks if a {@link Loan} exists for the given {@link User} with the given item id and status in the given list of {@link LoanStatus}.
     * <p>
     * This is a convenience method that allows to check if a loan exists for the given user and item with any of the given statuses.
     * </p>
     *
     * @param user     the {@link User}
     * @param item     the {@link Item}
     * @param statuses the list of {@link LoanStatus} to query
     * @return true if a {@link Loan} exists, false otherwise
     */
    boolean existsByUserAndItemAndStatusIn(User user, Item item, List<LoanStatus> statuses);
    /**
     * Checks if a {@link Loan} exists for the given {@link User} with due date before the given date and status equal to the given {@link LoanStatus}.
     *
     * @param user   the {@link User}
     * @param today  the date to check if the due date is before
     * @param status the {@link LoanStatus} to query
     * @return true if a {@link Loan} exists, false otherwise
     */
    boolean existsByUserAndDueDateBeforeAndStatus(User user, LocalDate today, LoanStatus status);
    /**
     * Returns a list of all {@link Loan} with the given status and due date before the given date.
     *
     * @param status the {@link LoanStatus} to query
     * @param today  the date to check if the due date is before
     * @return the list of {@link Loan}
     */
    List<Loan> findAllByStatusAndDueDateBefore(LoanStatus status, LocalDate today);
}
