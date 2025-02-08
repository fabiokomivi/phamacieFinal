package com.pharmacie.session;

import com.pharmacie.models.User;
import com.pharmacie.controllers.UserController;
import com.pharmacie.models.Login;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionUtil {

    private static UserController userController = new UserController();
    private static User currentUser;
    private static Login currentLogin;
    
    // Thread pool pour exécuter des tâches en arrière-plan
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Méthode synchrone pour obtenir l'utilisateur actuel (s'assurer que le thread n'est pas modifié en même temps)
    public synchronized static User getCurrentUser() {
        System.out.println(String.valueOf(currentUser.getPurchases().size()));
        return currentUser;
    }

    // Méthode pour mettre à jour l'utilisateur dans un thread séparé
    public static void updateCurrentUser() {
        executorService.submit(() -> {
            try {
                // Simuler la mise à jour de l'utilisateur
                currentUser = userController.getUserById(currentUser.getId());
                System.out.println("Utilisateur mis à jour : " + currentUser.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Méthode pour définir l'utilisateur actuel
    public synchronized static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Méthode pour obtenir la session de connexion actuelle
    public static Login getCurrentLogin() {
        return currentLogin;
    }

    // Méthode pour définir la session de connexion actuelle
    public static void setCurrentLogin(Login login) {
        currentLogin = login;
    }

    // Fermer correctement l'ExecutorService
    public static void shutdownExecutor() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
