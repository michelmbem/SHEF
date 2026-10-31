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

import java.awt.Color;
import java.awt.Dimension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionSelectionSynchronizer;
import org.bushe.swing.action.BasicAction;
import org.bushe.swing.action.Separator;

public class ActionUIFactory {
    private static ActionUIFactory INSTANCE;
    private static Map NAMED_INSTANCES;
    private static Object INSTANCE_LOCK;
    private static boolean sToolbarRequestFocusEnabled14;
    private static boolean sSetButtonSizeFor13;
    private Dimension toolbarButtonPreferredSize = null;
    private static boolean DEBUG;
    private ActionManager actionManager;

    static {
        INSTANCE_LOCK = new Object();
        sToolbarRequestFocusEnabled14 = System.getProperty("java.vm.version").indexOf("1.3") <= -1;
        sSetButtonSizeFor13 = System.getProperty("java.vm.version").indexOf("1.3") > -1;
        DEBUG = false;
    }

    protected ActionUIFactory(ActionManager manager) {
        if (manager == null) {
            manager = ActionManager.getInstance();
        }
        this.actionManager = manager;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ActionUIFactory getInstance() {
        Object object = INSTANCE_LOCK;
        synchronized (object) {
            if (INSTANCE == null) {
                INSTANCE = new ActionUIFactory(ActionManager.getInstance());
                ActionUIFactory.setInstance(null, INSTANCE);
            }
        }
        return INSTANCE;
    }

    public static ActionUIFactory createNamedInstance(String name, ActionManager manager) {
        ActionUIFactory factory = new ActionUIFactory(manager);
        ActionUIFactory.setInstance(name, factory);
        return factory;
    }

    public static void setInstance(String name, ActionUIFactory factory) {
        ActionUIFactory.setInstance(name, factory, factory == null ? null : factory.actionManager);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setInstance(String name, ActionUIFactory factory, ActionManager manager) {
        Object object = INSTANCE_LOCK;
        synchronized (object) {
            if (NAMED_INSTANCES == null) {
                NAMED_INSTANCES = Collections.synchronizedMap(new HashMap());
            }
            if (factory != null) {
                factory.actionManager = manager;
            }
            NAMED_INSTANCES.put(name, factory);
            if (name == null) {
                INSTANCE = factory;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ActionUIFactory getInstance(String name) {
        Object object = INSTANCE_LOCK;
        synchronized (object) {
            block5: {
                if (NAMED_INSTANCES != null) break block5;
                if (name == null) {
                    return ActionUIFactory.getInstance();
                }
                return null;
            }
            return (ActionUIFactory)NAMED_INSTANCES.get(name);
        }
    }

    public ActionManager getActionManager() {
        return this.actionManager;
    }

    public void setToolbarButtonPreferredSize(Dimension toolbarButtonPreferredSize) {
        this.toolbarButtonPreferredSize = toolbarButtonPreferredSize;
    }

    public JToolBar createToolBar(Object actionListId) {
        ActionList actions = this.getActionManager().getActionList(actionListId);
        if (actions == null) {
            return null;
        }
        return this.createToolBar(actions);
    }

    public JMenuBar createMenuBar(Object actionListId) {
        ActionList list = this.getActionManager().getActionList(actionListId);
        if (list == null) {
            return null;
        }
        JMenuBar menubar = this.instantiateJMenuBar();
        this.loadActions(list, menubar);
        return menubar;
    }

    public JToolBar createToolBar(ActionList actions) {
        JToolBar toolBar = this.instantiateJToolBar();
        this.loadActions(actions, toolBar);
        return toolBar;
    }

    public JMenuBar createMenuBar(ActionList actions) {
        JMenuBar menuBar = this.instantiateJMenuBar();
        this.loadActions(actions, menuBar);
        return menuBar;
    }

    public JMenu createMenu(Object listId) {
        return this.createMenu(this.getActionManager().getActionList(listId));
    }

    public JMenu createMenu(ActionList actions) {
        if (actions == null) {
            return null;
        }
        Action triggerAction = this.getActionManager().getAction(actions.getTriggerActionId());
        return this.createMenu(triggerAction, actions);
    }

    public JMenu createMenu(Action menuTriggerAction, ActionList actions) {
        JMenu menu = this.instantiateJMenu(menuTriggerAction);
        this.loadActions(menu, actions);
        return menu;
    }

    public JMenu createMenu(String menuName, ActionList actions) {
        JMenu menu = this.instantiateJMenu(menuName);
        this.loadActions(menu, actions);
        return menu;
    }

    public JPopupMenu createPopupMenu(Object listId) {
        ActionList actions = this.getActionManager().getActionList(listId);
        if (actions == null) {
            return null;
        }
        return this.createPopupMenu(actions);
    }

    public JPopupMenu createPopupMenu(ActionList list) {
        JPopupMenu popup = new JPopupMenu();
        this.loadActions(popup, list);
        return popup;
    }

    private JMenuItem createMenuItem(Object id) {
        Action action = this.getActionManager().getAction(id);
        if (action == null) {
            return null;
        }
        JMenuItem menuItem = this.createMenuItem(action);
        return menuItem;
    }

    public JMenuItem createMenuItem(Action action) {
        String buttonType = "" + action.getValue("BUTTON_TYPE");
        JMenuItem menuItem = action.getValue("GROUP") != null ? this.instantiateCheckBoxMenuItem(action) : ("toggle".equals(buttonType) ? this.instantiateCheckBoxMenuItem(action) : ("radio".equals(buttonType) ? this.instantiateRadioButtonMenuItem(action) : ("checkbox".equals(buttonType) ? this.instantiateCheckBoxMenuItem(action) : this.instantiateJMenuItem(action))));
        this.configureMenuItem(menuItem);
        return menuItem;
    }

    public AbstractButton createButton(Object id) {
        Action action = this.getActionManager().getAction(id);
        if (action == null) {
            return null;
        }
        return this.createButton(action);
    }

    public AbstractButton createButton(Action action) {
        AbstractButton button = null;
        Object buttonType = action.getValue("BUTTON_TYPE");
        button = action.getValue("GROUP") != null ? this.instantiateToggleButton(action) : ("toggle".equals(buttonType) ? this.instantiateToggleButton(action) : ("radio".equals(buttonType) ? this.instantiateRadioButton(action) : ("checkbox".equals(buttonType) ? this.instantiateJCheckBox(action) : this.instantiateJButton(action))));
        this.configureToolBarButton(button);
        return button;
    }

    protected void configureToolBarButton(AbstractButton button) {
        this.commonConfig(button);
        button.setText("");
        Action action = button.getAction();
        if (action != null) {
            if (action.getValue("ShortDescription") == null) {
                button.setToolTipText((String)action.getValue("Name"));
            }
            if (action instanceof BasicAction && !((BasicAction)action).getToolbarShowsText()) {
                button.setText("");
            }
        }
        if (this.toolbarButtonPreferredSize != null) {
            button.setPreferredSize(this.toolbarButtonPreferredSize);
            button.setMaximumSize(this.toolbarButtonPreferredSize);
        } else if (sSetButtonSizeFor13) {
            button.setPreferredSize(new Dimension(32, 32));
            button.setMaximumSize(new Dimension(32, 32));
        }
        if (sToolbarRequestFocusEnabled14) {
            button.setRequestFocusEnabled(false);
        }
    }

    protected void configureMenuItem(JMenuItem menuItem) {
        this.commonConfig(menuItem);
        menuItem.setToolTipText(null);
        Action action = menuItem.getAction();
        if (action == null) {
            return;
        }
        try {
            Boolean shows = (Boolean)action.getValue("MENU_SHOWS_ICON");
            if (shows != null && !shows.booleanValue()) {
                menuItem.setIcon(null);
            }
        }
        catch (ClassCastException classCastException) {
            // empty catch block
        }
    }

    private void commonConfig(AbstractButton buttonOrMenuItem) {
        Boolean selected;
        Action action = buttonOrMenuItem.getAction();
        if (action == null) {
            return;
        }
        buttonOrMenuItem.setActionCommand((String)action.getValue("ActionCommandKey"));
        if (this.isShouldSyncSelectedProperty(buttonOrMenuItem, action)) {
            ActionSelectionSynchronizer actionSelectionSynchronizer = new ActionSelectionSynchronizer(buttonOrMenuItem, action);
        }
        if ((selected = (Boolean)action.getValue("SELECTED")) != null) {
            buttonOrMenuItem.setSelected(selected);
        }
    }

    protected boolean isShouldSyncSelectedProperty(AbstractButton buttonOrMenuItem, Action action) {
        return true;
    }

    protected JToolBar instantiateJToolBar() {
        JToolBar toolBar = new JToolBar();
        return toolBar;
    }

    protected JMenuBar instantiateJMenuBar() {
        JMenuBar menubar = new JMenuBar();
        return menubar;
    }

    protected JMenu instantiateJMenu(Action menuAction) {
        JMenu menu = new JMenu(menuAction);
        return menu;
    }

    protected JMenu instantiateJMenu(String menuName) {
        JMenu menu = new JMenu(menuName);
        return menu;
    }

    protected JMenuItem instantiateJMenuItem(Action itemAction) {
        return new JMenuItem(itemAction);
    }

    protected JRadioButtonMenuItem instantiateRadioButtonMenuItem(Action itemAction) {
        return new JRadioButtonMenuItem(itemAction);
    }

    protected JCheckBoxMenuItem instantiateCheckBoxMenuItem(Action itemAction) {
        return new JCheckBoxMenuItem(itemAction);
    }

    protected JButton instantiateJButton(Action action) {
        return new JButton(action);
    }

    protected JCheckBox instantiateJCheckBox(Action action) {
        JCheckBox menu = new JCheckBox(action);
        return menu;
    }

    protected JToggleButton instantiateToggleButton(Action action) {
        return new JToggleButton(action);
    }

    protected JRadioButton instantiateRadioButton(Action action) {
        return new JRadioButton(action);
    }

    protected JSeparator instantiateSeparator() {
        return new JToolBar.Separator(null);
    }

    protected JSeparator instantiateInvisibleSeparator() {
        return new JToolBar.Separator(null){

            public Color getForeground() {
                return this.getBackground();
            }
        };
    }

    public void loadActions(ActionList actions, JToolBar toolBar) {
        if (actions == null) {
            return;
        }
        HashMap<Object, ButtonGroup> buttonGroupsByGroupID = new HashMap<Object, ButtonGroup>();
        Iterator iter = actions.iterator();
        while (iter.hasNext()) {
            Object elem = iter.next();
            if (elem == null || elem instanceof Separator) {
                if (elem instanceof Separator && !((Separator)elem).isLineVisible()) {
                    toolBar.add(this.instantiateSeparator());
                    continue;
                }
                toolBar.add(this.instantiateInvisibleSeparator());
                continue;
            }
            Action action = (Action)elem;
            AbstractButton button = this.createButton(action);
            toolBar.add(button);
            if (action.getValue("GROUP") == null) continue;
            ButtonGroup buttonGroup = (ButtonGroup)buttonGroupsByGroupID.get(action.getValue("GROUP"));
            if (buttonGroup == null) {
                buttonGroup = new ButtonGroup();
                buttonGroupsByGroupID.put(action.getValue("GROUP"), buttonGroup);
            }
            buttonGroup.add(button);
        }
        if (sToolbarRequestFocusEnabled14) {
            toolBar.setRequestFocusEnabled(false);
        }
    }

    private void loadActions(ActionList list, JMenuBar menubar) {
        JMenu menu = null;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Object element = iter.next();
            if (element == null || element instanceof Separator) {
                if (menu == null) continue;
                menu.addSeparator();
                continue;
            }
            if (element instanceof ActionList) {
                menu = this.createMenu((ActionList)element);
                if (menu == null) continue;
                menubar.add(menu);
                continue;
            }
            if (menu == null) continue;
            if (element instanceof Action) {
                menu.add(this.createMenuItem((Action)element));
                continue;
            }
            menu.add(this.createMenuItem(element));
        }
    }

    private void loadActions(JMenu menu, ActionList actions) {
        Iterator iter = actions.iterator();
        while (iter.hasNext()) {
            Object element = iter.next();
            if (element == null || element instanceof Separator) {
                menu.addSeparator();
                continue;
            }
            if (element instanceof ActionList) {
                JMenu newMenu = this.createMenu((ActionList)element);
                if (newMenu == null) continue;
                menu.add(newMenu);
                continue;
            }
            if (element instanceof Action) {
                menu.add(this.createMenuItem((Action)element));
                continue;
            }
            menu.add(this.createMenuItem(element));
        }
    }

    private void loadActions(JPopupMenu popup, ActionList actions) {
        HashMap buttonGroups = new HashMap();
        Iterator iter = actions.iterator();
        while (iter.hasNext()) {
            JMenuItem mi;
            Object element = iter.next();
            if (element == null || element instanceof Separator) {
                popup.addSeparator();
                continue;
            }
            if (element instanceof ActionList) {
                JMenu newMenu = this.createMenu((ActionList)element);
                if (newMenu == null) continue;
                popup.add(newMenu);
                continue;
            }
            if (element instanceof Action) {
                mi = this.createMenuItem((Action)element);
                this.handleButtonGroups(mi, buttonGroups);
                popup.add(mi);
                continue;
            }
            mi = this.createMenuItem(element);
            this.handleButtonGroups(mi, buttonGroups);
            popup.add(mi);
        }
    }

    private void handleButtonGroups(AbstractButton buttonOrMenu, Map buttonGroups) {
        Object group;
        if (buttonOrMenu == null) {
            return;
        }
        if (buttonOrMenu.getAction() != null && (group = buttonOrMenu.getAction().getValue("GROUP")) != null) {
            ButtonGroup bg = (ButtonGroup)buttonGroups.get(group);
            if (bg == null) {
                bg = new ButtonGroup();
                buttonGroups.put(group, bg);
            }
            bg.add(buttonOrMenu);
        }
    }
}

