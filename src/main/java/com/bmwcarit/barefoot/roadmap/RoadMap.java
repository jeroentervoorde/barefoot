package com.bmwcarit.barefoot.roadmap;

import com.bmwcarit.barefoot.spatial.SpatialIndex;
import com.bmwcarit.barefoot.topology.Graph;

import java.io.Serializable;

public interface RoadMap extends Serializable, Graph<Road> {
    SpatialIndex<RoadPoint> spatial();
}
