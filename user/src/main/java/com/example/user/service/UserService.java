package com.example.user.service;

import com.example.user.dto.UserRequestDto;
import com.example.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto request);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    List<UserResponseDto> searchByFullName(String keyword);

    List<UserResponseDto> getInactiveUsers();

    void deleteUser(Long id);

    UserResponseDto updateUser(Long id, UserRequestDto request);
}
