/*******************************************************************************
 * Copyright (c) 2014, 2019 IBM Corporation and others.
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
package com.ibm.ws.annocache.targets.delta;

import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.ws.annocache.targets.internal.AnnotationTargetsImpl_Factory;
import com.ibm.wsspi.annocache.util.Util_BidirectionalMapDelta;
import com.ibm.wsspi.annocache.util.Util_PrintLogger;

public interface TargetsDelta_Annotations {
    String getHashText();

    void log(Logger useLogger);
    void log(PrintWriter writer);
    void log(Util_PrintLogger useLogger);

    void describe(String prefix, List<String> nonNull);

    //

    AnnotationTargetsImpl_Factory getFactory();

    //

    Util_BidirectionalMapDelta getPackageAnnotationDelta();
    Util_BidirectionalMapDelta getClassAnnotationDelta();
    Util_BidirectionalMapDelta getFieldAnnotationDelta();
    Util_BidirectionalMapDelta getMethodAnnotationDelta();

    boolean isNull();
    boolean isNull(boolean ignoreRemovedPackages);
}
