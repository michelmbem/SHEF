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

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.bushe.lang.reflect.MethodCallbackInvocationHandler;
import org.bushe.swing.SwingUtils;
import org.bushe.swing.action.ActionAttributes;
import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionXMLReader;
import org.bushe.swing.action.Actionable;
import org.bushe.swing.action.ContextAware;
import org.bushe.swing.action.EnabledUpdater;
import org.bushe.swing.action.IconResolver;
import org.bushe.swing.action.Separator;
import org.xml.sax.SAXException;

public class ActionManager
implements IconResolver {
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
    private static Class[] ARRAY_OF_ACTION_LISTENER_CLASS;
    private static Class[] ARRAY_OF_ACTION_EVENT_CLASS;
    private static Class[] ARRAY_OF_ITEM_LISTENER_CLASS;
    private static Class[] ARRAY_OF_ITEM_EVENT_CLASS;
    private Map s_prototypes = Collections.synchronizedMap(new HashMap());
    private Map s_actionIdLists = Collections.synchronizedMap(new HashMap());
    private Map s_globalActions = Collections.synchronizedMap(new HashMap());
    private Map s_globalActionLists = Collections.synchronizedMap(new HashMap());
    private List roles;
    private IconResolver iconResolver;
    private static ActionManager INSTANCE;
    private static Map NAMED_INSTANCES;
    private static Object INSTANCE_LOCK;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    static /* synthetic */ Class class$2;
    static /* synthetic */ Class class$3;

    static {
        Class[] classArray = new Class[1];
        Class<?> clazz = class$0;
        if (clazz == null) {
            try {
                clazz = class$0 = Class.forName("java.awt.event.ActionListener");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        classArray[0] = clazz;
        ARRAY_OF_ACTION_LISTENER_CLASS = classArray;
        Class[] classArray2 = new Class[1];
        Class<?> clazz2 = class$1;
        if (clazz2 == null) {
            try {
                clazz2 = class$1 = Class.forName("java.awt.event.ActionEvent");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        classArray2[0] = clazz2;
        ARRAY_OF_ACTION_EVENT_CLASS = classArray2;
        Class[] classArray3 = new Class[1];
        Class<?> clazz3 = class$2;
        if (clazz3 == null) {
            try {
                clazz3 = class$2 = Class.forName("java.awt.event.ItemListener");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        classArray3[0] = clazz3;
        ARRAY_OF_ITEM_LISTENER_CLASS = classArray3;
        Class[] classArray4 = new Class[1];
        Class<?> clazz4 = class$3;
        if (clazz4 == null) {
            try {
                clazz4 = class$3 = Class.forName("java.awt.event.ItemEvent");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        classArray4[0] = clazz4;
        ARRAY_OF_ITEM_EVENT_CLASS = classArray4;
        INSTANCE_LOCK = new Object();
    }

    protected ActionManager() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ActionManager getInstance() {
        Object object = INSTANCE_LOCK;
        synchronized (object) {
            if (INSTANCE == null) {
                INSTANCE = new ActionManager();
                ActionManager.setInstance(null, INSTANCE);
            }
        }
        return INSTANCE;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setInstance(String name, ActionManager actionManager) {
        Object object = INSTANCE_LOCK;
        synchronized (object) {
            if (NAMED_INSTANCES == null) {
                NAMED_INSTANCES = Collections.synchronizedMap(new HashMap());
            }
            NAMED_INSTANCES.put(name, actionManager);
            if (name == null) {
                INSTANCE = actionManager;
            }
        }
    }

    public static ActionManager createNamedInstance(String name) {
        ActionManager actionManager = new ActionManager();
        ActionManager.setInstance(name, actionManager);
        return actionManager;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ActionManager getInstance(String name) {
        Object object = INSTANCE_LOCK;
        synchronized (object) {
            block5: {
                if (NAMED_INSTANCES != null) break block5;
                if (name == null) {
                    return ActionManager.getInstance();
                }
                return null;
            }
            return (ActionManager)NAMED_INSTANCES.get(name);
        }
    }

    public void reset() {
        this.s_prototypes = Collections.synchronizedMap(new HashMap());
        this.s_actionIdLists = Collections.synchronizedMap(new HashMap());
        this.s_globalActions = Collections.synchronizedMap(new HashMap());
        this.s_globalActionLists = Collections.synchronizedMap(new HashMap());
    }

    public void setRoles(List roles) {
        this.roles = roles;
    }

    public List getRoles() {
        if (this.roles == null) {
            return null;
        }
        return Collections.unmodifiableList(this.roles);
    }

    public synchronized void register(File f) throws IOException, SAXException {
        ActionXMLReader actionXMLReader = new ActionXMLReader(this);
        actionXMLReader.loadActions(f);
    }

    public synchronized void register(URL actionXMLURL) throws IOException, SAXException {
        this.register(actionXMLURL, false);
    }

    public synchronized void register(URL actionXMLURL, boolean debug) throws IOException, SAXException {
        ActionXMLReader actionXMLReader = new ActionXMLReader(this);
        actionXMLReader.setDebug(debug);
        actionXMLReader.loadActions(actionXMLURL);
    }

    public synchronized void register(InputStream actionXMLStream) throws IOException, SAXException {
        this.register(actionXMLStream, false);
    }

    public synchronized void register(InputStream actionXMLStream, boolean debug) throws IOException, SAXException {
        ActionXMLReader actionXMLReader = new ActionXMLReader(this);
        actionXMLReader.setDebug(debug);
        actionXMLReader.loadActions(actionXMLStream);
    }

    public synchronized void registerAction(Object id, Action action) throws IllegalArgumentException {
        if (action == null) {
            throw new IllegalArgumentException("Null action registered to ActionManager.");
        }
        if (this.s_globalActions.containsKey(id)) {
            throw new IllegalArgumentException("An action by the name " + id + "already exists.");
        }
        if (this.s_prototypes.containsKey(id)) {
            throw new IllegalArgumentException("An prototype action by the name " + id + "already exists.");
        }
        this.s_globalActions.put(id, action);
    }

    public synchronized boolean deregisterAction(Object id) {
        Object o = this.s_globalActions.remove(id);
        return o != null;
    }

    public synchronized void registerActionPrototype(Object id, ActionAttributes actionAtts) throws IllegalArgumentException {
        if (actionAtts == null) {
            throw new IllegalArgumentException("Null action registered to ActionManager.");
        }
        if (this.s_prototypes.containsKey(id)) {
            throw new IllegalArgumentException("An action protoype by the name " + id + "already exists.");
        }
        if (this.s_globalActions.containsKey(id)) {
            throw new IllegalArgumentException("An action by the name " + id + "already exists.");
        }
        this.s_prototypes.put(id, actionAtts);
    }

    public Set getActionIds() {
        HashSet actionIds = new HashSet();
        actionIds.addAll(this.s_globalActions.keySet());
        actionIds.addAll(this.s_prototypes.keySet());
        return actionIds;
    }

    public Set getGlobalActionIds() {
        return this.s_globalActions.keySet();
    }

    public Set getPrototypeActionIds() {
        return this.s_prototypes.keySet();
    }

    public ActionAttributes getPrototype(Object id) {
        return (ActionAttributes)this.s_prototypes.get(id);
    }

    public Action getAction(Object actionId) {
        List list;
        Action action = (Action)this.s_globalActions.get(actionId);
        if (action == null && (action = this.createAction(actionId)) != null) {
            this.s_globalActions.put(actionId, action);
        }
        if (action != null && (list = (List)action.getValue(ACTION_ROLES)) != null && !this.containsAny(this.roles, list)) {
            return null;
        }
        return action;
    }

    public Action createAction(Object prototypeId) {
        return this.createAction(prototypeId, null);
    }

    public Action createAction(Object prototypeId, Map context) {
        ActionAttributes attrs = (ActionAttributes)this.s_prototypes.get(prototypeId);
        if (attrs == null) {
            return null;
        }
        Action action = attrs.createAction();
        if (!this.passesRoleTest(action)) {
            return null;
        }
        if (action instanceof ContextAware) {
            ContextAware caa = (ContextAware)((Object)action);
            caa.setContext(context);
        }
        return action;
    }

    protected final boolean passesRoleTest(Action action) {
        if (action == null) {
            return false;
        }
        return this.passesRoleTest((List)action.getValue(ACTION_ROLES));
    }

    protected final boolean passesRoleTest(ActionList actionList) {
        return this.passesRoleTest(actionList.getRoles());
    }

    private boolean passesRoleTest(List defRoles) {
        if (defRoles == null) {
            return true;
        }
        return this.containsAny(this.roles, defRoles);
    }

    public boolean containsAny(Collection src, Collection target) {
        if (src == null) {
            return false;
        }
        Iterator iter = src.iterator();
        while (iter.hasNext()) {
            if (!target.contains(iter.next())) continue;
            return true;
        }
        return false;
    }

    public synchronized void registerActionList(ActionList actionList) throws IllegalArgumentException {
        if (this.s_globalActionLists.get(actionList.getId()) != null) {
            throw new IllegalArgumentException("Already registered action list named:" + actionList.getId());
        }
        this.s_globalActionLists.put(actionList.getId(), actionList);
    }

    public synchronized void registerActionIdList(ActionList actionIds) throws IllegalArgumentException {
        if (this.s_actionIdLists.get(actionIds.getId()) != null) {
            throw new IllegalArgumentException("Already registered action id list named:" + actionIds.getId());
        }
        this.s_actionIdLists.put(actionIds.getId(), actionIds);
    }

    public Set getActionListIds() {
        return this.s_actionIdLists.keySet();
    }

    public synchronized ActionList getActionIdList(Object listId) {
        ActionList idList = (ActionList)this.s_actionIdLists.get(listId);
        if (idList == null || !this.passesRoleTest(idList)) {
            return null;
        }
        return idList;
    }

    public synchronized boolean deregisterIdList(Object listId) {
        Object o = this.s_actionIdLists.remove(listId);
        this.s_globalActionLists.remove(listId);
        return o != null;
    }

    public synchronized ActionList getActionList(Object listId) {
        ActionList result = (ActionList)this.s_globalActionLists.get(listId);
        if (result == null) {
            result = this.createActionList(listId, true, null);
            if (result == null) {
                return null;
            }
            this.s_globalActionLists.put(listId, result);
            return result;
        }
        if (!this.passesRoleTest(result)) {
            return null;
        }
        return result;
    }

    public synchronized ActionList createActionList(Object actionListId) {
        return this.createActionList(actionListId, true, null);
    }

    public synchronized ActionList createActionList(Object actionListId, Map context) {
        return this.createActionList(actionListId, true, context);
    }

    public synchronized ActionList createActionList(Object actionListId, boolean useGlobalActions) {
        return this.createActionList(actionListId, useGlobalActions, null);
    }

    public synchronized ActionList createActionList(Object actionListId, boolean useGlobalActions, Map context) {
        ActionList actionIdList = this.getActionIdList(actionListId);
        if (actionIdList == null) {
            return null;
        }
        ActionList actionList = new ActionList(actionIdList.getId(), actionIdList.getTriggerActionId());
        actionList.setWeight(actionIdList.getWeight());
        this.fillListWithActions(actionList, actionIdList, useGlobalActions, context);
        return actionList;
    }

    private synchronized void fillListWithActions(List toFill, List actionIds, boolean useGlobalActions, Map context) {
        int i = 0;
        while (i < actionIds.size()) {
            block4: {
                Number weight;
                block6: {
                    List subList;
                    List subIds;
                    block8: {
                        block7: {
                            Object elem;
                            block5: {
                                block3: {
                                    elem = actionIds.get(i);
                                    weight = null;
                                    if (elem != null) break block3;
                                    toFill.add(null);
                                    break block4;
                                }
                                if (!(elem instanceof Separator)) break block5;
                                weight = ((Separator)elem).getWeight();
                                this.insertWithRespectToWeights(weight, toFill, elem);
                                break block4;
                            }
                            if (!(elem instanceof List)) break block6;
                            subIds = (List)elem;
                            subList = null;
                            if (!(subIds instanceof ActionList)) break block7;
                            ActionList subAL = (ActionList)subIds;
                            if (!this.passesRoleTest(subAL)) break block4;
                            subList = new ActionList(subAL.getId(), subAL.getTriggerActionId());
                            weight = subAL.getWeight();
                            ((ActionList)subList).setWeight(weight);
                            break block8;
                        }
                        subList = new ArrayList(subIds.size());
                    }
                    this.insertWithRespectToWeights(weight, toFill, subList);
                    this.fillListWithActions(subList, subIds, useGlobalActions, context);
                    break block4;
                }
                Action action = null;
                action = useGlobalActions ? this.getAction(actionIds.get(i)) : this.createAction(actionIds.get(i), context);
                if (action != null) {
                    Object weightVal = action.getValue(WEIGHT);
                    if (weightVal != null) {
                        weight = Double.valueOf("" + weightVal);
                    }
                    this.insertWithRespectToWeights(weight, toFill, action);
                }
            }
            ++i;
        }
    }

    private void insertWithRespectToWeights(Number weight, List toFill, Object toInsert) throws NumberFormatException {
        int i = 0;
        while (i < toFill.size()) {
            Object val;
            Number childWeight = null;
            Object child = toFill.get(i);
            if (child instanceof ActionList) {
                childWeight = ((ActionList)child).getWeight();
            } else if (child instanceof Action) {
                val = ((Action)child).getValue(WEIGHT);
                if (val != null) {
                    childWeight = Double.valueOf("" + val);
                }
            } else if (child instanceof Separator && (val = ((Separator)child).getWeight()) != null) {
                childWeight = Double.valueOf("" + val);
            }
            if (childWeight == null) {
                if (weight != null && weight.doubleValue() < 0.0) {
                    toFill.add(i, toInsert);
                    return;
                }
            } else if (weight == null) {
                if (childWeight.doubleValue() > 0.0) {
                    toFill.add(i, toInsert);
                    return;
                }
            } else if (weight.doubleValue() < childWeight.doubleValue()) {
                toFill.add(i, toInsert);
                return;
            }
            ++i;
        }
        toFill.add(toInsert);
    }

    public void registerActionCallback(Object actionId, Object callback, String method) throws NoSuchMethodException {
        Action action = this.getAction(actionId);
        if (action == null) {
            throw new IllegalArgumentException("No action found for action id " + actionId + ", registering method " + method + ", callback " + callback);
        }
        if (!(action instanceof Actionable)) {
            throw new NoSuchMethodException("Action does not implement Actionable, no addActionListener(0 to use: action " + action + ", registering method " + method + ", callback " + callback);
        }
        this.registerActionCallback((Actionable)((Object)action), callback, method);
    }

    public void registerActionCallback(Actionable action, Object callback, String method) throws NoSuchMethodException {
        Class<?> clazz = class$0;
        if (clazz == null) {
            try {
                clazz = class$0 = Class.forName("java.awt.event.ActionListener");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        ActionListener listenerProxy = (ActionListener)this.registerCallback(action, callback, method, ARRAY_OF_ACTION_EVENT_CLASS, clazz, ARRAY_OF_ACTION_LISTENER_CLASS);
        action.addActionListener(listenerProxy);
    }

    public void registerStaticActionCallback(Object actionId, Class callbackClass, String staticMethod) throws NoSuchMethodException {
        this.registerActionCallback(actionId, (Object)callbackClass, staticMethod);
    }

    private Object registerCallback(Actionable action, Object callback, String method, Class[] args, Class implementingInterface, Class[] arrayOfInterfaces) throws IllegalArgumentException, SecurityException, NoSuchMethodException {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null.");
        }
        try {
            return MethodCallbackInvocationHandler.createMethodCallbackProxy(callback, method, args, implementingInterface, arrayOfInterfaces);
        }
        catch (NoSuchMethodException ex) {
            String err = "Callback Method named " + method;
            err = callback instanceof Class ? String.valueOf(err) + " either not found or is not static " : String.valueOf(err) + " is not found ";
            err = String.valueOf(err) + " not found in callback " + callback + ", registering for action =" + action;
            throw new NoSuchMethodException(err);
        }
    }

    public void setEnabledAll(boolean enable) {
        if (this.s_globalActions == null || this.s_globalActions.isEmpty()) {
            return;
        }
        Iterator iter = this.s_globalActions.keySet().iterator();
        while (iter.hasNext()) {
            this.setEnabled(iter.next(), enable);
        }
    }

    public void setEnabled(Object id, boolean enable) {
        if (this.s_globalActions == null || this.s_globalActions.isEmpty()) {
            return;
        }
        Action action = (Action)this.s_globalActions.get(id);
        if (action == null) {
            return;
        }
        action.setEnabled(enable);
    }

    public void updateEnabledAll() {
        if (this.s_globalActions == null || this.s_globalActions.isEmpty()) {
            return;
        }
        Iterator iter = this.s_globalActions.keySet().iterator();
        while (iter.hasNext()) {
            this.updateEnabled(iter.next());
        }
    }

    public void putContextValueForAll(Object key, Object contextValue) {
        if (this.s_globalActions == null || this.s_globalActions.isEmpty()) {
            return;
        }
        Iterator iter = this.s_globalActions.keySet().iterator();
        while (iter.hasNext()) {
            Object action = this.s_globalActions.get(iter.next());
            if (!(action instanceof ContextAware)) continue;
            ContextAware contextAwareAction = (ContextAware)action;
            contextAwareAction.putContextValue(key, contextValue);
        }
    }

    public void updateEnabled(Object id) {
        if (this.s_globalActions == null || this.s_globalActions.isEmpty()) {
            return;
        }
        Action action = (Action)this.s_globalActions.get(id);
        if (action instanceof EnabledUpdater) {
            ((EnabledUpdater)((Object)action)).updateEnabled();
        }
    }

    public static void mapKeystrokeForAction(JComponent comp, Action action, int condition) {
        KeyStroke keystroke = (KeyStroke)action.getValue("AcceleratorKey");
        if (keystroke == null) {
            return;
        }
        comp.getActionMap().put(action.getValue("ActionCommandKey"), action);
        comp.getInputMap(condition).put(keystroke, action.getValue("ActionCommandKey"));
    }

    public static void mapKeystrokeForAction(JComponent comp, Action action) {
        ActionManager.mapKeystrokeForAction(comp, action, 1);
    }

    public IconResolver getIconResolver() {
        return this.iconResolver;
    }

    public void setIconResolver(IconResolver iconResolver) {
        this.iconResolver = iconResolver;
    }

    public Icon resolveIcon(String imageURL) {
        if (this.iconResolver != null) {
            return this.iconResolver.resolveIcon(imageURL);
        }
        return SwingUtils.getIcon(imageURL);
    }
}

