package com.example.myitemsrest.repository;

import com.example.myitemsrest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Integer> {


}
