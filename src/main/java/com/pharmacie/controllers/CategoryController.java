package com.pharmacie.controllers;

import com.pharmacie.models.Category;
import com.pharmacie.services.CategoryService;
import java.util.List;

public class CategoryController {

    private CategoryService categoryService;

    public CategoryController() {
        this.categoryService = new CategoryService();
    }

    // Ajouter une nouvelle catégorie
    public void saveCategory(Category category) {
        categoryService.addCategory(category);
    }

    // Récupérer toutes les catégories
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // Récupérer une catégorie par son ID
    public Category getCategoryById(Long id) {
        return categoryService.getCategoryById(id);
    }

    // Mettre à jour une catégorie
    public void updateCategory(Category category) {
        categoryService.updateCategory(category);
    }

    // Supprimer une catégorie
    public void deleteCategory(int id) {
        categoryService.deleteCategory(id);
    }

    // raffraichir une catégorie
    public Category refresh(int id) {
        return categoryService.refresh(id);
    }
}
