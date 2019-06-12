/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.applibtest.impl;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterTypeDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import com.ibm.ws.cdi.extension.WebSphereCDIExtension;

/**
 *
 */
@Component(service = WebSphereCDIExtension.class, configurationPolicy = ConfigurationPolicy.IGNORE)
public class AppLibTestExtension implements WebSphereCDIExtension, Extension {

    @SuppressWarnings("unused")
    private void addTypes(@Observes AfterTypeDiscovery event, BeanManager bm) {
        event.addAnnotatedType(BeanProvider.class, "AppLibTestExtension");
    }
}
