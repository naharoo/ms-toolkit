package com.naharoo.commons.mstoolkit.crudservices;

import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CrudService<T, I> {

    @NonNull
    T create(@NonNull T object);

    @NonNull
    List<T> createAll(@NonNull Collection<? extends T> objects);

    @NonNull
    T update(@NonNull T object);

    @NonNull
    List<T> updateAll(@NonNull Collection<? extends T> objects);

    @NonNull
    Optional<T> find(@NonNull I id);

    @NonNull
    T get(@NonNull I id);

    @NonNull
    List<T> getAll();

    @NonNull
    List<T> getByIds(@NonNull Collection<? extends I> ids);

    void delete(@NonNull I id);

    void deleteAll();

    void deleteByIds(@NonNull Collection<? extends I> ids);
}
