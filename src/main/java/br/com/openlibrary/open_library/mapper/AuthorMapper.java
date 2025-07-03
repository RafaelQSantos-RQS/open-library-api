package br.com.openlibrary.open_library.mapper;

import br.com.openlibrary.open_library.dto.author.AuthorDto;
import br.com.openlibrary.open_library.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto toDto(Author author);
}
