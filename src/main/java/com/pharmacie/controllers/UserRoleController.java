package com.pharmacie.controllers;


import com.pharmacie.models.UserRole;
import com.pharmacie.services.UserRoleService;

import java.util.List;

public class UserRoleController {

    private UserRoleService userRoleService;

    public UserRoleController() {
        this.userRoleService = new UserRoleService();
    }

    // Méthode pour enregistrer une UserRole
    public void saveUserRole(UserRole userRole) {
        userRoleService.saveUserRole(userRole);
    }

    // Méthode pour récupérer une UserRole par son ID
    public UserRole getUserRoleById(int id) {
        return userRoleService.getUserRoleById(id);
    }

    // Méthode pour récupérer toutes les UserRoles
    public List<UserRole> getAllUserRoles() {
        return userRoleService.getAllUserRoles();
    }

    // Méthode pour mettre à jour une UserRole
    public void updateUserRole(UserRole userRole) {
        userRoleService.updateUserRole(userRole);
    }

    // Méthode pour supprimer une UserRole
    public void deleteUserRole(int id) {
        userRoleService.deleteUserRole(id);
    }
}
