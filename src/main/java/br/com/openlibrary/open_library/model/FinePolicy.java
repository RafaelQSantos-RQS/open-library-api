package br.com.openlibrary.open_library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fine_policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fine_amount_per_day", nullable = false)
    private BigDecimal fineAmountPerDay;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    public FinePolicy(BigDecimal bigDecimal, LocalDate now) {
        this.fineAmountPerDay = bigDecimal;
        this.effectiveFrom = now;
    }
}
