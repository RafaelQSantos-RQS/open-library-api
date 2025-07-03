package br.com.openlibrary.open_library.repository;

import br.com.openlibrary.open_library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    /**
     * Checks if an author with the given name exists in the repository.
     *
     * @param name the name of the author to check for existence
     * @return true if an author with the given name exists, false otherwise
     */
    boolean existsByName(String name);
}
