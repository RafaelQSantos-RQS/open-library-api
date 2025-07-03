package br.com.openlibrary.open_library.mapper;

import br.com.openlibrary.open_library.dto.subject_area.SubjectAreaDto;
import br.com.openlibrary.open_library.model.SubjectArea;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectAreaMapper {
    SubjectAreaDto toDto(SubjectArea subjectArea);
}
