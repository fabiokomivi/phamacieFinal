package com.pharmacie.dao;

import com.pharmacie.models.Category;
import com.pharmacie.models.Supplier;
import com.pharmacie.models.SupplierCategory;
import com.pharmacie.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SupplierCategoryDAO {

    // Méthode pour enregistrer une relation fournisseur-catégorie
    public void save(SupplierCategory supplierCategory) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(supplierCategory);  // Utilisation de persist() pour insérer
            transaction.commit();  // Valider la transaction
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();  // Annuler en cas d'erreur
            }
            throw e;
        } finally {
            session.close();  // Fermer la session
        }
    }

    // Méthode pour récupérer une relation fournisseur-catégorie par son ID
    public SupplierCategory getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        SupplierCategory supplierCategory = session.get(SupplierCategory.class, id);  // Recherche par ID
        session.close();
        return supplierCategory;
    }

    // Méthode pour récupérer toutes les relations fournisseur-catégorie
    public List<SupplierCategory> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SupplierCategory> supplierCategories = session.createQuery("FROM SupplierCategory", SupplierCategory.class).list();
        session.close();
        return supplierCategories;
    }

    // Méthode pour mettre à jour une relation fournisseur-catégorie existante
    public void update(SupplierCategory supplierCategory) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(supplierCategory);  // Utilisation de merge() pour mettre à jour
            transaction.commit();  // Valider la transaction
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();  // Annuler en cas d'erreur
            }
            throw e;
        } finally {
            session.close();  // Fermer la session
        }
    }

    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
    
            // Récupérer l'entité SupplierCategory
            SupplierCategory supplierCategory = session.get(SupplierCategory.class, id);
            if (supplierCategory == null) {
                throw new IllegalArgumentException("SupplierCategory avec l'ID " + id + " n'existe pas.");
            }
    
            // Supprimer les références des parents pour maintenir la cohérence
            Supplier supplier = supplierCategory.getSupplier();
            if (supplier != null) {
                supplier.getCategoriesLink().remove(supplierCategory);
            }
    
            Category category = supplierCategory.getCategory();
            if (category != null) {
                category.getSuppliersLink().remove(supplierCategory);
            }
    
            // Supprimer l'entité elle-même
            session.remove(supplierCategory);
    
            // Synchroniser l'état avec la base de données
            session.flush();
    
            // Valider la transaction
            transaction.commit();
    
            // Afficher la taille des relations pour validation (facultatif)
            if (category != null) {
                System.out.println("Taille des liens après suppression : " + category.getSuppliersLink().size());
            }
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
    
    
}
