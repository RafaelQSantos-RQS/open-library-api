package br.com.openlibrary.open_library.repository;

import br.com.openlibrary.open_library.model.SubjectArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectAreaRepository extends JpaRepository<SubjectArea, Long> {
    /**
     * Checks if a subject area with the specified name exists in the repository.
     *
     * @param name the name of the subject area to check for existence
     * @return true if a subject area with the specified name exists, false otherwise
     */
    boolean existsByName(String name);
}
