/*******************************************************************************
 * Copyright (c) 2018, 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.security.jwtsso.utils;

import com.ibm.websphere.ras.Tr;
import com.ibm.websphere.ras.TraceComponent;

public class JwtSsoConstants {
	private static final TraceComponent tc = Tr.register(JwtSsoConstants.class);

	public static final String CFG_KEY_COOKIENAME = "cookieName";
	public static final String CFG_KEY_HTTPONLYCOOKIES = "httpOnlyCookies";
	public static final String CFG_KEY_SSOUSEDOMAINFROMURL = "ssoUseDomainFromURL";
	public static final String CFG_KEY_SSODOMAINNAMES = "ssoDomainNames";
	public static final String CFG_KEY_SETCOOKIEPATHTOWEBAPPCONTEXTPATH = "setCookiePathToWebAppContextRoot";
	public static final String CFG_KEY_INCLUDELTPACOOKIE = "includeLtpaCookie";
	public static final String CFG_USE_LTPA_IF_JWT_ABSENT = "useLtpaIfJwtAbsent";
	public static final String CFG_KEY_COOKIESECUREFLAG = "setCookieSecureFlag";
	public static final String CFG_KEY_DISABLE_JWT_COOKIE = "disableJwtCookie";
	public static final String CFG_KEY_AUTH_FILTER_REF = "authFilterRef";

	public static final String CFG_KEY_JWTBUILDERREF = "jwtBuilderRef";
	public static final String CFG_KEY_JWTCONSUMERREF = "mpjwtConsumerRef";
	public static final String DEFAULT_JWTSSO_ID = "defaultJwtSso";
	public static final String GROUP_PREFIX = "group:";
	public static final String TOKEN_TYPE_JWT = "JWT";
	public static final String UNAUTHENTICATED = "UNAUTHENTICATED";

}
