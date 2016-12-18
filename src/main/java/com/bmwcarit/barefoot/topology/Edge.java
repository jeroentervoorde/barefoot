package com.bmwcarit.barefoot.topology;

import java.io.Serializable;

/**
 * Created by jeroen on 12/17/16.
 */
public interface Edge extends Serializable {
    long id();

    long source();

    long target();
}
