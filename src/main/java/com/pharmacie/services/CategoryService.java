package com.pharmacie.services;

import com.pharmacie.dao.CategoryDAO;
import com.pharmacie.models.Category;
import java.util.List;

public class CategoryService {

    private CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    // Enregistrer une nouvelle catégorie
    public void addCategory(Category category) {
        categoryDAO.save(category);
    }

    // Récupérer une catégorie par ID
    public Category getCategoryById(Long id) {
        return categoryDAO.getById(id);
    }

    // Récupérer toutes les catégories
    public List<Category> getAllCategories() {
        return categoryDAO.getAll();
    }

    // Mettre à jour une catégorie
    public void updateCategory(Category category) {
        categoryDAO.update(category);
    }

    // Supprimer une catégorie
    public void deleteCategory(int id) {
        categoryDAO.delete(id);
    }

    // raffraichir une catégorie
    public Category refresh(int id) {
        return categoryDAO.refresh(id);
    }
}
