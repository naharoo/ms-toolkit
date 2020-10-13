package com.naharoo.commons.mstoolkit.domainmodel;

import java.io.Serializable;

/**
 * Marker interface for all identifiable entities, domain objects, transfer objects
 *
 * @param <I> the type of an <code>id</code> field
 */
@FunctionalInterface
public interface Identifiable<I extends Serializable> extends Serializable {
    I getId();
}
