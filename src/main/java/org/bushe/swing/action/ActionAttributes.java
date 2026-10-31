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

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActionAttributes {
    private static Class<? extends Action> defaultActionClass;
    private final Map<String, Object> attMap = new HashMap<>(10);

    public ActionAttributes() {
    }

    public ActionAttributes(ActionAttributes attrs) {
        attrs.getKeys().forEach(key -> putValue(key, attrs.getValue(key)));
    }

    public static Class<? extends Action> getDefaultActionClass() {
        return defaultActionClass;
    }

    public static void setDefaultActionClass(Class<? extends Action> defaultActionClass) {
        ActionAttributes.defaultActionClass = defaultActionClass;
    }

    public Set<String> getKeys() {
        return attMap.keySet();
    }

    public Object getValue(String key) {
        return attMap.get(key);
    }

    public void putValue(String key, Object value) {
        attMap.put(key, value);
    }

    public Action createAction() {
        String specificClass = attMap.get("ACTION_CLASS").toString();
        Action action = null;

        if (specificClass != null) {
            try {
                Class<?> actionClass = Class.forName(specificClass);
                action = (Action) actionClass.getConstructor().newInstance();
            } catch (Exception ex) {
                return null;
            }
        }

        if (action == null) {
            action = instantiateDefaultAction();
        }

        configureAction(action);
        return action;
    }

    protected Action instantiateDefaultAction() {
        if (defaultActionClass != null) {
            try {
                return defaultActionClass.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Could not create action.", e);
            }
        }

        return new BasicAction();
    }

    public void configureAction(Action action) {
        for (Map.Entry<String, Object> entry : attMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if ("enabled".equals(value)) {
                action.setEnabled(true);
                continue;
            }

            action.putValue(key, value);
        }
    }

    public String toString() {
        return this.attMap.toString();
    }
}

