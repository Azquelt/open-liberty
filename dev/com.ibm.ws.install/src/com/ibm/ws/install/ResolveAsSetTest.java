/*******************************************************************************n * Copyright (c) 2019 IBM Corporation and others.n * All rights reserved. This program and the accompanying materialsn * are made available under the terms of the Eclipse Public License v1.0n * which accompanies this distribution, and is available atn * http://www.eclipse.org/legal/epl-v10.htmln *n * Contributors:n *     IBM Corporation - initial API and implementationn *******************************************************************************/
package com.ibm.ws.install;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.ibm.ws.install.internal.Product;
import com.ibm.ws.kernel.feature.provisioning.ProvisioningFeatureDefinition;
import com.ibm.ws.kernel.productinfo.ProductInfo;
import com.ibm.ws.product.utility.extension.ifix.xml.IFixInfo;
import com.ibm.ws.repository.connections.ProductDefinition;
import com.ibm.ws.repository.connections.RepositoryConnection;
import com.ibm.ws.repository.connections.RepositoryConnectionList;
import com.ibm.ws.repository.connections.RestRepositoryConnection;
import com.ibm.ws.repository.connections.RestRepositoryConnectionProxy;
import com.ibm.ws.repository.connections.liberty.MainRepository;
import com.ibm.ws.repository.connections.liberty.ProductInfoProductDefinition;
import com.ibm.ws.repository.exceptions.RepositoryBackendIOException;
import com.ibm.ws.repository.resolver.RepositoryResolver;
import com.ibm.ws.repository.resources.EsaResource;
import com.ibm.ws.repository.resources.RepositoryResource;

/**
 *
 */
public class ResolveAsSetTest {

    // Initialize with the location of the Liberty kernel
    static Product product = new Product(new File("C:\\dev\\installations\\liberty\\wlp-kernel-19.0.0.7_p\\wlp"));

    /**
     * @param args
     * @throws InstallException
     * @throws RepositoryBackendIOException
     */
    public static void main(String[] args) {

        try {
            Collection<String> assetNamesProcessed = new ArrayList<String>();
            assetNamesProcessed.add("com.ibm.websphere.appserver.jsp-2.3");
//            assetNamesProcessed.add("com.ibm.websphere.appserver.wmqJmsClient-2.0");
//            assetNamesProcessed.add("com.ibm.websphere.appserver.jndi-1.0");
//            assetNamesProcessed.add("com.ibm.websphere.appserver.localConnector-1.0");
//            assetNamesProcessed.add("com.ibm.websphere.appserver.jsf-2.3");
//            assetNamesProcessed.add("com.ibm.websphere.appserver.jaxws-2.2");
//            assetNamesProcessed.add("com.ibm.websphere.appserver.ssl-1.0");
//            assetNamesProcessed.add("com.ibm.websphere.appserver.passwordUtilities-1.0");
//            assetNamesProcessed.add("com.ibm.websphere.appserver.jdbc-4.2");

            RepositoryConnectionList loginInfo = getRepositoryConnectionList(assetNamesProcessed);

            Collection<ProductDefinition> productDefinitions = new HashSet<ProductDefinition>();
            for (ProductInfo productInfo : ProductInfo.getAllProductInfo().values()) {
                productDefinitions.add(new ProductInfoProductDefinition(productInfo));
            }

            RepositoryResolver resolver;
            Collection<List<RepositoryResource>> installResources;
            Map<String, ProvisioningFeatureDefinition> installedFeatureDefinitions = product.getFeatureDefinitions();
            Collection<ProvisioningFeatureDefinition> installedFeatures = installedFeatureDefinitions.values();
            Collection<IFixInfo> installedIFixes = new HashSet<IFixInfo>();
            resolver = new RepositoryResolver(productDefinitions, installedFeatures, installedIFixes, loginInfo);
            System.out.println("*** Calling old resolver resolve api *** ");
            installResources = resolver.resolve(assetNamesProcessed);
            System.out.println("Old resolver resolve api returns :");
            printFeatures(installResources);
            System.out.println("*** Calling new resolver resolveAsSet api *** ");
            installResources = resolver.resolveAsSet(assetNamesProcessed);
            System.out.println("New resolver resolveAsSet api returns :");
            printFeatures(installResources);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Get Liberty Repository connection
    private static RepositoryConnectionList getRepositoryConnectionList(Collection<String> featureNames) throws InstallException, RepositoryBackendIOException {
        List<RepositoryConfig> repositoryConfigs = RepositoryConfigUtils.getRepositoryConfigs(RepositoryConfigUtils.loadRepoProperties());
        final RestRepositoryConnectionProxy proxy = RepositoryConfigUtils.getProxyInfo(RepositoryConfigUtils.loadRepoProperties());
        List<RepositoryConnection> loginEntries = new ArrayList<RepositoryConnection>(repositoryConfigs.size());
        RepositoryConnection lie = MainRepository.createConnection(proxy);

        String ua = System.getProperty(InstallConstants.UA_PROPERTY_NAME);
        String productVersion = product.getProductVersion();
        String productEdition = product.getProductEdition();
        if (ua != null && !ua.isEmpty()) {
            ua = String.format(InstallConstants.USER_AGENT, productVersion, productEdition, ua);
        }
        if (lie != null) {
            if (ua != null && !ua.isEmpty()) {
                ((RestRepositoryConnection) lie).setUserAgent(ua);
            }
            loginEntries.add(lie);
        }
        return new RepositoryConnectionList(loginEntries);
    }

    private static void printFeatures(Collection<List<RepositoryResource>> installResources) {
        System.out.println("");
        for (List<RepositoryResource> mrList : installResources) {
            for (RepositoryResource installResource : mrList) {
                EsaResource esa = (EsaResource) installResource;
                System.out.println(esa.getProvideFeature());
            }
        }
        System.out.println("");
    }

}
