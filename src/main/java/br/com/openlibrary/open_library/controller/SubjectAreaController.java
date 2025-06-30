package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.dto.page.PageDTO;
import br.com.openlibrary.open_library.dto.subject_area.SubjectAreaDTO;
import br.com.openlibrary.open_library.model.SubjectArea;
import br.com.openlibrary.open_library.service.SubjectAreaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/subject-area")
public class SubjectAreaController {
    private final SubjectAreaService subjectAreaService;

    @Autowired
    public SubjectAreaController(SubjectAreaService subjectAreaService) {
        this.subjectAreaService = subjectAreaService;
    }

    @PostMapping
    public ResponseEntity<SubjectArea> createSubjectArea(@Valid @RequestBody SubjectAreaDTO subjectAreaDTO) {
        SubjectArea subjectArea = subjectAreaService.createSubjectArea(subjectAreaDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(subjectArea.getId())
                .toUri();
        return ResponseEntity.created(location).body(subjectArea);
    }

    @GetMapping
    public ResponseEntity<PageDTO<SubjectArea>> getAllSubjectAreas(Pageable pageable) {
        PageDTO<SubjectArea> areasDto = subjectAreaService.findAllSubjectAreas(pageable);
        return ResponseEntity.ok(areasDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectArea> getSubjectAreaById(@PathVariable Long id) {
        return subjectAreaService.findSubjectAreaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectArea> updateSubjectArea(@PathVariable Long id, @Valid @RequestBody SubjectAreaDTO subjectAreaDto) {
        return subjectAreaService.updateSubjectArea(id, subjectAreaDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubjectArea(@PathVariable Long id) {
        boolean wasDeleted = subjectAreaService.deleteSubjectArea(id);
        if (wasDeleted) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        }
    }
}
