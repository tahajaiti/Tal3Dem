package org.kyojin.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kyojin.core.Inject;
import org.kyojin.provider.EntityManagerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public abstract class JpaRepository<T, I> implements Repository<T, I> {

    private static final Logger logger = LoggerFactory.getLogger(JpaRepository.class);

    @Inject
    private EntityManagerProvider emProvider;

    private final Class<T> entityClass;

    protected JpaRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T save(T entity) {
        EntityManager em = emProvider.get();
        try {
            if (em.contains(entity)) {
                return em.merge(entity);
            } else {
                em.persist(entity);
                return entity;
            }
        } catch (Exception e) {
            logger.error("Error saving entity: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<T> find(I id) {
        EntityManager em = emProvider.get();
        try {
            return Optional.ofNullable(em.find(entityClass, id));
        } catch (Exception e) {
            logger.error("Error finding entity with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<T> findBy(String fieldName, Object value) {
        EntityManager em = emProvider.get();
        try {
            String query = "SELECT * FROM" + entityClass.getSimpleName() + " WHERE " + fieldName + " = ?";

            TypedQuery<T> queryObject = em.createQuery(query, entityClass);
            queryObject.setParameter(1, value);

            return Optional.ofNullable(queryObject.getSingleResult());
        } catch(Exception e) {
            logger.error("Error finding entity with name {}: {}", fieldName, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public T update(T entity) {
        EntityManager em = emProvider.get();
        try {
            return em.merge(entity);
        } catch (Exception e) {
            logger.error("Error updating entity: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(I id) {
        EntityManager em = emProvider.get();
        try {
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
        } catch (Exception e) {
            logger.error("Error deleting entity with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<T> findAll() {
        EntityManager em = emProvider.get();
        try {
            return em.createQuery("FROM " + entityClass.getSimpleName(), entityClass)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error finding all entities: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<T> findAll(int page, int size) {
        EntityManager em = emProvider.get();
        try {
            return em.createQuery("FROM " + entityClass.getSimpleName(), entityClass)
                    .setFirstResult(page * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error finding paginated entities: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<T> findAllBy(String fieldName, Object value) {
        EntityManager em = emProvider.get();
        try {
            String query = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";
            TypedQuery<T> typedQuery = em.createQuery(query, entityClass);
            typedQuery.setParameter("value", value);
            return typedQuery.getResultList();
        } catch (Exception e) {
            logger.error("Error finding entities by {}: {}", fieldName, e.getMessage(), e);
            throw e;
        }
    }

    public List<T> findAllBy(String fieldName, Object value, int page, int size) {
        EntityManager em = emProvider.get();
        try {
            String query = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";
            TypedQuery<T> typedQuery = em.createQuery(query, entityClass);
            typedQuery.setParameter("value", value);
            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);
            return typedQuery.getResultList();
        } catch (Exception e) {
            logger.error("Error finding paginated entities by {}: {}", fieldName, e.getMessage(), e);
            throw e;
        }
    }

    public long count() {
        EntityManager em = emProvider.get();
        try {
            return em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting entities: {}", e.getMessage(), e);
            throw e;
        }
    }
}