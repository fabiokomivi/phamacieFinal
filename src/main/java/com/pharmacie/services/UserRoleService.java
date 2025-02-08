package com.pharmacie.services;


import com.pharmacie.dao.UserRoleDAO;
import com.pharmacie.models.UserRole;

import java.util.List;

public class UserRoleService {

    private UserRoleDAO userRoleDAO;

    public UserRoleService() {
        this.userRoleDAO = new UserRoleDAO();
    }

    // Méthode pour enregistrer une UserRole
    public void saveUserRole(UserRole userRole) {
        userRoleDAO.save(userRole);
    }

    // Méthode pour récupérer une UserRole par son ID
    public UserRole getUserRoleById(int id) {
        return userRoleDAO.getById(id);
    }

    // Méthode pour récupérer toutes les UserRoles
    public List<UserRole> getAllUserRoles() {
        return userRoleDAO.getAll();
    }

    // Méthode pour mettre à jour une UserRole
    public void updateUserRole(UserRole userRole) {
        userRoleDAO.update(userRole);
    }

    // Méthode pour supprimer une UserRole
    public void deleteUserRole(int id) {
        userRoleDAO.delete(id);
    }
}
