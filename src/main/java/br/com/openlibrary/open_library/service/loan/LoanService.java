package br.com.openlibrary.open_library.service.loan;

import br.com.openlibrary.open_library.dto.loan.LoanCreateDTO;
import br.com.openlibrary.open_library.dto.loan.LoanResponseDTO;
import br.com.openlibrary.open_library.model.LoanStatus;

public interface LoanService {
    /**
     * Create a new loan. This method will validate the given loanCreateDTO,
     * create a new Loan entity and update the item quantity.
     *
     * @param loanCreateDTO the LoanCreateDTO with the data to create a new loan.
     * @return a LoanResponseDTO with the created Loan data.
     */
    LoanResponseDTO createLoan(LoanCreateDTO loanCreateDTO);
    /**
     * Updates the status of overdue loans to {@link LoanStatus#OVERDUE}. This
     * method is meant to be used by a scheduler to update the status of all
     * overdue loans periodically.
     */
    void updateOverdueLoansStatus();
    /**
     * Retrieves a loan by its unique identifier. This method will return a
     * LoanResponseDTO with the details of the loan if found. If the loan with the
     * specified id is not found, an IllegalArgumentException will be thrown.
     *
     * @param id the unique identifier of the loan to be retrieved.
     * @return a LoanResponseDTO containing the details of the loan if found.
     * @throws IllegalArgumentException if the loan with the specified id is not found.
     */
    LoanResponseDTO returnLoan(Long id);
}
