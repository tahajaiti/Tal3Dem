package org.kyojin.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, I> {
    T save (T entity);
    Optional<T> find(I id);
    Optional<T> findBy(String fieldName, Object value);
    T update(T entity);
    void delete(I id);
    List<T> findAll();
    List<T> findAll(int page, int size);
    List<T> findAllBy(String fieldName, Object value);
    List<T> findAllBy(String fieldName, Object value, int page, int size);
    long count();
}
