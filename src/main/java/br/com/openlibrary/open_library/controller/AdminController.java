package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.service.loan.LoanService;
import br.com.openlibrary.open_library.service.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/tasks")
public class AdminController {

    private final LoanService loanService;
    private final ReservationService reservationService;

    public AdminController(LoanService loanService, ReservationService reservationService) {
        this.loanService = loanService;
        this.reservationService = reservationService;
    }

    @PostMapping("/update-overdue-loans")
    public ResponseEntity<String> triggerUpdateOverdueLoans() {
        loanService.updateOverdueLoansStatus();
        return ResponseEntity.ok("Overdue loans updated successfully");
    }

    @PostMapping("/expire-old-reservations")
    public ResponseEntity<String> triggerExpireOldReservations() {
        reservationService.expireOldReservations();
        return ResponseEntity.ok("Old reservations expired successfully");
    }
}
