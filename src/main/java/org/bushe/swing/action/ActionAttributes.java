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

package org.bushe.swing.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import org.bushe.swing.action.BasicAction;

public class ActionAttributes {
    private Map attMap = new HashMap(10);
    private static Class defaultActionClass;

    public static Class getDefaultActionClass() {
        return defaultActionClass;
    }

    public static void setDefaultActionClass(Class defaultActionClass) {
        ActionAttributes.defaultActionClass = defaultActionClass;
    }

    public ActionAttributes() {
    }

    public ActionAttributes(ActionAttributes attrs) {
        Set keys = attrs.getKeys();
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            this.putValue(key, attrs.getValue((String)key));
        }
    }

    public Object getValue(String key) {
        return this.attMap.get(key);
    }

    public Set getKeys() {
        return this.attMap.keySet();
    }

    public void putValue(Object key, Object value) {
        this.attMap.put(key, value);
    }

    public Action createAction() {
        String specificClass = (String)this.attMap.get("ACTION_CLASS");
        Action action = null;
        if (specificClass != null) {
            try {
                Class<?> actionClass = Class.forName(specificClass);
                action = (Action)actionClass.newInstance();
            }
            catch (IllegalAccessException ex1) {
                return null;
            }
            catch (InstantiationException ex1) {
                return null;
            }
            catch (ClassNotFoundException ex) {
                return null;
            }
        }
        if (action == null) {
            action = this.instantiateDefaultAction();
        }
        this.configureAction(action);
        return action;
    }

    protected Action instantiateDefaultAction() {
        if (defaultActionClass != null) {
            try {
                return (Action)defaultActionClass.newInstance();
            }
            catch (InstantiationException e) {
                throw new RuntimeException("Could not create action.", e);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException("Could not create action.", e);
            }
        }
        return new BasicAction();
    }

    public void configureAction(Action action) {
        Set keys = this.attMap.keySet();
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            Object value = this.attMap.get(key);
            if ("enabled".equals(value)) {
                action.setEnabled(Boolean.valueOf("" + value));
                continue;
            }
            action.putValue(key, value);
        }
    }

    public String toString() {
        return this.attMap.toString();
    }
}

