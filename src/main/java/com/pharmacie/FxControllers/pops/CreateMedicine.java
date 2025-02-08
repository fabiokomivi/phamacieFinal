package com.pharmacie.FxControllers.pops;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;

import com.pharmacie.FxControllers.cards.MedicineCategoryCard;
import com.pharmacie.controllers.CategoryController;
import com.pharmacie.controllers.MedicineController;
import com.pharmacie.models.Category;
import com.pharmacie.models.Medicine;
import com.pharmacie.utilities.Dialogs;
import com.pharmacie.utilities.OneSelectable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CreateMedicine implements Initializable, OneSelectable{

    MedicineController medicineController = new MedicineController();
    CategoryController categoryController = new CategoryController();

    Medicine medicine = null;
    String imagePath = "";

    @FXML
    private VBox categoriesBox;

    @FXML
    private TextField medicineName;

    @FXML
    private TextField medicinePrice;

    @FXML
    private TextField medicineThreshold;


    @FXML
    private VBox root;

    @FXML
    private ImageView medicineImage;

    @FXML
    void importImage(ActionEvent event) {
        choisirImage();
    }

    @FXML
    void onClose(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    void onValidate(ActionEvent event) {
        if (medicineName.getText().trim().isEmpty()) {
            Dialogs.showSimpleMessage("veuillez specifier le nom du produit");
            return;
        } else if (medicinePrice.getText().trim().isEmpty()){
            Dialogs.showSimpleMessage("veuillez specifier le prix du produit");
            return;
        } else{
            try {
                Double.valueOf(medicinePrice.getText().trim());
            }
            catch (Exception e) {
                Dialogs.showSimpleMessage("prix invalide");
                return;
            }

            try {
                Integer.valueOf(medicineThreshold.getText().trim());
            }
            catch (Exception e) {
                Dialogs.showSimpleMessage("seuil invalide");
                return;
            }
        }
        
        String name = medicineName.getText().trim();
        double price = Double.valueOf(medicinePrice.getText().trim());
        int threshold = Integer.valueOf(medicineThreshold.getText().trim());

        MedicineCategoryCard selectedMedicineCategoryCard = null;

        for (Node node : categoriesBox.getChildren()) {
            MedicineCategoryCard controller = (MedicineCategoryCard) node.getUserData();
            if(controller.isChecked()) {
                selectedMedicineCategoryCard = controller;
                break;
            }
        }

        if( selectedMedicineCategoryCard == null) {
            Dialogs.showSimpleMessage("veuillez selectionner une categorie pour le produit");
            return;
        }

        if (medicine == null) {
            medicine = new Medicine(name, price);
            medicine.setImage(imagePath);
            medicine.setThreshold(threshold);
            medicine.setCategory(selectedMedicineCategoryCard.getCategory());
            medicineController.saveMedicine(medicine);
        } else {
            medicine.setName(name);
            medicine.setPrice(price);
            medicine.setThreshold(threshold);
            medicine.setImage(imagePath);
            medicine.getCategory().removeMedicine(medicine);
            medicine.setCategory(selectedMedicineCategoryCard.getCategory());
            medicineController.updateMedicine(medicine);
        }

        closeWindow(event);
    }

    private void loadCategories() throws IOException {

        List<Category> categories = categoryController.getAllCategories();

        if (!categories.isEmpty())
            for (Category category : categories){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/medicineCategorie.fxml"));
                Parent root = loader.load();

                MedicineCategoryCard controller = loader.getController();

                controller.setData(category);
                controller.setOnCategorySelectedListener(this);

                root.setUserData(controller);

                categoriesBox.getChildren().add(root);
            }
    }

    public void setData(Medicine medicine) {
        this.medicine = medicine;
        medicineName.setText(medicine.getName());
        medicinePrice.setText(String.valueOf(medicine.getPrice()));
        medicineThreshold.setText(String.valueOf(medicine.getThreshold()));

        for (Node node : categoriesBox.getChildren()) {
            MedicineCategoryCard medicineCategoryCard = (MedicineCategoryCard) node.getUserData();
            if (medicineCategoryCard.getCategory().hasMedicine(medicine))
            medicineCategoryCard.check();
        }
    }

    public Medicine getMedicine() {
        return medicine;
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadCategories();
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    @Override
    public void onSelection(Object selectedCard) {
        // Parcourir toutes les cartes et décocher celles qui ne sont pas sélectionnées
        for (Node node : categoriesBox.getChildren()) {
            MedicineCategoryCard medicineCategoryCard = (MedicineCategoryCard) node.getUserData();
            if (medicineCategoryCard != selectedCard) {
                medicineCategoryCard.uncheck();
            }
        }
    }

    private void choisirImage() {
        // Configurer le FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer une image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"),
            new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
    
        try {
            // Récupérer le dossier depuis les ressources
            URL resourceURL = getClass().getResource("/com/pharmacie/images/medicines");
            if (resourceURL != null) {
                File initialDirectory = new File(resourceURL.toURI());
                if (initialDirectory.exists() && initialDirectory.isDirectory()) {
                    fileChooser.setInitialDirectory(initialDirectory);
                } else {
                    System.err.println("Le répertoire initial spécifié n'existe pas ou n'est pas valide.");
                }
            } else {
                System.err.println("Impossible de localiser le dossier des ressources.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la définition du répertoire initial : " + e.getMessage());
        }
    
        // Ouvrir la boîte de dialogue pour sélectionner un fichier
        File selectedFile = fileChooser.showOpenDialog(medicineImage.getScene().getWindow());
    
        if (selectedFile != null) {
            try {
                // Définir le dossier cible dans l'application
                Path targetDirectory = Path.of("images"); // Dossier relatif dans votre projet
                if (!Files.exists(targetDirectory)) {
                    Files.createDirectories(targetDirectory); // Créer le dossier s'il n'existe pas
                }
    
                // Copier le fichier dans le dossier cible
                Path targetPath = targetDirectory.resolve(selectedFile.getName());
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    
                // Mettre à jour le chemin relatif de l'image
                imagePath = selectedFile.getName();
                System.out.println(imagePath);
    
                // Charger et afficher l'image dans l'ImageView
                Image image = new Image(targetPath.toUri().toString());
                medicineImage.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Erreur lors du traitement de l'image : " + e.getMessage());
            }
        }
    }
    
    
}


