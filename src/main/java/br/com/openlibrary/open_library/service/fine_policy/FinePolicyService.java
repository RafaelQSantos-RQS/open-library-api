package br.com.openlibrary.open_library.service.fine_policy;

import java.math.BigDecimal;

public interface FinePolicyService {
    BigDecimal getCurrentFineAmountPerDay();
}