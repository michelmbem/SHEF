package net.atlanticbb.tantlinger.shef;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.DefaultAction;
import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.*;
import net.atlanticbb.tantlinger.ui.text.actions.*;
import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * A Java Swing HTML WYSIWYG editor
 *
 * @author Bob Tantlinger
 */
public class HTMLEditorPane extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final I18n I18N = I18n.getInstance("net.atlanticbb.tantlinger.shef");
    private static final String[] INVALID_TAGS = {"html", "head", "body", "title"};

    private final FocusListener focusHandler = new FocusHandler();
    private final DocumentListener textChangedHandler = new TextChangedHandler();
    private final ActionListener fontChangeHandler = new FontChangeHandler();
    private final ActionListener paragraphComboHandler = new ParagraphComboHandler();
    private final CaretListener caretHandler = new CaretHandler();
    private final MouseListener popupHandler = new PopupHandler();

    private JTabbedPane tabbedPane;
    private JEditorPane wysiwygEditor;
    private SourceCodeEditor sourceEditor;
    private JTextComponent activeEditor;
    private ActionList actionList;
    private JMenu editMenu;
    private JMenu formatMenu;
    private JMenu insertMenu;
    private JPopupMenu wysiwygPopupMenu;
    private JPopupMenu sourcePopupMenu;
    private JToolBar formatToolBar;
    private JComboBox<Font> fontFamilyCombo;
    private JComboBox<Action> paragraphCombo;
    private boolean wysiwygTextChanged;

    public HTMLEditorPane(boolean showHTMLEditor) {
        initUI(showHTMLEditor);
    }

    public void setCaretPosition(int pos) {
        switch (tabbedPane.getSelectedIndex()) {
            case 0:
                wysiwygEditor.setCaretPosition(pos);
                wysiwygEditor.requestFocusInWindow();
                break;
            case 1:
                sourceEditor.setCaretPosition(pos);
                sourceEditor.requestFocusInWindow();
                break;
        }
    }

    public void setSelectedTab(int i) {
        tabbedPane.setSelectedIndex(i);
    }

    public void dispose() {
        wysiwygEditor.removeAll();
        wysiwygEditor.removeCaretListener(caretHandler);
        wysiwygEditor.removeFocusListener(focusHandler);
        wysiwygEditor.getDocument().removeDocumentListener(textChangedHandler);

        sourceEditor.removeAll();
        sourceEditor.removeCaretListener(caretHandler);
        sourceEditor.removeFocusListener(focusHandler);
        sourceEditor.getDocument().removeDocumentListener(textChangedHandler);
    }

    public JMenu getEditMenu() {
        return editMenu;
    }

    public JMenu getFormatMenu() {
        return formatMenu;
    }

    public JMenu getInsertMenu() {
        return insertMenu;
    }

    private void initUI(boolean showHTMLEditor) {
        createEditorTabs(showHTMLEditor);
        createEditorActions(showHTMLEditor);
        setLayout(new BorderLayout());
        add(formatToolBar, BorderLayout.PAGE_START);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void createEditorTabs(boolean showHTMLEditor) {
        tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);
        if (!showHTMLEditor) {
            // set the tab height to zero to hide them from the panel
            tabbedPane.setUI(new BasicTabbedPaneUI() {
                @Override
                protected int calculateTabAreaHeight(int tabPlacement, int runCount, int maxTabHeight) {
                    return 0;
                }
            });
        }

        wysiwygEditor = createWysiwygEditor();
        sourceEditor = createSourceEditor();

        RTextScrollPane sourceScrollPane = new RTextScrollPane(sourceEditor);
        sourceScrollPane.setLineNumbersEnabled(true);

        tabbedPane.addTab("Edit", new JScrollPane(wysiwygEditor));
        tabbedPane.addTab("HTML", sourceScrollPane);
        tabbedPane.addChangeListener(e -> updateEditView());
    }

    private void createEditorActions(boolean enableHTML) {
        actionList = new ActionList("editor-actions");

        ActionList paraActions = new ActionList("paraActions");
        ActionList fontSizeActions = new ActionList("fontSizeActions");
        ActionList editActions = HTMLEditorActionFactory.createEditActionList();
        Action objectPropertiesAction = new HTMLElementPropertiesAction();

        ActionUIFactory actionUIFactory = ActionUIFactory.getInstance();

        // create editor popup menus
        wysiwygPopupMenu = actionUIFactory.createPopupMenu(editActions);
        wysiwygPopupMenu.addSeparator();
        wysiwygPopupMenu.add(objectPropertiesAction);
        sourcePopupMenu = actionUIFactory.createPopupMenu(editActions);

        // create edit menu   
        Action action;
        ActionList list = new ActionList("edits");
        if (enableHTML) {
            action = new ChangeTabAction(0);
            list.add(action);
            action = new ChangeTabAction(1);
            list.add(action);
            list.add(null); // separator
        }
        list.addAll(editActions);
        list.add(null);
        list.add(new FindReplaceAction(false));
        actionList.addAll(list);
        editMenu = actionUIFactory.createMenu(list);
        editMenu.setText(I18N.str("edit"));

        // create format menu
        formatMenu = new JMenu(I18N.str("format"));
        list = HTMLEditorActionFactory.createFontSizeActionList();
        actionList.addAll(list);
        formatMenu.add(createMenu(list, I18N.str("size")));
        fontSizeActions.addAll(list);

        list = HTMLEditorActionFactory.createInlineActionList();
        actionList.addAll(list);
        formatMenu.add(createMenu(list, I18N.str("style")));

        action = new HTMLFontColorAction();
        actionList.add(action);
        formatMenu.add(action);

        action = new HTMLFontAction();
        actionList.add(action);
        formatMenu.add(action);

        action = new ClearStylesAction();
        actionList.add(action);
        formatMenu.add(action);
        formatMenu.addSeparator();

        list = HTMLEditorActionFactory.createBlockElementActionList();
        actionList.addAll(list);
        formatMenu.add(createMenu(list, I18N.str("paragraph")));
        paraActions.addAll(list);

        list = HTMLEditorActionFactory.createListElementActionList();
        actionList.addAll(list);
        formatMenu.add(createMenu(list, I18N.str("list")));
        formatMenu.addSeparator();
        paraActions.addAll(list);

        list = HTMLEditorActionFactory.createAlignActionList();
        actionList.addAll(list);
        formatMenu.add(createMenu(list, I18N.str("align")));

        JMenu tableMenu = new JMenu(I18N.str("table"));
        list = HTMLEditorActionFactory.createInsertTableElementActionList();
        actionList.addAll(list);
        tableMenu.add(createMenu(list, I18N.str("insert")));

        list = HTMLEditorActionFactory.createDeleteTableElementActionList();
        actionList.addAll(list);
        tableMenu.add(createMenu(list, I18N.str("delete")));
        formatMenu.add(tableMenu);
        formatMenu.addSeparator();

        actionList.add(objectPropertiesAction);
        formatMenu.add(objectPropertiesAction);

        // create insert menu
        insertMenu = new JMenu(I18N.str("insert"));
        action = new HTMLLinkAction();
        actionList.add(action);
        insertMenu.add(action);

        action = new HTMLImageAction();
        actionList.add(action);
        insertMenu.add(action);

        action = new HTMLTableAction();
        actionList.add(action);
        insertMenu.add(action);
        insertMenu.addSeparator();

        action = new HTMLLineBreakAction();
        actionList.add(action);
        insertMenu.add(action);

        action = new HTMLHorizontalRuleAction();
        actionList.add(action);
        insertMenu.add(action);

        action = new SpecialCharAction();
        actionList.add(action);
        insertMenu.add(action);

        createFormatToolBar(paraActions, fontSizeActions);
    }

    private JEditorPane createWysiwygEditor() {
        JEditorPane editor = new JEditorPane();
        editor.setEditorKitForContentType("text/html", new WysiwygHTMLEditorKit());
        editor.setContentType("text/html");

        insertHTML(editor, "<div></div>", 0);

        editor.addCaretListener(caretHandler);
        editor.addFocusListener(focusHandler);
        editor.addMouseListener(popupHandler);

        HTMLDocument document = (HTMLDocument) editor.getDocument();
        CompoundUndoManager cum = new CompoundUndoManager(document, new UndoManager());
        document.addUndoableEditListener(cum);
        document.addDocumentListener(textChangedHandler);

        return editor;
    }

    private SourceCodeEditor createSourceEditor() {
        SourceCodeEditor editor = new SourceCodeEditor();
        AbstractDocument document = (AbstractDocument) editor.getDocument();
        CompoundUndoManager cum = new CompoundUndoManager(document, new UndoManager());
        document.addUndoableEditListener(cum);
        document.setDocumentFilter(new IndentationFilter());
        document.addDocumentListener(textChangedHandler);

        editor.addFocusListener(focusHandler);
        editor.addCaretListener(caretHandler);
        editor.addMouseListener(popupHandler);

        return editor;
    }

    private void createFormatToolBar(ActionList blockActs, ActionList fontSizeActs) {
        formatToolBar = new JToolBar();
        formatToolBar.setFloatable(false);
        formatToolBar.setFocusable(false);

        Font comboFont = new Font("Dialog", Font.PLAIN, 12);
        PropertyChangeListener propLst = evt -> {
            if (evt.getPropertyName().equals("selected") &&
                    evt.getNewValue().equals(Boolean.TRUE)) {
                paragraphCombo.removeActionListener(paragraphComboHandler);
                paragraphCombo.setSelectedItem(evt.getSource());
                paragraphCombo.addActionListener(paragraphComboHandler);
            }
        };

        for (Object o : blockActs) {
            if (o instanceof DefaultAction)
                ((DefaultAction) o).addPropertyChangeListener(propLst);
        }

        final Dimension paragraphComboSize = new Dimension(120, 22);
        paragraphCombo = new JComboBox<>(toArray(blockActs));
        paragraphCombo.setPreferredSize(paragraphComboSize);
        paragraphCombo.setMinimumSize(paragraphComboSize);
        paragraphCombo.setMaximumSize(paragraphComboSize);
        paragraphCombo.setFont(comboFont);
        paragraphCombo.addActionListener(paragraphComboHandler);
        paragraphCombo.setRenderer(new ParagraphComboRenderer());
        formatToolBar.add(paragraphCombo);
        formatToolBar.addSeparator();

        java.util.List<String> fonts = new ArrayList<>();
        fonts.add("Default");
        fonts.add("serif");
        fonts.add("sans-serif");
        fonts.add("monospaced");
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fonts.addAll(Arrays.asList(gEnv.getAvailableFontFamilyNames()));

        final Dimension fontFamilyComboSize = new Dimension(150, 22);
        fontFamilyCombo = new JComboBox<>();
        fontFamilyCombo.setPreferredSize(fontFamilyComboSize);
        fontFamilyCombo.setMinimumSize(fontFamilyComboSize);
        fontFamilyCombo.setMaximumSize(fontFamilyComboSize);
        fontFamilyCombo.setFont(comboFont);
        fontFamilyCombo.addActionListener(fontChangeHandler);

        for (String f : fonts) {
            fontFamilyCombo.addItem(new Font(f, Font.PLAIN, 12));
        }

        fontFamilyCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                JLabel renderer = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                Font font = (Font) value;
                renderer.setFont(font);
                renderer.setText(font.getFontName());
                fontFamilyCombo.setFont(font);
                return renderer;
            }
        });

        formatToolBar.add(fontFamilyCombo);

        final JButton fontSizeButton = new JButton(UIUtils.getIcon(UIUtils.X16, "fontsize.png"));
        final JPopupMenu sizePopup = ActionUIFactory.getInstance().createPopupMenu(fontSizeActs);
        ActionListener al = e -> sizePopup.show(fontSizeButton, 0, fontSizeButton.getHeight());
        fontSizeButton.addActionListener(al);
        configToolbarButton(fontSizeButton);
        formatToolBar.add(fontSizeButton);

        Action act = new HTMLFontColorAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);
        formatToolBar.addSeparator();

        act = new HTMLInlineAction(HTMLInlineAction.BOLD);
        act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
        actionList.add(act);
        addToToolBar(formatToolBar, act);

        act = new HTMLInlineAction(HTMLInlineAction.ITALIC);
        act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
        actionList.add(act);
        addToToolBar(formatToolBar, act);

        act = new HTMLInlineAction(HTMLInlineAction.UNDERLINE);
        act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
        actionList.add(act);
        addToToolBar(formatToolBar, act);
        formatToolBar.addSeparator();

        ActionList list = HTMLEditorActionFactory.createListElementActionList();
        for (Object o : list) {
            act = (Action) o;
            act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
            actionList.add(act);
            addToToolBar(formatToolBar, act);
        }
        formatToolBar.addSeparator();

        list = HTMLEditorActionFactory.createAlignActionList();
        for (Object o : list) {
            act = (Action) o;
            act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
            actionList.add(act);
            addToToolBar(formatToolBar, act);
        }
        formatToolBar.addSeparator();

        act = new HTMLLinkAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);

        act = new HTMLImageAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);

        act = new HTMLTableAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);
    }

    private void addToToolBar(JToolBar toolbar, Action act) {
        AbstractButton button = ActionUIFactory.getInstance().createButton(act);
        configToolbarButton(button);
        toolbar.add(button);
    }

    /**
     * Converts an action list to an array.
     * Any of the null "separators" or sub ActionLists are ommited from the array.
     *
     * @param actionList The action list to convert
     * @return An array of Actions
     */
    private Action[] toArray(ActionList actionList) {
        return actionList.toArray(Action[]::new);
    }

    private void configToolbarButton(AbstractButton button) {
        final Dimension buttonSize = new Dimension(22, 22);
        button.setText(null);
        button.setMnemonic(0);
        button.setMargin(new Insets(1, 1, 1, 1));
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setPreferredSize(buttonSize);
        button.setFocusable(false);
        button.setFocusPainted(false);
        Action a = button.getAction();
        if (a != null) button.setToolTipText(
                a.getValue(Action.NAME).toString());
    }

    private JMenu createMenu(ActionList actionList, String menuName) {
        JMenu menu = ActionUIFactory.getInstance().createMenu(actionList);
        menu.setText(menuName);
        return menu;
    }

    private void insertHTML(JEditorPane editor, String html, int location) {
        try {
            HTMLEditorKit editorKit = (HTMLEditorKit) editor.getEditorKit();
            Document document = editor.getDocument();
            StringReader reader = new StringReader(HTMLUtils.jEditorPaneizeHTML(html));
            editorKit.read(reader, document, location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateEditView() {
        if (tabbedPane.getSelectedIndex() == 0) {
            String html = removeInvalidTags(sourceEditor.getText());
            wysiwygEditor.setText("");
            insertHTML(wysiwygEditor, html, 0);
            CompoundUndoManager.discardAllEdits(wysiwygEditor.getDocument());
        } else {
            String html = removeInvalidTags(wysiwygEditor.getText());
            if (wysiwygTextChanged || sourceEditor.getText().isEmpty()) {
                String outdented = deIndent(html);
                outdented = Entities.HTML40.unescapeUnknownEntities(outdented);
                sourceEditor.setText(outdented);
            }
            CompoundUndoManager.discardAllEdits(sourceEditor.getDocument());
        }

        wysiwygTextChanged = false;
        paragraphCombo.setEnabled(tabbedPane.getSelectedIndex() == 0);
        fontFamilyCombo.setEnabled(tabbedPane.getSelectedIndex() == 0);
        updateState();
    }

    public String getText() {
        String topText;

        if (tabbedPane.getSelectedIndex() == 0) {
            topText = removeInvalidTags(wysiwygEditor.getText());
        } else {
            topText = removeInvalidTags(sourceEditor.getText());
            topText = deIndent(removeInvalidTags(topText));
            topText = Entities.HTML40.unescapeUnknownEntities(topText);
        }

        return topText;
    }

    public void setText(String text) {
        String topText = removeInvalidTags(text);

        if (tabbedPane.getSelectedIndex() == 0) {
            wysiwygEditor.setText("");
            insertHTML(wysiwygEditor, topText, 0);
            CompoundUndoManager.discardAllEdits(wysiwygEditor.getDocument());
        } else {
            String t = deIndent(removeInvalidTags(topText));
            t = Entities.HTML40.unescapeUnknownEntities(t);
            sourceEditor.setText(t);
            CompoundUndoManager.discardAllEdits(sourceEditor.getDocument());
        }
    }

    /* *******************************************************************
     *  Methods for dealing with HTML between wysiwyg and source editors
     * ******************************************************************/
    private String deIndent(String html) {
        String ws = "\n    ";
        StringBuilder sb = new StringBuilder(html);

        while (sb.indexOf(ws) != -1) {
            int s = sb.indexOf(ws);
            int e = s + ws.length();
            sb.delete(s, e);
            sb.insert(s, "\n");
        }

        return sb.toString();
    }

    private String removeInvalidTags(String html) {
        for (String invalidTag : INVALID_TAGS) {
            html = deleteOccurance(html, '<' + invalidTag + '>');
            html = deleteOccurance(html, "</" + invalidTag + '>');
        }

        return html.trim();
    }

    private String deleteOccurance(String text, String word) {
        StringBuilder sb = new StringBuilder(text);
        int p;

        while ((p = sb.toString().toLowerCase(Locale.ROOT)
                .indexOf(word.toLowerCase())) != -1) {
            sb.delete(p, p + word.length());
        }

        return sb.toString();
    }
    /* ************************************* */

    private void updateState() {
        if (activeEditor == wysiwygEditor) {
            fontFamilyCombo.removeActionListener(fontChangeHandler);
            String fontName = HTMLUtils.getFontFamily(wysiwygEditor);
            if (fontName == null)
                fontFamilyCombo.setSelectedIndex(0);
            else
                fontFamilyCombo.setSelectedItem(new Font(fontName, Font.PLAIN, 12));
            fontFamilyCombo.addActionListener(fontChangeHandler);
        }

        actionList.putContextValueForAll(HTMLTextEditAction.EDITOR, activeEditor);
        actionList.updateEnabledForAll();
    }

    private static class ParagraphComboRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Action) {
                value = ((Action) value).getValue(Action.NAME);
            }

            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    private class ChangeTabAction extends DefaultAction {
        private static final long serialVersionUID = 1L;
        final int tab;

        public ChangeTabAction(int tab) {
            super(I18N.str(tab == 0 ? "rich_text" : "source"));
            this.tab = tab;
            putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_RADIO);
        }

        @Override
        protected void execute(ActionEvent e) {
            tabbedPane.setSelectedIndex(tab);
            setSelected(true);
        }

        @Override
        protected void contextChanged() {
            setSelected(tabbedPane.getSelectedIndex() == tab);
        }
    }

    private class CaretHandler implements CaretListener {
        /* (non-Javadoc)
         * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
         */
        public void caretUpdate(CaretEvent e) {
            updateState();
        }
    }

    private class PopupHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            checkForPopupTrigger(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            checkForPopupTrigger(e);
        }

        private void checkForPopupTrigger(MouseEvent e) {
            if (e.isPopupTrigger()) {
                JPopupMenu p;

                if (e.getSource() == wysiwygEditor)
                    p = wysiwygPopupMenu;
                else if (e.getSource() == sourceEditor)
                    p = sourcePopupMenu;
                else
                    return;

                p.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private class FocusHandler implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            if (e.getComponent() == wysiwygEditor) {
                CompoundUndoManager.updateUndo(wysiwygEditor.getDocument());
                activeEditor = wysiwygEditor;
                updateState();
            } else if (e.getComponent() == sourceEditor) {
                CompoundUndoManager.updateUndo(sourceEditor.getDocument());
                activeEditor = sourceEditor;
                updateState();
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            // Does nothing
        }
    }

    private class TextChangedHandler implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            textChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            textChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            textChanged();
        }

        private void textChanged() {
            if (tabbedPane.getSelectedIndex() == 0)
                wysiwygTextChanged = true;
        }
    }

    private class ParagraphComboHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == paragraphCombo) {
                Action a = (Action) (paragraphCombo.getSelectedItem());
                a.actionPerformed(e);
            }
        }
    }

    private class FontChangeHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == fontFamilyCombo && activeEditor == wysiwygEditor) {
                HTMLDocument document = (HTMLDocument) activeEditor.getDocument();
                CompoundUndoManager.beginCompoundEdit(document);

                if (fontFamilyCombo.getSelectedIndex() != 0) {
                    HTMLUtils.setFontFamily(wysiwygEditor,
                            ((Font) fontFamilyCombo.getSelectedItem()).getFamily());
                } else {
                    HTMLUtils.setFontFamily(wysiwygEditor, null);
                }

                CompoundUndoManager.endCompoundEdit(document);
            }
        }
    }
}
