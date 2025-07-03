package br.com.openlibrary.open_library.service.strategy.loan_limit;

public interface LoanLimitStrategy {
    /**
     * Verify if the given active loan count does not exceed the limit.
     *
     * @param activeLoanCount the number of active loans
     * @throws IllegalArgumentException if the number of active loans is greater
     *         than the limit
     */
    void verifyLimit(Long activeLoanCount);
}
