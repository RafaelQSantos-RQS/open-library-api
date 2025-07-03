package br.com.openlibrary.open_library.service.strategy.loan_limit;

import org.springframework.stereotype.Component;

@Component("teacherLoanLimitStrategy")
public class TeacherLoanLimitStrategy implements LoanLimitStrategy {
    private static final int MAX_LOAN_COUNT = 5;

    @Override
    public void verifyLimit(Long activeLoanCount) {
        if (activeLoanCount > MAX_LOAN_COUNT)
            throw new IllegalStateException("User has reached the maximum loan limit for teachers (" + MAX_LOAN_COUNT + " items)!");
    }
}
