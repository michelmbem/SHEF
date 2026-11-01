# SAM - The Swing Action Manager

## Introduction

The Swing Action Manager (SAM) library gives more power to Swing Actions. SAM
 aids in the creation, usage and management of javax.swing.Actions in Swing application
 toolbars, menus and elsewhere.

### Action Background

Users of SAM are expected to be familiar with Swing Actions. See How to
 Use Actions in The Swing Tutorial for a nice introduction.

Actions were intended to be centralized command classes that can be shared
 amongst widgets (such as toolbar buttons and menu items), but they were not

### Main Features

SAM is flexible enough for use in complex applications, yet easy to use in
 simple applications. SAM mainly features:

- A centralized ActionManager that doles out predefined Actions and ActionLists,
    both of which can be globally shared (e.g., a main frame's toolbar and menu)
    or local (e.g, a table's popup menu).

- A customizable ActionUIFactory that creates buttons, toolbars and hierarchical
    menus from Actions and ActionLists.

- Action classes and interfaces that make Actions more widely usable, specifically
    adding support for context, enablement, synchronization and delegation.

- Some specialized features that break the limitations of simple Swing Actions,
    such as adding a listener to the action instead of the component(s) that use
    it, or keeping the currently selected state of a toolbar button in sync with
    a matching menu item sharing the same action. These are major limitations
    to Swing Action reusability.

- A flexible XML format for declaring actions and lists of actions, including
    the ability to merge multiple files together (great for plug-in architectures).

- Support for menu ordering based on weight, even in hierarchical menus. This
    feature is especially handy for plug-in architectures.

- Role-based security - action will not appear for users who do not act in
    at least one of the action's defined roles (all users see all actions without
    any defined roles).

- Allows actions to call back to an arbitray method, e.g. - a method in a
    controller.

- Exceptional documentation for an open source project, along with demos
    and a decent test suite.

- Coming: A visual tool to edit actions, menus, and toolbars and save the
    definitions as XML.

If you want to get going quickly, see Sam Usage.

### SAM and JDNC Actions

Mark Davidson's name is on a lot of the Swing classes' @author tags. I searched
 out Mark at JavaOne in 2003 to discuss the Action framework that had I created
 over the previous year. He said, "Did you see the
 article posted today on JavaDesktop.org"? (Except his words weren't
 hyperlinked :-&gt; ). As we discussed the capabilities of both frameworks,
 we realized we covered some of the same ground. After JavaOne I merged
 some of his code into SAM, stealing the XML parsing outright and making the
 APIs similar. Since then, his work has been incorporated into JDNC. SAM
 evolved separately, but I am now finally offering to contribute it back to
 JDNC, if they will have it. SAM has many features that do not exist in
 JDNC Actions, so SAM may be more suitable to your project. I intend to
 incorporate the concepts of JDNC Actions into SAM if it gets merged, of course.
 Let's look at the capabilities of SAM in detail, meanwhile comparing SAM
 features to JDNC Action features.

## SAM Features in Detail

Unless otherwise specified, all classes are located in the org.bushe.swing.action
 package.

### BasicAction

SAM provides a {@link org.bushe.swing.action.BasicAction} convenience class
 to use as a replacement for Swing's AbstractAction, which it extends (if only
 Swing APIs were based on interfaces...oh well).

SAM in no way requires the use of BasicAction, and assumes
 only javax.swing.Action in its signatures. Swing's AbstractAction is pretty
 much just a set of name-value pairs that fires property change events when the
 values change. SAM always treats Actions as string/value pairs, just like Swing.
 An action used with SAM will have it's PropertyChangeListener notified
 of any changes to properties that SAM defines. Specifically, all action properties,
 including those specific to BasicAction, are set via Action's putValue(String,
 Object) method, except for listeners and behavioral delegates (which are not
 "properties"). 

BasicAction is useful, but still "basic." It is recommended that
 you extend BasicAction for your own purposes instead of using it directly out
 of the box. At a minimum, override the handleException() to do something better
 than printing to System.err - like using whatever logging framework your application
 uses. You may want to add other facilities like adjusting the cursor state,
 using glass panes, handling threading or using SwingWorker-like facilities.

If you make your own base action class you can install it programmatically
 by setting the defaultActionClass of ActionAttributes, see ActionAttributes.setDefaultActionClass(Class).
 In an Action XML file, you can set the defaultActionClass property of the &lt;action-set&gt;
 element which sets the default class for actions in that XML file, unless an
 individual &lt;action&gt; element overrides it by setting its own class via
 its actionClass property of the &lt;action&gt; element.

Even though you are encouraged to create your own Action base class, BasicAction
 provides a lot of functionality right out of the box.

- BasicAction provides convenience methods for all known
    properties in javax.swing.AbstractAction such as getActionName(), and getAccelerator(),
    wrapping the getValue/putValue methods of AbstractAction, like so:
    ```java
    public KeyStroke getAccelerator() {
        return (KeyStroke)getValue(ACCELERATOR_KEY);
    }
    
    public void setAccelerator(KeyStroke accelerator) {
        putValue(ACCELERATOR_KEY, accelerator);
    }
    ```

- BasicAction implements actionPerformed() using a template
    method pattern. The actionPerformed method is final and simply calls actionPerformedTemplate().
    ```java
    public final void actionPerformed(ActionEvent evt) {
        actionPerformedTemplate(evt);
    }
    ```
    This method is final to ensure developers don't override actionPerformed()
    and bypass the goodies in BasicAction, such as calling delegate listeners.
    Instead, developers should override the execute(ActionEvent event) method
    that is called by the actionTemplate method. (If you would like to use a different
    template, you can override actionPerformedTemplate() in your application's
    base Action class.) BasicAction's actionTemplate() surrounds execute() with
    a few other calls. Let's look at it:
    ```java
    protected void actionPerformedTemplate(ActionEvent evt) {
        try {
            actionPerformedTry();
            execute(evt);
            propagateActionEvent(evt);
        } catch (Throwable t) {
            actionPerformedCatch(t);
        } finally {
            actionPerformedFinally();
        }
    }
    ```

    The template calls a number of overridable methods. The execute() method is
    the heart of BasicAction. The actionPerformedTry(), actionPerformedCatch()
    and actionPerformedFinally() methods wrap execute(ActionEvent).
    The action itself gets the first crack at handling the action through the
    execute(ActionEvent) method. Afterward, the event is propagated to "delegate"
    listeners. This allows multiple listeners for an action, and allows actions
    to be handled by objects that are not actions (such as a form's controller).
    The only wrapper of the execute() method that is implemented in BasicAction
    is actionPerformedCatch(), which prints the exception to System.err and throws
    a RuntimeException so that the exception is not eaten. (As an aside, a nice
    way to handle Exceptions in Swing is to install an AWTExceptionHandler (see
    {@link java.awt.EventDispatchThread#handleException(Throwable)}.The org.bushe.swing.AWTExceptionHandler
    is included in the open source BDesktop.jar that ships with the SAM demos
    (\demo\lib). It presents a dialog for all thrown exception that the user copy
    into an error report, or even send an email if JDIC is installed. See BE
    Products for more information.)

- BasicAction adds "delegate" action listeners
    to which ActionEvents are propagated by implementing the addActionListener()
    and removeActionListener() methods in the Actionable interface. Thus application
    controllers can listen to action events by adding listeners to the shared
    action, instead of having to add listeners to all the menus and buttons that
    share the action. Multiple delegate listeners are allowed, and if the action's
    execute() method is overridden, the action's work is performed in addition
    to the delegate(s). Another advantage of delegating listeners to the action
    is that the listener does not have navigate a Swing component hierarchy to
    find a menu or toolbar to which it adds a listener, nor wait until the menu or toolbar
    is created. A listener can get the action from the ActionManager (see below)
    and add a listener to the action at any time. It then will react to the click
     of any menu item or button that is created with that same action instance.

- BasicAction gives Actions a context by implementing the
    ContextAware interface. Typically when an action does its work, it does so
    against a specific object - it saves the active file, submits the
    current values in a form, shows a dialog for the currently selected
    table row, etc. These objects (the Document, Form Model, or the table
    Row Object, in these examples) is the context within which the action works.

    This is a form of the dependency injection pattern, and has
    many advantanges for Swing Actions. Using plain javax.swing.AbstractAction,
    an action listener would typically find a reference to the currently active
    document using a static class, or collect the values in a form that is set
    on it, or navigate a table member variable's selection model. This increases
    coupling, makes action code more complicated, decreases reuseablity and makes
    testing more difficult.
    Instead, by using BasicAction, an application controller will typically make
    calls such as setContext("file", file) when the file is opened,
    or setContext("FormModel", formModel) when a form is created, or
    setContext("data", table.getSelectedRowObject()) whenever the table
    selection changes or whenever a popup menu is shown. Convenience methods allow
    the context to be set on all the actions in an ActionList (see below), or
    all the actions in the ActionManager. When action's have a context, the execute()
    method can inspect the context and perform its work on the associated objects.
    Actions are isolated in their work. This simplifies their code, decouples
    them from other classes, increases resuability, and making them easily testable.
    In JUnit tests, you can test the action in isolation by setting context values
    without needing to hook the action up to action menus, forms or tables. You
    do write JUnit test for you client side code too, don't you? :-) In practice,
    it is far more difficult to test client code than server code, contextualizing
    actions takes steps in the right direction.

- BasicAction provides an API to handle action enablement.
    AbstractAction's setEnabled() method is probably the most useful feature of
    javax.swing.Actions. You can have a toolbar button and a menu item share an action, and
    both disable when setEnable(false) is called. The problem is that Swing's
    Action design doesn't help you actually enable or disable the action - an
    outside class (usually a controller) has to do so. BasicAction extends enablement
    in two directions - internally and externally to the action. Of course, setEnabled()
    can still be used, but BasicAction supports intelligent design:

 - BasicAction has an shouldBeEnabled() method that is
        used to ask the action whether or not it should be enabled given it's
        current knowlege of itself. Combined with the action context, this becomes
        pretty handy. Implementations of BasicAction will often use their context
        values to determine if they have the necessary valid information to do
        their work. Calling shouldBeEnabled() does not change the action's enabled
        state. BasicAction returns true by default for shouldBeEnabled() (if there
        are no CheckEnabledDelegates, see below) to make the framework easy to
        use. You can install your own base action class to override this behavior,
        see {@link org.bushe.swing.action.ActionAttributes}.

 - BasicAction has an updateEnabledState() method that
        is used to tell the action to compute and update its own enabled state.
        BasicAction's updateEnabledState() is simple:
    ```java
    public void updateEnabledState() {
        boolean shouldBe = shouldBeEnabled();
        setEnabled(shouldBe);
    }
    ```
    Calling updateEnabledState() calls setEnabled() with the results of shouldBeEnabled().
    The action is responsible for implementing shouldBeEnabled(). The action
    can solve this internally, typically by looking at its context. In fact,
    by default, when context values change updateEnabledState() is called
    automatically.
    BasicAction allows the enablement computation to be delegated externally
    by implementing the {@link org.bushe.swing.action.DelegatesEnabled} interface.
    Another class, typically an application controller, can implement the
    {@link org.bushe.swing.action.ShouldBeEnabledDelegate} interface, and
    be added to the action via the {@link org.bushe.swing.action.DelegatesEnabled#addShouldBeEnabledDelegate(ShouldBeEnabledDelegate)}
    method. When shouldBeEnabled() is called on a BasicAction (and it is not
    overridden), if any of the delegates return true
    for their shouldBeEnabled() method, the action is enabled.

- BasicAction supports ItemEvents by implementing the ItemAction
    interface. Unlike the above, this is not something the developer usually has
    to write code for (since menu items and toolbar buttons typically fire actionPerformed
    when they change state), but out-of-the-box BasicAction supplies a very important
    feature for SAM - Selected State Synchronization. This is
    only effective if you use the ActionUIFactory (or hook up an ActionSelectionSynchronizer
    to your own buttons). There is a problem with sharing javax.swing.Actions
    between two toggle buttons (radio buttons or checkboxes) . Once one is selected,
    the other is not selected, as it is below. Instead of using a centralized
    Action, the developer must resort to controlling the selected state of both
    buttons by hand - even though tied to the same action, which defeats the purpose
    of Actions for toggle buttons.

    ![LeftAlignMatching](github-images/LeftAlignMatching.gif)

SAM synchronizes toggle buttons that
    share the same action. Run "ant demo" to see it in action.

- Tiny nice-to-haves that BasicAction has:

 - BasicAction has an idForDebugging property to easily identify actions
        while debugging (see below). To figure out which Action you are looking
        at when debugging a javax.swing.AbstractAction, you have to dig through
        the array of properties, which is time-consuming.

 - BasicAction supports role-based security by providing a getRoles() and
        setRoles() convenience methods. Any Action can have roles defined in its
        "role" property. See the ActionManager section of this document for
        more information on role-based security.

 - BasicAction has an intelligent toString()

    ![IDForDebugging](github-images/IDForDebugging.gif)

 - BasicAction's createIcon() methods create icons from classpath URLs

 - MenuShowsIcon and ToolbarShowsText properties, to override default component
        visuals. These used to work, but don't seem to in the demos. todo!

#### JDNC comparison for BasicAction

I'm not too familiar with the growth of JDNC Actions from the initial version
 Mark Davidson wrote. I'm just looking at the Javadoc for now.

JDNC has an AbstractActionExt
 class. It has the convenience methods for the typical action properties. It has
 support for ItemEvents, including group and selected properties. It also has
 a dispose() method (what is this, C?).

JDNC has a hierarchy of actions extending AbstractActionExt - BoundAction,
 TargetableAction, CompositeAction; and one that does not: ServerAction (which
 does an HTTP post, not what I usually do with my servers). The hierarchy is
 generally a bad idea in a general purpose library. What if you want an action
 that is bound and composite or bound and targetable or targetable and server?
 You have pick one class to extend from and re-write the other one or two. Instead,
 SAM prefers interfaces so that you can use your own actions. BasicAction implements
 all the interfaces to create a single extension point. JDNC's ActionManager
 has getBoundAction(), getTargetableAction(), getServerAction, isBoundAction,
 isServerAction(), etc. This is bloat, in my opinion, and gets more bloated as
 new actions are used. In my applications, I have dozens of Action classes and
 I'll take the cast myself, instead of having the ActionManager do it for me..

JDNCs BoundAction extends AbstractActionEx supports the addActionListener and
 addItemListener delegations. It supports the isSelected() and getGroup() methods.
 BoundAction also supports a loosely bound model, enabling one to register a
 callback method name on any object when actionPerformed is called. I'm religiously
 against this practice, but SAM provides it anyway, though not in BasicAction.
 Instead, SAM's ActionManager can tie any object's method to any action that
 implements the Actionable interface (addActionListener()) (JDNC's ActionManager
 is similar).

What SAM's actions don't have but JDNC has :

1. TargetManager
    and Targetable
    Actions, which I'm not sure I really understand the role of. I think this
    is akin to the bushe.org's EventBus and it's EventAction.

2. CompositeAction
    - an action that contains a list of action ids.

3. ServerAction (a.k.a HTTP Post action, which has nothing to do with Swing
    and should be moved out of org.jdesktop.swing.actions to org.jdesktop.jdnc.actions).

What SAM's actions have but JDNC doesn't have :

1. Support for enablement.

2. Contextualization.

3. Interface-based structure

4. Support for Role-based security

5. Item button synchronization

6. Good documentation.

### ActionList

SAM's ActionList is a bit of a misnomer. An ActionList is a List of Actions,
 and implements the List interface, but it's more like an ActionTree or ActionGroup
 (a good name used by the GUI Commands project). ActionLists are hierarchical
 lists of Actions. An ActionList can contain Actions and other ActionLists. ActionLists
 represent the Actions that are used to create a toolbar or a hierarchical menu
 structure. ActionLists can represent JSeparators by containing null or an instance
 of a Separator in the list.

ActionLists have a few properties in addition to the actions they hold:

- An id to uniquely identify a list within the ActionManager.

- A triggerActionId that identifies the Action in the ActionManager
    that triggers the list. The is only applicable to menus. When someone clicks
    on the "File" menu, the menu's action gets an actionPerformed()
    called on it. This is a nice feature of Swing that is not often taken advantage
    of. The action that triggers the menu can be used to call updateEnabledState()
    on all the actions in the menu to only enable the appropriate menus before
    they are shown to the user. The same is true for popup menus. For example,
    the selected row can be set as the context of all the menus in the tables'
    popup menu before it is displayed, enabling actions depending on the data
    in the row.

- A weight, similar to an Action's weight, to position the
    ActionList relative to other Actions and ActionLists in a hierarchical menu.

- Roles that determine which users see which action lists
    (menus and toolbars). If roles are set and the none of the user's roles match are in
    the list's roles, then the menu is not shown.

ActionLists have a series of convenience methods:

- The addActionListenerToAll(ActionListener) method calls
    addActionListener() on all the Actionable Actions in the hierarchical list.
    This is handy if one controller handles an entire menu.

- The setContextForAll(Map) and putContextValueForAll(Object,
    Object) sets context on the hierarchical list.

- Call updateEnabledForAll() and setEnabledForAll()
    to change the enablement of all the actions in the list.

- The getActionById(id) method traverses the hierarchy for
    a specific action.

ActionLists can be defined in XML or in code and are typically returned by
 the ActionManager and used by the ActionUIFactory to make toolbars and menus.
 This is further explained below. Keep in mind that actions in an action-list
 can refer to actions defined elsewhere or they can be defined directly inside
 the list. Here is an example:

```xml
<action-list id="main-menu">
    <action-list id="file-menu" triggerActionRefId="file-menu-action">
        <action idref="new-action"/>
        <action id="open-action-for-menu" idref="open-action" mnemonic="P"/>
        <action idref="save-action"/>
        <separator/>
        <action idref="exit-action"/>
    </action-list>
    <action-list id="edit-menu" triggerActionRefId="edit-menu-action">
        <action idref="copy-action"/>
        <action idref="cut-action"/>
        <action idref="paste-action"/>
    </action-list>
    <action-list id="view-menu" triggerActionRefId="view-menu-action">
        <group id="align">
            <action idref="align-left-action"/>
            <action idref="align-center-action"/>
            <action idref="align-right-action"/>
        </group>
        <separator/>
        <action id="history-action" mnemonic="B" desc="Shows or hides the status bar"/>
        <role name="Manager"/>
    </action-list>
    <action-list id="help-menu" name="Help" mnemonic="H" desc="Help Operations">
        <action idref="about-action"/>
    </action-list>
</action-list>
```

#### JDNC comparison for ActionList

The is no corollary for ActionList in JDNC, though action-list is used as an
XML element.

### Action Manager

The ActionManager is a repository for Actions and ActionLists. There is one
 static ActionManager created by default. You can create other named instances
 to separate groups of actions if your design sees fit. This usually is not necessary
 for simple applications, and even complex applications can make use of the abillity
 to merge XML files instead of using multiple ActionManagers, however, if multiple
 action XML files are coming from various sources, loading each set of actions
 into their own named Action Manager may make management easier.

#### Registering Actions

The ActionManager can be loaded with Actions and ActionLists programmatically
 or via an XML file. Usually you will just register an entire Action XML document
 using one of {@link org.bushe.swing.action.ActionManager#register(File)}, {@link
 org.bushe.swing.action.ActionManager#register(URL)},
or {@link org.bushe.swing.action.ActionManager#register(InputStream)}.
 Actions are not instantiated when an Action XML file is read (todo: add flags
 to the XML so that they can be instantiated right away if desired). Loading an Action XML document
   instead loads ActionAttributes prototypes and Action Id Lists (see below).

Actions can be registered programmatically by loading an Action or an ActionAttributes
 prototype, using {@link org.bushe.swing.action.ActionManager#registerAction(Object, Action)} or
   {@link org.bushe.swing.action.ActionManager#registerActionPrototype(Object, ActionAttributes)},
   respectively. Since Actions are simply sets of name/value
 pairs, then only name/value pairs are needed to define them. ActionAttributes
 is just a name-value pair class. Loading ActionAttributes defers the step of
 instantiating Actions and their Action classes until they are needed. The Actions
 that correspond to the ActionAttributes are registered the first time they are
 requested via {@link org.bushe.swing.action.ActionManager#getAction(Object)}
 or {@link org.bushe.swing.action.ActionManager#createAction(Object)}.

This is an ugly detail you usually don't need to worry about, however, this
 is the hook to defining your own default Action class to use instead of using
 BasicAction - call {@link ActionAttributes#setDefaultActionClass(Class)}. In
 an Actions XML file, set the defaultActionClass attribute of an &lt;action-set&gt;
 element to apply a class to all the actions in the set.

Likewise, you can register an ActionList with the ActionManager
via {@link org.bushe.swing.action.ActionManager#registerActionList(ActionList)}
(though this is rare in practice), or an ActionList prototype called an Action
 Id List using {@link org.bushe.swing.action.ActionManager#registerActionIdList(ActionList
 )}. Loading an Action XML file loads an Action Id List for each &lt;action-list&gt;
 element (see below). The ActionList passed into registerActionIdList does not
 contain Actions like most ActionLists, instead it contains ids to actions or
 action prototypes (or even action list prototypes for submenus) already registered.
 (Reuse of the ActionList for this purpose may give the class too many responsibilities,
 but it works.) Again, this is another detail you usually don't have to worry
 about, but the next section is related, and important.

#### Action Instance Management (Global Actions vs. Local Actions)

When an Action or an ActionList is registered programmatically with the ActionManager,
 they exist globally. Calling ActionManager.getAction(Object id), retrieves the
 same instance each time, likewise for ActionManager.getActionList(Object id).
 However, when prototypes of Actions and ActionList are registered, such as when
 an XML file is registered, then they are not immediately instantiated and do
 not exist globally. These action prototypes can be used to create Actions that
 are global or local. Global Actions and global
 ActionLists can be created and retrieved via {@link org.bushe.swing.action.ActionManager#getAction(Object
 id)} and {@link org.bushe.swing.action.ActionManager#getActionList(Object
 id)}. When calling these methods, if the Action or ActionList does not exist,
 it is created from its prototype and registered globally. Thereafter, calling
 getAction(Object) retrieves the single global instance.

You can instead create local instances of Actions and ActionLists
 as well via the {@link org.bushe.swing.action.ActionManager#createAction(Object)}
 and {@link org.bushe.swing.action.ActionManager#createActionList(Object)}
 methods of ActionManager. Actions and ActionLists created with these methods
 are not held by the ActionManager and a new instance if created each time.

It is important to understand the difference between global and local actions
 and the usage of ActionManager's getAction() vs. createAction(). In simple applications,
 there is typically one globally shared Action instance per Action id and one
 global ActionList per list id that points to the global action. For example:

![GlobalActions](github-images/GlobalActions.gif)

The following code produces this result, assuming XML loads up the ActionManager
with an ActionList and the three actions:

```java
ActionManager.register(new File("./actions.xml"));
ActionList globalList = ActionManager.getActionList("main-list");
JToolbar mainToolbar = ActionUIFactory.createToolbar(globalList);
JPopupMenu mainMenu = ActionUIFactory.createPopupMenu(globalList);
JButton miscButton = ActionUIFactory.createButton("right-align-action");
```

This is the typical, small application usage. All actions are globally shared.
 A JToolbar and JPopupMenu menu each are created with the same global action
 list reference (we're peaking ahead at the ActionUIFactory here, see below).
 The global list points to global actions that are created and maintained in
 the Action Manager. A JButton is also created with one of the global actions.
 The list is acquired via getActionList() and the global action
 for the button is retrieved by getAction() (used by default
 in the ActionUIFactory). All JComponents created with the Actions share the same
 global Action instance.

This is an easy way to work, and is the default behavior of ActionManager.
 When the selected state is changed (if ItemActions), the button, toolbar and
 popup menu are all updated. When the context is set on teh global action, it
 applies to all JComponents. This is the way Actions are supposed to work! Nice
 and easy. The screenshot of the demo above is an application
 that uses this scheme.

There are use cases, however, when a more complex motif is necessary. Let's
 say your application has a toolbar and a popup menu, but they are not identical.
 The popup menu has an additional "history" menu item. You still want
 to share the same global Actions, so that your components are kept in sync,
 but you can't use the same list as above, since it doesn't have a History action.
 What you need to do is create local ActionLists referencing global Actions.

![GlobalActionsLocalActionLists](github-images/GlobalActionsLocalActionLists.gif)

There are now two local action lists created with ActionManager.createActionList().
 (Yes this effect could have been done with two global list, but that wouldn't
 illustrate the point as well). To illustrate the "localness" of the
 lists, this could be done like so:

```java
ActionManager.register(new File("./actions.xml"));
ActionList localToolbarList = ActionManager.createActionList("main-toolbar-list", true);
ActionList localPopupList = ActionManager.createActionList("main-toolbar-list", true);
popupList.add(ActionManager.getAction("history-action"));
JToolbar toolbar = ActionUIFactory.createToolbar(localToolbarList);
JPopupMenu menu = ActionUIFactory.createPopupMenu(localPopupList);
JButton miscButton = ActionUIFactory.createButton("right-align-action");
JButton historyButton = ActionUIFactory.createButton("history-action");
```

The "true" passed to createActionList() tells the ActionManager to
 create a local List, but have the actions in the local list be references to
 the globally shared actions (created on the fly if necessary). The localPopupList
 is modified to add the global history action to it, so the popup can look different
 from the toolbar. The history button still shares the same global action as
 the popup menu item.

There's a third motif common to complex applications such as network management
 consoles, intensive reporting applications, trading workstations, etc. In these
 immersive applications, users often look at the same set of data in many different
 ways or work on similar but different sets of data in the same way. These applications
 often have JInternalPanes, JSplitPanes, or Docking Frameworks (such as JIDE
 or FlexDock) that the user can manipulate
 to see data side by side. In these cases, there may be two or more "internal"
 windows with the same kind of view, each with its own toolbar that applies to
 the data in that view. A more common example is multi-file editor that may have
 somewhat different popup menus depending on the file type (XML, Java, etc.).
 These situations call for actions local to the instance of the view. The context
 and enablement of the actions will depend on the data in each view - therefore
 the actions should not be shared. Here is an example where there are two document
 windows. The document on the top is right-aligned, the document on the bottom
 is left aligned.

![LocalActions](github-images/LocalActions.gif)

Here is the code to produce this result:

```java
ActionManager.register(new File("./actions.xml"));
View topView = new View("right");
View bottomView = new View("left");
//...
public View(String alignment) {
   ActionList localToolbarList = ActionManager.createActionList("main-toolbar-list", false);//false = do not use global actions for this list
   //This may be the right, center or left alignment depending on the alignment property of this view
   BasicAction alignedAction = (BasicAction)localToolbarList.getActionById(alignment+"-align-action");
   alignAction.setSelected(true);
   JToolbar toolbar = ActionUIFactory.createToolbar(localToolbarList);
   add(BorderLayout.NORTH, toolbar);
   alignAction.setContext("document", doc);//setup the action with this view's data
}
```

SAM's ActionEditor uses the local action motif for each internal frame. Below
is a screenshot, to run it live, "ant editor". Each internal frame
has its own save and preview buttons, each applies to the document it's editing:

![ActionEditor](github-images/ActionEditor.gif)

Other ActionManager Features

- Dynamic callbacks: I must say I absolutely loathe this
    feature. Some people love it, and JDNC has it, so here it is. You can register
    a method of any any object instance to be called back when an action is fired
    via ActionManager.registerActionCallback(Object actionId, Object callback,
    String method); You can also register a static method of a class if you don't
    have an instance to create the ultimate dynamic monolith. It's bad enough
    you have to duplicate your XML string Action Ids when using Action XML, don't
    do this to yourself.

- Enablement and disablement of all actions registered with
    the ActionManager is supported, including having all call their updateEnabledState()
    method (see BasicAction above).

- Contextualizing all registered actions makes it easy to
    populate all actions with a chunk of data they all may need - say a URL they
    can use.

- Accelerator keystokes can be added to the context map
  of any JComponent through the mapKeystrokeForAction() convenience methods.

#### JDNC comparison for ActionManager

JDNC's ActionManager is much simpler than SAM's ActionManager. It has only
 getAction() methods to return global instances, and no local action or action
 list support. JDNC's ActionManager also has some questionable methods that return
 specific action types such as getTargetableAction(). SAM prefers casting, even
 for BasicAction, since nearly all actions end up being cast to their specific
 type in actual use.

### Action UI Factory

The final piece of the puzzle, from an API standpoint, is the ActionUIFactory.
 Suffice it to say that you can create toolbars, menus, popupmenus, and buttons
 from Actions and ActionLists. It is extensible in the sense that you are not
 limited to creating simple JButtons. If you want to create FooButtons or JXButtons,
 or JGoodies Buttons, then you can override the instantiateButton method in your
 own factory extension. Other methods can be overridden to provide the utmost
 extensibility.

Speaking of buttons, ActionUIFactory will create the right kind of button depending
 on the Action's "buttonType" property - toggle, checkbox, radio, or
 plain. The ActionUIFactory respects groups - it will create toggle buttons or
 menu items and create their groups. A feature unique to SAM is the synchronization
 of toggle button Item State created from the same action without leaking memory
 (it uses bidriectional WeakReferences). An example of this is shown
 above.

You can create multiple named ActionUIFactories, just like you can have multiple
 named ActionManagers. You might want multiple named ActionUIFactories if you
 create different types of widgets. An ActionUIFactory refers to the default
 ActionManager by default, but you can set a specific ActionManager on a factory.

#### JDNC comparison for ActionUIFactory

JDNC's UIFactory is similar than SAM's ActionUIFactory, but does not allow
 the extension points, and therefore is quite limited. It doens't support synchronization
 of selection states out of the box, and what support it does have is begging
 for a memory leak.

## Action XML Files

Why write Actions in XML when you can just code them? XML should be used judiciously.
 There are the drawbacks of a lack of compile time type and name safety - there
 is nothing to ensure your code is refering to the action using the same string
 your XML is using or that the class name in the XML actually exists.

The advantages to using XML for Actions are (1) the XML is truly easier to
 read and edit than Action or JPopupMenu code, (2) it forces centralization of
 a concern - it's much easier to find a menu in an XML file by name than to determine
 in a pile of Swing code which class defined it, and (3) menu merging for plugin
 architectures is made possible through Action XML.

SAM has a Action XML editor to make it easier to edit action files. Currently
 this is just demo quality. Don't worry, editing Action XML is pretty simple.

The details for each attribute is included in the res\action-set.dtd file documentation.
 This document will outline only high-level issues. The action-set.xsd schema
 is autogenerated from the DTD, not documented, and not used in the project.
 It is provided merely as a convenience and may not even reflect the most accurate
 schema for Actions.

Each action XML file has a root &lt;action-set&gt; element. The id of the action-set
 is not used by SAM at this time, nor is the action-set itself. The only other
 property of the action set is is the defaultActionClass. If you create a BasicAction
 extension (or even an AbstractAction extension) for all your actions, specify
 it here. The action-set can contain &lt;action&gt; and &lt;action-list&gt; elements.
 The &lt;action-list&gt; element can contain &lt;actions&gt; or other &lt;action-list&gt;
 elements.

### Writing Actions

Actions are defined with the &lt;action&gt; element that has attributes matching
 all the property names that AbstractAction and BasicAction define. The attributes
 are fully explained in the DTD. Here is an example from the ActionEditor (with
 the "weight" attribute and the "role" element added).

```xml
 <action id="new-action"
         name="New Action XML File"
         mnemonic="N"
         smicon="/toolbarButtonGraphics/general/New16.gif"
         lgicon="/toolbarButtonGraphics/general/New24.gif"
         accel="control N"
         desc="Create a new Action XML File"
         weight="5"
         actionClass="org.bushe.swing.action.editor.NewInternalFrameAction">
    <name-value-pair name="newOrOpen" value="new"/>
    <role name="Editor"/>
    <role name="Admin"/>
</action>
```

It is fairly self-explanatory. Keep in mind that the lgicon is not used by
 SAM (though you might find a need for it in your application), nor is the longdesc
 (not shown). The desc is used as the toolip. The weight determines where it
 will be placed in a menu. Weight is not necessary and usually only helpful when
 merging files, see "Merging" below.

The name-value-pair element allows you to add arbitrary values to the Action.
 The above element is equivalent to:

```java
action.putValue("newOrOpen", "new");
```

The &lt;role&gt; elements act as an authentication mechanism. The way this
 works is that once the user logs in, you can find out the role names that they
 act in (this is determined by your application's authentication mechanism. Once
 you have the role names, set them on the ActionManager. As your application
 tries to create toolbars and menus, if an Action's role is specified and at
 least one of those roles is not set on the ActionManager, then that action will
 be skipped. For example, if I logged in as Admin, and my toolbar had 3 buttons,
 one with the above roles and the other two with no roles, then when I log in
 as guest, then there would only be two buttons on the same toolbar.

#### Action Inheritance

You can define a parent &lt;action&gt; that other actions can extend.
 This is done through the XML idref property. For example, the action editor
 has two actions that use the same NewInternalFrameAction class. One uses the
 newOrOpen value of "new" the other uses a value of "open."
 One action class handles both responsibilities, since they are very similar.
 The &lt;action&gt; elements could have used a common ancestor, like so:

```xml
 <action id="internalFrameAbstractAction"
          weight="5"
         actionClass="org.bushe.swing.action.editor.NewInternalFrameAction">
    <role name="Editor"/>
    <role name="Admin"/>
</action>
 <action id="new-action"
         idref="internalFrameAbstractAction"
         name="New Action XML File"
         mnemonic="N"
         smicon="/toolbarButtonGraphics/general/New16.gif"
         lgicon="/toolbarButtonGraphics/general/New24.gif"
         accel="control N"
         desc="Create a new Action XML File">
    <name-value-pair name="newOrOpen" value="new"/>
</action>
<action id="open-action"
        idref="internalFrameAbstractAction"
        name="Open Action XML File"
        mnemonic="O"
        smicon="/toolbarButtonGraphics/general/Open16.gif"
        lgicon="/toolbarButtonGraphics/general/Open24.gif"
        accel="control O"
        desc="Open Action XML File"
        weight="10">
    <name-value-pair name="newOrOpen" value="new"/>
</action>
```

The new-action and the open-action inherit actionClass and weight attributes
 and the roles from their common parent internalFrameAbstractAction. Similar
 to Java classes, actions support overrides. The open-action overrides the inherited
 weight attribute with its own.

An action cannot inherit from an action that is defined below it in the XML
 file. Put base actions near the top of the Action XML.

#### Writing Action Lists

ActionLists are used to create menu items and toolbars. They are hierarchical
 and can contain &lt;action&gt; and other &lt;action-list&gt; elements. They
 can also hold &lt;role&gt; elements to specify which users have access to them
 (see Action roles for a description), and &lt;separator&gt; elements for the
 creation of JSeparators. Let's look at an example. Notice the &lt;role&gt;,
 &lt;separator&gt;, and hierarchical &lt;action-list&gt; elements.

```xml
 <action-list id="main-menu">
    <action-list id="file-menu" triggerActionRefId="file-menu-action">
        <action idref="new-action"/>
        <action id="open-action-for-menu" idref="open-action" mnemonic="P"/>
        <action idref="save-action"/>
        <separator/>
        <action idref="exit-action"/>
    </action-list>
    <action-list id="edit-menu" triggerActionRefId="edit-menu-action">
        <action idref="copy-action"/>
        <action id="cut-action"
                name="Cut"
                mnemonic="T"
                smicon="/toolbarButtonGraphics/general/Cut16.gif"
                lgicon="/toolbarButtonGraphics/general/Cut24.gif"
                accel="control X"
                desc="Remove selected item"/>
        <action idref="paste-action"/>
    </action-list>
    <action-list id="view-menu" triggerActionRefId="view-menu-action">
        <group id="align">
            <action idref="align-left-action"/>
            <action idref="align-center-action"/>
            <action idref="align-right-action"/>
        </group>
        <separator/>
        <!-- The following command demonstrates attribute prototype usage.
        The history-action-menu-overridde has the properties of a
        history-action command with the mnemonic and description overriden.-->
        <action id="history-action-menu-overridde"
                idref="history-action"
                mnemonic="B"
                desc="Shows or hides the status bar"/>
        <role name="Manager"/>
    </action-list>
    <!-- The following help-menu action-list demonstrated an inline definition
    of an action. This may be more convenient then defining the action separately -->
    <action-list id="help-menu" name="Help" mnemonic="H" desc="Help Operations">
        <action idref="about-action"/>
    </action-list>
</action-list>
```

The first thing to notice is that most of the &lt;action&gt; elements refer
 to previously defined actions ("save-action" for example), though
 some do not. The "cut-action" is an example of an "inlined"
 action. The "cut-action" is defined inside the &lt;action-list&gt;
 and is equivalent in every way to an action defined outside of an &lt;action-list&gt;.
 So if you don't like &lt;action&gt; elements, you don't ever have to use them.

Every &lt;action-list&gt; creates an associated Action with it either implicitly
 or explicitly. The ActionList's Action is the action that you listen to when
 the menu is shown - i.e., the action of the JMenu or JPopupMenu. The ActionList
 action can be defined by an &lt;action&gt; element above the &lt;action-list&gt;
 element and referred to by the triggerActionRefId attribute of the &lt;action-list&gt;.
 Otherwise the Action is created implicity. Implicit actions for an ActionList
 can be returned from the ActionManager.getAction(Object id) passing the id of
 the &lt;action-list&gt;. An implicit or explicit action can be augmented in the
 &lt;action-list&gt; element - the &lt;action-list&gt; shares all the attributes
 of an &lt;action&gt; such as mnemonic, desc, etc. The help-menu above is an
 example of an augmented implicit ActionList action. You don't have to worry
 about these implicit actions being named the same as an &lt;action&gt; you define.
 XML id's ensure that only one id for any element can be merged. This can be
 a problem during merges, but a runtime exception will be thrown as the "repeat"
 offender is loaded in the ActionManager.

Lastly, an &lt;action-list&gt; can contain a &lt;group&gt; element that surrounds
 &lt;action&gt; elements. When a group element is defined, a radio button or
 a radio button menu item are created for the toolbar or menu, respectively.
 These radio buttons are added to the ButtonGroup whose id is the id of the &lt;group&gt;
 element.

### Merging XML Files

Simple applications will benefit some from using SAM, complex applications
 benefit much more. Large Swing teams can suffer the same fate as large Struts
 team have suffered - managing the merging of multiple revisions of one big honking
 XML file that is critical to your application. Navigating such files are often
 difficult as well. SAM allows you to break up your action definitions into multiple
 XML files and merge them at runtime. Merging files is pretty simple and works
 on the idref mechanism described above.

Multiple files can be loaded into the ActionManager and the ActionManager will
 simply add the actions and action lists for all files. When a second file is
 loaded it can refer to any &lt;action&gt; or &lt;action-list&gt; refid attributes
 loaded in a previous file, so yes, the order in which files are loaded matters.
 Loading &lt;actions&gt; are simply additive, there's no merging involved, except
 you should know that an action in the second loaded file can inherit from actions
 in the first loaded file.

When creating an &lt;action-list&gt; the element can refer to any &lt;action&gt;
 or &lt;action-list&gt; refid's defined in previously loaded files. An &lt;action-list&gt;
 can be filled in by multiple files. Each file can add it's &lt;action&gt; and
 &lt;action-list&gt; elements to an action list. The elements are added in the
 order they are processed, unless they define "weight" attributes,
 in which case they are ordered according to their weights. Hint: weights can
 be real numbers (and negative) in case you get stuck with integers that are
 too close. If two weights are the same, the element is added in the order it
 was processed (last element on the bottom).

Merging can be done at any &lt;action-list&gt; level by specifying the hierarchy.
 It's about time we looked at another example. In this example there are two
 top-level &lt;action-list&gt; elements foobarbaz and listWithSubList. The listWithSubList
 ActionList has a sub list called subBazBarFoo. Action elements &lt;foo&gt;,
 &lt;bar&gt;, &lt;baz&gt;, &lt;subfoo&gt;, &lt;subbar&gt;, and &lt;subbaz&gt;
 are defined in files Foo.xml, Bar.xml and Baz.xml. The result is one with menu
 with:

     foo
     bar
     baz

and another with:

     subBazBarFoo
     ---baz
     ---bar
     ---foo

File _Foo.xml_:

```xml
<action-set id="actions-mulitple-file-merge-test">
    <action id="foo" name="foo" weight="1"/>
    <action id="subfoo" name="foo" weight="3"/>
    <action-list id="foobarbaz">
         <action idref="foo"/>
    </action-list>
    <action-list id="listWithSubList">
        <action-list id="subBazBarFoo">
            <action idref="subfoo"/>
        </action-list>
    </action-list>
</action-set>
```

File _Bar.xml_:

```xml
<action-set id="actions-mulitple-file-merge-test">
    <action id="bar" name="bar" weight="2"/>
    <action id="subbar" name="bar" weight="2"/>
    <action-list idref="foobarbaz">
        <action idref="bar"/>
    </action-list>
    <action-list idref="listWithSubList">
        <action-list idref="subBazBarFoo">
            <action idref="subbar"/>
        </action-list>
    </action-list>
</action-set>
```

File _Baz.xml_:

```xml
<action-set id="actions-mulitple-file-merge-test">
    <action id="baz" name="baz" weight="3"/> <action id="subbaz" name="baz" weight="1"/>
    <action-list idref="foobarbaz">
        <action idref="baz"/>
    </action-list>
    <action-list idref="listWithSubList">
        <action-list idref="subBazBarFoo">
            <action idref="subbaz"/>
        </action-list>
    </action-list>
</action-set>
```

Notice that in order to build the sub menu, you have to point to it by pointing
 to the top level and then the sub level menus.

#### JDNC comparison for Action XML

The original XML code was borrowed (or "stolen" as Woody
 Guthrie would proudly say) from Mark Davidson's original ActionManager post,
 so it is similar to the JDNC format. If you want to convert your JDNC XML to
 SAM, you likely will only have to change your &lt;action-list&gt; "refid"
 attributes to a "triggerActionRefId" attribute. I took this liberty
 since it doesn't make sense to refid an &lt;action&gt; from an &lt;action-list&gt;,
 and I want to save refId in &lt;action-list&gt; to handle inheritance for action-lists,
 though this is not supported (idref in an&lt;action-list&gt; throws and exception
 and with text about converting from other XML formats). This may not be the
 only difference and I haven't tried converting JDNC XML.

JDNC does not have security-based roles, weights or merging. I'm not sure if
 inheritance is supported or not.

## SAM Usage

### Simple Usage

####

The simplest usage of the SAM consists of:

1. Creating an Action XML file or coding Actions and ActionLists by hand.
2. Loading the Actions into the ActionManager.
3. Getting Action from the ActionManager and listen to them.
4. Creating Toolbars and Menubars from Action Lists.

### 1. Create the Actions XML

Here is an example XML file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE action-set SYSTEM "dtd/action-set.dtd">
<action-set id="actions-usage">
    <action-list id="main-menu">
    <action id="New" name="New" mnemonic="N" accel="control N" smicon="/toolbarButtonGraphics/general/New16.gif"/>
    <action id="Open" name="Open..." mnemonic="O" accel="control O" smicon="/toolbarButtonGraphics/general/Open16.gif"/>
    <action id="Save"
            name="Save..."
            mnemonic="S"
            accel="control S"
            smicon="/toolbarButtonGraphics/general/Save16.gif"
            enabled="false"/>
    <action id="Exit"
            name="Exit"
            mnemonic="X"
            desc="Exits the application"/>
    <action-list>
</action-set>
```

Pretty simple, right? SAM also comes with an Action Editor, though it is not
 quite ready for prime time yet.

#### 2. Load the Actions into the ActionManager

```java
ActionManager.getInstance().register(new File("./actions/Foo.xml"));
```

When reading the XML file this way, the DTD is must stored relative to the
 file. So in this case there will be a ./actions/dtd/actions.dtd, since the dtd
 path in the doctype of the action XML was ' SYSTEM "dtd/action-set.dtd".'

#### 3. Get the Action from the ActionManager and Listen

Usually a simple app will have an application controller that listens to menus, like so:

```java
BasicAction action = (BasicAction) ActionManager.getInstance().getAction("New");
action.addActionListener(e -> createNewDocument());

action = (BasicAction) ActionManager.getInstance().getAction("Open");
action.addActionListener(e -> openDocument());
```

If you are a super-genius and never make a mistake work with super-geniuses
 who know your every thought, you can do this via reflection instead:

```java
ActionManager.registerActionCallback("New", this, "createNewDocument");
ActionManager.registerActionCallback("Open", this, "openDocument");
```

I'm not so smart and have to rely on my compiler more. I have yet to get stuck
 with someone changing an action-id on me, maybe because I use "new-action"
 for an id instead of just "new", since it's more searchable. Intellij
 IDEA takes care of this pretty well.

#### 4. Create menus and toolbars

Steps 3 and 4 can be done in any order. Here's how to create a menu:

```java
JMenuBar menuBar = ActionUIFactory.getInstance().createMenuBar("main-menu");
myFrame.setJMenuBar(menuBar);
```

### Alternatives

#### Creating Actions by Hand

A BasicAction can be instantiated and used directly instead:

```java
Action newAction = new BasicAction("New", KeyEvent.VK_N,
        KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK), newIcon);
Action openAction = new BasicAction("Open", KeyEvent.VK_O,
        KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK), openIcon);
Action saveAction = new BasicAction("Save", KeyEvent.VK_S,
        KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), saveIcon);
Action exitAction = new BasicAction("Exit", KeyEvent.VK_X,
        KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK), exitIcon);
```

An ActionList can then be created like a java.util.List:

```java
ActionList mainMenu = new ActionList();
mainMenu.add(newAction);
mainMenu.add(openAction);
mainMenu.add(saveAction);
mainMenu.add(exitAction);
```

Another way to create an Action is to make a class that extends BasicAction
 and instantiate the derived class. This technique encapsulates the Action in
 a class, which may be desirable.

```java
public class NewAction extends BasicAction {
    public static final String ID = "New";
    private static final ImageIcon ICON = ActionManager.resolveIcon("images/New.gif"));
    
    public NewAction() {
        super(ID, KeyEvent.VK_N, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK), ICON);
    }
    
    public void execute(ActionEvent evt) {
        //...Open the new document....
    }
}
```

The constructor could have just well-chosen to use the Action putValue methods
like so (though to some this may seem harder to read):

```java
public NewAction() {
    putValue(Action.NAME, NAME);
    putValue(Action.SMALL_ICON, ICON);
    putValue(Action.ACTION_COMMAND_KEY, COMMAND_KEY);
    putValue(Action.SHORT_DESCRIPTION, SHORT_DESC);
    putValue(Action.LONG_DESCRIPTION, LONG_DESC);
    putValue(Action.MNEMONIC_KEY, MNEMONIC);
    putValue(Action.ACCELERATOR_KEY, KEYSTROKE);
    setEnabled(true);
}
```

Another handy use of the ActionManager is to allow components to register themselves
as ShouldBeEnabledDelegates on Actions.

```java
BasicAction saveAction = (BasicAction)ActionManager.getAction("Save");
saveAction.addShouldBeEnabledDelegate(new ShouldBeEnabledDelegate() {
    @Override
    public boolean shouldBeEnabled() {
        return this.getModel().hasChanged();
    }
});
```

This delegate will be called when the action is told to update its enabled state:

```java
// MyModelClass
private void setModified(boolean isChanged) {
    modified = isChanged;
    Action saveAction = ActionManager.getAction("Save");
    saveAction.updateEnabled();
}
```

For any other techniques like security,
### TODOs

- INTERNATIONALIZATION!!!!! Of course you could XSLify your XML, but a nicer
    solution such @name attributes and resourceBundle attributes in action-set
    elements would be better.

- Action list inheritence is not yet supported- though it used to be! This
    is a way in the XML to handle the history case above - make a new list in
    the XML that extends the main-tool-bar list, and adds one element for the
    history action.

- Why doens't the following work anymore?

 - toolbarShowsText that determines whether or not a toolbar
        that has this actions show the text and the icon or just the icon. The
        default is to show just the icon. (The Java default is to show the text
        and icon in the toolbar).

 - menuShowsIcon that determines whether or not a menu
        that has this action shows the icon and the text or just the text. The
        default is false is to show the icon and the text.
