package com.unibuc.music.controllers;

import com.unibuc.music.dtos.ReactionDto;
import com.unibuc.music.services.ReactionService;
import com.unibuc.music.utils.KeycloakHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.unibuc.music.utils.HttpStatusUtility.successResponse;

@RestController
@RequestMapping("/reactions")
@Tag(name = "Reactions Controller", description = "Set of endpoints for reactions.")
public class ReactionController {
    private final ReactionService reactionService;

    @Autowired
    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping("/post/{postId}")
    @Operation(summary = "Add a reaction to a post.")
    public ResponseEntity<?> addReactionToPost(@Valid @RequestBody ReactionDto reactionDto, @PathVariable Long postId,
                                               Authentication authentication) {
        return new ResponseEntity<>(reactionService.addReactionToPost(reactionDto, postId, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }
    @PostMapping("/comment/{commentId}")
    @Operation(summary = "Add a reaction to a comment.")
    public ResponseEntity<?> addReactionToComment(@Valid @RequestBody ReactionDto reactionDto, @PathVariable Long commentId,
                                               Authentication authentication) {
        return new ResponseEntity<>(reactionService.addReactionToComment(reactionDto, commentId, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{reactionId}")
    @Operation(summary = "Remove reaction from an entity (post / comment).")
    public ResponseEntity<?> removeReaction(@PathVariable Long reactionId, Authentication authentication) {
        reactionService.removeReaction(reactionId, KeycloakHelper.getUserId(authentication));
        return successResponse();
    }
}
