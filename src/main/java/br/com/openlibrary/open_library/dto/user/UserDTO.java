package br.com.openlibrary.open_library.dto.user;

import br.com.openlibrary.open_library.model.UserType;

public record UserDTO (
        Long id,
        String name,
        String registrationNumber,
        UserType userType
){
}
