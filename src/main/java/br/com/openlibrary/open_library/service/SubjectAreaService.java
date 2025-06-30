package br.com.openlibrary.open_library.service;

import br.com.openlibrary.open_library.dto.page.PageDTO;
import br.com.openlibrary.open_library.dto.subject_area.SubjectAreaDTO;
import br.com.openlibrary.open_library.model.SubjectArea;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubjectAreaService {
    /**
     * Creates a new subject area.
     *
     * @param subjectAreaDTO the subject area to be created
     * @return the created subject area
     */
    SubjectArea createSubjectArea(SubjectAreaDTO subjectAreaDTO);


    /**
     * Finds all subject areas in the database.
     *
     * @param pageable the pagination object
     * @return a page of all subject areas
     */
    PageDTO<SubjectArea> findAllSubjectAreas(Pageable pageable);

    /**
     * Finds a subject area by its ID.
     *
     * @param id the ID of the subject area to find
     * @return an optional containing the subject area if found, otherwise an empty optional
     */
    Optional<SubjectArea> findSubjectAreaById(Long id);

    /**
     * Updates an existing subject area.
     *
     * @param id the ID of the subject area to be updated
     * @param subjectAreaDto the data transfer object containing updated subject area information
     * @return an optional containing the updated subject area if the subject area exists,
     *         otherwise an empty optional
     */
    Optional<SubjectArea> updateSubjectArea(Long id, SubjectAreaDTO subjectAreaDto);

    /**
     * Deletes a subject area by its ID.
     *
     * @param id the ID of the subject area to be deleted
     * @return true if the subject area was successfully deleted, false if the subject area was not found
     */
    boolean deleteSubjectArea(Long id);
}
