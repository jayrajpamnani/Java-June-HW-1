package com.example.user.service;

import com.example.user.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    User getUserByEmail(String email);
}
