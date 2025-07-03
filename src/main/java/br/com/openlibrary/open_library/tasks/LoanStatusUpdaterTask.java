package br.com.openlibrary.open_library.tasks;

import br.com.openlibrary.open_library.service.loan.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoanStatusUpdaterTask {
    private static final Logger log = LoggerFactory.getLogger(LoanStatusUpdaterTask.class);

    private final LoanService loanService;

    public LoanStatusUpdaterTask(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * Scheduled job that updates the status of overdue loans daily at 00:05.
     * This task logs the start and completion of the update process.
     */
    @Scheduled(cron = "0 5 0 * * *") // Run every day at 00:05
    public void updateOverdueLoanStatusJob() {
        log.info("Scheduled Task Started: Updating overdue loan statuses at {}", LocalDateTime.now());
        loanService.updateOverdueLoansStatus();
        log.info("Scheduled Task Finished: Overdue loan statuses updated successfully.");
    }
}
