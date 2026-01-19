package com.research.repository;

import com.research.model.BaseEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface BaseRepository<T extends BaseEntity> {
    T save(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    T update(T entity);
    void delete(int id);
    boolean existsById(int id);
    List<T> findBy(Predicate<T> predicate);
    Optional<T> findOneBy(Predicate<T> predicate);
}
