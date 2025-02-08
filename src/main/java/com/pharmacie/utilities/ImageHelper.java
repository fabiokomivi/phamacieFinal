package com.pharmacie.utilities;

import javafx.scene.image.Image;

public class ImageHelper {
    
    public Image loadImageFromResources(String imagePath) {
        try {
            // Obtenir l'URL de la ressource
            var resource = getClass().getResource("/com/pharmacie/images/medicines/" + imagePath);
    
            if (resource == null) {
                resource = getClass().getResource("/com/pharmacie/images/medicines/medicine.png");
            }

    
            // Charger l'image
            return new Image(resource.toString());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            return null;
        }
    }
    
}
