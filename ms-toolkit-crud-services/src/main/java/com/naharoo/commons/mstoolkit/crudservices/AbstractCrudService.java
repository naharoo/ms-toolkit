package com.naharoo.commons.mstoolkit.crudservices;

import com.naharoo.commons.mstoolkit.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.util.Assert.*;

public abstract class AbstractCrudService<T extends Identifiable<I>, I extends Serializable> implements CrudService<T, I> {

    protected final Class<T> entityClass = resolveGenericEntityType();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CrudRepository<T, I> crudRepository;

    protected AbstractCrudService(final CrudRepository<T, I> crudRepository) {
        this.crudRepository = crudRepository;
    }

    @SuppressWarnings("unchecked")
    private Class<T> resolveGenericEntityType() {
        final Class<?>[] classes = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractCrudService.class);
        if (classes == null) {
            throw new IllegalStateException(String.format(
                    "%s should provide generic type parameters to %s",
                    getClass().getSimpleName(),
                    AbstractCrudService.class.getSimpleName()
            ));
        }
        return (Class<T>) classes[0];
    }

    @Override
    @Transactional
    public T create(final T entity) {
        notNull(entity, "entity for creation cannot be null.");
        logger.trace("Creating a new {}...", entityClass.getSimpleName());

        assureCreationInvariants(entity);
        final T created = crudRepository.save(entity);

        logger.debug("Successfully created a new {}.", entityClass.getSimpleName());
        return created;
    }

    protected void assureCreationInvariants(final T entity) {
        notNull(entity, "entity for creation cannot be null.");
        final I id = entity.getId();
        isNull(id, "id of creation entity must be null.");
    }

    @Override
    @Transactional
    public List<T> createAll(final Collection<? extends T> entities) {
        notEmpty(entities, "entities for creation cannot be null or empty.");
        entities.forEach(entity -> notNull(entity, "entity for creation cannot be null."));
        logger.trace("Creating {} new {}s...", entities.size(), entityClass.getSimpleName());

        assureCreationInvariants(entities);
        final List<T> created = StreamSupport
                .stream(crudRepository.saveAll(entities).spliterator(), false)
                .collect(Collectors.toList());

        logger.debug("Successfully created {} new {}s.", entities.size(), entityClass.getSimpleName());
        return created;
    }

    protected void assureCreationInvariants(final Collection<? extends T> entities) {
        notEmpty(entities, "entities for creation cannot be null or empty.");
        entities.forEach(this::assureCreationInvariants);
    }

    @Override
    @Transactional
    public T update(final T entity) {
        notNull(entity, "entity for update cannot be null.");
        logger.trace("Updating {} with id:'{}'...", entityClass.getSimpleName(), entity.getId());

        assureUpdateInvariants(entity);
        final T updated = crudRepository.save(entity);

        logger.debug("Successfully updated {} with id:'{}'.", entityClass.getSimpleName(), entity.getId());
        return updated;
    }

    protected void assureUpdateInvariants(final T entity) {
        notNull(entity, "entity for update cannot be null.");
        final I id = entity.getId();
        notNull(id, "id of update entity cannot be null.");
    }

    @Override
    @Transactional
    public List<T> updateAll(final Collection<? extends T> entities) {
        notEmpty(entities, "entities for update cannot be null or empty.");
        entities.forEach(entity -> notNull(entity, "entity for update cannot be null."));
        logger.trace("Updating {} {}s...", entities.size(), entityClass.getSimpleName());

        assureUpdateInvariants(entities);
        final List<T> created = StreamSupport
                .stream(crudRepository.saveAll(entities).spliterator(), false)
                .collect(Collectors.toList());

        logger.debug("Successfully updated {} {}s.", entities.size(), entityClass.getSimpleName());
        return created;
    }

    protected void assureUpdateInvariants(final Collection<? extends T> entities) {
        notEmpty(entities, "entities for update cannot be null or empty.");
        entities.forEach(this::assureUpdateInvariants);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> find(final I id) {
        notNull(id, "id for find by id cannot be null.");
        logger.trace("Finding {} by id:'{}'...", entityClass.getSimpleName(), id);

        final Optional<T> entityOpt = crudRepository.findById(id);

        if (entityOpt.isPresent()) {
            logger.debug("Successfully found {} by id:'{}'.", entityClass.getSimpleName(), id);
        } else {
            logger.debug("No {} has been found by id:'{}'.", entityClass.getSimpleName(), id);
        }

        return entityOpt;
    }

    @Override
    @Transactional(readOnly = true)
    public T get(final I id) {
        notNull(id, "id for get by id cannot be null.");
        logger.trace("Getting {} by id:'{}'...", entityClass.getSimpleName(), id);

        final Optional<T> entityOpt = find(id);

        if (entityOpt.isPresent()) {
            logger.debug("Successfully got {} by id:'{}'.", entityClass.getSimpleName(), id);
            return entityOpt.get();
        }

        logger.debug("No {} has been found by id:'{}'. Nothing to get.", entityClass.getSimpleName(), id);
        throw ResourceNotFoundException.createInstance(entityClass, "id", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAll() {
        logger.trace("Getting all {}s...", entityClass.getSimpleName());

        final List<T> entities = StreamSupport
                .stream(crudRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        logger.debug("Successfully got {} {}s.", entities.size(), entityClass.getSimpleName());
        return entities;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getByIds(final Collection<? extends I> ids) {
        notEmpty(ids, "ids for get by ids cannot be null or empty.");
        ids.forEach(id -> notNull(id, "id for get by id cannot be null."));
        logger.trace("Getting {} {}s by ids...", ids.size(), entityClass.getSimpleName());

        final List<T> entities = StreamSupport
                .stream(crudRepository.findAllById(new ArrayList<>(ids)).spliterator(), false)
                .collect(Collectors.toList());

        logger.debug(
                "Successfully got {} from {} requested {}s by ids.",
                entities.size(),
                ids.size(),
                entityClass.getSimpleName()
        );
        return entities;
    }

    @Override
    @Transactional
    public void delete(final I id) {
        notNull(id, "id for delete by id cannot be null.");
        logger.trace("Deleting {} by id:'{}'...", entityClass.getSimpleName(), id);

        doDelete(id);

        logger.debug("Successfully deleted {} by id:'{}'.", entityClass.getSimpleName(), id);
    }

    protected void doDelete(final I id) {
        notNull(id, "id for delete by id cannot be null.");
        crudRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        logger.trace("Deleting all {}s...", entityClass.getSimpleName());

        crudRepository.deleteAll();

        logger.debug("Successfully deleted all {}s.", entityClass.getSimpleName());
    }

    @Override
    @Transactional
    public void deleteByIds(final Collection<? extends I> ids) {
        notEmpty(ids, "ids for delete by ids cannot be null or empty.");
        ids.forEach(id -> notNull(id, "id for delete by id cannot be null."));
        logger.trace("Deleting {} {}s by ids...", ids.size(), entityClass.getSimpleName());

        doDelete(ids);

        logger.debug("Successfully deleted {} {}s by ids.", ids.size(), entityClass.getSimpleName());
    }

    protected void doDelete(final Collection<? extends I> ids) {
        notEmpty(ids, "ids for delete by ids cannot be null or empty.");
        ids.forEach(this::doDelete);
    }
}
