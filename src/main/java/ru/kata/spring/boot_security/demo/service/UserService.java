package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public User getUser(int id) {
        return userDao.getById(id);
    }

    public void deleteUser(int id) {
        userDao.deleteById(id);
    }

    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); //У юзера получаем пароль, кодируем его и сетаем в поле пароль обратно
        userDao.save(user);
    }

    //Тот самый подходящий метод для поиска User-а по имени
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
}
