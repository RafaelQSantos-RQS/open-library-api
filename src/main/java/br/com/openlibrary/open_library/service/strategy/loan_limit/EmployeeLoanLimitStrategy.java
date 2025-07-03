package br.com.openlibrary.open_library.service.strategy.loan_limit;

import org.springframework.stereotype.Component;

@Component("employeeLoanLimitStrategy")
public class EmployeeLoanLimitStrategy implements LoanLimitStrategy{
    private final static int MAX_LOAN_COUNT = 5;
    @Override
    public void verifyLimit(Long activeLoanCount) {
        if (activeLoanCount >= MAX_LOAN_COUNT) {
            throw new IllegalArgumentException("Employee cannot have more than 5 active loans");
        }
    }
}
