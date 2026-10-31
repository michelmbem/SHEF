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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

class ActionSelectionSynchronizer implements PropertyChangeListener, ItemListener {
    private WeakReference<AbstractButton> buttonRef;
    private WeakReference<Action> actionRef;

    public ActionSelectionSynchronizer(AbstractButton button, Action action) {
        if (button != null) {
            buttonRef = new WeakReference<>(button);
            button.addItemListener(this);
        }
        
        if (action != null) {
            actionRef = new WeakReference<>(action);
            action.addPropertyChangeListener(this);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (!checkValidity()) return;

        AbstractButton button = buttonRef.get();
        Action action = actionRef.get();

        boolean buttonSelected = button.isSelected();
        Object value = action.getValue("SELECTED");
        boolean changeNeeded = true;

        if (value instanceof Boolean) {
            boolean isActionSelected = (Boolean) value;
            if (e.getStateChange() == ItemEvent.SELECTED && isActionSelected ||
                    e.getStateChange() == ItemEvent.DESELECTED && !isActionSelected) {
                changeNeeded = false;
            }
        }

        if (changeNeeded) {
            action.putValue("SELECTED", buttonSelected);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (!checkValidity()) return;

        String propertyName = evt.getPropertyName();
        if (propertyName.equals("SELECTED")) {
            boolean actionSelected;
            if (!checkValidity()) return;

            AbstractButton button = buttonRef.get();
            Action action = actionRef.get();

            boolean buttonSelected = button.isSelected();
            Object value = action.getValue("SELECTED");

            if (value instanceof Boolean && (actionSelected = (Boolean) value) != buttonSelected) {
                button.setSelected(actionSelected);
            }
        }
    }

    private boolean checkValidity() {
        if (buttonRef == null || actionRef == null ||
                buttonRef.get() == null || actionRef.get() == null) {
            removeListeners();
            return false;
        }

        return true;
    }

    private void removeListeners() {
        Action action = actionRef.get();
        if (action != null) {
            action.removePropertyChangeListener(this);
        }
        actionRef = null;

        AbstractButton button = buttonRef.get();
        if (button != null) {
            button.removeItemListener(this);
        }
        buttonRef = null;
    }
}

