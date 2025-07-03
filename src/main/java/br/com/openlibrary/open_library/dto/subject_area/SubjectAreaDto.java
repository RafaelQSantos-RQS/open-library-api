package br.com.openlibrary.open_library.dto.subject_area;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubjectAreaDto(
        @NotBlank(message = "Name cannot be empty or null.")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters.")
        String name
) {
}
