package com.pharmacie.services;


import com.pharmacie.dao.LoginDAO;
import com.pharmacie.models.Login;
import java.util.List;

public class LoginService {

    private LoginDAO loginDAO;

    public LoginService() {
        this.loginDAO = new LoginDAO();  // Instancier le DAO
    }

    // Enregistrer un login
    public void saveLogin(Login login) {
        loginDAO.save(login);
    }

    // Récupérer un login par ID
    public Login getLoginById(int id) {
        return loginDAO.getById(id);
    }

    // Récupérer tous les logins
    public List<Login> getAllLogins() {
        return loginDAO.getAll();
    }

    // Mettre à jour un login
    public void updateLogin(Login login) {
        loginDAO.update(login);
    }

    // Supprimer un login par ID
    public void deleteLogin(int id) {
        loginDAO.delete(id);
    }
}
