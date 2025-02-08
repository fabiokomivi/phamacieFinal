package com.pharmacie.dao;

import com.pharmacie.models.Stock;
import com.pharmacie.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class StockDAO {

    // Méthode pour enregistrer un stock
    public void save(Stock stock) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(stock);
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

    // Méthode pour récupérer un stock par son ID
    public Stock getById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Stock stock = session.get(Stock.class, id);
        session.close();
        return stock;
    }

    // Méthode pour récupérer tous les stocks
    public List<Stock> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Stock> stocks = session.createQuery("FROM Stock", Stock.class).list();
        session.close();
        return stocks;
    }

    // Méthode pour mettre à jour un stock existant
    public void update(Stock stock) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(stock);
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

    // Méthode pour supprimer un stock par son ID
    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Stock stock = session.get(Stock.class, id);
            if (stock != null) {
                session.remove(stock);
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
