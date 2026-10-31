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

import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class ActionXMLReader {
    public static final String ELEMENT_ACTION_SET = "action-set";
    public static final String ELEMENT_ACTION = "action";
    public static final String ELEMENT_ACTION_LIST = "action-list";
    public static final String ELEMENT_SEPARATOR = "separator";
    public static final String ELEMENT_GROUP = "group";
    public static final String ELEMENT_ROLE = "role";
    public static final String ELEMENT_NAME_VALUE_PAIR = "name-value-pair";
    public static final String ATTRIBUTE_DEFAULT_ACTION_CLASS = "defaultActionClass";
    public static final String ATTRIBUTE_TOOLBAR_BUTTON_PREF_WIDTH = "toolbarButtonPreferredWidth";
    public static final String ATTRIBUTE_TOOLBAR_BUTTON_PREF_HEIGHT = "toolbarButtonPreferredHeight";
    public static final String ATTRIBUTE_ACCEL = "accel";
    public static final String ATTRIBUTE_DESC = "desc";
    public static final String ATTRIBUTE_LONG_DESC = "longdesc";
    public static final String ATTRIBUTE_LGICON = "lgicon";
    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_COMMAND = "command";
    public static final String ATTRIBUTE_IDREF = "idref";
    public static final String ATTRIBUTE_MNEMONIC = "mnemonic";
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_ENABLED = "enabled";
    public static final String ATTRIBUTE_SELECTED = "selected";
    public static final String ATTRIBUTE_SMICON = "smicon";
    public static final String ATTRIBUTE_BUTTON_TYPE = "buttonType";
    public static final String ATTRIBUTE_TOOLBAR_SHOWS_TEXT = "toolbarShowsText";
    public static final String ATTRIBUTE_MENU_SHOWS_ICON = "menuShowsIcon";
    public static final String ATTRIBUTE_GROUP = "group";
    public static final String ATTRIBUTE_WEIGHT = "weight";
    public static final String ATTRIBUTE_ACTION_CLASS = "actionClass";
    public static final String ATTRIBUTE_ACTION_LIST_TRIGGER_ACTION_REF_ID = "triggerActionRefId";
    public static final String ATTRIBUTE_LINE_VISIBLE = "lineVisible";
    
    private static SAXParserFactory parserfactory;
    
    private final ActionManager actionManager;
    
    private ActionHandler handler;
    private Map<Object, Object> actionSetMap;
    private String defaultActionClass;
    private boolean debug = false;
    private boolean printValidationErrors;

    public ActionXMLReader(ActionManager manager) {
        actionManager = manager;
    }

    static void printActionAttributes(PrintStream stream, ActionAttributes actionAttributes) {
        stream.println("Attributes for " + actionAttributes.getValue("ActionCommandKey") + actionAttributes);
    }

    public static Document parseXMLDocument(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        return factory.newDocumentBuilder().parse(file);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setPrintValidationErrors(boolean printValidationErrors) {
        this.printValidationErrors = printValidationErrors;
    }

    public void loadActions(File f) throws IOException, SAXException {
        if (debug) {
            System.out.println("loadActions(" + f.getAbsolutePath() + ")");
        }

        SAXParser parser = getParser();
        FileInputStream fis = new FileInputStream(f);
        InputSource is = new InputSource(fis);
        is.setSystemId(new File(f.getParent()).toURI().toURL().toExternalForm());
        loadActions(is, parser);
    }

    public void loadActions(URL url) throws IOException, SAXException {
        if (debug) {
            System.out.println("loadActions(" + url + ")");
        }

        if (url == null) {
            throw new IllegalArgumentException("URL is null.");
        }

        try (InputStream stream = url.openStream()) {
            loadActions(new InputSource(stream), getParser());
        }
    }

    public void loadActions(InputStream stream) throws IOException, SAXException {
        SAXParser parser = getParser();
        loadActions(new InputSource(stream), parser);
    }

    public void loadActions(InputSource is, SAXParser parser) throws IOException {
        try {
            if (handler == null) {
                handler = new ActionHandler();
            }

            parser.parse(is, handler);
        } catch (SAXException ex) {
            printException("SAXException: " + ex.getMessage(), ex);
            throw new IOException("Error parsing: " + ex.getMessage());
        } catch (IOException ex2) {
            printException("IOException: " + ex2.getMessage(), ex2);
            throw ex2;
        }
    }

    public SAXParser getParser() throws SAXException {
        if (parserfactory == null) {
            parserfactory = SAXParserFactory.newInstance();
            parserfactory.setValidating(true);
        }

        try {
            return parserfactory.newSAXParser();
        } catch (ParserConfigurationException ex3) {
            printException("ParserConfigurationException: " + ex3.getMessage(), ex3);
            throw new SAXException("Error parsing: " + ex3.getMessage());
        }
    }

    private void addAttributes(Attributes attrs, String actionSet) {
        String id = attrs.getValue(ATTRIBUTE_ID);
        actionManager.registerActionPrototype(id, xmlAttrsToActionAttrs(attrs));
        addActionIdToActionSet(attrs.getValue(ATTRIBUTE_ID), actionSet);
    }

    public ActionAttributes xmlAttrsToActionAttrs(Attributes attrs) {
        ActionAttributes actionAttrs = new ActionAttributes();
        updateAttributes(actionAttrs, attrs);
        return actionAttrs;
    }

    public void updateAttributes(ActionAttributes actionAttrs, Attributes attrs) {
        String accel;
        String longDesc;
        String commandKey;

        String value = attrs.getValue(ATTRIBUTE_NAME);
        if (value != null) {
            actionAttrs.putValue("Name", value);
        }

        if ((value = attrs.getValue(ATTRIBUTE_ID)) != null) {
            actionAttrs.putValue("ID", value);
            actionAttrs.putValue("ActionCommandKey", value);
        }

        if ((commandKey = attrs.getValue(ATTRIBUTE_COMMAND)) != null) {
            actionAttrs.putValue("ActionCommandKey", commandKey);
        }

        if ((value = attrs.getValue(ATTRIBUTE_DESC)) != null) {
            actionAttrs.putValue("ShortDescription", value);
            actionAttrs.putValue("LongDescription", value);
        }

        if ((longDesc = attrs.getValue(ATTRIBUTE_LONG_DESC)) != null) {
            actionAttrs.putValue("LongDescription", longDesc);
        } else {
            value = attrs.getValue(ATTRIBUTE_DESC);
            if (value != null) {
                actionAttrs.putValue("LongDescription", value);
            }
        }

        String mnemonic = attrs.getValue(ATTRIBUTE_MNEMONIC);
        if (mnemonic != null && !mnemonic.isEmpty()) {
            actionAttrs.putValue("MnemonicKey", (int) mnemonic.charAt(0));
        }

        if ((accel = attrs.getValue(ATTRIBUTE_ACCEL)) != null && !accel.isEmpty()) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(accel.trim());
            actionAttrs.putValue("AcceleratorKey", keyStroke);
        }

        if ((value = attrs.getValue(ATTRIBUTE_SMICON)) != null) {
            actionAttrs.putValue("SmallIcon", actionManager.resolveIcon(value));
        }

        if ((value = attrs.getValue(ATTRIBUTE_LGICON)) != null) {
            actionAttrs.putValue("LARGE_ICON", actionManager.resolveIcon(value));
        }

        if ((value = attrs.getValue(ATTRIBUTE_TOOLBAR_SHOWS_TEXT)) != null) {
            actionAttrs.putValue("TOOLBAR_SHOWS_TEXT", Boolean.valueOf(value));
        }

        if ((value = attrs.getValue(ATTRIBUTE_MENU_SHOWS_ICON)) != null) {
            actionAttrs.putValue("MENU_SHOWS_ICON", Boolean.valueOf(value));
        }

        if ((value = attrs.getValue("group")) != null) {
            actionAttrs.putValue("GROUP", value);
        }

        if ((value = attrs.getValue(ATTRIBUTE_BUTTON_TYPE)) != null) {
            actionAttrs.putValue("BUTTON_TYPE", value);
        }

        if ((value = attrs.getValue(ATTRIBUTE_WEIGHT)) != null) {
            actionAttrs.putValue(ATTRIBUTE_WEIGHT, value);
        }

        if ((value = attrs.getValue(ATTRIBUTE_ACTION_CLASS)) == null && defaultActionClass != null) {
            value = defaultActionClass;
        }

        if (value != null) {
            actionAttrs.putValue("ACTION_CLASS", value);
        }
    }

    public void addRoles(String action, List roles) {
        ActionAttributes actionAttrs = actionManager.getPrototype(action);
        if (actionAttrs == null) return;
        actionAttrs.putValue("ROLES", roles);
    }

    private void addAttributes(String actionId, ActionAttributes inheritedAttrs,
                               Attributes overridenAttrs, String actionset) {

        String id = overridenAttrs.getValue(ATTRIBUTE_ID);
        ActionAttributes attrs = new ActionAttributes(inheritedAttrs);
        attrs.putValue("ID", id);
        attrs.putValue("ActionCommandKey", id);
        updateAttributes(attrs, overridenAttrs);
        actionManager.registerActionPrototype(id, attrs);
        addActionIdToActionSet(id, actionset);
    }

    private void addActionIdToActionSet(String id, String actionSet) {
        if (actionSet != null && !actionSet.isEmpty()) {
            List<String> list = Objects.requireNonNullElse(
                    getActionSet(actionSet), new ArrayList<>());
            list.add(id);
            addActionSet(actionSet, list);
        }
    }

    public Set<Object> getActionSetIDs() {
        if (actionSetMap == null) {
            actionSetMap = new HashMap<>();
        }

        return actionSetMap.keySet();
    }

    private List<String> getActionSet(Object id) {
        if (actionSetMap == null) {
            actionSetMap = new HashMap<>();
        }

        return (List<String>) actionSetMap.get(id);
    }

    private void addActionSet(String key, List<String> set) {
        if (actionSetMap == null) {
            actionSetMap = new HashMap<>();
        }

        actionSetMap.put(key, set);
    }

    private void printException(String message, Exception ex) {
        System.out.println(message);
        if (debug) ex.printStackTrace();
    }

    class ActionHandler extends DefaultHandler {
        private String element;
        private Stack<ActionList> actionListStack;
        private Stack<String> actionSetStack;
        private String actionSet;
        private ActionList actionlist;
        private String action;
        private String group;
        private List<Object> roles;

        ActionHandler() {
        }

        public void startDocument() {
            element = "";
            actionListStack = new Stack<>();
            actionSetStack = new Stack<>();
            actionSet = null;
            actionlist = null;
            action = null;
            group = null;
            roles = null;
        }

        public void startElement(String nameSpace, String localName, String name, Attributes attributes) {
            if (debug) {
                System.out.print("startElement(" + nameSpace + "," + localName + "," + name + ",...)");
                String id = attributes.getValue(ATTRIBUTE_ID);
                System.out.println("id=" + id);
                if (id != null && id.equals("inherit-action")) {
                    System.out.println("in");
                }
            }

            element = name;
            if (ELEMENT_ACTION_SET.equals(name)) {
                String newSet = attributes.getValue(ATTRIBUTE_ID);
                if (actionSet != null) {
                    actionSetStack.push(actionSet);
                }
                actionSet = newSet;

                String strWidth = attributes.getValue(ATTRIBUTE_TOOLBAR_BUTTON_PREF_WIDTH);
                String strHeight = attributes.getValue(ATTRIBUTE_TOOLBAR_BUTTON_PREF_HEIGHT);
                if (strWidth != null && strHeight != null) {
                    int width = Integer.parseInt(strWidth);
                    int height = Integer.parseInt(strHeight);
                    if (width > -1 && height > -1) {
                        ActionUIFactory.getInstance().setToolbarButtonPreferredSize(new Dimension(width, height));
                    }
                }

                defaultActionClass = attributes.getValue(ATTRIBUTE_DEFAULT_ACTION_CLASS);
            } else if (ELEMENT_ACTION_LIST.equals(name)) {
                String listId = attributes.getValue(ATTRIBUTE_ID);
                String actionListRefId = attributes.getValue(ATTRIBUTE_IDREF);

                if (listId == null && actionListRefId == null) {
                    throw new IllegalArgumentException("Action lists require either an id or an idref.");
                }

                if (listId != null && actionListRefId != null) {
                    throw new UnsupportedOperationException("Error with action-list named:" + listId +
                            " The idref attribute in action-list elements would refer to inherited lists, " +
                            "but they are not yet supported.  If the idref is meant to refering to the action " +
                            "that triggers the list (menu/toolbar), then use triggerActionRefId instead " +
                            "(this differs from the original JDNC XML).");
                }

                String triggerActionRefId = attributes.getValue(ATTRIBUTE_ACTION_LIST_TRIGGER_ACTION_REF_ID);
                if (actionListRefId != null && triggerActionRefId != null) {
                    throw new IllegalArgumentException("Action lists cannot use triggerActionRefId and idref, " +
                            "i.e. a list cannot tell another action-list what it's trigger is.");
                }

                if (triggerActionRefId == null && listId != null) {
                    triggerActionRefId = listId;
                    ActionAttributes actionAtts = actionManager.getPrototype(listId);
                    if (actionAtts == null) addAttributes(attributes, actionSet);
                }

                ActionList newOrReferredToList = null;
                if (actionListRefId == null) {
                    newOrReferredToList = new ActionList(listId, triggerActionRefId);
                    String weight = attributes.getValue(ATTRIBUTE_WEIGHT);
                    if (weight != null) {
                        newOrReferredToList.setWeight(Double.valueOf(weight));
                    }
                    actionManager.registerActionIdList(newOrReferredToList);
                    if (actionlist != null) {
                        actionlist.add(newOrReferredToList);
                    }
                } else {
                    newOrReferredToList = actionManager.getActionIdList(actionListRefId);
                    if (newOrReferredToList == null) {
                        throw new IllegalArgumentException("Action-list for idref " + actionListRefId + " does not exist.  " + "Note: You may need to migrate your XML if " + "you are changing ActionManager implementations.  " + "Mark Davidson's original ActionManager uses the " + "action-lists idref to point to actions.  Use " + "triggerActionIdRef to point to actions.  Action-list " + "idrefs are used to merge action-lists.");
                    }
                }

                if (actionlist != null) {
                    actionListStack.push(actionlist);
                }

                actionlist = newOrReferredToList;
            } else if (ELEMENT_ACTION.equals(name)) {
                action = attributes.getValue(ATTRIBUTE_ID);
                String inheritedAction = attributes.getValue(ATTRIBUTE_IDREF);
                validateAction(attributes, inheritedAction);

                if (action != null) {
                    addAttributesFromInheritedAction(attributes, inheritedAction);
                } else {
                    action = inheritedAction;
                }

                if (actionlist != null) {
                    ActionAttributes actionAttrs;
                    if (!actionlist.contains(action)) {
                        actionlist.add(action);
                    }
                    if (group != null && (actionAttrs = actionManager.getPrototype(action)) != null) {
                        actionAttrs.putValue("GROUP", group);
                    }
                }
            } else if (ELEMENT_NAME_VALUE_PAIR.equals(name)) {
                ActionAttributes actionAttrs = actionManager.getPrototype(action);
                if (actionAttrs != null) {
                    actionAttrs.putValue(attributes.getValue(ATTRIBUTE_NAME), attributes.getValue("value"));
                }
            } else if (ELEMENT_ROLE.equals(name)) {
                if (roles == null) {
                    roles = new ArrayList(3);
                }
                roles.add(attributes.getValue(ATTRIBUTE_NAME));
            } else if ("group".equals(name)) {
                group = attributes.getValue(ATTRIBUTE_ID);
            } else if (ELEMENT_SEPARATOR.equals(name) && actionlist != null) {
                Number weight = null;
                String weightStr = null;

                try {
                    weightStr = attributes.getValue(ATTRIBUTE_WEIGHT);
                    if (weightStr != null) {
                        weight = Integer.valueOf(weightStr);
                    }
                } catch (NumberFormatException ex) {
                    try {
                        weight = Double.valueOf(weightStr);
                    } catch (NumberFormatException ignored) {
                    }
                }

                boolean isLineVisible = true;
                if (attributes.getValue(ATTRIBUTE_LINE_VISIBLE) != null) {
                    isLineVisible = Boolean.getBoolean(attributes.getValue(ATTRIBUTE_LINE_VISIBLE));
                }

                Separator separator = new Separator(attributes.getValue(ATTRIBUTE_ID), weight, isLineVisible);
                actionlist.add(separator);
            }
        }

        private void addAttributesFromInheritedAction(Attributes attributes, String inheritedAction) throws RuntimeException {
            if (inheritedAction == null) {
                addAttributes(attributes, actionSet);
            } else {
                ActionAttributes inheritedActionAtts = actionManager.getPrototype(inheritedAction);
                if (inheritedActionAtts == null) {
                    throw new RuntimeException("Action " + action + " inherits from action " + inheritedAction +
                            " but it is not yet defined.  Actions that inherit from others must be declared " +
                            "after the action they inherit from.");
                }

                addAttributes(action, inheritedActionAtts, attributes, actionSet);
            }
        }

        private void validateAction(Attributes attributes, String inheritedAction) throws RuntimeException {
            if (action == null && inheritedAction == null) {
                throw new RuntimeException("Actions must have an id or an idref defined. Element name:" + element +
                        "Attributes:" + stringForAttributes(attributes));
            }

            int numOtherAtts = attributes.getLength();
            if (action != null) --numOtherAtts;
            if (inheritedAction != null) --numOtherAtts;
            if (numOtherAtts > 0 && action == null) {
                throw new RuntimeException("An action that defines an attribute must declare an id.  " +
                        "Inherited action:" + inheritedAction);
            }
        }

        private String stringForAttributes(Attributes attributes) {
            if (attributes == null) return "null";

            StringBuilder result = new StringBuilder();
            int i = 0;

            while (i < attributes.getLength()) {
                result.append(attributes.getLocalName(i))
                        .append("=")
                        .append(attributes.getValue(i))
                        .append(" ");
                ++i;
            }

            return result.toString();
        }

        @Override
        public void endElement(String nameSpace, String localName, String name) {
            if (debug) {
                System.out.println("endElement(" + nameSpace + "," + localName + "," + name + ")");
            }

            if (ELEMENT_ACTION_SET.equals(name)) {
                try {
                    actionSet = actionSetStack.pop();
                } catch (EmptyStackException ex) {
                    actionSet = null;
                }
            } else if (ELEMENT_ACTION_LIST.equals(name)) {
                try {
                    if (roles != null) {
                        actionlist.setRoles(roles);
                    }
                    roles = null;
                    actionlist = actionListStack.pop();
                } catch (EmptyStackException ex) {
                    actionlist = null;
                }
            } else if (ELEMENT_ACTION.equals(name)) {
                if (roles != null) {
                    addRoles(action, roles);
                }
                roles = null;
            } else if ("group".equals(name)) {
                group = null;
            }
        }

        @Override
        public void endDocument() {
            element = "";
            actionListStack = new Stack();
            actionSetStack = new Stack();
            actionSet = null;
            actionlist = null;
            action = null;
            group = null;
        }

        @Override
        public void error(SAXParseException ex) throws SAXException {
            if (printValidationErrors) {
                System.out.println("**** validation error");
                reportException(ex);
            }
        }

        @Override
        public void warning(SAXParseException ex) throws SAXException {
            if (printValidationErrors) {
                System.out.println("**** validation warning");
                reportException(ex);
            }
        }

        @Override
        public void fatalError(SAXParseException ex) throws SAXException {
            if (printValidationErrors) {
                System.out.println("**** validation fatalError");
                reportException(ex);
            }
        }

        private void reportException(SAXParseException ex) {
            if (printValidationErrors) {
                System.out.println(ex.getLineNumber() + ":" + ex.getColumnNumber() + " " + ex.getMessage());
                System.out.println("Public ID: " + ex.getPublicId() + "\t" + "System ID: " + ex.getSystemId());
            }
            if (debug) {
                ex.printStackTrace();
            }
        }
    }
}

