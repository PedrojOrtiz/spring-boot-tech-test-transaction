package ec.sofka.springboottechtesttransaction.util;

import ec.sofka.springboottechtesttransaction.exception.DataNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Abstract class that provides basic CRUD operations for a given entity
 * @param <T> Entity
 * @param <K> Repository
 * @param <L> ID type
 */
@RequiredArgsConstructor
public abstract class AbstractService<T, K, L> {

    protected final K repository;
    private final String className;

    public T save(T t) {
        T newT = ((JpaRepository<T,L>) repository)
                .save(preSave(t));
        postSave(newT);
        return newT;
    }

    public T update(T t) {
        T upT = ((JpaRepository<T,L>) repository)
                .save(preUpdate(t));
        postUpdate(upT);
        return upT;
    }

    public T read(L id) throws Throwable {
        Optional optional = ((JpaRepository<T,L>) repository)
                .findById(id);
        return (T) optional
                .orElseThrow(() -> new DataNotFound(className + " with ID " + id + " not found"));
    }

    public List<T> readAll() {
        return ((JpaRepository<T,L>) repository)
                .findAll();
    }

    public void delete(L id) {
        ((JpaRepository<T,L>) repository)
                .deleteById(preDelete(id));
        postDelete(id);
    }

    /**
     * Hook method to be overridden by subclasses
     * just before saving an entity
     * @param t
     * @return
     */
    public T preSave(T t) {
        return t;
    }

    /**
     * Hook method to be overridden by subclasses
     * just after saving an entity
     * @param t
     * @return
     */
    public T postSave(T t) {
        return t;
    }

    /**
     * Hook method to be overridden by subclasses
     * just before updating an entity
     * @param t
     * @return
     */
    public T preUpdate(T t) {
        return t;
    }

    /**
     * Hook method to be overridden by subclasses
     * just after updating an entity
     * @param t
     * @return
     */
    public T postUpdate(T t) {
        return t;
    }

    /**
     * Hook method to be overridden by subclasses
     * just before deleting an entity
     * @param id
     * @return
     */
    public L preDelete(L id) {
        return id;
    }

    /**
     * Hook method to be overridden by subclasses
     * just after deleting an entity
     * @param id
     * @return
     */
    public L postDelete(L id) {
        return id;
    }

}