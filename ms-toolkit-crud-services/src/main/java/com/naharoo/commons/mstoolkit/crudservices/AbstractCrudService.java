package com.naharoo.commons.mstoolkit.crudservices;

import com.naharoo.commons.mstoolkit.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

public abstract class AbstractCrudService<T, I> implements CrudService<T, I> {

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
    public T create(final T object) {
        notNull(object, "object for creation cannot be null.");
        logger.trace("Creating a new {}...", entityClass.getSimpleName());

        assureCreationInvariants(object);
        final T created = crudRepository.save(object);

        logger.debug("Successfully created a new {}.", entityClass.getSimpleName());
        return created;
    }

    protected void assureCreationInvariants(final T object) {
        notNull(object, "object for creation cannot be null.");
    }

    @Override
    @Transactional
    public List<T> createAll(final Collection<? extends T> objects) {
        notEmpty(objects, "objects for creation cannot be null or empty.");
        logger.trace("Creating {} new {}s...", objects.size(), entityClass.getSimpleName());

        assureCreationInvariants(objects);
        final List<T> created = StreamSupport
                .stream(crudRepository.saveAll(objects).spliterator(), false)
                .collect(Collectors.toList());

        logger.debug("Successfully created {} new {}s.", objects.size(), entityClass.getSimpleName());
        return created;
    }

    protected void assureCreationInvariants(final Collection<? extends T> objects) {
        notEmpty(objects, "objects for creation cannot be null or empty.");
        objects.forEach(this::assureCreationInvariants);
    }

    @Override
    @Transactional
    public T update(final T object) {
        notNull(object, "object for update cannot be null.");
        logger.trace("Updating {}...", entityClass.getSimpleName());

        assureUpdateInvariants(object);
        final T updated = crudRepository.save(object);

        logger.debug("Successfully updated {}.", entityClass.getSimpleName());
        return updated;
    }

    protected void assureUpdateInvariants(final T object) {
        notNull(object, "object for update cannot be null.");
    }

    @Override
    @Transactional
    public List<T> updateAll(final Collection<? extends T> objects) {
        notEmpty(objects, "objects for update cannot be null or empty.");
        logger.trace("Updating {} {}s...", objects.size(), entityClass.getSimpleName());

        assureUpdateInvariants(objects);
        final List<T> created = StreamSupport
                .stream(crudRepository.saveAll(objects).spliterator(), false)
                .collect(Collectors.toList());

        logger.debug("Successfully updated {} {}s.", objects.size(), entityClass.getSimpleName());
        return created;
    }

    protected void assureUpdateInvariants(final Collection<? extends T> objects) {
        notEmpty(objects, "objects for update cannot be null or empty.");
        objects.forEach(this::assureUpdateInvariants);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> find(final I id) {
        notNull(id, "id for find by id cannot be null.");
        logger.trace("Finding {} by id:'{}'...", entityClass.getSimpleName(), id);

        final Optional<T> objectOpt = crudRepository.findById(id);

        if (objectOpt.isPresent()) {
            logger.debug("Successfully found {} by id:'{}'.", entityClass.getSimpleName(), id);
        } else {
            logger.debug("No {} has been found by id:'{}'.", entityClass.getSimpleName(), id);
        }

        return objectOpt;
    }

    @Override
    @Transactional(readOnly = true)
    public T get(final I id) {
        notNull(id, "id for get by id cannot be null.");
        logger.trace("Getting {} by id:'{}'...", entityClass.getSimpleName(), id);

        final Optional<T> objectOpt = find(id);

        if (objectOpt.isPresent()) {
            logger.debug("Successfully got {} by id:'{}'.", entityClass.getSimpleName(), id);
            return objectOpt.get();
        }

        logger.debug("No {} has been found by id:'{}'. Nothing to get.", entityClass.getSimpleName(), id);
        throw ResourceNotFoundException.createInstance(entityClass, "id", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAll() {
        logger.trace("Getting all {}s...", entityClass.getSimpleName());

        final List<T> objects = StreamSupport
                .stream(crudRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        logger.debug("Successfully got {} {}s.", objects.size(), entityClass.getSimpleName());
        return objects;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getByIds(final Collection<? extends I> ids) {
        notEmpty(ids, "ids for get by ids cannot be null or empty.");
        logger.trace("Getting {} {}s by ids...", ids.size(), entityClass.getSimpleName());

        final List<T> objects = StreamSupport
                .stream(crudRepository.findAllById(new ArrayList<>(ids)).spliterator(), false)
                .collect(Collectors.toList());

        logger.debug(
                "Successfully got {} from {} requested {}s by ids.",
                objects.size(),
                ids.size(),
                entityClass.getSimpleName()
        );
        return objects;
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
        logger.trace("Deleting {} {}s by ids...", ids.size(), entityClass.getSimpleName());

        doDelete(ids);

        logger.debug("Successfully deleted {} {}s by ids.", ids.size(), entityClass.getSimpleName());
    }

    protected void doDelete(final Collection<? extends I> ids) {
        notEmpty(ids, "ids for delete by ids cannot be null or empty.");
        ids.forEach(this::doDelete);
    }
}
