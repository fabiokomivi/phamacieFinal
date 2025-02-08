package com.pharmacie.dao;

import com.pharmacie.models.Supplier;
import com.pharmacie.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SupplierDAO {

    // Méthode pour enregistrer un fournisseur
    public void save(Supplier supplier) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(supplier);  // Utilisation de persist() pour insérer
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

    // Méthode pour récupérer un fournisseur par son ID
    public Supplier getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Supplier supplier = session.get(Supplier.class, id);  // Recherche par ID
        session.close();
        return supplier;
    }

    // Méthode pour récupérer tous les fournisseurs
    public List<Supplier> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Supplier> suppliers = session.createQuery("FROM Supplier", Supplier.class).list();
        session.close();
        return suppliers;
    }

    // Méthode pour mettre à jour un fournisseur existant
    public void update(Supplier supplier) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(supplier);  // Utilisation de merge() pour mettre à jour
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

    // Méthode pour supprimer un fournisseur par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Supplier supplier = session.get(Supplier.class, id);  // Récupérer le fournisseur
            if (supplier != null) {
                session.remove(supplier);  // Utilisation de remove() pour supprimer
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

    public Supplier refresh(int supplierId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Supplier supplier = null;
        try {
            transaction = session.beginTransaction();
    
            // Récupérer l'entité Category
            supplier = session.get(Supplier.class, supplierId);
            if (supplier == null) {
                throw new IllegalArgumentException("Supplier avec l'ID " + supplierId + " n'existe pas.");
            }
    
            // Rafraîchir l'état de l'entité depuis la base de données
            session.refresh(supplier);
    
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return supplier;
    }
}
