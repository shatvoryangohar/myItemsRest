package com.example.myitemsrest.repository;

import com.example.myitemsrest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);
}
