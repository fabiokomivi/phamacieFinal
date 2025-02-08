package com.pharmacie.dao;

import com.pharmacie.models.Login;
import com.pharmacie.util.HibernateUtil;


import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LoginDAO {

    // Méthode pour enregistrer un nouveau login
    public void save(Login login) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(login);  // Utilisation de persist() pour insérer
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

    // Méthode pour récupérer un login par son ID
    public Login getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Login login = session.get(Login.class, id);  // Recherche par ID
        session.close();
        return login;
    }
    

    // Méthode pour récupérer tous les logins
    public List<Login> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Login> logins = session.createQuery("FROM Login", Login.class).list();
        session.close();
        return logins;
    }

    // Méthode pour mettre à jour un login existant
    public void update(Login login) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(login);  // Utilisation de merge() pour mettre à jour
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

    // Méthode pour supprimer un login par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Login login = session.get(Login.class, id);  // Récupérer le login
            if (login != null) {
                session.remove(login);  // Utilisation de remove() pour supprimer
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
