package br.com.openlibrary.open_library.service.author;

import br.com.openlibrary.open_library.dto.author.AuthorCreateDto;
import br.com.openlibrary.open_library.dto.author.AuthorDto;
import br.com.openlibrary.open_library.dto.page.PageDto;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    /**
     * Creates a new author based on the given {@link AuthorCreateDto} and
     * returns a {@link AuthorDto} with the created author's data.
     *
     * @param createDto the {@link AuthorCreateDto} containing the data
     *                  to create a new author.
     * @return a {@link AuthorDto} with the created author's data.
     */
    AuthorDto createAuthor(AuthorCreateDto createDto);
    /**
     * Finds all authors in the database.
     *
     * @param pageable the pagination details.
     * @return a page of all authors.
     */
    PageDto<AuthorDto> findAllAuthors(Pageable pageable);
}
