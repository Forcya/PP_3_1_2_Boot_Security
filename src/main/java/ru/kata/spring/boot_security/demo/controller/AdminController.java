package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;  //Чтобы не создавать объект используем Di
    @Autowired
    private RoleService roleService;

    @GetMapping
    public String showAllUsers(Model model) {

        model.addAttribute("allUsr", userService.getAllUsers());

        return "allUsers";
    }

    @GetMapping("/new")
    public String addNewUser(@ModelAttribute("user") User user, Model model) {
        //model.addAttribute("listRoles", roleService.findAllRoles());
        return "newUser";
    }

    @PostMapping
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(required=false) String roleAdmin,
                           @RequestParam(required=false) String roleUser) {
        setRolesFromRequest(user, roleAdmin, roleUser);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/update")
    public String updateUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "updateUser";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam(required=false) String roleAdmin,
                         @RequestParam(required=false) String roleUser) {
        setRolesFromRequest(user, roleAdmin, roleUser);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    private void setRolesFromRequest(@ModelAttribute("person") User user,
                                     @RequestParam(required = false) String roleAdmin,
                                     @RequestParam(required = false) String roleUser) {
        Set<Role> roles = new HashSet<>();
        if (roleAdmin != null) {
            roles.add(roleService.getRoleByName(roleAdmin));
        }
        if (roleUser != null) {
            roles.add(roleService.getRoleByName(roleUser));
        }
        user.setRoles(roles);
    }
}
