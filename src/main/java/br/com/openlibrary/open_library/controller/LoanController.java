package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.dto.loan.LoanCreateDto;
import br.com.openlibrary.open_library.dto.loan.LoanResponseDto;
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
    public ResponseEntity<LoanResponseDto> createLoan(@RequestBody LoanCreateDto loanCreateDTO) {
        LoanResponseDto loanResponseDTO = loanService.createLoan(loanCreateDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(loanResponseDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(loanResponseDTO);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<LoanResponseDto> returnLoan(@PathVariable Long id) {
        LoanResponseDto loanResponseDTO = loanService.returnLoan(id);
        return ResponseEntity.ok(loanResponseDTO);
    }

    @PostMapping("/{id}/renew")
    public ResponseEntity<LoanResponseDto> renewLoan(@PathVariable Long id) {
        Optional<LoanResponseDto> loanResponseDTO = loanService.renewLoan(id);
        return loanResponseDTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
