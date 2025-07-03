package br.com.openlibrary.open_library.mapper;

import br.com.openlibrary.open_library.dto.user.UserDto;
import br.com.openlibrary.open_library.dto.user.UserUpdateDto;
import br.com.openlibrary.open_library.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Partially updates the fields of a User entity with values from a UserUpdateDto.
     * Only the fields present in the UserUpdateDto are updated; null values are ignored.
     *
     * @param user the User entity to be updated
     * @param userUpdateDTO the data transfer object containing updated user information
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget User user, UserUpdateDto userUpdateDTO);

    UserDto toUserDTO(User user);
}
