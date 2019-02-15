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
package componenttest.matchers;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.internal.ReflectiveTypeFinder;

/**
 * Extension of {@link TypeSafeDiagnosingMatcher} which works in FAT tests without requiring extra permissions
 * <p>
 * TypeSafeDiagnosingMatcher uses reflection in order to work out which types the matcher should accept. When it is used on the server in FAT tests, this requires the app to have
 * the {@code accessDeclaredMembers} permission.
 * <p>
 * FAT Tests can avoid needing to grant that permission by extending this class instead.
 * <p>
 * Notes: This class avoids needing the permission by (a) being part of the componenttest feature, not part of the test app and (b) extending hamcrest's ReflectiveTypeFinder to
 * wrap the type lookup in a {@code doPrivileged} block.
 */
public abstract class LibertyTypeSafeDiagnosingMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    private static SecureReflectiveTypeFinder SECURE_TYPE_FINDER = new SecureReflectiveTypeFinder("matchesSafely", 2, 0);

    public LibertyTypeSafeDiagnosingMatcher() {
        super(SECURE_TYPE_FINDER);
    }

    public LibertyTypeSafeDiagnosingMatcher(Class<?> expectedType) {
        super(expectedType);
    }

    public LibertyTypeSafeDiagnosingMatcher(ReflectiveTypeFinder typeFinder) {
        super(typeFinder);
    }

    private static class SecureReflectiveTypeFinder extends ReflectiveTypeFinder {

        public SecureReflectiveTypeFinder(String methodName, int expectedNumberOfParameters, int typedParameter) {
            super(methodName, expectedNumberOfParameters, typedParameter);
        }

        @Override
        public Class<?> findExpectedType(final Class<?> clazz) {
            return AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {
                @Override
                public Class<?> run() {
                    return SecureReflectiveTypeFinder.super.findExpectedType(clazz);
                }
            });
        }

    }
}
