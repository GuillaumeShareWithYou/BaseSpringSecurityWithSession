package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    User findByEmail(String login);
}
