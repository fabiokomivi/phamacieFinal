package com.pharmacie.dao;


import com.pharmacie.models.UserRole;
import com.pharmacie.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserRoleDAO {

    // Méthode pour enregistrer une UserRole
    public void save(UserRole userRole) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(userRole);  // Utilisation de persist() pour insérer
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

    // Méthode pour récupérer une UserRole par son ID
    public UserRole getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        UserRole userRole = session.get(UserRole.class, id);  // Recherche par ID
        session.close();
        return userRole;
    }

    // Méthode pour récupérer toutes les UserRoles
    public List<UserRole> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<UserRole> userRoles = session.createQuery("FROM UserRole", UserRole.class).list();
        session.close();
        return userRoles;
    }

    // Méthode pour mettre à jour une UserRole existante
    public void update(UserRole userRole) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(userRole);  // Utilisation de merge() pour mettre à jour
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

    // Méthode pour supprimer une UserRole par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            UserRole userRole = session.get(UserRole.class, id);  // Récupérer la UserRole
            if (userRole != null) {
                session.remove(userRole);  // Utilisation de remove() pour supprimer
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
