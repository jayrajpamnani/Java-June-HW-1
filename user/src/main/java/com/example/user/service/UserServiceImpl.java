package com.example.user.service;

import com.example.user.aop.LogExecutionTime;
import com.example.user.dto.UserRequestDto;
import com.example.user.dto.UserResponseDto;
import com.example.user.entity.User;
import com.example.user.exception.DuplicateResourceException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    @LogExecutionTime
    public UserResponseDto createUser(UserRequestDto request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(existing -> {
            throw new DuplicateResourceException(
                    "A user with username " + request.getUsername() + " already exists");
        });
        userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            throw new DuplicateResourceException(
                    "A user with email " + request.getEmail() + " already exists");
        });

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .registeredDate(request.getRegisteredDate())
                .active(request.getActive())
                .build();
        User saved = userRepository.save(user);
        return toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @LogExecutionTime
    public UserResponseDto getUserById(Long id) {
        User user = findUserOrThrow(id);
        return toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    @LogExecutionTime
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    @LogExecutionTime
    public List<UserResponseDto> searchByFullName(String keyword) {
        return userRepository.findByFullNameContainingIgnoreCase(keyword).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @LogExecutionTime
    public List<UserResponseDto> getInactiveUsers() {
        return userRepository.findInactiveUsers().stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    @LogExecutionTime
    public void deleteUser(Long id) {
        User user = findUserOrThrow(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    @LogExecutionTime
    public UserResponseDto updateUser(Long id, UserRequestDto request) {
        User user = findUserOrThrow(id);

        if (!user.getUsername().equals(request.getUsername())) {
            userRepository.findByUsername(request.getUsername()).ifPresent(existing -> {
                throw new DuplicateResourceException(
                        "A user with username " + request.getUsername() + " already exists");
            });
        }
        if (!user.getEmail().equals(request.getEmail())) {
            userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
                throw new DuplicateResourceException(
                        "A user with email " + request.getEmail() + " already exists");
            });
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFullName(request.getFullName());
        user.setRegisteredDate(request.getRegisteredDate());
        user.setActive(request.getActive());
        User updated = userRepository.save(user);
        return toResponseDto(updated);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserResponseDto toResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .registeredDate(user.getRegisteredDate())
                .active(user.getActive())
                .build();
    }
}
