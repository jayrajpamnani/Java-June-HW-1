package com.example.user.controller;

import com.example.user.dto.UserRequestDto;
import com.example.user.dto.UserResponseDto;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto request) {
        UserResponseDto created = userService.createUser(request);
        URI location = URI.create("/api/users/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsers(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchByFullName(keyword));
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<UserResponseDto>> getInactiveUsers() {
        return ResponseEntity.ok(userService.getInactiveUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id, @Valid @RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}