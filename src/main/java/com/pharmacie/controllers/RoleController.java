package com.pharmacie.controllers;



import com.pharmacie.models.Role;
import com.pharmacie.services.RoleService;

import java.util.List;

public class RoleController {

    private RoleService roleService;

    public RoleController() {
        this.roleService = new RoleService();
    }

    // Méthode pour enregistrer un rôle
    public void saveRole(Role role) {
        roleService.saveRole(role);
    }

    // Méthode pour récupérer un rôle par son ID
    public Role getRoleById(int id) {
        return roleService.getRoleById(id);
    }

    // Méthode pour récupérer tous les rôles
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    // Méthode pour mettre à jour un rôle
    public void updateRole(Role role) {
        roleService.updateRole(role);
    }

    // Méthode pour supprimer un rôle par son ID
    public void deleteRole(int id) {
        roleService.deleteRole(id);
    }
}
