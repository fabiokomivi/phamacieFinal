package com.pharmacie.utilities;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BehaviorSetter {

    public static Stage ModalDraggableTransparent(Parent root) {
        final double[] xOffset = {0};
    final double[] yOffset = {0};

    // Rendre le contenu draggable
    root.setOnMousePressed(event -> {
        xOffset[0] = event.getSceneX();
        yOffset[0] = event.getSceneY();
    });

    root.setOnMouseDragged(event -> {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset[0]);
        stage.setY(event.getScreenY() - yOffset[0]);
    });

    // Créer une scène transparente
    Scene scene = new Scene(root);
    scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

    // Configurer un stage transparent
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initStyle(StageStyle.UNDECORATED);
    stage.initStyle(StageStyle.TRANSPARENT);

    // Associer la scène au stage
    stage.setScene(scene);

    return stage;
    }

}
