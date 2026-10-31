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
import java.awt.event.ActionListener;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ActionList implements List<Object> {
    private final Object id;
    private Object triggerActionId;
    private Number weight;
    private final List<Object> list = new ArrayList<>();
    private List<Object> roles;

    public ActionList(Object id) {
        this.id = id;
    }

    public ActionList(Object id, Object triggerActionId) {
        this(id);
        this.triggerActionId = triggerActionId;
    }

    public Object getId() {
        return id;
    }

    public Object getTriggerActionId() {
        return triggerActionId;
    }

    public List<Object> getRoles() {
        return roles == null ? null : Collections.unmodifiableList(roles);
    }

    public void setRoles(List<Object> roles) {
        this.roles = roles;
    }

    public Number getWeight() {
        return weight;
    }

    public void setWeight(Number weight) {
        this.weight = weight;
    }

    public Action getActionById(Object id) {
        return findActionInList(id, list);
    }

    public void addActionListenerToAll(ActionListener delegate) {
        list.forEach(item -> {
            if (item instanceof BasicAction) {
                ((BasicAction) item).addActionListener(delegate);
            } else if (item instanceof ActionList) {
                ((ActionList) item).addActionListenerToAll(delegate);
            }
        });
    }

    private static Action findActionInList(Object id, List<?> actionList) {
        Action action;

        for (Object item : actionList) {
            if (item instanceof Action) {
                action = (Action) item;
                if (Objects.equals(id, action.getValue("ID")))
                    return action;
            }

            if (item instanceof ActionList) {
                action = findActionInList(id, (ActionList) item);
                if (action != null) return action;
            }
        }

        return null;
    }

    public void setContextForAll(Map context) {
        for (Object item : list) {
            if (!(item instanceof ContextAware)) continue;
            ContextAware contextAwareAction = (ContextAware) item;
            contextAwareAction.setContext(context);
        }
    }

    public void putContextValueForAll(Object key, Object contextValue) {
        for (Object item : list) {
            if (!(item instanceof ContextAware)) continue;
            ContextAware contextAwareAction = (ContextAware) item;
            contextAwareAction.putContextValue(key, contextValue);
        }
    }

    public void updateEnabledForAll() {
        for (Object item : list) {
            if (!(item instanceof EnabledUpdater)) continue;
            EnabledUpdater updateEnabledAction = (EnabledUpdater) item;
            updateEnabledAction.updateEnabled();
        }
    }

    public void setEnabledForAll(boolean enabled) {
        for (Object item : list) {
            if (!(item instanceof Action)) continue;
            ((Action) item).setEnabled(enabled);
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return list.iterator();
    }

    @Override
    public void forEach(Consumer<? super Object> o) {
        list.forEach(o);
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return list.toArray(generator);
    }

    @Override
    public boolean add(Object o) {
        return list.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean removeIf(Predicate<? super Object> filter) {
        return list.removeIf(filter);
    }

    @Override
    public void replaceAll(UnaryOperator<Object> operator) {
        list.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super Object> c) {
        list.sort(c);
    }

    @Override
    public boolean containsAll(Collection c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Object get(int index) {
        return list.get(index);
    }

    @Override
    public Object set(int index, Object o) {
        return list.set(index, o);
    }

    @Override
    public void add(int index, Object o) {
        list.add(index, o);
    }

    @Override
    public Object remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<Object> spliterator() {
        return list.spliterator();
    }

    @Override
    public Stream<Object> stream() {
        return list.stream();
    }

    @Override
    public Stream<Object> parallelStream() {
        return list.parallelStream();
    }
}

