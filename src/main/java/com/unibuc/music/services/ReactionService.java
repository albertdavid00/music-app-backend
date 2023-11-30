package com.unibuc.music.services;

import com.unibuc.music.dtos.ReactionDto;
import com.unibuc.music.models.*;
import com.unibuc.music.repositories.CommentRepository;
import com.unibuc.music.repositories.PostRepository;
import com.unibuc.music.repositories.ReactionRepository;
import com.unibuc.music.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

@Service
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ReactionService(ReactionRepository reactionRepository, PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.reactionRepository = reactionRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public Long addReactionToPost(ReactionDto reactionDto, Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new NotFoundException("Post with id " + postId + " not found!"));
        Reaction reaction = Reaction.builder()
                .reactionType(reactionDto.getReactionType())
                .post(post)
                .user(user)
                .build();
        return reactionRepository.save(reaction).getId();
    }

    public Long addReactionToComment(ReactionDto reactionDto, Long commentId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundException("Comment with id " + commentId + " not found!"));
        Reaction reaction = Reaction.builder()
                .reactionType(reactionDto.getReactionType())
                .comment(comment)
                .user(user)
                .build();
        return reactionRepository.save(reaction).getId();
    }

    public void removeReaction(Long reactionId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found."));
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new NotFoundException("Reaction with id" + reactionId + " not found."));
        if (!reaction.getUser().getId().equals(userId) && !user.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("User cannot remove other user's reactions. Only admins are allowed!");
        }
        reactionRepository.delete(reaction);
    }
}
