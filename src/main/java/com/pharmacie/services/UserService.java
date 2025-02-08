package com.pharmacie.services;


import com.pharmacie.dao.UserDAO;
import com.pharmacie.models.User;

import java.util.List;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // Méthode pour enregistrer un utilisateur
    public void saveUser(User user) {
        userDAO.save(user);
    }

    // Méthode pour récupérer un utilisateur par son ID
    public User getUserById(int id) {
        return userDAO.getById(id);
    }

    // Méthode pour récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    // Méthode pour mettre à jour un utilisateur
    public void updateUser(User user) {
        userDAO.update(user);
    }

    // Méthode pour supprimer un utilisateur
    public void deleteUser(int id) {
        userDAO.delete(id);
    }

    // Méthode pour récupérer un utilisateur par son email
    public User getUserByEmail(String email) {
        return userDAO.getByEmail(email);
    }
}
