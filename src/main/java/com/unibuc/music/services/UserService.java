package com.unibuc.music.services;

import com.unibuc.music.dtos.RegisterDto;
import com.unibuc.music.dtos.UserDto;
import com.unibuc.music.models.Role;
import com.unibuc.music.models.User;
import com.unibuc.music.models.UserFollow;
import com.unibuc.music.repositories.UserFollowRepository;
import com.unibuc.music.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final UserFollowRepository userFollowRepository;

    @Autowired
    public UserService(UserRepository userRepository, KeycloakAdminService keycloakAdminService, UserFollowRepository userFollowRepository) {
        this.userRepository = userRepository;
        this.keycloakAdminService = keycloakAdminService;
        this.userFollowRepository = userFollowRepository;
    }

    @Transactional
    public Long register(RegisterDto registerDto) {
        Optional<User> inAppUser = userRepository.findByEmail(registerDto.getEmail());
        if (inAppUser.isPresent()) {
            throw new BadRequestException("User with email " + registerDto.getEmail() + " already exists!");
        }
        User newUser = User.builder()
                .email(registerDto.getEmail())
                .joinDateTime(Instant.now().truncatedTo(ChronoUnit.MINUTES))
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .age(registerDto.getAge())
                .role(Role.USER)
                .build();
        newUser = userRepository.save(newUser);

        keycloakAdminService.addUserToKeycloak(newUser, registerDto.getPassword(), String.valueOf(Role.USER));
        return newUser.getId();
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

    public Long followUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new NotFoundException("User with id " + followerId + " not found!"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new NotFoundException("User with id " + followingId + " not found!"));
        if (userFollowRepository.findByFollowerAndFollowing(follower, following).isPresent()) {
            throw new BadRequestException("User is already following this account.");
        }
        UserFollow userFollow = UserFollow.builder()
                .follower(follower)
                .following(following)
                .followTime(Instant.now().truncatedTo(ChronoUnit.MINUTES))
                .build();

        return userFollowRepository.save(userFollow).getId();
    }

    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new NotFoundException("User with id " + followerId + " not found!"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new NotFoundException("User with id " + followingId + " not found!"));
        UserFollow userFollow = userFollowRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new NotFoundException("Unable to complete operation. User with id "
                        + followerId + " is not following the user with the id " + followingId));

        userFollowRepository.delete(userFollow);
    }
}