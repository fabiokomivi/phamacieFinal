package com.pharmacie.FxControllers.pops;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.pharmacie.controllers.CategoryController;
import com.pharmacie.controllers.SupplierController;
import com.pharmacie.FxControllers.cards.CategorySupplierCard;
import com.pharmacie.models.Category;
import com.pharmacie.models.Supplier;
import com.pharmacie.models.SupplierCategory;
import com.pharmacie.utilities.Dialogs;
import com.pharmacie.controllers.SupplierCategoryController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateCategory implements Initializable{

    SupplierController supplierController = new SupplierController();
    CategoryController categoryController = new CategoryController();
    SupplierCategoryController supplierCategoryController = new SupplierCategoryController();


    Category category = null;

    @FXML
    private VBox categorySupplierBox;

    @FXML
    private TextArea description;

    @FXML
    private TextField name;

    @FXML
    void onClose(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    void onCreate(ActionEvent event) throws IOException {
        if (name.getText().trim().isEmpty()) {
            Dialogs.showSimpleMessage("veuillez specifier le nom de la categorie");
            return;
        }

        if (category == null) {
            String categoryName = name.getText().trim();
            String categoryDescription = description.getText().trim();
            category = new Category(categoryName, categoryDescription);
            categoryController.saveCategory(category);

            for (Node node : categorySupplierBox.getChildren()) {
                CategorySupplierCard categorySupplierCard = (CategorySupplierCard) node.getUserData();
                if (categorySupplierCard.isChecked()) {
                    SupplierCategory supplierCategory = new SupplierCategory();
                    category.addSupplierLink(supplierCategory);
                    categorySupplierCard.getSupplier().addCategoryLink(supplierCategory);
                    supplierCategoryController.saveSupplierCategory(supplierCategory);
                }
            }
        } else {
            List<SupplierCategory> toRemove = new ArrayList<>();

            for (Node node : categorySupplierBox.getChildren()) {
                CategorySupplierCard categorySupplierCard = (CategorySupplierCard) node.getUserData();

                // Ajout de nouveaux liens
                if (categorySupplierCard.isChecked() && !category.hasSupplier(categorySupplierCard.getSupplier())) {
                    SupplierCategory supplierCategory = new SupplierCategory();
                    category.addSupplierLink(supplierCategory);
                    categorySupplierCard.getSupplier().addCategoryLink(supplierCategory);
                    supplierCategoryController.saveSupplierCategory(supplierCategory);
                }

                // Marquer les liens pour suppression
                else if (!categorySupplierCard.isChecked() && category.hasSupplier(categorySupplierCard.getSupplier())) {
                    for (SupplierCategory supplierCategory : category.getSuppliersLink()) {
                        if (supplierCategory.getSupplier().getId() == categorySupplierCard.getSupplier().getId()) {
                            toRemove.add(supplierCategory);
                            System.out.println("a supprimer trouver");
                        }
                    }
                }
            }
            // Suppression des liens après l'itération
            for (SupplierCategory supplierCategory : toRemove) {

                System.out.println(supplierCategory.toString());
                
                // Supprimer l'entité dans la base de données
                supplierCategoryController.deleteSupplierCategory(supplierCategory.getId());

                //supplierController.updateSupplier(supplier);
                category = categoryController.refresh(category.getId());
            }
        }

        closeWindow(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadSuppliers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    



    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    
    private void loadSuppliers() throws IOException {

        List<Supplier> suppliers = supplierController.getAllSuppliers();

        if (!suppliers.isEmpty())
            for (Supplier supplier : suppliers){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/categorieFournisseurs.fxml"));
                Parent root = loader.load();

                CategorySupplierCard controller = loader.getController();

                controller.setData(supplier);

                root.setUserData(controller);

                categorySupplierBox.getChildren().add(root);
            }
    }


    public void setData(Category category) {
        this.category = category;
        name.setText(category.getName());
        description.setText(category.getDescription());
        for (Node node : categorySupplierBox.getChildren()) {
            CategorySupplierCard categorySupplierCard = (CategorySupplierCard) node.getUserData();
            if (categorySupplierCard.getSupplier().hasCategory(category))
                categorySupplierCard.check();
        }
    }

    public Category getCategory() {
        return this.category;
    }
}



