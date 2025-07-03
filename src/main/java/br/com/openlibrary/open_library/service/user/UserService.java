package br.com.openlibrary.open_library.service.user;

import br.com.openlibrary.open_library.dto.page.PageDto;
import br.com.openlibrary.open_library.dto.user.UserDto;
import br.com.openlibrary.open_library.dto.user.UserUpdateDto;
import br.com.openlibrary.open_library.model.User;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    /**
     * Create a new user
     *
     * @param user the user to be created
     * @return the created user
     */
    User createUser(User user);


    /**
     * Finds all users in the database
     *
     * @param pageable the pagination object
     * @return a page of all users
     */
    PageDto<UserDto> findAllUsers(Pageable pageable);

    /**
     * Finds a user by their id
     *
     * @param id the user's id
     * @return an optional user if the user exists
     */
    Optional<User> findById(Long id);

    /**
     * Updates an existing user with new information.
     *
     * @param id the ID of the user to be updated
     * @param userDTO the data transfer object containing updated user information
     * @return an optional containing the updated user if the user exists, otherwise an empty optional
     */
    Optional<User> updateUser(Long id, UserUpdateDto userDTO);

    /**
     * Partially updates an existing user with new information. Only the fields that are
     * present in the provided UserUpdateDto are updated. If the user does not exist,
     * an empty optional is returned.
     *
     * @param id        the ID of the user to be updated
     * @param userDTO   the data transfer object containing updated user information
     * @return an optional containing the updated user if the user exists, otherwise an empty optional
     */
    Optional<User> partialUpdateUser(Long id, UserUpdateDto userDTO);

    boolean deleteUser(Long id);
}
