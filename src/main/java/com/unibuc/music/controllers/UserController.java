package com.unibuc.music.controllers;

import com.unibuc.music.dtos.RegisterDto;
import com.unibuc.music.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Contains endpoints for managing users.")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Sign up a new user into the application.")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping()
    @Operation(summary = "Returns a list of all users.")
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Returns the user with the specified id.")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }
}