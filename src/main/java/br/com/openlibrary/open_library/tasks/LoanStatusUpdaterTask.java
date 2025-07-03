package br.com.openlibrary.open_library.tasks;

import br.com.openlibrary.open_library.service.loan.LoanService;
import br.com.openlibrary.open_library.service.reservation.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoanStatusUpdaterTask {
    private static final Logger log = LoggerFactory.getLogger(LoanStatusUpdaterTask.class);

    private final LoanService loanService;
    private final ReservationService reservationService;

    public LoanStatusUpdaterTask(LoanService loanService, ReservationService reservationService) {
        this.reservationService = reservationService;
        this.loanService = loanService;
    }

    /**
     * Scheduled job that updates the status of overdue loans daily at 00:05.
     * This task logs the start and completion of the update process.
     */
    @Scheduled(cron = "0 5 0 * * *")
    public void updateOverdueLoanStatusJob() {
        log.info("Scheduled Task Started: Updating overdue loan statuses at {}", LocalDateTime.now());
        loanService.updateOverdueLoansStatus();
        log.info("Scheduled Task Finished: Overdue loan statuses updated successfully.");
    }
    /**
     * Scheduled job that expires old reservations daily at 00:15.
     * This task logs the start and completion of the reservation expiration process.
     */
    @Scheduled(cron = "0 15 0 * * *")
    public void expireOldReservationsJob() {
        log.info("Scheduled Task Started: Expiring old reservations...");
        reservationService.expireOldReservations();
        log.info("Scheduled Task Finished: Old reservations expired.");
    } // Run every day at 00:00

}
