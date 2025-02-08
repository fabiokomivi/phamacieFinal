package com.pharmacie.FxControllers.pops;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.pharmacie.FxControllers.cards.SupplierCategoryCard;
import com.pharmacie.controllers.CategoryController;
import com.pharmacie.controllers.SupplierCategoryController;
import com.pharmacie.controllers.SupplierController;
import com.pharmacie.models.Category;
import com.pharmacie.models.Supplier;
import com.pharmacie.models.SupplierCategory;
import com.pharmacie.utilities.Dialogs;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CreateSupplier implements Initializable{

    SupplierController supplierController = new SupplierController();
    CategoryController categoryController = new CategoryController();
    SupplierCategoryController supplierCategoryController = new SupplierCategoryController();

    private static final String EMAIL_REGEX = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    Supplier supplier = null;

    @FXML
    private VBox categoriesBox;

    @FXML
    private TextField contact;

    @FXML
    private TextField email;

    @FXML
    private TextField fullName;


    @FXML
    private VBox root;

    @FXML
    void onClose(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    void onValidate(ActionEvent event) {
        if (!checkedAll())
            return;
        if (supplier == null) {

            final String name = fullName.getText();
            final String supplierEmail = email.getText();
            final String supplierContact = contact.getText();

            supplier = new Supplier(name, supplierContact, supplierEmail);
            supplierController.saveSupplier(supplier);

            for (Node node : categoriesBox.getChildren()) {
                SupplierCategoryCard supplierCategoryCard = (SupplierCategoryCard) node.getUserData();
                if (supplierCategoryCard.isChecked()) {
                    SupplierCategory supplierCategory = new SupplierCategory();

                    supplier.addCategoryLink(supplierCategory);
                    supplierCategoryCard.getCategory().addSupplierLink(supplierCategory);

                    supplierCategoryController.saveSupplierCategory(supplierCategory);
                }
            }

        } else {
            List<SupplierCategory> toRemove = new ArrayList<>();

            for (Node node : categoriesBox.getChildren()) {
                SupplierCategoryCard supplierCategoryCard = (SupplierCategoryCard) node.getUserData();

                // Ajout de nouveaux liens
                if (supplierCategoryCard.isChecked() && !supplier.hasCategory(supplierCategoryCard.getCategory())) {
                    SupplierCategory supplierCategory = new SupplierCategory();

                    supplier.addCategoryLink(supplierCategory);
                    supplierCategoryCard.getCategory().addSupplierLink(supplierCategory);

                    supplierCategoryController.saveSupplierCategory(supplierCategory);
                }

                // Marquer les liens pour suppression
                else if (!supplierCategoryCard.isChecked() && supplier.hasCategory(supplierCategoryCard.getCategory())) {
                    for (SupplierCategory supplierCategory : supplier.getCategoriesLink()) {
                        if (supplierCategory.getCategory().getId() == supplierCategoryCard.getCategory().getId()) {
                            toRemove.add(supplierCategory);
                        }
                    }
                }
            }

            // Suppression des liens après l'itération
            for (SupplierCategory supplierCategory : toRemove) {
                
                // Supprimer l'entité dans la base de données
                supplierCategoryController.deleteSupplierCategory(supplierCategory.getId());

                //supplierController.updateSupplier(supplier);
                supplier = supplierController.refresh(supplier.getId());
            }
        }

        closeWindow(event);
    }
    

    void makeRed(TextField field) {
        // Appliquer un style rouge au TextField
        field.setStyle("-fx-border-color: red; -fx-background-color: red;");

        // Démarrer un temporisateur pour réinitialiser le style après 2 secondes
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            // Réinitialiser le style après 2 secondes
            field.setStyle("-fx-border-color: green;");
        });
        pause.play();
    }

    boolean checkedAll() {
        if (this.fullName.getText().isEmpty()) {
            makeRed(this.email);
            Dialogs.showSimpleMessage("veuillez remplir le nom");
            return false;
        }
        else if (this.email.getText().isEmpty() || !isValidEmail(this.email.getText())) {
            Dialogs.showSimpleMessage("email invalide");
            makeRed(this.email);
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setData(Supplier supplier) {

        this.supplier = supplier;

        fullName.setText(supplier.getName());
        contact.setText(supplier.getContact());
        email.setText(supplier.getEmail());

        for (Node node : categoriesBox.getChildren()) {
            SupplierCategoryCard supplierCategoryCard = (SupplierCategoryCard) node.getUserData();
            if (supplierCategoryCard.getCategory().hasSupplier(supplier))
                supplierCategoryCard.check();
        }
    }

    public Supplier getSupplier() {
        return this.supplier;
    }
    
    private void loadCategories() throws IOException {

        List<Category> categories = categoryController.getAllCategories();

        if (!categories.isEmpty())
            for (Category category : categories){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/fournisseurCategories.fxml"));
                    Parent root = loader.load();
    
                    SupplierCategoryCard controller = loader.getController();

                    controller.setData(category);
    
                    root.setUserData(controller);
    
                    categoriesBox.getChildren().add(root);
            }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadCategories();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
