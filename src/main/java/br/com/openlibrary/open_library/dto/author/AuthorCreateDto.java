package br.com.openlibrary.open_library.dto.author;

import jakarta.validation.constraints.NotBlank;

public record AuthorCreateDto(
        @NotBlank(message = "Name cannot be blank")
        String name
) {
}
