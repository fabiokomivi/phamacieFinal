package com.pharmacie.dao;

import com.pharmacie.models.Category;
import com.pharmacie.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class CategoryDAO {

    // Méthode pour enregistrer une nouvelle catégorie
    public void save(Category category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(category);  // Enregistrer l'entité Category
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

    // Méthode pour récupérer une catégorie par son ID
    public Category getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Category category = session.get(Category.class, id);  // Recherche par ID
        session.close();
        return category;
    }

    // Méthode pour récupérer toutes les catégories
    public List<Category> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Category> categories = session.createQuery("FROM Category", Category.class).list();
        session.close();
        return categories;
    }

    // Méthode pour mettre à jour une catégorie existante
    public void update(Category category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(category);  // Mettre à jour l'entité Category
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

    // Méthode pour supprimer une catégorie par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Category category = session.get(Category.class, id);  // Récupérer la catégorie à supprimer
            if (category != null) {
                session.remove(category);  // Supprimer l'entité Category
            }
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

    public Category refresh(int categoryId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Category category = null;
        try {
            transaction = session.beginTransaction();
    
            // Récupérer l'entité Category
            category = session.get(Category.class, categoryId);
            if (category == null) {
                throw new IllegalArgumentException("Category avec l'ID " + categoryId + " n'existe pas.");
            }
    
            // Rafraîchir l'état de l'entité depuis la base de données
            session.refresh(category);
    
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return category;
    }
    
}
