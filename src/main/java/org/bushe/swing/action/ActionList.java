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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.swing.Action;
import org.bushe.swing.action.BasicAction;
import org.bushe.swing.action.ContextAware;
import org.bushe.swing.action.EnabledUpdater;

public class ActionList
implements List {
    private Object id;
    private Object triggerActionId;
    private Number weight;
    private List list = new ArrayList();
    private List roles;

    public ActionList(Object id) {
        this.id = id;
    }

    public ActionList(Object id, Object triggerActionId) {
        this(id);
        this.triggerActionId = triggerActionId;
    }

    public Object getId() {
        return this.id;
    }

    public Object getTriggerActionId() {
        return this.triggerActionId;
    }

    public List getRoles() {
        if (this.roles == null) {
            return null;
        }
        return Collections.unmodifiableList(this.roles);
    }

    public Number getWeight() {
        return this.weight;
    }

    public void setWeight(Number weight) {
        this.weight = weight;
    }

    public void setRoles(List roles) {
        this.roles = roles;
    }

    public Action getActionById(Object id) {
        return this.findActionInList(id, this.list);
    }

    public void addActionListenerToAll(ActionListener delegate) {
        BasicAction action = null;
        Iterator iter = this.iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            if (item == null || !(item instanceof BasicAction)) continue;
            action = (BasicAction)item;
            action.addActionListener(delegate);
        }
        iter = this.iterator();
        while (iter.hasNext()) {
            Object subList = iter.next();
            if (subList == null || !(subList instanceof ActionList)) continue;
            ((ActionList)subList).addActionListenerToAll(delegate);
        }
    }

    private Action findActionInList(Object id, List actionList) {
        Iterator iter = actionList.iterator();
        while (iter.hasNext()) {
            Action action;
            Object item = iter.next();
            if (item == null || !(item instanceof Action) || !id.equals((action = (Action)item).getValue("ID"))) continue;
            return action;
        }
        iter = actionList.iterator();
        while (iter.hasNext()) {
            Action result;
            Object subList = iter.next();
            if (subList == null || !(subList instanceof ActionList) || (result = this.findActionInList(id, (ActionList)subList)) == null) continue;
            return result;
        }
        return null;
    }

    public void setContextForAll(Map context) {
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            Object action = iterator.next();
            if (!(action instanceof ContextAware)) continue;
            ContextAware contextAwareAction = (ContextAware)action;
            contextAwareAction.setContext(context);
        }
    }

    public void putContextValueForAll(Object key, Object contextValue) {
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            Object action = iterator.next();
            if (!(action instanceof ContextAware)) continue;
            ContextAware contextAwareAction = (ContextAware)action;
            contextAwareAction.putContextValue(key, contextValue);
        }
    }

    public void updateEnabledForAll() {
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            Object action = iterator.next();
            if (!(action instanceof EnabledUpdater)) continue;
            EnabledUpdater updateEnabledAction = (EnabledUpdater)action;
            updateEnabledAction.updateEnabled();
        }
    }

    public void setEnabledForAll(boolean enabled) {
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            Object action = iterator.next();
            if (!(action instanceof Action)) continue;
            ((Action)action).setEnabled(enabled);
        }
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    public Iterator iterator() {
        return this.list.iterator();
    }

    public Object[] toArray() {
        return this.list.toArray();
    }

    public Object[] toArray(Object[] a) {
        return this.list.toArray(a);
    }

    public boolean add(Object o) {
        return this.list.add(o);
    }

    public boolean remove(Object o) {
        return this.list.remove(o);
    }

    public boolean containsAll(Collection c) {
        return this.list.containsAll(c);
    }

    public boolean addAll(Collection c) {
        return this.list.addAll(c);
    }

    public boolean addAll(int index, Collection c) {
        return this.list.addAll(index, c);
    }

    public boolean removeAll(Collection c) {
        return this.list.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return this.list.retainAll(c);
    }

    public void clear() {
        this.list.clear();
    }

    public Object get(int index) {
        return this.list.get(index);
    }

    public Object set(int index, Object element) {
        return this.list.set(index, element);
    }

    public void add(int index, Object element) {
        this.list.add(index, element);
    }

    public Object remove(int index) {
        return this.list.remove(index);
    }

    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return this.list.listIterator();
    }

    public ListIterator listIterator(int index) {
        return this.list.listIterator(index);
    }

    public List subList(int fromIndex, int toIndex) {
        return this.list.subList(fromIndex, toIndex);
    }
}

