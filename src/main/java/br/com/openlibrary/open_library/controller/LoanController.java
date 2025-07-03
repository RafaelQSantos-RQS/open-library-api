package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.dto.loan.LoanCreateDTO;
import br.com.openlibrary.open_library.dto.loan.LoanResponseDTO;
import br.com.openlibrary.open_library.service.loan.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/loans")
@AllArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanCreateDTO loanCreateDTO) {
        LoanResponseDTO loanResponseDTO = loanService.createLoan(loanCreateDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(loanResponseDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(loanResponseDTO);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<LoanResponseDTO> returnLoan(@PathVariable Long id) {
        LoanResponseDTO loanResponseDTO = loanService.returnLoan(id);
        return ResponseEntity.ok(loanResponseDTO);
    }

    @PostMapping("/{id}/renew")
    public ResponseEntity<LoanResponseDTO> renewLoan(@PathVariable Long id) {
        Optional<LoanResponseDTO> loanResponseDTO = loanService.renewLoan(id);
        return loanResponseDTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
