/*******************************************************************************
 * Copyright (c) 2017, 2024 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package io.openliberty.jakartaee11.internal.apps.jakartaee11.web.jsonb;

public class Team {
    public String name;
    public int size;
    public float winLossRatio;

    @Override
    public String toString() {
        return "name=" + name + "  size=" + size + "  winLossRatio=" + winLossRatio;
    }
}
