package com.pharmacie.controllers;


import com.pharmacie.models.Login;
import com.pharmacie.services.LoginService;

import java.util.List;

public class LoginController {

    private LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();  // Instancier le service
    }

    // Ajouter un login
    public void addLogin(Login login) {
        loginService.saveLogin(login);
    }

    // Mettre à jour un login
    public void endLogin(Login login) {
        if (login != null) {
            login.setEndedAt();
            loginService.updateLogin(login);
        }
    }

    // Supprimer un login
    public void deleteLogin(int id) {
        loginService.deleteLogin(id);
    }

    // Afficher tous les logins
    public List<Login> getAllLogins() {
        return loginService.getAllLogins();
    }

    // Afficher un login par ID
    public Login getLoginById(int id) {
        return loginService.getLoginById(id);
    }
}
