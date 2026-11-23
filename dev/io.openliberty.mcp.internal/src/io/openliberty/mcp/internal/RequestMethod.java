/*******************************************************************************
 * Copyright (c) 2025, 2026 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package io.openliberty.mcp.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 */
public enum RequestMethod {

    TOOLS_CALL("tools/call", MethodType.REQUEST),
    TOOLS_LIST("tools/list", MethodType.REQUEST),
    INITIALIZE("initialize", MethodType.PRE_INIT_REQUEST, McpProtocolVersion.V_2026_07_28),
    INITIALIZED("notifications/initialized", MethodType.NOTIFICATION, McpProtocolVersion.V_2026_07_28),
    CANCELLED("notifications/cancelled", MethodType.NOTIFICATION),
    PING("ping", MethodType.PRE_INIT_REQUEST, McpProtocolVersion.V_2026_07_28);

    private static final Map<String, RequestMethod> METHODS_BY_NAME;
    private final String methodName;
    private final MethodType methodType;
    private final McpProtocolVersion removedIn;

    public enum MethodType {
        PRE_INIT_REQUEST,
        REQUEST,
        NOTIFICATION
    }

    RequestMethod(String methodName, MethodType methodType) {
        this(methodName, methodType, null);
    }

    /**
     * @param methodName the JSON-RPC method name
     * @param methodType the type of method
     * @param removedIn the first protocol version in which this method no longer exists, or {@code null} if it has not been removed
     */
    RequestMethod(String methodName, MethodType methodType, McpProtocolVersion removedIn) {
        this.methodName = methodName;
        this.methodType = methodType;
        this.removedIn = removedIn;
    }

    static {
        METHODS_BY_NAME = new HashMap<>();
        for (RequestMethod m : values()) {
            METHODS_BY_NAME.put(m.getMethodName(), m);
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public boolean isPreInit() {
        return methodType == MethodType.PRE_INIT_REQUEST;
    }

    /**
     * Returns whether this method exists in the given protocol version.
     *
     * @param version the protocol version, or {@code null} if it is not known
     * @return {@code false} if {@code version} is the version in which the method was removed or a newer version,
     *         otherwise {@code true}
     */
    public boolean isSupportedIn(McpProtocolVersion version) {
        if (removedIn == null || version == null) {
            return true;
        }
        // The method exists in any version which is older than the one that removed it
        return removedIn.isNewerThan(version);
    }

    public static RequestMethod getForMethodName(String methodName) throws NoSuchElementException {
        RequestMethod result = METHODS_BY_NAME.get(methodName);
        if (result == null) {
            throw new NoSuchElementException(methodName);
        }
        return result;
    }
}
