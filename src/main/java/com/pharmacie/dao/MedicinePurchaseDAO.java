package com.pharmacie.dao;

import com.pharmacie.models.MedicinePurchase;
import com.pharmacie.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MedicinePurchaseDAO {

    // Méthode pour enregistrer un achat de médicament
    public void save(MedicinePurchase medicinePurchase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(medicinePurchase);  // Utilisation de persist() pour insérer
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

    // Méthode pour récupérer un achat de médicament par son ID
    public MedicinePurchase getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        MedicinePurchase medicinePurchase = session.get(MedicinePurchase.class, id);  // Recherche par ID
        session.close();
        return medicinePurchase;
    }

    // Méthode pour récupérer tous les achats de médicaments
    public List<MedicinePurchase> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<MedicinePurchase> medicinePurchases = session.createQuery("FROM MedicinePurchase", MedicinePurchase.class).list();
        session.close();
        return medicinePurchases;
    }

    // Méthode pour mettre à jour un achat de médicament existant
    public void update(MedicinePurchase medicinePurchase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(medicinePurchase);  // Utilisation de merge() pour mettre à jour
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

    // Méthode pour supprimer un achat de médicament par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            MedicinePurchase medicinePurchase = session.get(MedicinePurchase.class, id);  // Récupérer l'achat
            if (medicinePurchase != null) {
                session.remove(medicinePurchase);  // Utilisation de remove() pour supprimer
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
