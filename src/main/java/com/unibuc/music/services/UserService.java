package com.unibuc.music.services;

import com.unibuc.music.dtos.RegisterDto;
import com.unibuc.music.dtos.UserDto;
import com.unibuc.music.models.Role;
import com.unibuc.music.models.User;
import com.unibuc.music.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;

    @Autowired
    public UserService(UserRepository userRepository, KeycloakAdminService keycloakAdminService) {
        this.userRepository = userRepository;
        this.keycloakAdminService = keycloakAdminService;
    }

    @Transactional
    public void register(RegisterDto registerDto) {
        Optional<User> inAppUser = userRepository.findByEmail(registerDto.getEmail());
        if (inAppUser.isPresent()) {
            throw new BadRequestException("User with email " + registerDto.getEmail() + " already exists!");
        }
        User newUser = User.builder()
                .email(registerDto.getEmail())
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .age(registerDto.getAge())
                .role(Role.USER)
                .build();
        newUser = userRepository.save(newUser);

        keycloakAdminService.addUserToKeycloak(newUser, registerDto.getPassword(), String.valueOf(Role.USER));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .age(user.getAge())
                        .build())
                .collect(Collectors.toList());
    }
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .build();
    }
}