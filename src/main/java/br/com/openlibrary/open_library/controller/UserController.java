package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.dto.loan.LoanHistoryItemDto;
import br.com.openlibrary.open_library.dto.page.PageDto;
import br.com.openlibrary.open_library.dto.user.UserDto;
import br.com.openlibrary.open_library.dto.user.UserUpdateDto;
import br.com.openlibrary.open_library.model.User;
import br.com.openlibrary.open_library.service.loan.LoanService;
import br.com.openlibrary.open_library.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LoanService loanService;

    @Autowired
    public UserController(UserService userService, LoanService loanService) {
        this.userService = userService;
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(newUser);
    }

    @GetMapping
    public ResponseEntity<PageDto<UserDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDTO) {
        return userService.updateUser(id,userUpdateDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUserPartial(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDTO) {
        return userService.partialUpdateUser(id,userUpdateDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }

    @GetMapping("/{userId}/loans")
    public ResponseEntity<PageDto<LoanHistoryItemDto>> getLoansByUserId(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(loanService.findLoansByUserId(userId, pageable));
    }
}
