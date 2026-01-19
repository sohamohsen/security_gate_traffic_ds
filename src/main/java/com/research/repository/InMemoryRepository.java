package com.research.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class InMemoryRepository<T extends com.research.model.BaseEntity> implements BaseRepository<T> {
    protected final Map<Integer, T> storage = new HashMap<>();
    protected final AtomicInteger idCounter = new AtomicInteger(1);
    protected final String entityName;

    public InMemoryRepository(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public T save(T entity) {
        if (entity.getId() == 0) {
            entity.setId(idCounter.getAndIncrement());
        } else {
            if (existsById(entity.getId())) {
                throw new com.research.exception.DuplicateIdException(entityName, entity.getId());
            }
            // Update idCounter if needed
            if (entity.getId() >= idCounter.get()) {
                idCounter.set(entity.getId() + 1);
            }
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public T update(T entity) {
        int id = entity.getId();
        if (!existsById(id)) {
            throw new com.research.exception.NotFoundException(entityName, id);
        }
        entity.setUpdatedAt(java.time.LocalDateTime.now());
        storage.put(id, entity);
        return entity;
    }

    @Override
    public void delete(int id) {
        if (!existsById(id)) {
            throw new com.research.exception.NotFoundException(entityName, id);
        }
        storage.remove(id);
    }

    @Override
    public boolean existsById(int id) {
        return storage.containsKey(id);
    }

    @Override
    public List<T> findBy(Predicate<T> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }

    @Override
    public Optional<T> findOneBy(Predicate<T> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .findFirst();
    }

    public Map<Integer, T> getStorage() {
        return storage;
    }
}