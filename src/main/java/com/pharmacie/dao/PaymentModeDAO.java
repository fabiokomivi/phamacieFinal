package com.pharmacie.dao;

import com.pharmacie.models.PaymentMode;
import com.pharmacie.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PaymentModeDAO {

    // Méthode pour enregistrer un mode de paiement
    public void save(PaymentMode paymentMode) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(paymentMode);  // Utilisation de persist() pour insérer
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

    // Méthode pour récupérer un mode de paiement par son ID
    public PaymentMode getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        PaymentMode paymentMode = session.get(PaymentMode.class, id);  // Recherche par ID
        session.close();
        return paymentMode;
    }

    // Méthode pour récupérer tous les modes de paiement
    public List<PaymentMode> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<PaymentMode> paymentModes = session.createQuery("FROM PaymentMode", PaymentMode.class).list();
        session.close();
        return paymentModes;
    }

    // Méthode pour mettre à jour un mode de paiement existant
    public void update(PaymentMode paymentMode) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(paymentMode);  // Utilisation de merge() pour mettre à jour
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

    // Méthode pour supprimer un mode de paiement par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            PaymentMode paymentMode = session.get(PaymentMode.class, id);  // Récupérer le mode de paiement
            if (paymentMode != null) {
                session.remove(paymentMode);  // Utilisation de remove() pour supprimer
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
