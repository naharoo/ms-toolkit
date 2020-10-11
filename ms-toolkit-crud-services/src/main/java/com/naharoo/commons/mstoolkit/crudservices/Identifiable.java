package com.naharoo.commons.mstoolkit.crudservices;

import java.io.Serializable;

public interface Identifiable<I extends Serializable> extends Serializable {

    I getId();
}
