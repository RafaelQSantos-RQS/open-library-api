package br.com.openlibrary.open_library.repository;

import br.com.openlibrary.open_library.model.FinePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface FinePolicyRepository extends JpaRepository<FinePolicy, Long> {
    // Busca a política mais recente com data de vigência anterior ou igual a hoje
    Optional<FinePolicy> findTopByEffectiveFromLessThanEqualOrderByEffectiveFromDesc(LocalDate date);
}
