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

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.KeyStroke;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.bushe.swing.action.ActionAttributes;
import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;
import org.bushe.swing.action.Separator;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

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
    private ActionHandler handler;
    private Map actionSetMap;
    private String defaultActionClass;
    private boolean debug = false;
    private boolean printValidationErrors;
    private ActionManager actionManager;

    public ActionXMLReader(ActionManager manager) {
        this.actionManager = manager;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setPrintValidationErrors(boolean printValidationErrors) {
        this.printValidationErrors = printValidationErrors;
    }

    public void loadActions(File f) throws IOException, SAXException {
        if (this.debug) {
            System.out.println("loadActions(" + f.getAbsolutePath() + ")");
        }
        SAXParser parser = this.getParser();
        FileInputStream fis = new FileInputStream(f);
        InputSource is = new InputSource(fis);
        is.setSystemId(new File(f.getParent()).toURL().toExternalForm());
        this.loadActions(is, parser);
    }

    public void loadActions(URL url) throws IOException, SAXException {
        if (this.debug) {
            System.out.println("loadActions(" + url + ")");
        }
        if (url == null) {
            throw new IllegalArgumentException("URL is null.");
        }
        InputStream stream = url.openStream();
        try {
            this.loadActions(new InputSource(stream), this.getParser());
        }
        finally {
            stream.close();
        }
    }

    public void loadActions(InputStream stream) throws IOException, SAXException {
        SAXParser parser = this.getParser();
        this.loadActions(new InputSource(stream), parser);
    }

    public void loadActions(InputSource is, SAXParser parser) throws IOException {
        try {
            if (this.handler == null) {
                this.handler = new ActionHandler();
            }
            parser.parse(is, (DefaultHandler)this.handler);
        }
        catch (SAXException ex) {
            this.printException("SAXException: " + ex.getMessage(), ex);
            throw new IOException("Error parsing: " + ex.getMessage());
        }
        catch (IOException ex2) {
            this.printException("IOException: " + ex2.getMessage(), ex2);
            throw ex2;
        }
    }

    public SAXParser getParser() throws SAXException, IOException {
        if (parserfactory == null) {
            parserfactory = SAXParserFactory.newInstance();
            parserfactory.setValidating(true);
        }
        try {
            return parserfactory.newSAXParser();
        }
        catch (ParserConfigurationException ex3) {
            this.printException("ParserConfigurationException: " + ex3.getMessage(), ex3);
            throw new SAXException("Error parsing: " + ex3.getMessage());
        }
    }

    private void addAttributes(Attributes attrs, String actionset) {
        String id = attrs.getValue(ATTRIBUTE_ID);
        this.actionManager.registerActionPrototype(id, this.xmlAttrsToActionAttrs(attrs));
        this.addActionIdToActionSet(attrs.getValue(ATTRIBUTE_ID), actionset);
    }

    public ActionAttributes xmlAttrsToActionAttrs(Attributes attrs) {
        ActionAttributes actionAttrs = new ActionAttributes();
        this.updateAttributes(actionAttrs, attrs);
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
        if (mnemonic != null && !mnemonic.equals("")) {
            actionAttrs.putValue("MnemonicKey", new Integer(mnemonic.charAt(0)));
        }
        if ((accel = attrs.getValue(ATTRIBUTE_ACCEL)) != null && !accel.equals("")) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(accel.trim());
            actionAttrs.putValue("AcceleratorKey", keyStroke);
        }
        if ((value = attrs.getValue(ATTRIBUTE_SMICON)) != null) {
            actionAttrs.putValue("SmallIcon", this.actionManager.resolveIcon(value));
        }
        if ((value = attrs.getValue(ATTRIBUTE_LGICON)) != null) {
            actionAttrs.putValue("LARGE_ICON", this.actionManager.resolveIcon(value));
        }
        if ((value = attrs.getValue(ATTRIBUTE_TOOLBAR_SHOWS_TEXT)) != null) {
            actionAttrs.putValue("TOOLBAR_SHOWS_TEXT", new Boolean(value.toString()));
        }
        if ((value = attrs.getValue(ATTRIBUTE_MENU_SHOWS_ICON)) != null) {
            actionAttrs.putValue("MENU_SHOWS_ICON", new Boolean(value.toString()));
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
        if ((value = attrs.getValue(ATTRIBUTE_ACTION_CLASS)) == null && this.defaultActionClass != null) {
            value = this.defaultActionClass;
        }
        if (value != null) {
            actionAttrs.putValue("ACTION_CLASS", value);
        }
    }

    public void addRoles(String action, List roles) {
        ActionAttributes actionAttrs = this.actionManager.getPrototype(action);
        if (actionAttrs == null) {
            return;
        }
        actionAttrs.putValue("ROLES", roles);
    }

    private void addAttributes(String actionId, ActionAttributes inheritedAttrs, Attributes overridenAttrs, String actionset) {
        String id = overridenAttrs.getValue(ATTRIBUTE_ID);
        ActionAttributes attrs = new ActionAttributes(inheritedAttrs);
        attrs.putValue("ID", id);
        attrs.putValue("ActionCommandKey", id);
        this.updateAttributes(attrs, overridenAttrs);
        this.actionManager.registerActionPrototype(id, attrs);
        this.addActionIdToActionSet(id, actionset);
    }

    private void addActionIdToActionSet(String id, String actionset) {
        if (actionset != null && !actionset.equals("")) {
            List list = this.getActionSet(actionset);
            if (list == null) {
                list = new ArrayList<String>();
            }
            list.add(id);
            this.addActionSet(actionset, list);
        }
    }

    public Set getActionSetIDs() {
        if (this.actionSetMap == null) {
            this.actionSetMap = new HashMap();
        }
        return this.actionSetMap.keySet();
    }

    private List getActionSet(Object id) {
        if (this.actionSetMap == null) {
            this.actionSetMap = new HashMap();
        }
        return (List)this.actionSetMap.get(id);
    }

    private void addActionSet(String key, List set) {
        if (this.actionSetMap == null) {
            this.actionSetMap = new HashMap();
        }
        this.actionSetMap.put(key, set);
    }

    private void printException(String message, Exception ex) {
        System.out.println(message);
        if (this.debug) {
            ex.printStackTrace();
        }
    }

    static void printActionAttributes(PrintStream stream, ActionAttributes actionAttributes) {
        stream.println("Attributes for " + actionAttributes.getValue("ActionCommandKey") + actionAttributes);
    }

    public static Document parseXMLDocument(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        return factory.newDocumentBuilder().parse(file);
    }

    class ActionHandler
    extends DefaultHandler {
        private String element;
        private Stack actionListStack;
        private Stack actionSetStack;
        private String actionset;
        private ActionList actionlist;
        private String action;
        private String group;
        private List roles;

        ActionHandler() {
        }

        public void startDocument() {
            this.element = "";
            this.actionListStack = new Stack();
            this.actionSetStack = new Stack();
            this.actionset = null;
            this.actionlist = null;
            this.action = null;
            this.group = null;
            this.roles = null;
        }

        public void startElement(String nameSpace, String localName, String name, Attributes attributes) {
            if (ActionXMLReader.this.debug) {
                System.out.print("startElement(" + nameSpace + "," + localName + "," + name + ",...)");
                String id = attributes.getValue(ActionXMLReader.ATTRIBUTE_ID);
                System.out.println("id=" + id);
                if (id != null && id.equals("inherit-action")) {
                    System.out.println("in");
                }
            }
            this.element = name;
            if (ActionXMLReader.ELEMENT_ACTION_SET.equals(name)) {
                String newSet = attributes.getValue(ActionXMLReader.ATTRIBUTE_ID);
                if (this.actionset != null) {
                    this.actionSetStack.push(this.actionset);
                }
                this.actionset = newSet;
                String strWidth = attributes.getValue(ActionXMLReader.ATTRIBUTE_TOOLBAR_BUTTON_PREF_WIDTH);
                String strHeight = attributes.getValue(ActionXMLReader.ATTRIBUTE_TOOLBAR_BUTTON_PREF_HEIGHT);
                if (strWidth != null && strHeight != null) {
                    int width = Integer.parseInt(strWidth);
                    int height = Integer.parseInt(strHeight);
                    if (width > -1 && height > -1) {
                        ActionUIFactory.getInstance().setToolbarButtonPreferredSize(new Dimension(width, height));
                    }
                }
                ActionXMLReader.this.defaultActionClass = attributes.getValue(ActionXMLReader.ATTRIBUTE_DEFAULT_ACTION_CLASS);
            } else if (ActionXMLReader.ELEMENT_ACTION_LIST.equals(name)) {
                String listId = attributes.getValue(ActionXMLReader.ATTRIBUTE_ID);
                String actionListRefId = attributes.getValue(ActionXMLReader.ATTRIBUTE_IDREF);
                if (listId == null && actionListRefId == null) {
                    throw new IllegalArgumentException("Action lists require either an id or an idref.");
                }
                if (listId != null && actionListRefId != null) {
                    throw new UnsupportedOperationException("Error with action-list named:" + listId + " The idref attribute in action-list elements would refer to inherited lists, but they are not yet supported.  If the idref is meant to refering to the action that triggers the list (menu/toolbar), then use triggerActionRefId instead (this differs from the original JDNC XML).");
                }
                String triggerActionRefId = attributes.getValue(ActionXMLReader.ATTRIBUTE_ACTION_LIST_TRIGGER_ACTION_REF_ID);
                if (actionListRefId != null && triggerActionRefId != null) {
                    throw new IllegalArgumentException("Action lists cannot use triggerActionRefId and idref, i.e. a list cannot tell another action-list what it's trigger is.");
                }
                if (triggerActionRefId == null && listId != null) {
                    triggerActionRefId = listId;
                    ActionAttributes actionAtts = ActionXMLReader.this.actionManager.getPrototype(listId);
                    if (actionAtts == null) {
                        ActionXMLReader.this.addAttributes(attributes, this.actionset);
                    }
                }
                ActionList newOrReferredToList = null;
                if (actionListRefId == null) {
                    newOrReferredToList = new ActionList(listId, triggerActionRefId);
                    String weight = attributes.getValue(ActionXMLReader.ATTRIBUTE_WEIGHT);
                    if (weight != null) {
                        newOrReferredToList.setWeight(Double.valueOf(weight));
                    }
                    ActionXMLReader.this.actionManager.registerActionIdList(newOrReferredToList);
                    if (this.actionlist != null) {
                        this.actionlist.add(newOrReferredToList);
                    }
                } else {
                    newOrReferredToList = ActionXMLReader.this.actionManager.getActionIdList(actionListRefId);
                    if (newOrReferredToList == null) {
                        throw new IllegalArgumentException("Action-list for idref " + actionListRefId + " does not exist.  " + "Note: You may need to migrate your XML if " + "you are changing ActionManager implementations.  " + "Mark Davidson's original ActionManager uses the " + "action-lists idref to point to actions.  Use " + "triggerActionIdRef to point to actions.  Action-list " + "idrefs are used to merge action-lists.");
                    }
                }
                if (this.actionlist != null) {
                    this.actionListStack.push(this.actionlist);
                }
                this.actionlist = newOrReferredToList;
            } else if (ActionXMLReader.ELEMENT_ACTION.equals(name)) {
                this.action = attributes.getValue(ActionXMLReader.ATTRIBUTE_ID);
                String inheritedAction = attributes.getValue(ActionXMLReader.ATTRIBUTE_IDREF);
                this.validateAction(attributes, inheritedAction);
                if (this.action != null) {
                    this.addAttributesFromInheritedAction(attributes, inheritedAction);
                } else {
                    this.action = inheritedAction;
                }
                if (this.actionlist != null) {
                    ActionAttributes actionAttrs;
                    if (!this.actionlist.contains(this.action)) {
                        this.actionlist.add(this.action);
                    }
                    if (this.group != null && (actionAttrs = ActionXMLReader.this.actionManager.getPrototype(this.action)) != null) {
                        actionAttrs.putValue("GROUP", this.group);
                    }
                }
            } else if (ActionXMLReader.ELEMENT_NAME_VALUE_PAIR.equals(name)) {
                ActionAttributes actionAttrs = ActionXMLReader.this.actionManager.getPrototype(this.action);
                if (actionAttrs != null) {
                    actionAttrs.putValue(attributes.getValue(ActionXMLReader.ATTRIBUTE_NAME), attributes.getValue("value"));
                }
            } else if (ActionXMLReader.ELEMENT_ROLE.equals(name)) {
                if (this.roles == null) {
                    this.roles = new ArrayList(3);
                }
                this.roles.add(attributes.getValue(ActionXMLReader.ATTRIBUTE_NAME));
            } else if ("group".equals(name)) {
                this.group = attributes.getValue(ActionXMLReader.ATTRIBUTE_ID);
            } else if (ActionXMLReader.ELEMENT_SEPARATOR.equals(name) && this.actionlist != null) {
                Number weight = null;
                String weightStr = null;
                try {
                    weightStr = attributes.getValue(ActionXMLReader.ATTRIBUTE_WEIGHT);
                    if (weightStr != null) {
                        weight = Integer.valueOf(weightStr);
                    }
                }
                catch (NumberFormatException ex) {
                    try {
                        weight = Double.valueOf(weightStr);
                    }
                    catch (NumberFormatException newOrReferredToList) {
                        // empty catch block
                    }
                }
                boolean isLineVisible = true;
                if (attributes.getValue(ActionXMLReader.ATTRIBUTE_LINE_VISIBLE) != null) {
                    isLineVisible = Boolean.getBoolean(attributes.getValue(ActionXMLReader.ATTRIBUTE_LINE_VISIBLE));
                }
                Separator separator = new Separator(attributes.getValue(ActionXMLReader.ATTRIBUTE_ID), weight, isLineVisible);
                this.actionlist.add(separator);
            }
        }

        private void addAttributesFromInheritedAction(Attributes attributes, String inheritedAction) throws RuntimeException {
            if (inheritedAction == null) {
                ActionXMLReader.this.addAttributes(attributes, this.actionset);
            } else {
                ActionAttributes inheritedActionAtts = ActionXMLReader.this.actionManager.getPrototype(inheritedAction);
                if (inheritedActionAtts == null) {
                    throw new RuntimeException("Action " + this.action + " inherits from action " + inheritedAction + " but it is not yet defined.  Actions that inherit from others must be declared after the action they inherit from.");
                }
                ActionXMLReader.this.addAttributes(this.action, inheritedActionAtts, attributes, this.actionset);
            }
        }

        private void validateAction(Attributes attributes, String inheritedAction) throws RuntimeException {
            if (this.action == null && inheritedAction == null) {
                throw new RuntimeException("Actions must have an id or an idref defined. Element name:" + this.element + "Attributes:" + this.stringForAttributes(attributes));
            }
            int numOtherAtts = attributes.getLength();
            if (this.action != null) {
                --numOtherAtts;
            }
            if (inheritedAction != null) {
                --numOtherAtts;
            }
            if (numOtherAtts > 0 && this.action == null) {
                throw new RuntimeException("An action that defines an attribute must declare an id.  Inherited action:" + inheritedAction);
            }
        }

        private String stringForAttributes(Attributes attributes) {
            if (attributes == null) {
                return "null";
            }
            String result = "";
            int i = 0;
            while (i < attributes.getLength()) {
                result = String.valueOf(result) + attributes.getLocalName(i) + "=" + attributes.getValue(i) + " ";
                ++i;
            }
            return result;
        }

        public void endElement(String nameSpace, String localName, String name) {
            if (ActionXMLReader.this.debug) {
                System.out.println("endElement(" + nameSpace + "," + localName + "," + name + ")");
            }
            if (ActionXMLReader.ELEMENT_ACTION_SET.equals(name)) {
                try {
                    this.actionset = (String)this.actionSetStack.pop();
                }
                catch (EmptyStackException ex) {
                    this.actionset = null;
                }
            } else if (ActionXMLReader.ELEMENT_ACTION_LIST.equals(name)) {
                try {
                    if (this.roles != null) {
                        this.actionlist.setRoles(this.roles);
                    }
                    this.roles = null;
                    this.actionlist = (ActionList)this.actionListStack.pop();
                }
                catch (EmptyStackException ex) {
                    this.actionlist = null;
                }
            } else if (ActionXMLReader.ELEMENT_ACTION.equals(name)) {
                if (this.roles != null) {
                    ActionXMLReader.this.addRoles(this.action, this.roles);
                }
                this.roles = null;
            } else if ("group".equals(name)) {
                this.group = null;
            }
        }

        public void endDocument() {
            this.element = "";
            this.actionListStack = new Stack();
            this.actionSetStack = new Stack();
            this.actionset = null;
            this.actionlist = null;
            this.action = null;
            this.group = null;
        }

        public void error(SAXParseException ex) throws SAXException {
            if (ActionXMLReader.this.printValidationErrors) {
                System.out.println("**** validation error");
                this.reportException(ex);
            }
        }

        public void warning(SAXParseException ex) throws SAXException {
            if (ActionXMLReader.this.printValidationErrors) {
                System.out.println("**** validation warning");
                this.reportException(ex);
            }
        }

        public void fatalError(SAXParseException ex) throws SAXException {
            if (ActionXMLReader.this.printValidationErrors) {
                System.out.println("**** validation fatalError");
                this.reportException(ex);
            }
        }

        private void reportException(SAXParseException ex) {
            if (ActionXMLReader.this.printValidationErrors) {
                System.out.println(String.valueOf(ex.getLineNumber()) + ":" + ex.getColumnNumber() + " " + ex.getMessage());
                System.out.println("Public ID: " + ex.getPublicId() + "\t" + "System ID: " + ex.getSystemId());
            }
            if (ActionXMLReader.this.debug) {
                ex.printStackTrace();
            }
        }
    }
}

