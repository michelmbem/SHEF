/*
 * Copyright 2005 Bushe Enterprises, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Decompiled from sam.jar using CFR 0.152. This reconstructed source
 * may differ from the original source formatting and comments.
 */

package org.bushe.lang.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

public class MethodCallbackInvocationHandler implements InvocationHandler {
    Object target;
    Method method;
    boolean useArgs;

    public MethodCallbackInvocationHandler(Class<?> interfac, Object callback, Method m) {
        target = callback;
        method = m;

        if (m == null) {
            throw new IllegalArgumentException("Method is null.");
        }

        if (!Modifier.isStatic(m.getModifiers()) && target == null) {
            throw new IllegalArgumentException("Method is static and target is null.");
        }

        Class<?>[] cl = m.getParameterTypes();
        if (cl.length > 0) {
            useArgs = true;
        }
    }

    public static Object createMethodCallbackProxy(Object callback, String method, Class<?>[] args,
                                                   Class<?> implementingInterface, Class<?>[] arrayOfInterfaces)
            throws UnsupportedOperationException, SecurityException, NoSuchMethodException {

        if (callback == null) {
            throw new IllegalArgumentException("Handler cannot be null.");
        }

        if (method == null) {
            throw new IllegalArgumentException("Method cannot be null.");
        }

        boolean methodShouldBeStatic = false;
        Method methodToInvoke;
        Class<?> callbackClass;

        if (callback instanceof Class) {
            methodShouldBeStatic = true;
            callbackClass = (Class<?>) callback;
        } else {
            callbackClass = callback.getClass();
        }

        try {
            methodToInvoke = callbackClass.getMethod(method, args);
        } catch (NoSuchMethodException e1) {
            try {
                methodToInvoke = callbackClass.getMethod(method);
            } catch (NoSuchMethodException e2) {
                throw new NoSuchMethodException("Callback Method named " + method +
                        " not found in class " + callbackClass);
            }
        }

        boolean isStaticMethod = Modifier.isStatic(methodToInvoke.getModifiers());
        if (isStaticMethod && methodShouldBeStatic) {
            throw new NoSuchMethodException("Callback Method named " + method +
                    " must be static in class " + callbackClass);
        }

        return Proxy.newProxyInstance(
                MethodCallbackInvocationHandler.class.getClassLoader(),
                arrayOfInterfaces,
                new MethodCallbackInvocationHandler(implementingInterface, callback, methodToInvoke));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (useArgs) {
            this.method.invoke(target, args);
        } else {
            this.method.invoke(target);
        }

        return null;
    }
}

