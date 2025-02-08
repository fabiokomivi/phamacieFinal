package com.pharmacie.dao;

import com.pharmacie.models.Role;
import com.pharmacie.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RoleDAO {

    // Méthode pour enregistrer un rôle
    public void save(Role role) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(role);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    // Méthode pour récupérer un rôle par son ID
    public Role getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Role role = session.get(Role.class, id);
        session.close();
        return role;
    }

    // Méthode pour récupérer tous les rôles
    public List<Role> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Role> roles = session.createQuery("FROM Role", Role.class).list();
        session.close();
        return roles;
    }

    // Méthode pour mettre à jour un rôle existant
    public void update(Role role) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(role);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    // Méthode pour supprimer un rôle par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Role role = session.get(Role.class, id);
            if (role != null) {
                session.remove(role);
            }
            transaction.commit();
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
