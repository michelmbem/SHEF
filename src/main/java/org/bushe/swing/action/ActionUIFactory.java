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
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ActionUIFactory {
    private static final Object INSTANCE_LOCK = new Object();
    private static final boolean TOOLBAR_REQUEST_FOCUS_ENABLED_14 =
            !System.getProperty("java.vm.version").contains("1.3");
    private static final boolean SET_BUTTON_SIZE_FOR_13 =
            System.getProperty("java.vm.version").contains("1.3");
    private static final Map<String, ActionUIFactory> NAMED_INSTANCES =
            Collections.synchronizedMap(new HashMap<>());

    private static ActionUIFactory INSTANCE = new ActionUIFactory(ActionManager.getInstance());

    private Dimension toolbarButtonPreferredSize = null;
    private ActionManager actionManager;

    protected ActionUIFactory(ActionManager manager) {
        if (manager == null)
            manager = ActionManager.getInstance();
        actionManager = manager;
    }

    /*
     * WARNING - Removed try catching itself - possible behavior change.
     */
    public static ActionUIFactory getInstance() {
        return INSTANCE;
    }

    /*
     * WARNING - Removed try catching itself - possible behavior change.
     */
    public static ActionUIFactory getInstance(String name) {
        return name == null ? INSTANCE : NAMED_INSTANCES.get(name);
    }

    public static void setInstance(String name, ActionUIFactory factory) {
        setInstance(name, factory, factory == null ? null : factory.actionManager);
    }

    /*
     * WARNING - Removed try catching itself - possible behavior change.
     */
    public static void setInstance(String name, ActionUIFactory factory, ActionManager manager) {
        synchronized (INSTANCE_LOCK) {
            if (factory != null && factory.actionManager != manager) {
                factory.actionManager = manager;
            }

            if (name == null) {
                INSTANCE = factory;
            } else {
                NAMED_INSTANCES.put(name, factory);
            }
        }
    }

    public static ActionUIFactory createNamedInstance(String name, ActionManager manager) {
        ActionUIFactory factory = new ActionUIFactory(manager);
        setInstance(name, factory);
        return factory;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void setToolbarButtonPreferredSize(Dimension toolbarButtonPreferredSize) {
        this.toolbarButtonPreferredSize = toolbarButtonPreferredSize;
    }

    public JToolBar createToolBar(Object actionListId) {
        ActionList actions = getActionManager().getActionList(actionListId);
        return actions == null ? null : createToolBar(actions);
    }

    public JMenuBar createMenuBar(Object actionListId) {
        ActionList list = getActionManager().getActionList(actionListId);
        if (list == null) return null;

        JMenuBar menubar = instantiateJMenuBar();
        loadActions(list, menubar);
        return menubar;
    }

    public JToolBar createToolBar(ActionList actions) {
        JToolBar toolBar = instantiateJToolBar();
        loadActions(actions, toolBar);
        return toolBar;
    }

    public JMenuBar createMenuBar(ActionList actions) {
        JMenuBar menuBar = instantiateJMenuBar();
        loadActions(actions, menuBar);
        return menuBar;
    }

    public JMenu createMenu(Object listId) {
        return createMenu(getActionManager().getActionList(listId));
    }

    public JMenu createMenu(ActionList actions) {
        if (actions == null) return null;

        Action triggerAction = getActionManager().getAction(actions.getTriggerActionId());
        return createMenu(triggerAction, actions);
    }

    public JMenu createMenu(Action menuTriggerAction, ActionList actions) {
        JMenu menu = instantiateJMenu(menuTriggerAction);
        loadActions(menu, actions);
        return menu;
    }

    public JMenu createMenu(String menuName, ActionList actions) {
        JMenu menu = instantiateJMenu(menuName);
        loadActions(menu, actions);
        return menu;
    }

    public JPopupMenu createPopupMenu(Object listId) {
        ActionList actions = getActionManager().getActionList(listId);
        return actions == null ? null : createPopupMenu(actions);
    }

    public JPopupMenu createPopupMenu(ActionList list) {
        JPopupMenu popup = new JPopupMenu();
        loadActions(popup, list);
        return popup;
    }

    private JMenuItem createMenuItem(Object id) {
        Action action = getActionManager().getAction(id);
        return action == null ? null : createMenuItem(action);
    }

    public JMenuItem createMenuItem(Action action) {
        String buttonType = "" + action.getValue("BUTTON_TYPE");
        JMenuItem menuItem = action.getValue("GROUP") != null
                ? instantiateCheckBoxMenuItem(action)
                : ("toggle".equals(buttonType)
                ? instantiateCheckBoxMenuItem(action)
                : ("radio".equals(buttonType)
                ? instantiateRadioButtonMenuItem(action)
                : ("checkbox".equals(buttonType)
                ? instantiateCheckBoxMenuItem(action)
                : instantiateJMenuItem(action))));

        configureMenuItem(menuItem);
        return menuItem;
    }

    public AbstractButton createButton(Object id) {
        Action action = getActionManager().getAction(id);
        return action == null ? null : createButton(action);
    }

    public AbstractButton createButton(Action action) {
        Object buttonType = action.getValue("BUTTON_TYPE");
        AbstractButton button = action.getValue("GROUP") != null
                ? instantiateToggleButton(action)
                : ("toggle".equals(buttonType)
                ? instantiateToggleButton(action)
                : ("radio".equals(buttonType)
                ? instantiateRadioButton(action)
                : ("checkbox".equals(buttonType)
                ? instantiateJCheckBox(action)
                : instantiateJButton(action))));

        configureToolBarButton(button);
        return button;
    }

    protected void configureToolBarButton(AbstractButton button) {
        commonConfig(button);
        button.setText("");

        Action action = button.getAction();
        if (action != null && action.getValue("ShortDescription") == null) {
            button.setToolTipText((String) action.getValue("Name"));
        }

        if (toolbarButtonPreferredSize != null) {
            button.setPreferredSize(toolbarButtonPreferredSize);
            button.setMaximumSize(toolbarButtonPreferredSize);
        } else if (SET_BUTTON_SIZE_FOR_13) {
            final Dimension buttonSize = new Dimension(32, 32);
            button.setPreferredSize(buttonSize);
            button.setMaximumSize(buttonSize);
        }

        if (TOOLBAR_REQUEST_FOCUS_ENABLED_14) {
            button.setRequestFocusEnabled(false);
        }
    }

    protected void configureMenuItem(JMenuItem menuItem) {
        commonConfig(menuItem);
        menuItem.setToolTipText(null);

        Action action = menuItem.getAction();
        if (action == null) return;

        try {
            Boolean showsIcon = (Boolean) action.getValue("MENU_SHOWS_ICON");
            if (showsIcon != null && !showsIcon) {
                menuItem.setIcon(null);
            }
        } catch (ClassCastException ignored) {
        }
    }

    private void commonConfig(AbstractButton buttonOrMenuItem) {
        Boolean selected;

        Action action = buttonOrMenuItem.getAction();
        if (action == null) return;

        buttonOrMenuItem.setActionCommand((String) action.getValue("ActionCommandKey"));
        if (isShouldSyncSelectedProperty(buttonOrMenuItem, action)) {
            new ActionSelectionSynchronizer(buttonOrMenuItem, action);
        }

        if ((selected = (Boolean) action.getValue("SELECTED")) != null) {
            buttonOrMenuItem.setSelected(selected);
        }
    }

    protected boolean isShouldSyncSelectedProperty(AbstractButton buttonOrMenuItem, Action action) {
        return true;
    }

    protected JToolBar instantiateJToolBar() {
        return new JToolBar();
    }

    protected JMenuBar instantiateJMenuBar() {
        return new JMenuBar();
    }

    protected JMenu instantiateJMenu(Action menuAction) {
        return new JMenu(menuAction);
    }

    protected JMenu instantiateJMenu(String menuName) {
        return new JMenu(menuName);
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
        return new JCheckBox(action);
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
        return new JToolBar.Separator(null) {
            @Override
            public Color getForeground() {
                return getBackground();
            }
        };
    }

    public void loadActions(ActionList actions, JToolBar toolBar) {
        if (actions == null) return;

        Map<Object, ButtonGroup> buttonGroupsByGroupId = new HashMap<>();

        for (Object elem : actions) {
            if (elem == null || elem instanceof Separator) {
                boolean lineVisible =
                        elem != null && ((Separator) elem).isLineVisible();

                toolBar.add(lineVisible
                        ? instantiateInvisibleSeparator()
                        : instantiateSeparator());

                continue;
            }

            Action action = (Action) elem;
            AbstractButton button = createButton(action);
            toolBar.add(button);

            Object groupId = action.getValue("GROUP");
            if (groupId == null) continue;

            buttonGroupsByGroupId.computeIfAbsent(
                    groupId, id -> new ButtonGroup())
                    .add(button);
        }

        if (TOOLBAR_REQUEST_FOCUS_ENABLED_14) {
            toolBar.setRequestFocusEnabled(false);
        }
    }

    private void loadActions(ActionList list, JMenuBar menubar) {
        JMenu menu = null;

        for (Object element : list) {
            if (element == null || element instanceof Separator) {
                if (menu != null) {
                    menu.addSeparator();
                }
                continue;
            }

            if (element instanceof ActionList) {
                menu = createMenu((ActionList) element);
                if (menu != null) {
                    menubar.add(menu);
                }
                continue;
            }

            if (menu == null) {
                continue;
            }

            JMenuItem menuItem = element instanceof Action
                    ? createMenuItem((Action) element)
                    : createMenuItem(element);
            menu.add(menuItem);
        }
    }

    private void loadActions(JMenu menu, ActionList actions) {
        for (Object element : actions) {
            if (element == null || element instanceof Separator) {
                menu.addSeparator();
                continue;
            }

            if (element instanceof ActionList) {
                JMenu newMenu = createMenu((ActionList) element);
                if (newMenu != null) {
                    menu.add(newMenu);
                }
                continue;
            }

            JMenuItem menuItem = element instanceof Action
                    ? createMenuItem((Action) element)
                    : createMenuItem(element);
            menu.add(menuItem);
        }
    }

    private void loadActions(JPopupMenu popup, ActionList actions) {
        Map<Object, ButtonGroup> buttonGroups = new HashMap<>();

        for (Object element : actions) {
            if (element == null || element instanceof Separator) {
                popup.addSeparator();
                continue;
            }

            if (element instanceof ActionList) {
                JMenu newMenu = createMenu((ActionList) element);
                if (newMenu != null) {
                    popup.add(newMenu);
                }
                continue;
            }

            JMenuItem menuItem = element instanceof Action
                    ? createMenuItem((Action) element)
                    : createMenuItem(element);

            handleButtonGroups(menuItem, buttonGroups);
            popup.add(menuItem);
        }
    }

    private void handleButtonGroups(
            AbstractButton buttonOrMenu,
            Map<Object, ButtonGroup> buttonGroups) {

        if (buttonOrMenu == null || buttonOrMenu.getAction() == null) {
            return;
        }

        Object groupId = buttonOrMenu.getAction().getValue("GROUP");
        if (groupId == null) {
            return;
        }

        ButtonGroup buttonGroup =
                buttonGroups.computeIfAbsent(groupId, id -> new ButtonGroup());
        buttonGroup.add(buttonOrMenu);
    }
}

