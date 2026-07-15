package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByFullNameContainingIgnoreCase(String keyword);

    List<User> findByActive(Boolean active);

    List<User> findByRegisteredDateAfter(LocalDate date);

    @Query("SELECT u FROM User u WHERE u.active = false")
    List<User> findInactiveUsers();

    @Query(value = "SELECT * FROM users ORDER BY registered_date DESC LIMIT 1", nativeQuery = true)
    Optional<User> findMostRecentlyRegisteredUser();
}