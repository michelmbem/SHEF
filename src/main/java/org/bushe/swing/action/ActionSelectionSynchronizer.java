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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import javax.swing.AbstractButton;
import javax.swing.Action;

class ActionSelectionSynchronizer
implements PropertyChangeListener,
ItemListener {
    private WeakReference buttonRef;
    private WeakReference actionRef;

    public ActionSelectionSynchronizer(AbstractButton button, Action action) {
        if (button != null) {
            this.buttonRef = new WeakReference<AbstractButton>(button);
            button.addItemListener(this);
        }
        if (action != null) {
            this.actionRef = new WeakReference<Action>(action);
            action.addPropertyChangeListener(this);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (!this.checkValidity()) {
            return;
        }
        AbstractButton button = (AbstractButton)this.buttonRef.get();
        Action action = (Action)this.actionRef.get();
        boolean buttonSelected = button.isSelected();
        Object value = action.getValue("SELECTED");
        boolean changeNeeded = true;
        if (value instanceof Boolean) {
            boolean isActionSelected = (Boolean)value;
            if (e.getStateChange() == 1 && isActionSelected) {
                changeNeeded = false;
            } else if (e.getStateChange() == 2 && !isActionSelected) {
                changeNeeded = false;
            }
        }
        if (changeNeeded) {
            action.putValue("SELECTED", new Boolean(buttonSelected));
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (!this.checkValidity()) {
            return;
        }
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("SELECTED")) {
            boolean actionSelected;
            if (!this.checkValidity()) {
                return;
            }
            AbstractButton button = (AbstractButton)this.buttonRef.get();
            Action action = (Action)this.actionRef.get();
            boolean buttonSelected = button.isSelected();
            Object value = action.getValue("SELECTED");
            if (value instanceof Boolean && (actionSelected = ((Boolean)value).booleanValue()) != buttonSelected) {
                button.setSelected(actionSelected);
            }
        }
    }

    private boolean checkValidity() {
        if (this.buttonRef == null || this.actionRef == null || this.buttonRef.get() == null || this.actionRef.get() == null) {
            this.removeListeners();
            return false;
        }
        return true;
    }

    private void removeListeners() {
        Action action = (Action)this.actionRef.get();
        if (action != null) {
            action.removePropertyChangeListener(this);
        }
        this.actionRef = null;
        AbstractButton button = (AbstractButton)this.buttonRef.get();
        if (button != null) {
            button.removeItemListener(this);
        }
        this.buttonRef = null;
    }
}

