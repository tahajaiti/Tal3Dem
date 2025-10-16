package org.kyojin.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.kyojin.core.annotation.Inject;
import org.kyojin.provider.EntityManagerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class JpaRepository<T, I> implements Repository<T, I> {

    protected static final Logger log = LoggerFactory.getLogger(JpaRepository.class);

    @Inject
    private EntityManagerProvider emProvider;

    private final Class<T> entityClass;

    protected JpaRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityManager em() {
        return emProvider.get();
    }

    protected <R> R executeInTransaction(Function<EntityManager, R> action) {
        EntityManager em = em();
        try {
            em.getTransaction().begin();
            R result = action.apply(em);
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            log.error("Transaction failed for {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public T save(T entity) {
        return executeInTransaction(em -> {
            Object id = em.getEntityManagerFactory()
                    .getPersistenceUnitUtil()
                    .getIdentifier(entity);
            if (id == null) {
                em.persist(entity);
                return entity;
            } else {
                return em.merge(entity);
            }
        });
    }

    @Override
    public Optional<T> find(I id) {
        try {
            return Optional.ofNullable(em().find(entityClass, id));
        } catch (Exception e) {
            log.error("Error finding {} with id {}: {}", entityClass.getSimpleName(), id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> findBy(String fieldName, Object value) {
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";
            TypedQuery<T> query = em().createQuery(jpql, entityClass);
            query.setParameter("value", value);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            log.error("Error finding {} by {} = {}: {}", entityClass.getSimpleName(), fieldName, value, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public T update(T entity) {
        return executeInTransaction(em -> em.merge(entity));
    }

    @Override
    public void delete(I id) {
        executeInTransaction(em -> {
            T entity = em.find(entityClass, id);
            if (entity != null) em.remove(entity);
            return null;
        });
    }

    @Override
    public List<T> findAll() {
        try {
            CriteriaBuilder cb = em().getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
            cq.select(root);
            return em().createQuery(cq).getResultList();
        } catch (Exception e) {
            log.error("Error fetching all {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<T> findAll(int page, int size) {
        try {
            CriteriaBuilder cb = em().getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
            cq.select(root);

            TypedQuery<T> query = em().createQuery(cq);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error fetching paginated {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<T> findAllBy(String fieldName, Object value) {
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";
            TypedQuery<T> query = em().createQuery(jpql, entityClass);
            query.setParameter("value", value);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error finding {} by {} = {}: {}", entityClass.getSimpleName(), fieldName, value, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<T> findAllBy(String fieldName, Object value, int page, int size) {
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";
            TypedQuery<T> query = em().createQuery(jpql, entityClass);
            query.setParameter("value", value);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error fetching paginated {} by {}: {}", entityClass.getSimpleName(), fieldName, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public long count() {
        try {
            CriteriaBuilder cb = em().getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<T> root = cq.from(entityClass);
            cq.select(cb.count(root));
            return em().createQuery(cq).getSingleResult();
        } catch (Exception e) {
            log.error("Error counting {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
            return 0;
        }
    }
}
