package br.com.openlibrary.open_library.service.user;

import br.com.openlibrary.open_library.dto.page.PageDto;
import br.com.openlibrary.open_library.dto.user.UserDto;
import br.com.openlibrary.open_library.dto.user.UserUpdateDto;
import br.com.openlibrary.open_library.mapper.UserMapper;
import br.com.openlibrary.open_library.model.User;
import br.com.openlibrary.open_library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public PageDto<UserDto> findAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDto> userDtos = userPage.getContent().stream()
                .map(userMapper::toUserDTO)
                .toList();

        return new PageDto<>(
                userDtos,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages()
        );
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<User> updateUser(Long id, UserUpdateDto userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userDTO.name());
                    existingUser.setRegistrationNumber(userDTO.registrationNumber());
                    existingUser.setCourse(userDTO.course());
                    existingUser.setDepartment(userDTO.department());
                    existingUser.setPhoneNumber(userDTO.phoneNumber());
                    existingUser.setUserType(userDTO.userType());
                    return userRepository.save(existingUser);
                });
    }

    @Override
    @Transactional
    public Optional<User> partialUpdateUser(Long id, UserUpdateDto userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    userMapper.partialUpdate(existingUser, userDTO);
                    return userRepository.save(existingUser);
                });
    }

    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
