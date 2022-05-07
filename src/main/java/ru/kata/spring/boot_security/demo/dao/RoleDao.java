package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, Integer>  {
    List<Role> findAll();
    Role getRoleByName(String name);
}
