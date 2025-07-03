package br.com.openlibrary.open_library.service.fine_policy;

import br.com.openlibrary.open_library.model.FinePolicy;
import br.com.openlibrary.open_library.repository.FinePolicyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class FinePolicyServiceImpl implements FinePolicyService {
    private final FinePolicyRepository finePolicyRepository;
    private static final BigDecimal DEFAULT_FINE = new BigDecimal("1.00");

    public FinePolicyServiceImpl(FinePolicyRepository finePolicyRepository) {
        this.finePolicyRepository = finePolicyRepository;
    }

    @Override
    public BigDecimal getCurrentFineAmountPerDay() {
        return finePolicyRepository.findTopByEffectiveFromLessThanEqualOrderByEffectiveFromDesc(LocalDate.now())
                .map(FinePolicy::getFineAmountPerDay)
                .orElse(DEFAULT_FINE);
    }
}
