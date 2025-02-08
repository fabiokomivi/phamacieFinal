package com.pharmacie.controllers;


import com.pharmacie.models.User;
import com.pharmacie.services.UserService;
import com.pharmacie.Email.EmailService;

import java.util.List;

public class UserController {

    private UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    // Méthode pour enregistrer un utilisateur
    public void saveUser(User user) {
        userService.saveUser(user);
    }

    // Méthode pour récupérer un utilisateur par son ID
    public User getUserById(int id) {
        return userService.getUserById(id);
    }

    // Méthode pour récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Méthode pour mettre à jour un utilisateur
    public void updateUser(User user) {
        userService.updateUser(user);
    }

    // Méthode pour supprimer un utilisateur
    public void deleteUser(int id) {
        userService.deleteUser(id);
    }

    // Méthode pour récupérer un utilisateur par son email
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    public void recoveryPassword(User user) {
        String password = EmailService.generatePassword(6);
        try {
            EmailService.sendEmail(user, password);
            user.setPassword(password);
            this.updateUser(user);
        } catch (Exception e) {
            
        }
    }
}
