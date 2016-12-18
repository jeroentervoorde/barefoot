/*
 * Copyright (C) 2015, BMW Car IT GmbH
 *
 * Author: Sebastian Mattheis <sebastian.mattheis@bmw-carit.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.bmwcarit.barefoot.topology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class GraphTest {

    @Test
    public void testConstruction() {
        {
            GraphImpl<EdgeImpl> graph = new GraphImpl<>();
            graph.add(new EdgeImpl(0, 0, 0));
            graph.construct();

            EdgeImpl edge = graph.get(0);
            assertEquals(edge.id(), edge.successor().id());
            assertEquals(edge.id(), edge.neighbor().id());
        }
        {
            GraphImpl<EdgeImpl> graph = new GraphImpl<>();

            graph.add(new EdgeImpl(0, 0, 1));
            graph.add(new EdgeImpl(1, 1, 0));
            graph.add(new EdgeImpl(2, 1, 2));
            graph.add(new EdgeImpl(3, 2, 1));
            graph.add(new EdgeImpl(4, 3, 1));
            graph.add(new EdgeImpl(6, 4, 0));
            graph.add(new EdgeImpl(7, 0, 4));
            graph.add(new EdgeImpl(8, 0, 5));

            graph.construct();

            Map<Long, Set<Long>> sources = new HashMap<>();

            sources.put(0L, new HashSet<>(Arrays.asList(0L, 7L, 8L)));
            sources.put(1L, new HashSet<>(Arrays.asList(1L, 2L)));
            sources.put(2L, new HashSet<>(Arrays.asList(3L)));
            sources.put(3L, new HashSet<>(Arrays.asList(4L)));
            sources.put(4L, new HashSet<>(Arrays.asList(6L)));
            sources.put(5L, new HashSet<Long>());

            Iterator<EdgeImpl> edges = graph.edges();
            while (edges.hasNext()) {
                EdgeImpl edge = edges.next();

                Iterator<EdgeImpl> outs = edge.successors();
                int count = 0;

                while (outs.hasNext()) {
                    assertTrue(sources.get(edge.target()).contains(outs.next().id()));
                    count += 1;
                }

                assertEquals(sources.get(edge.target()).size(), count);
            }
        }
    }

    @Test
    public void testComponents() {
        GraphImpl<EdgeImpl> graph = new GraphImpl<>();

        // Component with dead-end edge.
        graph.add(new EdgeImpl(0, 0, 1));
        graph.add(new EdgeImpl(1, 1, 0));
        graph.add(new EdgeImpl(2, 1, 2));
        graph.add(new EdgeImpl(3, 2, 1));
        graph.add(new EdgeImpl(4, 3, 1));
        graph.add(new EdgeImpl(6, 4, 0));
        graph.add(new EdgeImpl(7, 0, 4));
        graph.add(new EdgeImpl(8, 0, 5));

        // Component with circle.
        graph.add(new EdgeImpl(9, 6, 7));
        graph.add(new EdgeImpl(10, 7, 8));
        graph.add(new EdgeImpl(11, 8, 9));
        graph.add(new EdgeImpl(12, 9, 6));

        // Component with self-loop edge.
        graph.add(new EdgeImpl(13, 10, 10));

        // Component with only dead-end edges.
        graph.add(new EdgeImpl(14, 11, 12));
        graph.add(new EdgeImpl(15, 11, 13));
        graph.add(new EdgeImpl(16, 11, 14));

        graph.add(new EdgeImpl(17, 15, 16));
        graph.add(new EdgeImpl(18, 16, 17));
        graph.add(new EdgeImpl(19, 17, 18));
        graph.add(new EdgeImpl(20, 18, 19));
        graph.add(new EdgeImpl(21, 19, 20));
        graph.add(new EdgeImpl(22, 20, 21));
        graph.add(new EdgeImpl(23, 21, 22));

        graph.construct();

        Set<Set<Long>> sets = new HashSet<>();

        sets.add(new HashSet<>(Arrays.asList(0L, 1L, 2L, 3L, 4L, 6L, 7L, 8L)));
        sets.add(new HashSet<>(Arrays.asList(9L, 10L, 11L, 12L)));
        sets.add(new HashSet<>(Arrays.asList(13L)));
        sets.add(new HashSet<>(Arrays.asList(14L, 15L, 16L)));
        sets.add(new HashSet<>(Arrays.asList(17L, 18L, 19L, 20L, 21L, 22L, 23L)));

        Set<Set<EdgeImpl>> components = graph.components();

        assertEquals(sets.size(), components.size());
        for (Set<EdgeImpl> component : components) {
            Set<Long> set = null;

            for (Set<Long> set_ : sets) {
                if (set_.contains(component.iterator().next().id()))
                    set = set_;
            }

            assertNotNull(set);
            assertEquals(set.size(), component.size());

            for (Edge edge : component) {
                assertTrue(set.contains(edge.id()));
            }
        }
    }
}
