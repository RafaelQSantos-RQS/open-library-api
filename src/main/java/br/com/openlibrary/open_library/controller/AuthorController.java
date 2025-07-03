package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.dto.author.AuthorCreateDto;
import br.com.openlibrary.open_library.dto.author.AuthorDto;
import br.com.openlibrary.open_library.dto.page.PageDto;
import br.com.openlibrary.open_library.service.author.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorCreateDto createDto) {
        AuthorDto newAuthor = authorService.createAuthor(createDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newAuthor.id())
                .toUri();
        return ResponseEntity.created(location).body(newAuthor);
    }

    @GetMapping
    public ResponseEntity<PageDto<AuthorDto>> getAllAuthors(Pageable pageable) {
        return ResponseEntity.ok(authorService.findAllAuthors(pageable));
    }
}