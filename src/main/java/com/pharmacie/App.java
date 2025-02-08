package com.pharmacie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.pharmacie.controllers.UserController;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.pharmacie.controllers.RoleController;

import com.pharmacie.models.Role;
import com.pharmacie.util.HibernateUtil;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserController userController = new UserController();
        RoleController roleController = new RoleController();

        List<Role> roles = roleController.getAllRoles();
        if (roles.size()==0) {
            roleController.saveRole(new Role("root", null));
            roleController.saveRole(new Role("admin", null));
            roleController.saveRole(new Role("employe", null));
        }

        Parent root;

        // Charger le fichier FXML
        if (userController.getAllUsers().size()==0) 
            root = FXMLLoader.load(getClass().getResource("fxml/authentify/register.fxml"));
        else
            root = FXMLLoader.load(getClass().getResource("fxml/authentify/login.fxml"));

        // Créer une scène et l'attacher au stage
        Scene scene = new Scene(root, 1200, 600);

        // Configurer et afficher la fenêtre principale
        primaryStage.setTitle("Hello World Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Lance l'application JavaFX
    }

    @Override
    public void stop() {
        // Code de nettoyage ici
        System.out.println("Application arrêtée. Nettoyage en cours...");

        // Exemple : fermer les connexions Hibernate
        try {
            HibernateUtil.shutdown();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'arrêt des connexions Hibernate : " + e.getMessage());
        }

        // Arrêter proprement les threads
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executor.shutdownNow();
    }
}
