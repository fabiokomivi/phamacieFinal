package com.pharmacie.dao;

import com.pharmacie.models.Purchase;
import com.pharmacie.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PurchaseDAO {

    // Méthode pour enregistrer un achat
    public void save(Purchase purchase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(purchase);  // Utilisation de persist() pour insérer
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

    // Méthode pour récupérer un achat par son ID
    public Purchase getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Purchase purchase = session.get(Purchase.class, id);  // Recherche par ID
        session.close();
        return purchase;
    }

    // Méthode pour récupérer tous les achats
    public List<Purchase> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Purchase> purchases = session.createQuery("FROM Purchase", Purchase.class).list();
        session.close();
        return purchases;
    }

    // Méthode pour mettre à jour un achat existant
    public void update(Purchase purchase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(purchase);  // Utilisation de merge() pour mettre à jour
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

    // Méthode pour supprimer un achat par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Purchase purchase = session.get(Purchase.class, id);  // Récupérer l'achat
            if (purchase != null) {
                session.remove(purchase);  // Utilisation de remove() pour supprimer
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
}
