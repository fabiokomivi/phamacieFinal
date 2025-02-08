package com.pharmacie.FxControllers.sections;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.pharmacie.FxControllers.cards.CategoryCardFx;

import com.pharmacie.FxControllers.pops.CreateCategory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import com.pharmacie.models.Category;
import com.pharmacie.utilities.BehaviorSetter;
import com.pharmacie.utilities.Dialogs;
import com.pharmacie.utilities.Updatable;
import com.pharmacie.utilities.VarWrapper;
import com.pharmacie.controllers.CategoryController;

public class CategoryFx implements Initializable, Updatable{

    CategoryController categoryController = new CategoryController();
    List<Category> allCategories = categoryController.getAllCategories();

    @FXML
    private FlowPane categoriesGrid;

    @FXML
    private ToggleButton multipleSelector;

    @FXML
    private TextField searcher;

    @FXML
    void addCategory(ActionEvent event) throws IOException {
        openWindow(false);
        allCategories = categoryController.getAllCategories();
    }

    @FXML
    void editCategory(ActionEvent event) throws IOException {
        openWindow(true);
        allCategories = categoryController.getAllCategories();
    }

    @FXML
    void onCategorieChoice(MouseEvent event) {

    }

    @FXML
    void onMultipleSelection(ActionEvent event) {
        // Si multipleSelector est désactivé, désélectionner toutes les cartes
        if (!multipleSelector.isSelected()) {
            // Parcourir tous les enfants de supplierGrid (les cartes)
            for (Node node : categoriesGrid.getChildren()) {
                // Vérifier si l'élément a un contrôleur
                if (node.getUserData() != null) {
                    // Récupérer le contrôleur de chaque carte
                    CategoryCardFx cardController = (CategoryCardFx) node.getUserData();
                    // Enlever le style "selected" de chaque carte
                    cardController.removeSelection();
                }
            }
        }
    }

    @FXML
    void removeCategory(ActionEvent event) throws IOException {
        deleteCategoryCard();
        allCategories = categoryController.getAllCategories();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCategories();
        initializeSearcher();;
    }

    private void loadCategories() {
        for (Category category : allCategories)
            try {
                addCatoryCard(category);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void openWindow(boolean edit) throws IOException {

            VarWrapper<CategoryCardFx> selectedCardWrapper = new VarWrapper(null);


        FXMLLoader windowLoader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/pops/createCategorie.fxml"));
        Parent window = windowLoader.load();
        Stage stage = BehaviorSetter.ModalDraggableTransparent(window);

        CreateCategory windowController = windowLoader.getController();

    
        if (edit) {

            CategoryCardFx selectedCategoryCardFx = null;

            for (Node node : categoriesGrid.getChildren()) {

                CategoryCardFx currentCategoryCardFx = (CategoryCardFx) node.getUserData();

                if(currentCategoryCardFx!=null && currentCategoryCardFx.isCardSelected()) {
                    selectedCategoryCardFx = currentCategoryCardFx;
                    windowController.setData(selectedCategoryCardFx.getCategory());

                    selectedCardWrapper.setValue(selectedCategoryCardFx);

                    stage.setOnHidden(e -> {
                        Category updatedCategory = windowController.getCategory();
                        if(updatedCategory != null) {
                            CategoryCardFx unwrapedCategoryCardFx = selectedCardWrapper.getValue();
                            updateCategoryCard(unwrapedCategoryCardFx, updatedCategory);
                        }
                    });
                    break ;
                }
            }

            if(selectedCategoryCardFx == null) {
                Dialogs.showSimpleMessage("aucune categorie selectionnée");
                return;
            }

        } else {
            stage.setOnHidden(e -> {
                Category createdCategory = windowController.getCategory();
                if(createdCategory != null) {
                    try {
                        addCatoryCard(createdCategory);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
    
        // Afficher la fenêtre
        stage.showAndWait();
    }

    

    private void addCatoryCard(Category category) throws IOException {
        try{
            // Charger une carte category
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/categoryCard.fxml"));
            Parent categoryCard = loader.load(); // Charger le fichier FXML

            CategoryCardFx categoryCardController = loader.getController(); // Récupérer le contrôleur associé
            
            categoryCard.setUserData(categoryCardController);

            categoryCardController.setData(category);

            categoryCard.setOnMouseClicked(event -> styleBinder(categoryCard));

            categoriesGrid.getChildren().add(categoryCard);
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void updateCategoryCard(CategoryCardFx categoryCardFx, Category category) {
        categoryCardFx.setData(category);
    }

    private void deleteCategoryCard() throws IOException {
        // Collecter les nœuds à supprimer
        List<Node> nodesToRemove = collectSelectedNodes();
        
        if (nodesToRemove.isEmpty()) {
            Dialogs.showSimpleMessage("Aucune catégorie sélectionnée");
            return;
        }
        
        // Afficher le message de confirmation
        if (Dialogs.showAllowMessage("Voulez-vous vraiment supprimer ces catégories ?")) {
            for (Node node : nodesToRemove) {
                CategoryCardFx categoryCardFx = (CategoryCardFx) node.getUserData();
                if (categoryCardFx != null) {
                    categoryController.deleteCategory(categoryCardFx.getCategory().getId());
                }
            }
            categoriesGrid.getChildren().removeAll(nodesToRemove);
        }
    }

    // Méthode pour collecter les nœuds sélectionnés
    private List<Node> collectSelectedNodes() {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : categoriesGrid.getChildren()) {
            CategoryCardFx categoryCardFx = (CategoryCardFx) node.getUserData();
            if (categoryCardFx != null && categoryCardFx.isCardSelected()) {
                nodesToRemove.add(node);
            }
        }
        return nodesToRemove;
    }


    private void styleBinder(Parent card) {
        // Récupérer le contrôleur associé à la carte actuelle
        CategoryCardFx categoryCardController = (CategoryCardFx) card.getUserData();
        
        // Si multipleSelector est sélectionné, appliquez le toggle sur la carte
        if (multipleSelector.isSelected()) {
            categoryCardController.toggle();
        } else {
            // Si multipleSelector n'est pas sélectionné, itérer sur les enfants du supplierGrid
            for (Node node : categoriesGrid.getChildren()) {
                // Vérifier si l'élément a un contrôleur (on suppose que chaque node a un contrôleur associé)
                if (node.getUserData() != null && node != card) {
                    // Récupérer le contrôleur de chaque carte
                    CategoryCardFx otherCategoryCardController = (CategoryCardFx) node.getUserData();
                    // Appeler la méthode removeSelection() pour retirer le style "selected"
                    otherCategoryCardController.removeSelection();
                }
            }
            categoryCardController.toggle();
        }
    }

    @Override
    public void update() {
        allCategories = categoryController.getAllCategories();
        categoriesGrid.getChildren().clear();
        loadCategories();
    }

    private void initializeSearcher() {
        searcher.textProperty().addListener((observable, oldValue, newValue) -> {
            searchCategories(newValue.trim().toLowerCase());
        });
    }

    
    private void searchCategories(String query) {
        // Effacer les résultats actuels
        categoriesGrid.getChildren().clear();
    
        // Obtenir tous les médicaments
        
    
        // Parcourir les médicaments pour trouver ceux qui correspondent à la recherche
        for (Category category : allCategories) {
            if (category.getName().toLowerCase().contains(query)) {
                try {
                    addCatoryCard(category);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
