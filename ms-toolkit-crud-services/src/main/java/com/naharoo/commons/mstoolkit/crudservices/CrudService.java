package com.naharoo.commons.mstoolkit.crudservices;

import com.naharoo.commons.mstoolkit.domainmodel.Identifiable;
import com.naharoo.commons.mstoolkit.exceptions.ResourceNotFoundException;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Interface for generic CRUD operations on a service for a specific type.
 *
 * @param <T> Managed Entity's type
 * @param <I> Managed Entity's id's type
 */
public interface CrudService<T extends Identifiable<I>, I extends Serializable> {

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity to be saved. Must not be {@literal null}. It must not have an id set.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} has an <code>id</code> set.
     */
    @NonNull
    T create(@NonNull T entity);

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null} nor must it contain {@literal null}. They must not have ids set.
     * @return the saved entities; will never be {@literal null}. The returned {@literal List} will have the same size
     * as the {@literal List} passed as an argument.
     * @throws IllegalArgumentException in case the given {@link List entities} or one of its entities is {@literal null}.
     * @throws IllegalArgumentException in case the given {@link List entities} or one of its entities has an <code>id</code> set.
     */
    @NonNull
    List<T> createAll(@NonNull Collection<? extends T> entities);

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity to be saved. Must not be {@literal null}. It must have an id set.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} doesn't have <code>id</code> set.
     */
    @NonNull
    T update(@NonNull T entity);

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null} nor must it contain {@literal null}. They must have ids set.
     * @return the saved entities; will never be {@literal null}. The returned {@literal List} will have the same size
     * as the {@literal List} passed as an argument.
     * @throws IllegalArgumentException in case the given {@link List entities} or one of its entities is {@literal null}.
     * @throws IllegalArgumentException in case the given {@link List entities} or one of its entities doesn't have an <code>id</code> set.
     */
    @NonNull
    List<T> updateAll(@NonNull Collection<? extends T> entities);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @NonNull
    Optional<T> find(@NonNull I id);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id.
     * @throws IllegalArgumentException  if {@literal id} is {@literal null}.
     * @throws ResourceNotFoundException if no entity is found by {@literal id}.
     */
    @NonNull
    T get(@NonNull I id);

    /**
     * Returns all instances of the type {@code T}.
     *
     * @return all entities; will never be {@literal null}.
     */
    @NonNull
    List<T> getAll();

    /**
     * Returns all instances of the type {@code T} with the given IDs.
     * <p>
     * If some or all ids are not found, no entities are returned for these IDs.
     * <p>
     * Note that the order of elements in the result is not guaranteed.
     *
     * @param ids must not be {@literal null} nor contain any {@literal null} values.
     * @return guaranteed to be not {@literal null}. The size can be equal or less than the number of given {@literal ids}.
     * @throws IllegalArgumentException in case the given {@link Collection ids} or one of its items is {@literal null}.
     */
    @NonNull
    List<T> getByIds(@NonNull Collection<? extends I> ids);

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     */
    void delete(@NonNull I id);

    /**
     * Deletes all entities managed by the service.
     */
    void deleteAll();

    /**
     * Deletes all entities with ids inside provided {@link Collection ids}.
     *
     * @param ids must not be {@literal null} nor contain any {@literal null} values.
     * @throws IllegalArgumentException in case the given {@link Collection ids} or one of its items is {@literal null}.
     */
    void deleteByIds(@NonNull Collection<? extends I> ids);
}
