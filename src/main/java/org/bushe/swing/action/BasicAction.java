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
import java.util.*;

public class BasicAction
        extends AbstractAction
        implements Actionable,
        ItemAction,
        DelegatesEnabled,
        ContextAware {
    protected static boolean DEFAULT_ENABLED_STATE = true;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    EventListenerList listenerList = new EventListenerList();
    private List enabledDelegates;
    private Map context;
    private String idForDebugging = null;

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

    public BasicAction(String id, String shortDesc, String longDesc, Integer mnemonic, KeyStroke accelerator, Icon icon) {
        this(id, id, id, shortDesc, longDesc, mnemonic, accelerator, icon);
    }

    public BasicAction(String id, String actionName, String actionCommandName, String shortDesc, String longDesc, Integer mnemonic, KeyStroke accelerator, Icon icon) {
        this(id, id, id, shortDesc, longDesc, mnemonic, accelerator, icon, false, false);
    }

    public BasicAction(String id, String actionName, String actionCommandName, String shortDesc, String longDesc, Integer mnemonic, KeyStroke accelerator, Icon icon, boolean toolbarShowsText, boolean menuShowsIcon) {
        this.setId(id);
        this.setActionName(actionName);
        this.setActionCommandName(actionCommandName);
        this.setShortDescription(shortDesc);
        this.setLongDescription(longDesc);
        this.setMnemonic(mnemonic);
        this.setAccelerator(accelerator);
        this.setSmallIcon(icon);
        this.setToolbarShowsText(toolbarShowsText);
        this.setMenuShowsIcon(menuShowsIcon);
    }

    public void addActionListener(ActionListener listener) {
        this.listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        this.listenerList.remove(ActionListener.class, listener);
    }

    public final void actionPerformed(ActionEvent evt) {
        this.actionPerformedTemplate(evt);
    }

    protected void actionPerformedTemplate(ActionEvent evt) {
        try {
            try {
                this.actionPerformedTry();
                this.execute(evt);
                this.propogateActionEvent(evt);
            } catch (Throwable t) {
                this.actionPerformedCatch(t);
            }
        } finally {
            this.actionPerformedFinally();
        }
    }

    protected void propogateActionEvent(ActionEvent evt) {
        Object[] listeners = this.listenerList.getListenerList();
        int i = listeners.length - 2;
        while (i >= 0) {
            Object object = listeners[i];
            Class<?> clazz = class$0;
            if (clazz == null) {
                try {
                    clazz = Class.forName("java.awt.event.ActionListener");
                } catch (ClassNotFoundException classNotFoundException) {
                    throw new NoClassDefFoundError(classNotFoundException.getMessage());
                }
            }
            if (object == clazz) {
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

    public void addShouldBeEnabledDelegate(ShouldBeEnabledDelegate enabledDelegate) {
        if (this.enabledDelegates == null) {
            this.enabledDelegates = new ArrayList(3);
        }
        this.enabledDelegates.add(enabledDelegate);
    }

    public void removeShouldBeEnabledDelegate(ShouldBeEnabledDelegate enabledDelegate) {
        if (this.enabledDelegates == null) {
            return;
        }
        this.enabledDelegates.remove(enabledDelegate);
    }

    public void updateEnabledState() {
        boolean shouldBe = this.shouldBeEnabled();
        this.setEnabled(shouldBe);
    }

    public boolean shouldBeEnabled() {
        if (this.enabledDelegates == null || this.enabledDelegates.isEmpty()) {
            return DEFAULT_ENABLED_STATE;
        }
        ArrayList copy = new ArrayList(this.enabledDelegates);
        Iterator iter = copy.iterator();
        while (iter.hasNext()) {
            ShouldBeEnabledDelegate enabledUpdater = (ShouldBeEnabledDelegate) iter.next();
            if (enabledUpdater == null || enabledUpdater.shouldBeEnabled(this) == DEFAULT_ENABLED_STATE) continue;
            return !DEFAULT_ENABLED_STATE;
        }
        return DEFAULT_ENABLED_STATE;
    }

    public void addItemListener(ItemListener l) {
        this.listenerList.add(ItemListener.class, l);
    }

    public void removeItemListener(ItemListener l) {
        this.listenerList.remove(ItemListener.class, l);
    }

    protected void propogateItemEvent(ItemEvent evt) {
        Object[] listeners = this.listenerList.getListenerList();
        int i = listeners.length - 2;
        while (i >= 0) {
            Object object = listeners[i];
            Class<?> clazz = class$1;
            if (clazz == null) {
                try {
                    clazz = Class.forName("java.awt.event.ItemListener");
                } catch (ClassNotFoundException classNotFoundException) {
                    throw new NoClassDefFoundError(classNotFoundException.getMessage());
                }
            }
            if (object == clazz) {
                ((ItemListener) listeners[i + 1]).itemStateChanged(evt);
            }
            i -= 2;
        }
    }

    public boolean isSelected() {
        Object actionSelected = this.getValue("SELECTED");
        return Boolean.TRUE.equals(actionSelected);
    }

    public synchronized void setSelected(boolean selected) {
        boolean oldValue = this.isSelected();
        if (oldValue != selected) {
            Boolean selectedBool = selected ? Boolean.TRUE : Boolean.FALSE;
            this.putValue("SELECTED", selectedBool);
            this.firePropertyChange("selected", oldValue ? Boolean.TRUE : Boolean.FALSE, selectedBool);
        }
    }

    public Object getGroup() {
        return this.getValue("GROUP");
    }

    public Map getContext() {
        return this.context;
    }

    public void setContext(Map context) {
        this.context = context;
        this.contextChanged();
    }

    public void clearContext() {
        if (this.context != null) {
            this.context.clear();
        }
        this.contextChanged();
    }

    public void putContextValue(Object key, Object contextValue) {
        if (this.context == null) {
            this.context = new HashMap(3);
        }
        this.context.put(key, contextValue);
        this.contextChanged();
    }

    public Object getContextValue(Object key) {
        if (this.context == null) {
            return null;
        }
        return this.context.get(key);
    }

    protected void contextChanged() {
        this.updateEnabledState();
    }

    public String getId() {
        return (String) this.getValue("ID");
    }

    public void setId(String id) {
        this.putValue("ID", id);
    }

    public String getActionName() {
        return (String) this.getValue("Name");
    }

    public void setActionName(String actionName) {
        this.putValue("Name", actionName);
    }

    public String getActionCommandName() {
        return (String) this.getValue("ActionCommandKey");
    }

    public void setActionCommandName(String actionCommandName) {
        this.putValue("ActionCommandKey", actionCommandName);
    }

    public String getShortDescription() {
        return (String) this.getValue("ShortDescription");
    }

    public void setShortDescription(String shortDesc) {
        this.putValue("ShortDescription", shortDesc);
    }

    public String getLongDescription() {
        return (String) this.getValue("LongDescription");
    }

    public void setLongDescription(String longDesc) {
        this.putValue("LongDescription", longDesc);
    }

    public Integer getMnemonic() {
        return (Integer) this.getValue("MnemonicKey");
    }

    protected void setMnemonic(Integer mnemonic) {
        this.putValue("MnemonicKey", mnemonic);
    }

    public KeyStroke getAccelerator() {
        return (KeyStroke) this.getValue("AcceleratorKey");
    }

    public void setAccelerator(KeyStroke accelerator) {
        this.putValue("AcceleratorKey", accelerator);
    }

    public ImageIcon getSmallIcon() {
        return (ImageIcon) this.getValue("SmallIcon");
    }

    public void setSmallIcon(Icon smallIcon) {
        this.putValue("SmallIcon", smallIcon);
    }

    public boolean getToolbarShowsText() {
        Object value = this.getValue("TOOLBAR_SHOWS_TEXT");
        if (value == null) {
            return false;
        }
        return (Boolean) value;
    }

    public void setToolbarShowsText(boolean toolbarShowsText) {
        if (toolbarShowsText) {
            this.putValue("TOOLBAR_SHOWS_TEXT", Boolean.TRUE);
        } else {
            this.putValue("TOOLBAR_SHOWS_TEXT", Boolean.FALSE);
        }
    }

    public boolean getMenuShowsIcon() {
        Object value = this.getValue("MENU_SHOWS_ICON");
        if (value == null) {
            return false;
        }
        return (Boolean) value;
    }

    public void setMenuShowsIcon(boolean menuShowsIcon) {
        if (menuShowsIcon) {
            this.putValue("MENU_SHOWS_ICON", Boolean.TRUE);
        } else {
            this.putValue("MENU_SHOWS_ICON", Boolean.FALSE);
        }
    }

    public List getRoles() {
        return (List) this.getValue("ROLES");
    }

    public void setRoles(List roles) {
        this.putValue("ROLES", roles);
    }

    protected ImageIcon createIcon(String resourcePath) {
        return this.createIcon(this.getClass().getResource(resourcePath));
    }

    protected ImageIcon createIcon(URL imageURL) {
        if (imageURL == null) {
            return null;
        }
        return new ImageIcon(imageURL);
    }

    public void putValue(String key, Object value) {
        if (key != null && value != null) {
            if (key.equals("ID")) {
                this.idForDebugging = value.toString();
            }
            if (value instanceof String) {
                value = ((String) value).intern();
            }
        }
        super.putValue(key, value);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(100);
        buf.append(super.toString() + " [id=");
        buf.append(this.getId());
        buf.append(", enabled=");
        buf.append(this.isEnabled());
        buf.append(", values={");
        Object[] keys = this.getKeys();
        if (keys != null) {
            int i = 0;
            while (i < keys.length) {
                buf.append(keys[i]);
                buf.append("->");
                buf.append(this.getValue(String.valueOf(keys[i])));
                buf.append(";");
                ++i;
            }
        }
        buf.append("}, context=");
        buf.append(this.getContext());
        buf.append(", enabled delegates=");
        buf.append(this.enabledDelegates);
        buf.append("]");
        return buf.toString();
    }
}

