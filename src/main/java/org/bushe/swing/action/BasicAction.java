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
import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicAction
        extends AbstractAction
        implements Actionable, ItemAction, DelegatesEnabled, ContextAware {

    protected static boolean DEFAULT_ENABLED_STATE = true;
    EventListenerList listenerList = new EventListenerList();
    private List<ShouldBeEnabledDelegate> enabledDelegates;
    private Map<Object, Object> context;
    private String idForDebugging;

    public BasicAction() {
    }

    public BasicAction(String id) {
        this(id, null);
    }

    public BasicAction(String id, Icon icon) {
        this(id, null, null, null, null, icon);
    }

    public BasicAction(String id, Integer mnemonic, KeyStroke accelerator, Icon icon) {
        this(id, null, null, mnemonic, accelerator, icon);
    }

    public BasicAction(String id, String shortDesc, String longDesc,
                       Integer mnemonic, KeyStroke accelerator, Icon icon) {

        this(id, id, id, shortDesc, longDesc, mnemonic, accelerator, icon);
    }

    public BasicAction(String id, String actionName, String actionCommandName, String shortDesc,
                       String longDesc, Integer mnemonic, KeyStroke accelerator, Icon icon) {

        this(id, id, id, shortDesc, longDesc, mnemonic, accelerator, icon, false, false);
    }

    public BasicAction(String id, String actionName, String actionCommandName, String shortDesc,
                       String longDesc, Integer mnemonic, KeyStroke accelerator, Icon icon,
                       boolean toolbarShowsText, boolean menuShowsIcon) {

        setId(id);
        setActionName(actionName);
        setActionCommandName(actionCommandName);
        setShortDescription(shortDesc);
        setLongDescription(longDesc);
        setMnemonic(mnemonic);
        setAccelerator(accelerator);
        setSmallIcon(icon);
        setToolbarShowsText(toolbarShowsText);
        setMenuShowsIcon(menuShowsIcon);
    }

    @Override
    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    @Override
    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    @Override
    public final void actionPerformed(ActionEvent evt) {
        actionPerformedTemplate(evt);
    }

    protected void actionPerformedTemplate(ActionEvent evt) {
        try {
            try {
                actionPerformedTry();
                execute(evt);
                propagateActionEvent(evt);
            } catch (Exception ex) {
                actionPerformedCatch(ex);
            }
        } finally {
            actionPerformedFinally();
        }
    }

    protected void propagateActionEvent(ActionEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        int i = listeners.length - 2;

        while (i >= 0) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener) listeners[i + 1]).actionPerformed(evt);
            }

            i -= 2;
        }
    }

    protected void actionPerformedTry() {
    }

    protected void execute(ActionEvent evt) throws Exception {
    }

    protected void actionPerformedCatch(Throwable t) {
        System.err.println("Exception in action " + this + ".  Exception:" + t);
        t.printStackTrace(System.err);
        throw new RuntimeException(t);
    }

    protected void actionPerformedFinally() {
    }

    @Override
    public void addShouldBeEnabledDelegate(ShouldBeEnabledDelegate enabledDelegate) {
        if (enabledDelegates == null) {
            enabledDelegates = new ArrayList<>(3);
        }

        enabledDelegates.add(enabledDelegate);
    }

    @Override
    public void removeShouldBeEnabledDelegate(ShouldBeEnabledDelegate enabledDelegate) {
        if (enabledDelegates == null) return;
        enabledDelegates.remove(enabledDelegate);
    }

    public void updateEnabledState() {
        boolean shouldBe = shouldBeEnabled();
        setEnabled(shouldBe);
    }

    public boolean shouldBeEnabled() {
        if (enabledDelegates == null || enabledDelegates.isEmpty()) {
            return DEFAULT_ENABLED_STATE;
        }

        List<ShouldBeEnabledDelegate> copy = new ArrayList<>(enabledDelegates);
        for (ShouldBeEnabledDelegate enabledUpdater : copy) {
            if (enabledUpdater == null ||
                    enabledUpdater.shouldBeEnabled(this) == DEFAULT_ENABLED_STATE) continue;

            return !DEFAULT_ENABLED_STATE;
        }

        return DEFAULT_ENABLED_STATE;
    }

    @Override
    public void addItemListener(ItemListener l) {
        listenerList.add(ItemListener.class, l);
    }

    @Override
    public void removeItemListener(ItemListener l) {
        listenerList.remove(ItemListener.class, l);
    }

    protected void propogateItemEvent(ItemEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        int i = listeners.length - 2;

        while (i >= 0) {
            if (listeners[i] == ItemListener.class) {
                ((ItemListener) listeners[i + 1]).itemStateChanged(evt);
            }

            i -= 2;
        }
    }

    @Override
    public boolean isSelected() {
        return Boolean.TRUE.equals(getValue("SELECTED"));
    }

    @Override
    public synchronized void setSelected(boolean selected) {
        boolean oldValue = isSelected();
        if (oldValue != selected) {
            Boolean selectedBool = selected ? Boolean.TRUE : Boolean.FALSE;
            putValue("SELECTED", selectedBool);
            firePropertyChange("selected", oldValue ? Boolean.TRUE : Boolean.FALSE, selectedBool);
        }
    }

    public Object getGroup() {
        return getValue("GROUP");
    }

    @Override
    public Map<Object, Object> getContext() {
        return context;
    }

    @Override
    public void setContext(Map<Object, Object> context) {
        this.context = context;
        contextChanged();
    }

    @Override
    public void clearContext() {
        if (context != null) {
            context.clear();
        }

        contextChanged();
    }

    @Override
    public void putContextValue(Object key, Object contextValue) {
        if (context == null) {
            context = new HashMap<>(3);
        }

        context.put(key, contextValue);
        contextChanged();
    }

    @Override
    public Object getContextValue(Object key) {
        return context == null ? null : context.get(key);
    }

    protected void contextChanged() {
        updateEnabledState();
    }

    public String getId() {
        return (String) getValue("ID");
    }

    public void setId(String id) {
        putValue("ID", id);
    }

    public String getActionName() {
        return (String) getValue("Name");
    }

    public void setActionName(String actionName) {
        putValue("Name", actionName);
    }

    public String getActionCommandName() {
        return (String) getValue("ActionCommandKey");
    }

    public void setActionCommandName(String actionCommandName) {
        putValue("ActionCommandKey", actionCommandName);
    }

    public String getShortDescription() {
        return (String) getValue("ShortDescription");
    }

    public void setShortDescription(String shortDesc) {
        putValue("ShortDescription", shortDesc);
    }

    public String getLongDescription() {
        return (String) getValue("LongDescription");
    }

    public void setLongDescription(String longDesc) {
        putValue("LongDescription", longDesc);
    }

    public Integer getMnemonic() {
        return (Integer) getValue("MnemonicKey");
    }

    protected void setMnemonic(Integer mnemonic) {
        putValue("MnemonicKey", mnemonic);
    }

    public KeyStroke getAccelerator() {
        return (KeyStroke) getValue("AcceleratorKey");
    }

    public void setAccelerator(KeyStroke accelerator) {
        putValue("AcceleratorKey", accelerator);
    }

    public ImageIcon getSmallIcon() {
        return (ImageIcon) getValue("SmallIcon");
    }

    public void setSmallIcon(Icon smallIcon) {
        putValue("SmallIcon", smallIcon);
    }

    public boolean getToolbarShowsText() {
        Object value = getValue("TOOLBAR_SHOWS_TEXT");
        return value != null && (Boolean) value;
    }

    public void setToolbarShowsText(boolean toolbarShowsText) {
        if (toolbarShowsText) {
            putValue("TOOLBAR_SHOWS_TEXT", Boolean.TRUE);
        } else {
            putValue("TOOLBAR_SHOWS_TEXT", Boolean.FALSE);
        }
    }

    public boolean getMenuShowsIcon() {
        Object value = getValue("MENU_SHOWS_ICON");
        return value != null && (Boolean) value;
    }

    public void setMenuShowsIcon(boolean menuShowsIcon) {
        if (menuShowsIcon) {
            putValue("MENU_SHOWS_ICON", Boolean.TRUE);
        } else {
            putValue("MENU_SHOWS_ICON", Boolean.FALSE);
        }
    }

    public List<Object> getRoles() {
        return (List<Object>) getValue("ROLES");
    }

    public void setRoles(List<Object> roles) {
        putValue("ROLES", roles);
    }

    protected ImageIcon createIcon(String resourcePath) {
        return createIcon(getClass().getResource(resourcePath));
    }

    protected ImageIcon createIcon(URL imageURL) {
        return imageURL == null ? null : new ImageIcon(imageURL);
    }

    public void putValue(String key, Object value) {
        if (key != null && value != null) {
            if (key.equals("ID")) {
                idForDebugging = value.toString();
            }
            if (value instanceof String) {
                value = ((String) value).intern();
            }
        }

        super.putValue(key, value);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(100)
                .append(super.toString())
                .append(" [id=").append(getId())
                .append(", enabled=").append(isEnabled())
                .append(", values={");

        Object[] keys = getKeys();
        if (keys != null) {
            for (Object key : keys) {
                builder.append(key)
                        .append("->")
                        .append(getValue(String.valueOf(key)))
                        .append(";");
            }
        }

        builder.append("}, context=")
                .append(getContext())
                .append(", enabled delegates=")
                .append(enabledDelegates)
                .append("]");

        return builder.toString();
    }
}

