package br.com.openlibrary.open_library.service.author;

import br.com.openlibrary.open_library.dto.author.AuthorCreateDto;
import br.com.openlibrary.open_library.dto.author.AuthorDto;
import br.com.openlibrary.open_library.dto.page.PageDto;
import br.com.openlibrary.open_library.mapper.AuthorMapper;
import br.com.openlibrary.open_library.model.Author;
import br.com.openlibrary.open_library.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorDto createAuthor(AuthorCreateDto createDto) {
        if (authorRepository.existsByName(createDto.name())) {
            throw new IllegalStateException("Author with this name already exists.");
        }
        Author newAuthor = new Author();
        newAuthor.setName(createDto.name());
        return authorMapper.toDto(authorRepository.save(newAuthor));
    }

    @Override
    public PageDto<AuthorDto> findAllAuthors(Pageable pageable) {
        Page<Author> authorPage = authorRepository.findAll(pageable);

        List<AuthorDto> authors = authorPage.getContent().stream()
                .map(authorMapper::toDto)
                .toList();

        return new PageDto<>(
                authors,
                authorPage.getNumber(),
                authorPage.getSize(),
                authorPage.getTotalElements(),
                authorPage.getTotalPages()
        );
    }
}