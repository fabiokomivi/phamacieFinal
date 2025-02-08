package com.pharmacie.services;

import com.pharmacie.dao.RoleDAO;
import com.pharmacie.models.Role;

import java.util.List;

public class RoleService {

    private RoleDAO roleDAO;

    public RoleService() {
        this.roleDAO = new RoleDAO();
    }

    // Méthode pour enregistrer un rôle
    public void saveRole(Role role) {
        roleDAO.save(role);
    }

    // Méthode pour récupérer un rôle par son ID
    public Role getRoleById(int id) {
        return roleDAO.getById(id);
    }

    // Méthode pour récupérer tous les rôles
    public List<Role> getAllRoles() {
        return roleDAO.getAll();
    }

    // Méthode pour mettre à jour un rôle
    public void updateRole(Role role) {
        roleDAO.update(role);
    }

    // Méthode pour supprimer un rôle par son ID
    public void deleteRole(int id) {
        roleDAO.delete(id);
    }
}
