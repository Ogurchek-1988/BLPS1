package com.etoxto.lab1.auth.model;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User findByUsername(String username);
    void save(User user);
}
