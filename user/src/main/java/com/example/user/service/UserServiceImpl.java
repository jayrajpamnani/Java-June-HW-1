package com.example.user.service;

import com.example.user.aop.LogExecutionTime;
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
    @Transactional(readOnly = true)
    @LogExecutionTime
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @LogExecutionTime
    public User getUserById(Long id) {
        return findUserOrThrow(id);
    }

    @Override
    @Transactional
    @LogExecutionTime
    public User createUser(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(existing -> {
            throw new DuplicateResourceException(
                    "A user with email " + user.getEmail() + " already exists");
        });
        return userRepository.save(user);
    }

    @Override
    @Transactional
    @LogExecutionTime
    public User updateUser(Long id, User user) {
        User existing = findUserOrThrow(id);

        if (!existing.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(user.getEmail()).ifPresent(found -> {
                throw new DuplicateResourceException(
                        "A user with email " + user.getEmail() + " already exists");
            });
        }

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        existing.setAge(user.getAge());
        return userRepository.save(existing);
    }

    @Override
    @Transactional
    @LogExecutionTime
    public void deleteUser(Long id) {
        User user = findUserOrThrow(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    @LogExecutionTime
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
