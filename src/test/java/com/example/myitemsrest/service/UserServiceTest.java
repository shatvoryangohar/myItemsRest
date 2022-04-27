package com.example.myitemsrest.service;

import com.example.myitemsrest.entity.Role;
import com.example.myitemsrest.entity.User;
import com.example.myitemsrest.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void beforeAll() {
        user = new User();
        user.setName("poxos");
        user.setSurname("poxosyan");
        user.setEmail("poxos@gmail.com");
        user.setPassword("poxos");
        user.setRole(Role.USER);
        user.setPhone("847368473");
    }

    @Test
    void save() {
        userService.save(user);
        assertEquals(1, userRepository.count());
        Optional<User> byId = userRepository.findById(user.getId());
        assertTrue(byId.isPresent());
        assertEquals("poxos", byId.get().getName());
        assertEquals("poxos@gmail.com", byId.get().getEmail());
    }

    @Test
    void deleteById() {
        userService.save(user);
        userService.deleteById(user.getId());
        Optional<User> byId = userRepository.findById(user.getId());
        assertFalse(byId.isPresent());
    }

    @Test
    void deleteById_notFound() {
        EmptyResultDataAccessException thrown = assertThrows(EmptyResultDataAccessException.class, () -> {
            userService.deleteById(999);
        });
        assertEquals("No class com.example.myitemsrest.entity.User entity with id 999 exists!", thrown.getMessage());
    }
}