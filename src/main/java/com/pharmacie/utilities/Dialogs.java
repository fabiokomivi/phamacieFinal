package com.pharmacie.utilities;

import java.io.IOException;

import com.pharmacie.FxControllers.messages.AllowMessage;
import com.pharmacie.FxControllers.messages.SimpleMessage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Dialogs {

    private Dialogs() {
        // Constructeur privé pour empêcher l'instanciation
    }

    // Méthode pour afficher un simple message
    public static void showSimpleMessage(String message) {
        try {
            FXMLLoader msgLoader = new FXMLLoader(Dialogs.class.getResource("/com/pharmacie/fxml/messages/simpleMessage.fxml"));
            Parent msgBox = msgLoader.load();
            SimpleMessage msgLoaderController = msgLoader.getController();
            msgLoaderController.setMessage(message);
            Stage msgStage = BehaviorSetter.ModalDraggableTransparent(msgBox);
            msgStage.showAndWait();
        } catch (IOException e) {
            
        }
    }

    // Méthode pour afficher un message de confirmation
    public static boolean showAllowMessage(String message) {
        try {
            FXMLLoader msgLoader = new FXMLLoader(Dialogs.class.getResource("/com/pharmacie/fxml/messages/allowMessage.fxml"));
            Parent root = msgLoader.load();
            AllowMessage allowMessage = msgLoader.getController();
            allowMessage.setMessage(message);
            Stage stage = BehaviorSetter.ModalDraggableTransparent(root);
            stage.showAndWait();
            return allowMessage.getPermission();
        } catch (IOException e) {
            return false;
        }
    }
}

