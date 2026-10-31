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
import java.util.HashMap;
import java.util.Map;

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

    protected static Map<String, Map<String, Object>> sLookAndFeel = new HashMap<>();
    static Map<String, Map<String, Object>> mLookAndFeel;

    static {
        Class<?> thisClass = ActionLookAndFeel.class;

        addLookAndFeelActionEntry(ACTION_NEW, ACTION_NEW, ACTION_NEW, ACTION_NEW,
                new ImageIcon(thisClass.getResource("/toolbarButtonGraphics/general/New16.gif")),
                KeyStroke.getKeyStroke(78, 2), 78);

        addLookAndFeelActionEntry(ACTION_SAVE, ACTION_SAVE, ACTION_SAVE, ACTION_SAVE,
                new ImageIcon(thisClass.getResource("/toolbarButtonGraphics/general/Save16.gif")),
                KeyStroke.getKeyStroke(83, 2), 83);

        addLookAndFeelActionEntry(ACTION_SAVE_AS, "Save As", "Save As", "Save As",
                new ImageIcon(thisClass.getResource("/toolbarButtonGraphics/general/SaveAs16.gif")), null, 65);

        addLookAndFeelActionEntry(ACTION_EXIT, ACTION_EXIT, ACTION_EXIT, ACTION_EXIT, null, null, 88);

        addLookAndFeelActionEntry(ACTION_PREFERENCES, ACTION_PREFERENCES, ACTION_PREFERENCES, ACTION_PREFERENCES,
                new ImageIcon(thisClass.getResource("/toolbarButtonGraphics/general/Preferences16.gif")),
                KeyStroke.getKeyStroke(82, 2), 82);

        addLookAndFeelActionEntry(ACTION_PRINT, ACTION_PRINT, ACTION_PRINT, ACTION_PRINT,
                new ImageIcon(thisClass.getResource("/toolbarButtonGraphics/general/Print16.gif")),
                KeyStroke.getKeyStroke(80, 2), 80);

        addLookAndFeelActionEntry(ACTION_COPY, ACTION_COPY, ACTION_COPY, ACTION_COPY,
                new ImageIcon(thisClass.getResource("/toolbarButtonGraphics/general/Copy16.gif")),
                KeyStroke.getKeyStroke(67, 2), 67);

        addLookAndFeelActionEntry(ACTION_DELETE, ACTION_DELETE, ACTION_DELETE, ACTION_DELETE,
                new ImageIcon(thisClass.getResource("/toolbarButtonGraphics/general/Delete16.gif")),
                KeyStroke.getKeyStroke(68, 2), 68);

        addLookAndFeelActionEntry(ACTION_ABOUT, ACTION_ABOUT, ACTION_ABOUT, ACTION_ABOUT, null, null, 65);

        addLookAndFeelActionEntry(ACTION_REFRESH, ACTION_REFRESH, ACTION_REFRESH, ACTION_REFRESH,
                new ImageIcon(thisClass.getResource("/toolbarButtonGraphics/general/Refresh16.gif")),
                KeyStroke.getKeyStroke('t'), 82);
    }

    public ActionLookAndFeel() {
        mLookAndFeel = sLookAndFeel;
    }

    public ActionLookAndFeel(Map<String, Map<String, Object>> lookAndFeel) {
        mLookAndFeel = lookAndFeel;
    }

    public static void addLookAndFeelActionEntry(String name, String cmd, String desc, String longDesc,
                                                 ImageIcon icon, KeyStroke acc, Integer mnemonic) {

        HashMap<String, Object> valuesMap = new HashMap<>();
        valuesMap.put("Name", name);
        valuesMap.put("ActionCommandKey", cmd);
        valuesMap.put("ShortDescription", desc);
        valuesMap.put("LongDescription", longDesc);
        valuesMap.put("SmallIcon", icon);
        valuesMap.put("AcceleratorKey", acc);
        valuesMap.put("MnemonicKey", mnemonic);
        sLookAndFeel.put(name, valuesMap);
    }

    public static boolean setValuesToLookAndFeel(String actionConstant, Action action) {
        return setValuesToLookAndFeel(sLookAndFeel, actionConstant, action);
    }

    public static boolean setValuesToLookAndFeel(Map<String, Map<String, Object>> lookAndFeel,
                                                 String actionConstant, Action action) {

        Map<String, Object> values;
        if (lookAndFeel == null) {
            lookAndFeel = sLookAndFeel;
        }

        if ((values = lookAndFeel.get(actionConstant)) == null) {
            return false;
        }

        values.forEach(action::putValue);
        return true;
    }

    public boolean setValuesToInstanceLookAndFeel(String actionConstant, Action action) {
        return setValuesToLookAndFeel(mLookAndFeel, actionConstant, action);
    }
}

