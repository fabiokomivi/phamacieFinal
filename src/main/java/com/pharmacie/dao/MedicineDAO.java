package com.pharmacie.dao;

import com.pharmacie.models.Medicine;
import com.pharmacie.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MedicineDAO {

    // Méthode pour enregistrer un nouveau médicament
    public void save(Medicine medicine) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(medicine);  // Utilisation de persist() pour insérer
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

    // Méthode pour récupérer un médicament par son ID
    public Medicine getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Medicine medicine = session.get(Medicine.class, id);  // Recherche par ID
        session.close();
        return medicine;
    }

    // Méthode pour récupérer tous les médicaments
    public List<Medicine> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Medicine> medicines = session.createQuery("FROM Medicine", Medicine.class).list();
        session.close();
        return medicines;
    }

    // Méthode pour mettre à jour un médicament existant
    public void update(Medicine medicine) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(medicine);  // Utilisation de merge() pour mettre à jour
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

    // Méthode pour supprimer un médicament par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Medicine medicine = session.get(Medicine.class, id);  // Récupérer le médicament
            if (medicine != null) {
                session.remove(medicine);  // Utilisation de remove() pour supprimer
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
