package com.unibuc.music.controllers;

import com.unibuc.music.dtos.PostDto;
import com.unibuc.music.services.PostService;
import com.unibuc.music.utils.KeycloakHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/posts")
@Tag(name="Post Controller", description = "Set of endpoints for managing posts.")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @Operation(summary = "Upload a new post.")
    public ResponseEntity<?> uploadPost(@Valid @RequestBody PostDto postDto, Authentication authentication) {
        return new ResponseEntity<>(postService.uploadPost(postDto, KeycloakHelper.getUserId(authentication)), HttpStatus.CREATED);
    }
}
