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
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public class EdgeTest {
    @Test
    public void testSuccessors() {
        AbstractEdge edge0 = new EdgeImpl(0, 0, 1);
        AbstractEdge edge1 = new EdgeImpl(1, 1, 0);
        AbstractEdge edge2 = new EdgeImpl(2, 1, 2);
        AbstractEdge edge3 = new EdgeImpl(3, 2, 1);
        AbstractEdge edge4 = new EdgeImpl(4, 3, 1);
        AbstractEdge edge6 = new EdgeImpl(6, 4, 0);
        AbstractEdge edge7 = new EdgeImpl(7, 0, 4);
        AbstractEdge edge8 = new EdgeImpl(8, 0, 5);

        edge0.successor(edge1);
        edge0.neighbor(edge7);
        edge1.successor(edge0);
        edge1.neighbor(edge2);
        edge2.successor(edge3);
        edge2.neighbor(edge1);
        edge3.successor(edge2);
        edge3.neighbor(edge3);
        edge4.successor(edge1);
        edge4.neighbor(edge4);
        edge6.successor(edge7);
        edge6.neighbor(edge6);
        edge7.successor(edge6);
        edge7.neighbor(edge8);
        edge8.successor(null);
        edge8.neighbor(edge0);

        {
            Set<Long> verify = new HashSet<>();
            verify.add(1L);
            verify.add(2L);

            Iterator<Edge> successors = edge0.successors();
            Set<Long> out = new HashSet<>();

            while (successors.hasNext()) {
                Edge successor = successors.next();
                assertTrue(verify.contains(successor.id()));
                out.add(successor.id());
            }
            assertEquals(verify.size(), out.size());
        }
        {
            Set<Long> verify = new HashSet<>();
            verify.add(1L);
            verify.add(2L);

            Iterator<Edge> successors = edge3.successors();
            Set<Long> out = new HashSet<>();

            while (successors.hasNext()) {
                Edge successor = successors.next();
                assertTrue(verify.contains(successor.id()));
                out.add(successor.id());
            }
            assertEquals(verify.size(), out.size());
        }
        {
            Set<Long> verify = new HashSet<>();
            verify.add(0L);
            verify.add(7L);
            verify.add(8L);

            Iterator<Edge> successors = edge1.successors();
            Set<Long> out = new HashSet<>();

            while (successors.hasNext()) {
                Edge successor = successors.next();
                assertTrue(verify.contains(successor.id()));
                out.add(successor.id());
            }
            assertEquals(verify.size(), out.size());
        }
        {
            Set<Long> verify = new HashSet<>();

            Iterator<Edge> successors = edge8.successors();
            Set<Long> out = new HashSet<>();

            while (successors.hasNext()) {
                Edge successor = successors.next();
                assertTrue(verify.contains(successor.id()));
                out.add(successor.id());
            }
            assertEquals(verify.size(), out.size());
        }
        {
            Set<Long> verify = new HashSet<>();
            verify.add(3L);

            Iterator<Edge> successors = edge2.successors();
            Set<Long> out = new HashSet<>();

            while (successors.hasNext()) {
                Edge successor = successors.next();
                assertTrue(verify.contains(successor.id()));
                out.add(successor.id());
            }
            assertEquals(verify.size(), out.size());
        }
    }
}
