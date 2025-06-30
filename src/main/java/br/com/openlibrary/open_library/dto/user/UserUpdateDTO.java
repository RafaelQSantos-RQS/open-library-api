package br.com.openlibrary.open_library.dto.user;

import br.com.openlibrary.open_library.model.UserType;

public record UserUpdateDTO(
        String name,
        String registrationNumber,
        String course,
        String department,
        String phoneNumber,
        UserType userType
) {
}
