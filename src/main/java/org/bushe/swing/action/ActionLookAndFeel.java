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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public class ActionLookAndFeel {
    public static final String ACTION_NEW = "New";
    public static final String ACTION_SAVE = "Save";
    public static final String ACTION_SAVE_AS = "Save As...";
    public static final String ACTION_EXIT = "Exit";
    public static final String ACTION_PREFERENCES = "Preferences";
    public static final String ACTION_PRINT = "Print";
    public static final String ACTION_COPY = "Copy";
    public static final String ACTION_DELETE = "Delete";
    public static final String ACTION_ABOUT = "About";
    public static final String ACTION_USER_GUIDE = "User's Guide";
    public static final String ACTION_REFRESH = "Refresh";
    protected static Map sLookAndFeel = new HashMap();
    static Map mLookAndFeel;
    static /* synthetic */ Class class$0;

    static {
        Class<?> clazz = class$0;
        if (clazz == null) {
            try {
                clazz = class$0 = Class.forName("org.bushe.swing.action.ActionConfigurator");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        ActionLookAndFeel.addLookAndFeelActionEntry(ACTION_NEW, ACTION_NEW, ACTION_NEW, ACTION_NEW, new ImageIcon(clazz.getResource("/toolbarButtonGraphics/general/New16.gif")), KeyStroke.getKeyStroke(78, 2), new Integer(78));
        Class<?> clazz2 = class$0;
        if (clazz2 == null) {
            try {
                clazz2 = class$0 = Class.forName("org.bushe.swing.action.ActionConfigurator");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        ActionLookAndFeel.addLookAndFeelActionEntry(ACTION_SAVE, ACTION_SAVE, ACTION_SAVE, ACTION_SAVE, new ImageIcon(clazz2.getResource("/toolbarButtonGraphics/general/Save16.gif")), KeyStroke.getKeyStroke(83, 2), new Integer(83));
        Class<?> clazz3 = class$0;
        if (clazz3 == null) {
            try {
                clazz3 = class$0 = Class.forName("org.bushe.swing.action.ActionConfigurator");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        ActionLookAndFeel.addLookAndFeelActionEntry(ACTION_SAVE_AS, "Save As", "Save As", "Save As", new ImageIcon(clazz3.getResource("/toolbarButtonGraphics/general/SaveAs16.gif")), null, new Integer(65));
        ActionLookAndFeel.addLookAndFeelActionEntry(ACTION_EXIT, ACTION_EXIT, ACTION_EXIT, ACTION_EXIT, null, null, new Integer(88));
        Class<?> clazz4 = class$0;
        if (clazz4 == null) {
            try {
                clazz4 = class$0 = Class.forName("org.bushe.swing.action.ActionConfigurator");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        ActionLookAndFeel.addLookAndFeelActionEntry(ACTION_PREFERENCES, ACTION_PREFERENCES, ACTION_PREFERENCES, ACTION_PREFERENCES, new ImageIcon(clazz4.getResource("/toolbarButtonGraphics/general/Preferences16.gif")), KeyStroke.getKeyStroke(82, 2), new Integer(82));
        Class<?> clazz5 = class$0;
        if (clazz5 == null) {
            try {
                clazz5 = class$0 = Class.forName("org.bushe.swing.action.ActionConfigurator");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        ActionLookAndFeel.addLookAndFeelActionEntry(ACTION_PRINT, ACTION_PRINT, ACTION_PRINT, ACTION_PRINT, new ImageIcon(clazz5.getResource("/toolbarButtonGraphics/general/Print16.gif")), KeyStroke.getKeyStroke(80, 2), new Integer(80));
        Class<?> clazz6 = class$0;
        if (clazz6 == null) {
            try {
                clazz6 = class$0 = Class.forName("org.bushe.swing.action.ActionConfigurator");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        ActionLookAndFeel.addLookAndFeelActionEntry(ACTION_COPY, ACTION_COPY, ACTION_COPY, ACTION_COPY, new ImageIcon(clazz6.getResource("/toolbarButtonGraphics/general/Copy16.gif")), KeyStroke.getKeyStroke(67, 2), new Integer(67));
        Class<?> clazz7 = class$0;
        if (clazz7 == null) {
            try {
                clazz7 = class$0 = Class.forName("org.bushe.swing.action.ActionConfigurator");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        ActionLookAndFeel.addLookAndFeelActionEntry(ACTION_DELETE, ACTION_DELETE, ACTION_DELETE, ACTION_DELETE, new ImageIcon(clazz7.getResource("/toolbarButtonGraphics/general/Delete16.gif")), KeyStroke.getKeyStroke(68, 2), new Integer(68));
        ActionLookAndFeel.addLookAndFeelActionEntry(ACTION_ABOUT, ACTION_ABOUT, ACTION_ABOUT, ACTION_ABOUT, null, null, new Integer(65));
        Class<?> clazz8 = class$0;
        if (clazz8 == null) {
            try {
                clazz8 = class$0 = Class.forName("org.bushe.swing.action.ActionConfigurator");
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        }
        ActionLookAndFeel.addLookAndFeelActionEntry(ACTION_REFRESH, ACTION_REFRESH, ACTION_REFRESH, ACTION_REFRESH, new ImageIcon(clazz8.getResource("/toolbarButtonGraphics/general/Refresh16.gif")), KeyStroke.getKeyStroke('t'), new Integer(82));
    }

    public static void addLookAndFeelActionEntry(String name, String cmd, String desc, String longDesc, ImageIcon icon, KeyStroke acc, Integer mnemonic) {
        HashMap<String, Object> valuesMap = new HashMap<String, Object>();
        valuesMap.put("Name", name);
        valuesMap.put("ActionCommandKey", cmd);
        valuesMap.put("ShortDescription", desc);
        valuesMap.put("LongDescription", longDesc);
        valuesMap.put("SmallIcon", icon);
        valuesMap.put("AcceleratorKey", acc);
        valuesMap.put("MnemonicKey", mnemonic);
        sLookAndFeel.put(name, valuesMap);
    }

    public ActionLookAndFeel() {
        mLookAndFeel = sLookAndFeel;
    }

    public ActionLookAndFeel(Map lookAndFeel) {
        mLookAndFeel = lookAndFeel;
    }

    public boolean setValuesToInstanceLookAndFeel(String actionConstant, Action action) {
        return ActionLookAndFeel.setValuesToLookAndFeel(mLookAndFeel, actionConstant, action);
    }

    public static boolean setValuesToLookAndFeel(String actionConstant, Action action) {
        return ActionLookAndFeel.setValuesToLookAndFeel(sLookAndFeel, actionConstant, action);
    }

    public static boolean setValuesToLookAndFeel(Map lookAndFeel, String actionConstant, Action action) {
        Map values;
        if (lookAndFeel == null) {
            lookAndFeel = sLookAndFeel;
        }
        if ((values = (Map)lookAndFeel.get(actionConstant)) == null) {
            return false;
        }
        Iterator iter = values.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            Object value = values.get(key);
            action.putValue(key, value);
        }
        return true;
    }
}

