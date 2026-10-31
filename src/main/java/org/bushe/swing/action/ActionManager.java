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

import org.bushe.lang.reflect.MethodCallbackInvocationHandler;
import org.bushe.swing.SwingUtils;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class ActionManager implements IconResolver {
    public static final String ID = "ID";
    public static final String TOOLBAR_SHOWS_TEXT = "TOOLBAR_SHOWS_TEXT";
    public static final String MENU_SHOWS_ICON = "MENU_SHOWS_ICON";
    public static final String BUTTON_TYPE = "BUTTON_TYPE";
    public static final String BUTTON_TYPE_VALUE_TOGGLE = "toggle";
    public static final String BUTTON_TYPE_VALUE_RADIO = "radio";
    public static final String BUTTON_TYPE_VALUE_CHECKBOX = "checkbox";
    public static final String GROUP = "GROUP";
    public static final String LARGE_ICON = "LARGE_ICON";
    public static final String SELECTED = "SELECTED";
    public static final String ACTION_ROLES = "ROLES";
    public static final String ACTION_CLASS = "ACTION_CLASS";
    public static final String WEIGHT = "weight";
    private static final Map<String, ActionManager> NAMED_INSTANCES = Collections.synchronizedMap(new HashMap<>());
    private static final Class<?>[] ARRAY_OF_ACTION_LISTENER_CLASS = new Class[] {ActionListener.class};
    private static final Class<?>[] ARRAY_OF_ACTION_EVENT_CLASS = new Class[] {ActionEvent.class};
    private static final Object INSTANCE_LOCK = new Object();

    private static ActionManager INSTANCE = new ActionManager();

    private Map<Object, ActionAttributes> sPrototypes = Collections.synchronizedMap(new HashMap<>());
    private Map<Object, ActionList> sActionIdLists = Collections.synchronizedMap(new HashMap<>());
    private Map<Object, Action> sGlobalActions = Collections.synchronizedMap(new HashMap<>());
    private Map<Object, ActionList> sGlobalActionLists = Collections.synchronizedMap(new HashMap<>());
    private List<Object> roles;
    private IconResolver iconResolver;

    protected ActionManager() {
    }

    /*
     * WARNING - Removed try catching itself - possible behavior change.
     */
    public static ActionManager getInstance() {
        return INSTANCE;
    }

    /*
     * WARNING - Removed try catching itself - possible behavior change.
     */
    public static ActionManager getInstance(String name) {
        return name == null ? INSTANCE : NAMED_INSTANCES.get(name);
    }

    /*
     * WARNING - Removed try catching itself - possible behavior change.
     */
    public static void setInstance(String name, ActionManager actionManager) {
        synchronized (INSTANCE_LOCK) {
            if (name == null) {
                INSTANCE = actionManager;
            } else {
                NAMED_INSTANCES.put(name, actionManager);
            }
        }
    }

    public static ActionManager createNamedInstance(String name) {
        ActionManager actionManager = new ActionManager();
        setInstance(name, actionManager);
        return actionManager;
    }

    public static void mapKeystrokeForAction(JComponent comp, Action action, int condition) {
        KeyStroke keystroke = (KeyStroke) action.getValue("AcceleratorKey");
        if (keystroke == null) return;
        
        comp.getActionMap().put(action.getValue("ActionCommandKey"), action);
        comp.getInputMap(condition).put(keystroke, action.getValue("ActionCommandKey"));
    }

    public static void mapKeystrokeForAction(JComponent comp, Action action) {
        mapKeystrokeForAction(comp, action, 1);
    }

    public void reset() {
        sPrototypes = Collections.synchronizedMap(new HashMap<>());
        sActionIdLists = Collections.synchronizedMap(new HashMap<>());
        sGlobalActions = Collections.synchronizedMap(new HashMap<>());
        sGlobalActionLists = Collections.synchronizedMap(new HashMap<>());
    }

    public List<Object> getRoles() {
        return roles == null ? null : Collections.unmodifiableList(roles);
    }

    public void setRoles(List<Object> roles) {
        this.roles = roles;
    }

    public synchronized void register(File f) throws IOException, SAXException {
        ActionXMLReader actionXMLReader = new ActionXMLReader(this);
        actionXMLReader.loadActions(f);
    }

    public synchronized void register(URL actionXMLURL) throws IOException, SAXException {
        register(actionXMLURL, false);
    }

    public synchronized void register(URL actionXMLURL, boolean debug) throws IOException, SAXException {
        ActionXMLReader actionXMLReader = new ActionXMLReader(this);
        actionXMLReader.setDebug(debug);
        actionXMLReader.loadActions(actionXMLURL);
    }

    public synchronized void register(InputStream actionXMLStream) throws IOException, SAXException {
        register(actionXMLStream, false);
    }

    public synchronized void register(InputStream actionXMLStream, boolean debug) throws IOException, SAXException {
        ActionXMLReader actionXMLReader = new ActionXMLReader(this);
        actionXMLReader.setDebug(debug);
        actionXMLReader.loadActions(actionXMLStream);
    }

    public synchronized void registerAction(Object id, Action action) throws IllegalArgumentException {
        if (action == null) {
            throw new IllegalArgumentException("Null action registered to ");
        }

        if (sGlobalActions.containsKey(id)) {
            throw new IllegalArgumentException("An action by the name " + id + "already exists.");
        }

        if (sPrototypes.containsKey(id)) {
            throw new IllegalArgumentException("An prototype action by the name " + id + "already exists.");
        }

        sGlobalActions.put(id, action);
    }

    public synchronized boolean deregisterAction(Object id) {
        return sGlobalActions.remove(id) != null;
    }

    public synchronized void registerActionPrototype(Object id, ActionAttributes actionAtts)
            throws IllegalArgumentException {

        if (actionAtts == null) {
            throw new IllegalArgumentException("Null action registered to ");
        }

        if (sPrototypes.containsKey(id)) {
            throw new IllegalArgumentException("An action protoype by the name " + id + "already exists.");
        }

        if (sGlobalActions.containsKey(id)) {
            throw new IllegalArgumentException("An action by the name " + id + "already exists.");
        }

        sPrototypes.put(id, actionAtts);
    }

    public Set<Object> getActionIds() {
        Set<Object> actionIds = new HashSet<>();
        actionIds.addAll(sGlobalActions.keySet());
        actionIds.addAll(sPrototypes.keySet());
        return actionIds;
    }

    public Set<Object> getGlobalActionIds() {
        return sGlobalActions.keySet();
    }

    public Set<Object> getPrototypeActionIds() {
        return sPrototypes.keySet();
    }

    public ActionAttributes getPrototype(Object id) {
        return sPrototypes.get(id);
    }

    public Action getAction(Object actionId) {
        Action action = sGlobalActions.get(actionId);
        if (action == null && (action = createAction(actionId)) != null) {
            sGlobalActions.put(actionId, action);
        }

        List<Object> list;
        if (action != null &&
                (list = (List<Object>) action.getValue(ACTION_ROLES)) != null &&
                !containsAny(roles, list)) return null;

        return action;
    }

    public Action createAction(Object prototypeId) {
        return createAction(prototypeId, null);
    }

    public Action createAction(Object prototypeId, Map<Object, Object> context) {
        ActionAttributes attrs = sPrototypes.get(prototypeId);
        if (attrs == null) return null;

        Action action = attrs.createAction();
        if (!passesRoleTest(action)) return null;

        if (action instanceof ContextAware) {
            ContextAware contextAware = (ContextAware) action;
            contextAware.setContext(context);
        }

        return action;
    }

    protected final boolean passesRoleTest(Action action) {
        return action != null &&
                passesRoleTest((List<Object>) action.getValue(ACTION_ROLES));
    }

    protected final boolean passesRoleTest(ActionList actionList) {
        return passesRoleTest(actionList.getRoles());
    }

    private boolean passesRoleTest(List<Object> defRoles) {
        return defRoles == null || containsAny(roles, defRoles);
    }

    public boolean containsAny(Collection<?> src, Collection<?> target) {
        if (src == null || target == null) return false;
        return src.stream().anyMatch(x ->
                target.stream().anyMatch(y -> Objects.equals(x, y)));
    }

    public synchronized void registerActionList(ActionList actionList) throws IllegalArgumentException {
        if (sGlobalActionLists.get(actionList.getId()) != null) {
            throw new IllegalArgumentException("Already registered action list named:" + actionList.getId());
        }

        sGlobalActionLists.put(actionList.getId(), actionList);
    }

    public synchronized void registerActionIdList(ActionList actionIds) throws IllegalArgumentException {
        if (sActionIdLists.get(actionIds.getId()) != null) {
            throw new IllegalArgumentException("Already registered action id list named:" + actionIds.getId());
        }

        sActionIdLists.put(actionIds.getId(), actionIds);
    }

    public Set<Object> getActionListIds() {
        return sActionIdLists.keySet();
    }

    public synchronized ActionList getActionIdList(Object listId) {
        ActionList idList = sActionIdLists.get(listId);
        return idList == null || !passesRoleTest(idList) ? null : idList;
    }

    public synchronized boolean deregisterIdList(Object listId) {
        Object o = sActionIdLists.remove(listId);
        sGlobalActionLists.remove(listId);
        return o != null;
    }

    public synchronized ActionList getActionList(Object listId) {
        ActionList result = sGlobalActionLists.get(listId);

        if (result == null) {
            result = createActionList(listId, true, null);
            if (result == null) return null;

            sGlobalActionLists.put(listId, result);
            return result;
        }

        return passesRoleTest(result) ? result : null;
    }

    public synchronized ActionList createActionList(Object actionListId) {
        return createActionList(actionListId, true, null);
    }

    public synchronized ActionList createActionList(Object actionListId, Map context) {
        return createActionList(actionListId, true, context);
    }

    public synchronized ActionList createActionList(Object actionListId, boolean useGlobalActions) {
        return createActionList(actionListId, useGlobalActions, null);
    }

    public synchronized ActionList createActionList(
            Object actionListId, boolean useGlobalActions, Map<Object, Object> context) {

        ActionList actionIdList = getActionIdList(actionListId);
        if (actionIdList == null) return null;

        ActionList actionList = new ActionList(actionIdList.getId(), actionIdList.getTriggerActionId());
        actionList.setWeight(actionIdList.getWeight());
        fillListWithActions(actionList, actionIdList, useGlobalActions, context);
        return actionList;
    }

    private synchronized void fillListWithActions(List<Object> toFill, List<Object> actionIds,
                                                  boolean useGlobalActions, Map<Object, Object> context) {

        for (Object actionId : actionIds) {
            if (actionId == null) {
                toFill.add(null);
                continue;
            }

            if (actionId instanceof Separator) {
                Number weight = ((Separator) actionId).getWeight();
                insertWithRespectToWeights(weight, toFill, actionId);
                continue;
            }

            if (actionId instanceof List) {
                List<?> subIds = (List<?>) actionId;
                List<Object> subList;
                Number weight = null;

                if (subIds instanceof ActionList) {
                    ActionList subAL = (ActionList) subIds;

                    if (!passesRoleTest(subAL)) {
                        continue;
                    }

                    ActionList actionSubList =
                            new ActionList(subAL.getId(), subAL.getTriggerActionId());
                    weight = subAL.getWeight();
                    actionSubList.setWeight(weight);
                    subList = actionSubList;
                } else {
                    subList = new ArrayList<>(subIds.size());
                }

                insertWithRespectToWeights(weight, toFill, subList);
                fillListWithActions(subList, (List<Object>) subIds, useGlobalActions, context);
                continue;
            }

            Number weight = null;
            Action action = useGlobalActions
                    ? getAction(actionId)
                    : createAction(actionId, context);

            if (action != null) {
                Object weightVal = action.getValue(WEIGHT);
                if (weightVal != null) {
                    weight = Double.valueOf("" + weightVal);
                }
                insertWithRespectToWeights(weight, toFill, action);
            }
        }
    }

    private void insertWithRespectToWeights(Number weight, List<Object> toFill, Object toInsert)
            throws NumberFormatException {

        for (int i = 0; i < toFill.size(); i++) {
            Number childWeight = getWeight(toFill.get(i));

            if (shouldInsertBefore(weight, childWeight)) {
                toFill.add(i, toInsert);
                return;
            }
        }

        toFill.add(toInsert);
    }

    private Number getWeight(Object item) {
        if (item instanceof ActionList) {
            return ((ActionList) item).getWeight();
        }

        Object value = null;
        if (item instanceof Action) {
            value = ((Action) item).getValue(WEIGHT);
        } else if (item instanceof Separator) {
            return ((Separator) item).getWeight();
        }

        return value == null ? null : Double.valueOf("" + value);
    }

    private boolean shouldInsertBefore(Number weight, Number childWeight) {
        if (childWeight == null) {
            return weight != null && weight.doubleValue() < 0.0;
        }

        if (weight == null) {
            return childWeight.doubleValue() > 0.0;
        }

        return weight.doubleValue() < childWeight.doubleValue();
    }

    public void registerActionCallback(Object actionId, Object callback, String method)
            throws NoSuchMethodException {

        Action action = getAction(actionId);

        if (action == null) {
            throw new IllegalArgumentException("No action found for action id " + actionId +
                    ", registering method " + method + ", callback " + callback);
        }

        if (!(action instanceof Actionable)) {
            throw new NoSuchMethodException(
                    "Action does not implement Actionable, no addActionListener(0 to use: action " +
                    action + ", registering method " + method + ", callback " + callback);
        }

        registerActionCallback((Actionable) action, callback, method);
    }

    public void registerActionCallback(Actionable action, Object callback, String method)
            throws NoSuchMethodException {

        ActionListener listenerProxy = (ActionListener) registerCallback(action, callback, method,
                ARRAY_OF_ACTION_EVENT_CLASS, ActionListener.class, ARRAY_OF_ACTION_LISTENER_CLASS);
        action.addActionListener(listenerProxy);
    }

    public void registerStaticActionCallback(Object actionId, Class callbackClass, String staticMethod)
            throws NoSuchMethodException {

        registerActionCallback(actionId, callbackClass, staticMethod);
    }

    private Object registerCallback(Actionable action, Object callback, String method, Class<?>[] args,
                                    Class<?> implementingInterface, Class<?>[] arrayOfInterfaces)
            throws SecurityException, NoSuchMethodException {

        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null.");
        }

        try {
            return MethodCallbackInvocationHandler.createMethodCallbackProxy(
                    callback, method, args, implementingInterface, arrayOfInterfaces);
        } catch (NoSuchMethodException ex) {
            String err = "Callback Method named " + method;
            err = callback instanceof Class ? err + " either not found or is not static " : err + " is not found ";
            err = err + " not found in callback " + callback + ", registering for action =" + action;
            throw new NoSuchMethodException(err);
        }
    }

    public void setEnabledAll(boolean enable) {
        if (sGlobalActions == null || sGlobalActions.isEmpty()) return;
        sGlobalActions.values().forEach(action -> action.setEnabled(enable));
    }

    public void setEnabled(Object id, boolean enable) {
        if (sGlobalActions == null || sGlobalActions.isEmpty()) return;
        Action action = sGlobalActions.get(id);
        if (action == null) return;
        action.setEnabled(enable);
    }

    public void updateEnabledAll() {
        if (sGlobalActions == null || sGlobalActions.isEmpty()) return;
        sGlobalActions.values()
                .stream()
                .filter(EnabledUpdater.class::isInstance)
                .map(EnabledUpdater.class::cast)
                .forEach(EnabledUpdater::updateEnabled);
    }

    public void putContextValueForAll(Object key, Object contextValue) {
        if (sGlobalActions == null || sGlobalActions.isEmpty()) return;
        sGlobalActions.values()
                .stream()
                .filter(ContextAware.class::isInstance)
                .map(ContextAware.class::cast)
                .forEach(ca -> ca.putContextValue(key, contextValue));
    }

    public void updateEnabled(Object id) {
        if (sGlobalActions == null || sGlobalActions.isEmpty()) return;
        Action action = sGlobalActions.get(id);
        if (action instanceof EnabledUpdater) {
            ((EnabledUpdater) action).updateEnabled();
        }
    }

    public IconResolver getIconResolver() {
        return iconResolver;
    }

    public void setIconResolver(IconResolver iconResolver) {
        this.iconResolver = iconResolver;
    }

    public Icon resolveIcon(String imageURL) {
        return iconResolver == null
                ? SwingUtils.getIcon(imageURL)
                : iconResolver.resolveIcon(imageURL);
    }
}

