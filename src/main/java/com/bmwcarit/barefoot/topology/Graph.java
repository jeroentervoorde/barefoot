package com.bmwcarit.barefoot.topology;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jeroen on 12/17/16.
 */
public interface Graph<E extends AbstractEdge<E>> extends Serializable {
    E get(long id);

    int size();

    Iterator<E> edges();

    Set<Set<E>> components();
}
