package br.com.openlibrary.open_library.service.subject_area;

import br.com.openlibrary.open_library.dto.page.PageDto;
import br.com.openlibrary.open_library.dto.subject_area.SubjectAreaDto;
import br.com.openlibrary.open_library.model.SubjectArea;
import br.com.openlibrary.open_library.repository.SubjectAreaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubjectAreaServiceImpl implements SubjectAreaService {
    private final SubjectAreaRepository subjectAreaRepository;

    @Autowired
    public SubjectAreaServiceImpl(SubjectAreaRepository subjectAreaRepository) {
        this.subjectAreaRepository = subjectAreaRepository;
    }

    @Override
    @Transactional
    public SubjectArea createSubjectArea(SubjectAreaDto subjectAreaDTO) {
        if (subjectAreaRepository.existsByName(subjectAreaDTO.name())) {
            throw new IllegalArgumentException("Subject area with name " + subjectAreaDTO.name() + " already exists.");
        }
        SubjectArea subjectArea = new SubjectArea();
        subjectArea.setName(subjectAreaDTO.name());
        return subjectAreaRepository.save(subjectArea);
    }

    @Override
    public PageDto<SubjectArea> findAllSubjectAreas(Pageable pageable) {
        Page<SubjectArea> areas = subjectAreaRepository.findAll(pageable);
        return new PageDto<>(
                areas.getContent(),
                areas.getNumber(),
                areas.getSize(),
                areas.getTotalElements(),
                areas.getTotalPages()
        );
    }

    @Override
    public Optional<SubjectArea> findSubjectAreaById(Long id) {
        return subjectAreaRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<SubjectArea> updateSubjectArea(Long id, SubjectAreaDto subjectAreaDto) {
        return subjectAreaRepository.findById(id)
                .map(subjectArea -> {
                    subjectArea.setName(subjectAreaDto.name());
                    return subjectAreaRepository.save(subjectArea);
                });
    }

    @Override
    public boolean deleteSubjectArea(Long id) {
        if (subjectAreaRepository.existsById(id)) {
            subjectAreaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
