package net.atlanticbb.tantlinger.shef;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.DefaultAction;
import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.*;
import net.atlanticbb.tantlinger.ui.text.actions.*;
import novaworx.syntax.SyntaxFactory;
import novaworx.textpane.SyntaxDocument;
import novaworx.textpane.SyntaxGutter;
import novaworx.textpane.SyntaxGutterBase;
import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Bob Tantlinger
 */
public class HTMLEditorPane extends JPanel 
{
    private static final long serialVersionUID = 1L;
    private static final I18n i18n = I18n.getInstance("net.atlanticbb.tantlinger.shef");
    private static final String[] INVALID_TAGS = {"html", "head", "body", "title"};

    private JEditorPane wysEditor;
    private SourceCodeEditor srcEditor;
    private JEditorPane focusedEditor;
    private JComboBox fontFamilyCombo;
    private JComboBox paragraphCombo;
    private JTabbedPane tabs;
    private JToolBar formatToolBar;

    private JMenu editMenu;
    private JMenu formatMenu;
    private JMenu insertMenu;

    private JPopupMenu wysPopupMenu, srcPopupMenu;
    
    private ActionList actionList;
    
    private final FocusListener focusHandler = new FocusHandler();
    private final DocumentListener textChangedHandler = new TextChangedHandler();
    private final ActionListener fontChangeHandler = new FontChangeHandler();
    private final ActionListener paragraphComboHandler = new ParagraphComboHandler();
    private final CaretListener caretHandler = new CaretHandler();
    private final MouseListener popupHandler = new PopupHandler();
        
    private boolean isWysTextChanged;
    
    public HTMLEditorPane(boolean showHTMLEditor)
    {
    	initUI(showHTMLEditor);
    }
    
    public void setCaretPosition(int pos)
    {
    	if(tabs.getSelectedIndex() == 0)
    	{
    		wysEditor.setCaretPosition(pos);
    		wysEditor.requestFocusInWindow();
    	}
    	else if(tabs.getSelectedIndex() == 1)
    	{
    		srcEditor.setCaretPosition(pos);  
    		srcEditor.requestFocusInWindow();
    	}
    }
    
    public void setSelectedTab(int i)
    {
    	tabs.setSelectedIndex(i);
    }
    
    private void initUI(boolean showHTMLEditor)
    {
        createEditorTabs(showHTMLEditor);
        createEditorActions(showHTMLEditor);
        setLayout(new BorderLayout());
        add(formatToolBar, BorderLayout.PAGE_START);
        add(tabs, BorderLayout.CENTER);
    }
    
    public void dispose()
    {
        wysEditor.removeAll();
        wysEditor.removeCaretListener(caretHandler);
        wysEditor.removeFocusListener(focusHandler);
        wysEditor.getDocument().removeDocumentListener(textChangedHandler);
        
        srcEditor.removeAll();
        srcEditor.removeCaretListener(caretHandler);
        srcEditor.removeFocusListener(focusHandler);
        srcEditor.getDocument().removeDocumentListener(textChangedHandler);
    }

    public JMenu getEditMenu()
    {
        return editMenu;
    }

    public JMenu getFormatMenu()
    {
        return formatMenu;
    }

    public JMenu getInsertMenu()
    {
        return insertMenu;
    }

    private void createEditorActions(boolean enableHTML)
    {        
        actionList = new ActionList("editor-actions");
        
        ActionList paraActions = new ActionList("paraActions");
        ActionList fontSizeActions = new ActionList("fontSizeActions");
        ActionList editActions = HTMLEditorActionFactory.createEditActionList();
        Action objectPropertiesAction = new HTMLElementPropertiesAction();
        
        //create editor popupmenus
        wysPopupMenu = ActionUIFactory.getInstance().createPopupMenu(editActions);
        wysPopupMenu.addSeparator();
        wysPopupMenu.add(objectPropertiesAction);
        srcPopupMenu = ActionUIFactory.getInstance().createPopupMenu(editActions);               
                
        // create file menu
        JMenu fileMenu = new JMenu(i18n.str("file"));        
        
        // create edit menu   
        Action act;
        ActionList lst = new ActionList("edits");             
        if (enableHTML) {
            act = new ChangeTabAction(0);        
            lst.add(act);
            act = new ChangeTabAction(1);        
            lst.add(act);
            lst.add(null);//separator   
        }
        lst.addAll(editActions);
        lst.add(null);
        lst.add(new FindReplaceAction(false, enableHTML));
        actionList.addAll(lst);
        editMenu = ActionUIFactory.getInstance().createMenu(lst);
        editMenu.setText(i18n.str("edit"));        

        //create format menu
        formatMenu = new JMenu(i18n.str("format"));        
        lst = HTMLEditorActionFactory.createFontSizeActionList();
        actionList.addAll(lst);        
        formatMenu.add(createMenu(lst, i18n.str("size")));
        fontSizeActions.addAll(lst);
        
        lst = HTMLEditorActionFactory.createInlineActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, i18n.str("style")));
        
        act = new HTMLFontColorAction();
        actionList.add(act);
        formatMenu.add(act);
        
        act = new HTMLFontAction();
        actionList.add(act);
        formatMenu.add(act);
        
        act = new ClearStylesAction();
        actionList.add(act);
        formatMenu.add(act);
        formatMenu.addSeparator();
        
        lst = HTMLEditorActionFactory.createBlockElementActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, i18n.str("paragraph")));
        paraActions.addAll(lst);
        
        lst = HTMLEditorActionFactory.createListElementActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, i18n.str("list")));
        formatMenu.addSeparator();
        paraActions.addAll(lst);
        
        lst = HTMLEditorActionFactory.createAlignActionList();
        actionList.addAll(lst);        
        formatMenu.add(createMenu(lst, i18n.str("align")));
                
        JMenu tableMenu = new JMenu(i18n.str("table"));
        lst = HTMLEditorActionFactory.createInsertTableElementActionList();
        actionList.addAll(lst);
        tableMenu.add(createMenu(lst, i18n.str("insert")));
        
        lst = HTMLEditorActionFactory.createDeleteTableElementActionList();
        actionList.addAll(lst);
        tableMenu.add(createMenu(lst, i18n.str("delete")));
        formatMenu.add(tableMenu);
        formatMenu.addSeparator();
                
        actionList.add(objectPropertiesAction);
        formatMenu.add(objectPropertiesAction);

        //create insert menu
        insertMenu = new JMenu(i18n.str("insert"));
        act = new HTMLLinkAction();
        actionList.add(act);
        insertMenu.add(act);
        
        act = new HTMLImageAction();
        actionList.add(act);
        insertMenu.add(act);
        
        act = new HTMLTableAction();
        actionList.add(act);
        insertMenu.add(act);
        insertMenu.addSeparator();
        
        act = new HTMLLineBreakAction();
        actionList.add(act);
        insertMenu.add(act);
        
        act = new HTMLHorizontalRuleAction();
        actionList.add(act);
        insertMenu.add(act);
        
        act = new SpecialCharAction();
        actionList.add(act);
        insertMenu.add(act);
       
        createFormatToolBar(paraActions, fontSizeActions);
    }
    
    private void createFormatToolBar(ActionList blockActs, ActionList fontSizeActs)
    {
        formatToolBar = new JToolBar();
        formatToolBar.setFloatable(false);
        formatToolBar.setFocusable(false);
        
        Font comboFont = new Font("Dialog", Font.PLAIN, 12);
        PropertyChangeListener propLst = evt -> {
            if(evt.getPropertyName().equals("selected")
                    && evt.getNewValue().equals(Boolean.TRUE))
                {
                    paragraphCombo.removeActionListener(paragraphComboHandler);
                    paragraphCombo.setSelectedItem(evt.getSource());
                    paragraphCombo.addActionListener(paragraphComboHandler);
                }
        };

        for (Object o : blockActs) {
            if (o instanceof DefaultAction)
                ((DefaultAction) o).addPropertyChangeListener(propLst);
        }

        final Dimension fixedSize = new Dimension(120, 22);
        paragraphCombo = new JComboBox<>(toArray(blockActs));
        paragraphCombo.setPreferredSize(fixedSize);
        paragraphCombo.setMinimumSize(fixedSize);
        paragraphCombo.setMaximumSize(fixedSize);
        paragraphCombo.setFont(comboFont);
        paragraphCombo.addActionListener(paragraphComboHandler);
        paragraphCombo.setRenderer(new ParagraphComboRenderer());
        formatToolBar.add(paragraphCombo);
        formatToolBar.addSeparator();
                
        ArrayList<String> fonts = new ArrayList<String>();
        fonts.add("Default");
        fonts.add("serif");
        fonts.add("sans-serif");
        fonts.add("monospaced"); 
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fonts.addAll(Arrays.asList(gEnv.getAvailableFontFamilyNames()));             
        
        fontFamilyCombo = new JComboBox<Font>();
        fontFamilyCombo.setPreferredSize(new Dimension(150, 22));
        fontFamilyCombo.setMinimumSize(new Dimension(150, 22));
        fontFamilyCombo.setMaximumSize(new Dimension(150, 22));
        fontFamilyCombo.setFont(comboFont);
        fontFamilyCombo.addActionListener(fontChangeHandler);
        
        for (String f : fonts) {
            fontFamilyCombo.addItem(new Font(f, Font.PLAIN, 12));
        }

        fontFamilyCombo.setRenderer(new DefaultListCellRenderer() {
            private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                JLabel renderer = (JLabel) defaultRenderer
                        .getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
                renderer.setFont((Font)value);
                renderer.setText(((Font)value).getFontName());
                fontFamilyCombo.setFont((Font)value);
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
        
        List alst = HTMLEditorActionFactory.createListElementActionList();
        for (Object o : alst) {
            act = (Action) o;
            act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
            actionList.add(act);
            addToToolBar(formatToolBar, act);
        }
        formatToolBar.addSeparator();
        
        alst = HTMLEditorActionFactory.createAlignActionList();
        for (Object o : alst) {
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
    
    private void addToToolBar(JToolBar toolbar, Action act)
    {
        AbstractButton button = ActionUIFactory.getInstance().createButton(act);
        configToolbarButton(button);
        toolbar.add(button);
    }
    
    /**
     * Converts an action list to an array. 
     * Any of the null "separators" or sub ActionLists are ommited from the array.
     * @param lst
     * @return
     */
    private Action[] toArray(ActionList lst)
    {
        List<Action> acts = new ArrayList<>();
        for (Object v : lst) {
            if (v instanceof Action)
                acts.add((Action) v);
        }
        
        return acts.toArray(Action[]::new);
    }
        
    private void configToolbarButton(AbstractButton button)
    {
        button.setText(null);
        button.setMnemonic(0);
        button.setMargin(new Insets(1, 1, 1, 1));
        button.setMaximumSize(new Dimension(22, 22));
        button.setMinimumSize(new Dimension(22, 22));
        button.setPreferredSize(new Dimension(22, 22));
        button.setFocusable(false);
        button.setFocusPainted(false);
        Action a = button.getAction();
        if(a != null)
            button.setToolTipText(a.getValue(Action.NAME).toString());
    }   
    
    private JMenu createMenu(ActionList lst, String menuName)
    {
        JMenu m = ActionUIFactory.getInstance().createMenu(lst);
        m.setText(menuName);
        return m;
    }
    
    private void createEditorTabs(boolean show)
    {
        tabs = new JTabbedPane(SwingConstants.BOTTOM);
        
        if (!show) {  //set the tab height to zero to hide them from the panel
            tabs.setUI(new BasicTabbedPaneUI() {  
                @Override  
                protected int calculateTabAreaHeight(int tab_placement, int run_count, int max_tab_height) {
                        return 0;  
                }  
            });  
        }
                        
        wysEditor = createWysiwygEditor();
        srcEditor = createSourceEditor();        

        tabs.addTab("Edit", new JScrollPane(wysEditor));
        
        JScrollPane scrollPane = new JScrollPane(srcEditor);        
        SyntaxGutter gutter = new SyntaxGutter(srcEditor);
        SyntaxGutterBase gutterBase = new SyntaxGutterBase(gutter);
        scrollPane.setRowHeaderView(gutter);
        scrollPane.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, gutterBase);
        
        tabs.addTab("HTML", scrollPane);
        tabs.addChangeListener(e -> updateEditView());
    }
    
    private SourceCodeEditor createSourceEditor()
    {        
        SourceCodeEditor ed = new SourceCodeEditor();
        SyntaxDocument doc = new SyntaxDocument();
        doc.setSyntax(SyntaxFactory.getSyntax("html"));        
        CompoundUndoManager cuh = new CompoundUndoManager(doc, new UndoManager());        
        
        doc.addUndoableEditListener(cuh);
        doc.setDocumentFilter(new IndentationFilter());
        doc.addDocumentListener(textChangedHandler);
        ed.setDocument(doc);
        ed.addFocusListener(focusHandler);        
        ed.addCaretListener(caretHandler);
        ed.addMouseListener(popupHandler);
        
        return ed;
    }
    
    private JEditorPane createWysiwygEditor()
    {
        JEditorPane ed = new JEditorPane();
        ed.setEditorKitForContentType("text/html", new WysiwygHTMLEditorKit());
       
        ed.setContentType("text/html"); 
        
        insertHTML(ed, "<div></div>", 0);
                
        ed.addCaretListener(caretHandler);
        ed.addFocusListener(focusHandler);
        ed.addMouseListener(popupHandler);
        
        HTMLDocument document = (HTMLDocument)ed.getDocument();
        CompoundUndoManager cuh = new CompoundUndoManager(document, new UndoManager());
        document.addUndoableEditListener(cuh);
        document.addDocumentListener(textChangedHandler);
                
        return ed;        
    }

    //  inserts html into the wysiwyg editor TODO remove JEditorPane parameter
    private void insertHTML(JEditorPane editor, String html, int location) 
    {       
        try 
        {
            HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
            Document doc = editor.getDocument();
            StringReader reader = new StringReader(HTMLUtils.jEditorPaneizeHTML(html));
            kit.read(reader, doc, location);
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    // called when changing tabs
    private void updateEditView()
    {       
        if(tabs.getSelectedIndex() == 0)
        {           
            String topText = removeInvalidTags(srcEditor.getText());            
            wysEditor.setText("");
            insertHTML(wysEditor, topText, 0);            
            CompoundUndoManager.discardAllEdits(wysEditor.getDocument());
        }
        else 
        {           
            String topText = removeInvalidTags(wysEditor.getText());            
            if(isWysTextChanged || srcEditor.getText().equals(""))
            {
                String t = deIndent(removeInvalidTags(topText));
                t = Entities.HTML40.unescapeUnknownEntities(t);                
                srcEditor.setText(t);
            }            
            CompoundUndoManager.discardAllEdits(srcEditor.getDocument());            
        }       
        
        isWysTextChanged = false;
        paragraphCombo.setEnabled(tabs.getSelectedIndex() == 0);
        fontFamilyCombo.setEnabled(tabs.getSelectedIndex() == 0);
        updateState();        
    }
    
    public void setText(String text)
    {
    	String topText = removeInvalidTags(text);  
        
        if(tabs.getSelectedIndex() == 0)
        {
            wysEditor.setText("");
            insertHTML(wysEditor, topText, 0);            
            CompoundUndoManager.discardAllEdits(wysEditor.getDocument());
        }
        else 
        {
            {
                String t = deIndent(removeInvalidTags(topText));
                t = Entities.HTML40.unescapeUnknownEntities(t);                
                srcEditor.setText(t);
            }            
            CompoundUndoManager.discardAllEdits(srcEditor.getDocument());            
        }
    }
    
    public String getText()
    {
    	String topText;
    	if(tabs.getSelectedIndex() == 0)
        {           
           topText = removeInvalidTags(wysEditor.getText());
        }
        else 
        {           
            topText = removeInvalidTags(srcEditor.getText()); 
            topText = deIndent(removeInvalidTags(topText));
            topText = Entities.HTML40.unescapeUnknownEntities(topText); 
        }
    	
    	return topText;
    }
    
    
    /* *******************************************************************
     *  Methods for dealing with HTML between wysiwyg and source editors 
     * ******************************************************************/
    private String deIndent(String html)
    {
        String ws = "\n    ";
        StringBuffer sb = new StringBuffer(html);
        
        while(sb.indexOf(ws) != -1)
        {             
            int s = sb.indexOf(ws);            
            int e = s + ws.length();
            sb.delete(s, e);
            sb.insert(s, "\n");          
        }
        
        return sb.toString();
    }
    
    private String removeInvalidTags(String html)
    {
        for(int i = 0; i < INVALID_TAGS.length; i++)
        {
            html = deleteOccurance(html, '<' + INVALID_TAGS[i] + '>');
            html = deleteOccurance(html, "</" + INVALID_TAGS[i] + '>');
        }
           
        return html.trim();
    }
    
    private String deleteOccurance(String text, String word)
    {
        //if(text == null)return "";
        StringBuffer sb = new StringBuffer(text);       
        int p;
        while((p = sb.toString().toLowerCase().indexOf(word.toLowerCase())) != -1)
        {           
            sb.delete(p, p + word.length());            
        }
        return sb.toString();
    }
    /* ************************************* */
    
    private void updateState()
    {
        if(focusedEditor == wysEditor)
        {            
            fontFamilyCombo.removeActionListener(fontChangeHandler);
            String fontName = HTMLUtils.getFontFamily(wysEditor);
            if(fontName == null)
                fontFamilyCombo.setSelectedIndex(0);
            else
                fontFamilyCombo.setSelectedItem(new Font(fontName, Font.PLAIN, 12));
            fontFamilyCombo.addActionListener(fontChangeHandler);
        }
        
        actionList.putContextValueForAll(HTMLTextEditAction.EDITOR, focusedEditor);
        actionList.updateEnabledForAll();
    }
    
    
    private class CaretHandler implements CaretListener
    {
        /* (non-Javadoc)
         * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
         */
        public void caretUpdate(CaretEvent e)
        {            
            updateState();
        }        
    }
    
    private class PopupHandler extends MouseAdapter
    {
        public void mousePressed(MouseEvent e)
        { checkForPopupTrigger(e); }
        
        public void mouseReleased(MouseEvent e)
        { checkForPopupTrigger(e); }
        
        private void checkForPopupTrigger(MouseEvent e)
        {
            if(e.isPopupTrigger())
            {                    
                JPopupMenu p = null;
                if(e.getSource() == wysEditor)
                    p =  wysPopupMenu;
                else if(e.getSource() == srcEditor)
                    p = srcPopupMenu;
                else
                    return;
                p.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
    private class FocusHandler implements FocusListener
    {
        public void focusGained(FocusEvent e)
        {
            if(e.getComponent() instanceof JEditorPane)
            {
                JEditorPane ed = (JEditorPane)e.getComponent();
                CompoundUndoManager.updateUndo(ed.getDocument());
                focusedEditor = ed;
                
                updateState();
               // updateEnabledStates();
            }
        }
        
        public void focusLost(FocusEvent e)
        {
            
            if(e.getComponent() instanceof JEditorPane)
            {
                //focusedEditor = null;
                //wysiwygUpdated();
            }
        }
    }
    
    private class TextChangedHandler implements DocumentListener
    {
        public void insertUpdate(DocumentEvent e)
        {
            textChanged();
        }
        
        public void removeUpdate(DocumentEvent e)
        {
            textChanged();
        }
        
        public void changedUpdate(DocumentEvent e)
        {
            textChanged();
        }
        
        private void textChanged()
        {
            if(tabs.getSelectedIndex() == 0)
                isWysTextChanged = true;
        }
    }
    
    private class ChangeTabAction extends DefaultAction
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        int tab;        
        public ChangeTabAction(int tab)
        {
            super((tab == 0) ? i18n.str("rich_text") :
                i18n.str("source"));
            this.tab = tab;
            putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_RADIO);
        }
        
        protected void execute(ActionEvent e)
        {
            tabs.setSelectedIndex(tab);
            setSelected(true);
        }
        
        protected void contextChanged()
        {
            setSelected(tabs.getSelectedIndex() == tab);
        }
    }
    
    private class ParagraphComboHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == paragraphCombo)
            {
                Action a = (Action)(paragraphCombo.getSelectedItem());
                a.actionPerformed(e);
            }
        }
    }
    
    private class ParagraphComboRenderer extends DefaultListCellRenderer
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus)
        {
            if(value instanceof Action)
            {
                value = ((Action)value).getValue(Action.NAME);
            }
            
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
    
    private class FontChangeHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {            
            if(e.getSource() == fontFamilyCombo && focusedEditor == wysEditor )
            {                
                //MutableAttributeSet tagAttrs = new SimpleAttributeSet();
                HTMLDocument document = (HTMLDocument)focusedEditor.getDocument();
                CompoundUndoManager.beginCompoundEdit(document);
                
                if(fontFamilyCombo.getSelectedIndex() != 0)
                {
                    HTMLUtils.setFontFamily(wysEditor, ((Font)fontFamilyCombo.getSelectedItem()).getFamily());
                    
                }
                else
                {
                    HTMLUtils.setFontFamily(wysEditor, null);                                                        
                }
                CompoundUndoManager.endCompoundEdit(document);                
            }
        }

        /* (non-Javadoc)
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
        public void itemStateChanged(ItemEvent e)
        {
            
            
        }
    }	
}
