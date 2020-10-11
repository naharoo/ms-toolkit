package com.naharoo.commons.mstoolkit.crudservices;

import java.io.Serializable;

/**
 * Marker interface for all identifiable entities which will be managed
 * by {@link com.naharoo.commons.mstoolkit.crudservices.CrudService}
 *
 * @param <I> the type of an <code>id</code> field
 */
@FunctionalInterface
public interface Identifiable<I extends Serializable> extends Serializable {
    I getId();
}
