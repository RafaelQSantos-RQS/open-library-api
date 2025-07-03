package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.service.loan.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/tasks")
public class AdminController {

    private final LoanService loanService;

    public AdminController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/update-overdue-loans")
    public ResponseEntity<String> triggerUpdateOverdueLoans() {
        loanService.updateOverdueLoansStatus();
        return ResponseEntity.ok("Overdue loans updated successfully");
    }
}
