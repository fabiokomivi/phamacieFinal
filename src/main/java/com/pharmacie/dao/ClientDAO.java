package com.pharmacie.dao;

import com.pharmacie.models.Client;
import com.pharmacie.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ClientDAO {

    // Méthode pour enregistrer une nouvelle catégorie
    public void save(Client client) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(client);  // Utilisation de persist() pour insérer
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

    // Méthode pour récupérer un client par son ID
    public Client getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Client client = session.get(Client.class, id);  // Recherche par ID
        session.close();
        return client;
    }

    // Méthode pour récupérer tous les clients
    public List<Client> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Client> clients = session.createQuery("FROM Client", Client.class).list();
        session.close();
        return clients;
    }

    // Méthode pour mettre à jour un client existant
    public void update(Client client) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(client);  // Utilisation de merge() pour mettre à jour
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

    // Méthode pour supprimer un client par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Client client = session.get(Client.class, id);  // Récupérer le client
            if (client != null) {
                session.remove(client);  // Utilisation de remove() pour supprimer
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
