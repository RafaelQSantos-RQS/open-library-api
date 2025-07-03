package br.com.openlibrary.open_library.dto.item;

import br.com.openlibrary.open_library.model.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemCreateDTO(

        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotBlank(message = "Autho cannot be blank")
        String author,

        @NotNull(message = "Item type cannot be blank")
        ItemType itemType,

        @NotNull(message = "Subject area cannot be blank")
        @Positive(message = "Subject area id must be positive")
        Long subjectAreaId
) {
}
