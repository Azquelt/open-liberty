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

import java.util.Iterator;
import java.util.Stack;

import com.ibm.ws.cdi.CDIException;
import com.ibm.ws.cdi.internal.interfaces.WebSphereBeanDeploymentArchive;

public class BeanDeploymentArchiveVisitor {

    /**
     * This method implements a depth first search over all BDAs (excluding Runtime Extensions) and their children
     * to ensure children are scanned before parents.
     *
     * Since the dependency graph of BDAs is cyclic this algorithm can be nondeterministic in which order mutual
     * dependencies are scanned.
     *
     * How it works:
     *
     * LOOP START
     * Look at the current element from the iterator at the top of the stack, marking it visited:
     * If it is not a runtime extension get an iterator of all unvisited children and put it on the stack GOTO LOOP START
     * If it has already been visited scan the current element, then advance the iterator. GOTO LOOP START.
     * If the iterator is now empty, pop the stack. If the stack is empty RETURN, else GOTO LOOP START.
     *
     */
    public void visit(WebSphereBeanDeploymentArchive root) throws CDIException {
        Stack<StackElement> stack = new Stack<>();

        if (!root.hasBeenVisited()) {
            stack.push(new StackElement(root, root.visit()));
        }

        while (!stack.isEmpty()) {
            StackElement e = stack.peek();

            WebSphereBeanDeploymentArchive child = nextUnvisitedBda(e.childIterator);
            if (child != null) {
                // If there are unvisited children, they must be placed on the stack first
                // so that they will be scanned before us
                stack.push(new StackElement(child, child.visit()));
            } else {
                // When all our direct children have been visited and scanned, we can be scanned
                if (!e.bda.hasBeenVisited()) {
                    e.bda.visit();
                }
                stack.pop();
            }
        }
    }

    private static WebSphereBeanDeploymentArchive nextUnvisitedBda(Iterator<WebSphereBeanDeploymentArchive> i) {
        while (i.hasNext()) {
            WebSphereBeanDeploymentArchive bda = i.next();
            if (!bda.hasBeenVisited()) {
                return bda;
            }
        }
        return null;
    }

    private final class StackElement {
        private final WebSphereBeanDeploymentArchive bda;
        private final Iterator<WebSphereBeanDeploymentArchive> childIterator;

        public StackElement(WebSphereBeanDeploymentArchive bda, Iterator<WebSphereBeanDeploymentArchive> childIterator) {
            this.bda = bda;
            this.childIterator = childIterator;
        }
    }

}
