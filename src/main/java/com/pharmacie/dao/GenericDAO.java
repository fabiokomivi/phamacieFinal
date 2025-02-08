package com.pharmacie.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public class GenericDAO<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // Getter pour accéder à entityManager
    public EntityManager getEntityManager() {
        return entityManager;
    }

    // Récupérer toutes les entités
    public List<T> getAll() {
        return entityManager.createQuery("SELECT e FROM " + entityClass.getName() + " e", entityClass).getResultList();
    }

    // Récupérer une entité par ID
    public T getById(Long id) {
        return entityManager.find(entityClass, id);
    }

    // Ajouter une entité
    @Transactional
    public void save(T entity) {
        entityManager.persist(entity);
    }

    // Mettre à jour une entité
    @Transactional
    public void update(T entity) {
        entityManager.merge(entity);
    }

    // Supprimer une entité
    @Transactional
    public void delete(Long id) {
        T entity = entityManager.find(entityClass, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
