/*******************************************************************************
 * Copyright (c) 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.ibm.ws.repository.resolver;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
public class BreadthFirstWalker {

    public static <T> void walk(T root, Consumer<T> process, Function<T, List<? extends T>> getChildren) {
        Deque<WalkElement<T>> queue = new ArrayDeque<>();
        queue.add(new WalkElement<>(root, null));

        while (!queue.isEmpty()) {
            WalkElement<T> current = queue.pollFirst();
            process.accept(current.item());

            for (T child : getChildren.apply(current.item())) {
                if (!current.hasAncestor(child)) { // check for loops
                    queue.addLast(new WalkElement<T>(child, current));
                }
            }
        }
    }

    static class WalkElement<T> {
        private final T item;
        private final WalkElement<T> parent;

        public WalkElement(T item, WalkElement<T> parent) {
            super();
            this.item = item;
            this.parent = parent;
        }

        public T item() {
            return item;
        }

        public boolean hasAncestor(T search) {
            WalkElement<T> current = this;
            while (current != null) {
                if (current.item == search) {
                    return true;
                }
                current = current.parent;
            }
            return false;
        }

    }

}
