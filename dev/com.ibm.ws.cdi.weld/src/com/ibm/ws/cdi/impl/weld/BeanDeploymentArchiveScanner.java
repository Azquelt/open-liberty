/*******************************************************************************
 * Copyright (c) 2024 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.cdi.impl.weld;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;

import com.ibm.ws.cdi.CDIException;
import com.ibm.ws.cdi.internal.interfaces.WebSphereBeanDeploymentArchive;

public class BeanDeploymentArchiveScanner {

    /**
     * This method implements a depth first search over all BDAs (excluding Runtime Extensions) and their children
     * to ensure children are scanned before parents.
     * <p>
     * Since the dependency graph of BDAs is cyclic this algorithm can be nondeterministic in which order mutual
     * dependencies are scanned.
     *
     * @param root the BDA to start the scan from
     */
    public static void recursiveScan(WebSphereBeanDeploymentArchive root) throws CDIException {
        Deque<StackElement> stack = new ArrayDeque<>();

        if (!root.hasBeenVisited()) {
            stack.push(new StackElement(Collections.singletonList(root).iterator()));
        }

        while (!stack.isEmpty()) {
            StackElement e = stack.peek();

            if (e.currentBda != null) {
                // We've just popped a layer, we can now scan this BDA
                if (!e.currentBda.hasBeenScanned()) {
                    e.currentBda.scan();
                }
                e.currentBda = null;
            } else {
                // We're working through the list, find the next bda to visit
                e.currentBda = nextUnvisitedBda(e.bdas);
                if (e.currentBda == null) {
                    // No next bda? We've finished this stack layer
                    stack.pop();
                } else {
                    stack.push(new StackElement(e.currentBda.visit()));
                }
            }
        }
    }

    /**
     * Takes BDAs from an iterator until one is found where {@link WebSphereBeanDeploymentArchive#hasBeenVisited()} returns {@code false} and returns it.
     *
     * @param i the iterator
     * @return the next unvisited BDA retrieved from the iterator, or {@code null} if none is found
     */
    private static WebSphereBeanDeploymentArchive nextUnvisitedBda(Iterator<WebSphereBeanDeploymentArchive> i) {
        while (i.hasNext()) {
            WebSphereBeanDeploymentArchive bda = i.next();
            if (!bda.hasBeenVisited()) {
                return bda;
            }
        }
        return null;
    }

    private static final class StackElement {
        private WebSphereBeanDeploymentArchive currentBda;
        private final Iterator<WebSphereBeanDeploymentArchive> bdas;

        private StackElement(Iterator<WebSphereBeanDeploymentArchive> bdas) {
            this.bdas = bdas;
            this.currentBda = null;
        }
    }

}
