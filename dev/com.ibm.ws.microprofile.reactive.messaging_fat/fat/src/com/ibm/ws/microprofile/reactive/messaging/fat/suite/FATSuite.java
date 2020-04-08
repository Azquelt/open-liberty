/*******************************************************************************
 * Copyright (c) 2019, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.microprofile.reactive.messaging.fat.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.ws.microprofile.reactive.messaging.fat.blocking.BlockingTest;

@RunWith(Suite.class)
@SuiteClasses({
//                PlaintextTests.class,
//                TlsTests.class,
//                SaslPlainTests.class,
//                KafkaTestClientProviderTest.class,
//                LoginModuleClassloadingTest.class,
//                KafkaNoLibTest.class,
//                JsonbTest.class,
                BlockingTest.class
})
public class FATSuite {

}
