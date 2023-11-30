package com.unibuc.music.services;

import com.unibuc.music.dtos.PostDto;
import com.unibuc.music.models.Post;
import com.unibuc.music.models.User;
import com.unibuc.music.models.Visibility;
import com.unibuc.music.repositories.PostRepository;
import com.unibuc.music.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Long uploadPost(PostDto postDto, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + userId + " doesn't exist!");
        }

        Post post = Post.builder()
                .description(postDto.getDescription())
                .creationTime(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .visibility(postDto.getVisibility() != null ? postDto.getVisibility(): Visibility.PUBLIC)
                .videoUrl(postDto.getVideoUrl())
                .build();

        return postRepository.save(post).getId();
    }
}
