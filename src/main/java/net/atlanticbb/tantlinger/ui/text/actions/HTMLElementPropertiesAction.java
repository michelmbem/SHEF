package net.atlanticbb.tantlinger.ui.text.actions;

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;
import net.atlanticbb.tantlinger.ui.text.dialogs.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

//import java.util.Hashtable;

/**
 * Action for editing an element's properties depending on the
 * current caret position.
 * <p>
 * Currently supports links, images, tables, lists, and paragraphs.
 *
 * @author Bob Tantlinger
 *
 */
public class HTMLElementPropertiesAction extends HTMLTextEditAction {
    public static final int TABLE_PROPS = 0;
    public static final int LIST_PROPS = 1;
    public static final int IMG_PROPS = 2;
    public static final int LINK_PROPS = 3;
    public static final int ELEM_PROPS = 4;
    protected static final String[] PROPS = {
            i18n.str("table_properties_"),
            i18n.str("list_properties_"),
            i18n.str("image_properties_"),
            i18n.str("hyperlink_properties_"),
            i18n.str("object_properties_")
    };
    private static final long serialVersionUID = 1L;

    public HTMLElementPropertiesAction() {
        super(PROPS[ELEM_PROPS]);
        addShouldBeEnabledDelegate(a -> getEditMode() != SOURCE &&
                elementAtCaretPosition(getCurrentEditor()) != null);
    }

    protected void wysiwygEditPerformed(ActionEvent e, JEditorPane ed) {
        Element elem = elementAtCaretPosition(ed);
        if (elem == null) return;

        int type = getElementType(elem);
        int caret = ed.getCaretPosition();

        switch (type) {
            case LINK_PROPS:
                editLinkProps(elem);
                break;
            case IMG_PROPS:
                editImageProps(elem);
                break;
            case TABLE_PROPS:
                editTableProps(elem);
                break;
            case LIST_PROPS:
                editListProps(elem);
                break;
            case ELEM_PROPS:
                editStyleProps(elem.getParentElement());
                break;
            default:
                return;
        }

        try {
            ed.setCaretPosition(caret);
        } catch (Exception ignored) {
        }
    }

    private Map<String, String> getAttribs(Element elem) {
        Map<String, String> attribs = new HashMap<>();
        AttributeSet a = elem.getAttributes();

        for (Enumeration<?> e = a.getAttributeNames(); e.hasMoreElements(); ) {
            Object n = e.nextElement();
            //dont return the name attribute
            if (n.toString().equals("name") && !elem.getName().equals("a")) continue;
            attribs.put(n.toString(), a.getAttribute(n).toString());
        }

        return attribs;
    }

    private String getElementHTML(Element el, Map attribs) {
        StringBuilder html = new StringBuilder("<").append(el.getName());

        for (Object name : attribs.keySet()) {
            Object val = attribs.get(name);
            html.append(" ").append(name).append("=\"").append(val).append("\"");
        }

        String txt = HTMLUtils.getElementHTML(el, false);
        html.append(">\n").append(txt).append("\n</").append(el.getName()).append(">");
        return html.toString();
    }

    private Map<String, String> getLinkAttributes(Element elem) {
        String link = HTMLUtils.getElementHTML(elem, true).trim();
        Map<String, String> attribs = new HashMap<>();

        if (link.startsWith("<a")) {
            link = link.substring(0, link.indexOf('>'));
            link = link.substring(link.indexOf(' ')).trim();
            attribs = HTMLUtils.tagAttribsToMap(link);
        }

        return attribs;
    }

    private void editImageProps(Element elem) {
        ImageDialog d = createImageDialog();

        if (d != null) {
            Map<String, String> imgAttribs = getAttribs(elem);
            d.setImageAttributes(imgAttribs);
            d.setLocationRelativeTo(d.getParent());
            d.setVisible(true);
            if (!d.hasUserCancelled()) {
                replace(elem, d.getHTML());
            }
        }
    }

    private void editLinkProps(Element elem) {
        HyperlinkDialog d = createLinkDialog();
        if (d != null) {
            d.setAttributes(getLinkAttributes(elem));
            d.setLocationRelativeTo(d.getParent());

            try {
                //get the link text...
                String text = elem.getDocument().getText(
                        elem.getStartOffset(),
                        elem.getEndOffset() - elem.getStartOffset());
                d.setLinkText(text);
            } catch (BadLocationException ignored) {
            }

            d.setVisible(true);
            if (!d.hasUserCancelled()) {
                replace(elem, d.getHTML());
            }
        }
    }

    private void editTableProps(Element paraElem) {
        HTMLDocument doc = null;
        try {
            doc = (HTMLDocument) paraElem.getDocument();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        Element tdElem = HTMLUtils.getParent(paraElem, HTML.Tag.TD);
        Element trElem = HTMLUtils.getParent(paraElem, HTML.Tag.TR);
        Element tableElem = HTMLUtils.getParent(paraElem, HTML.Tag.TABLE);
        TablePropertiesDialog dlg = createTablePropertiesDialog();
        if (dlg == null || tdElem == null || trElem == null || tableElem == null)
            return; //no dialog or malformed table! Just return...

        dlg.setCellAttributes(getAttribs(tdElem));
        dlg.setRowAttributes(getAttribs(trElem));
        dlg.setTableAttributes(getAttribs(tableElem));
        dlg.setLocationRelativeTo(dlg.getParent());
        dlg.setVisible(true);

        if (!dlg.hasUserCancelled()) {
            CompoundUndoManager.beginCompoundEdit(doc);
            try {
                String html = getElementHTML(tdElem, dlg.getCellAttributes());
                doc.setOuterHTML(tdElem, html);

                html = getElementHTML(trElem, dlg.getRowAttributes());
                doc.setOuterHTML(trElem, html);

                html = getElementHTML(tableElem, dlg.getTableAttributes());
                doc.setOuterHTML(tableElem, html);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            CompoundUndoManager.endCompoundEdit(doc);
        }
    }

    private void editListProps(Element elem) {
        elem = HTMLUtils.getListParent(elem);
        if (elem == null)
            return;
        int type;
        if (elem.getName().equals("ul"))
            type = ListDialog.UNORDERED;
        else if (elem.getName().equals("ol"))
            type = ListDialog.ORDERED;
        else
            return;

        Map<String, String> attr = getAttribs(elem);
        ListDialog d = createListDialog();
        if (d == null) return;

        d.setListType(type);
        d.setListAttributes(attr);
        d.setLocationRelativeTo(d.getParent());
        d.setVisible(true);
        if (!d.hasUserCancelled()) {
            attr = d.getListAttributes();
            StringBuilder html = new StringBuilder();

            if (d.getListType() != type) {
                HTML.Tag tag = HTML.Tag.UL;
                if (d.getListType() == ListDialog.ORDERED)
                    tag = HTML.Tag.OL;

                html.append("<").append(tag);
                for (Object o : attr.keySet()) {
                    html.append(" ").append(o).append("=").append(attr.get(o));
                }

                String txt = HTMLUtils.getElementHTML(elem, false);
                html.append(">").append(txt).append("</").append(tag).append(">");
            } else {
                html.append(getElementHTML(elem, attr));
            }

            replace(elem, html.toString());
        }

    }

    private void editStyleProps(Element elem) {
        if (elem.getName().equals("p-implied"))
            elem = elem.getParentElement();

        Map<String, String> attr = getAttribs(elem);
        ElementStyleDialog d = createStyleDialog();
        if (d == null) return;

        d.setLocationRelativeTo(d.getParent());
        d.setStyleAttributes(attr);
        d.setVisible(true);

        if (!d.hasUserCancelled()) {
            String html = getElementHTML(elem, d.getStyleAttributes());
            replace(elem, html);
        }
    }

    protected HyperlinkDialog createLinkDialog() {
        Component c = getCurrentEditor();
        HyperlinkDialog d = null;

        if (c != null) {
            Window w = SwingUtilities.getWindowAncestor(c);
            if (w instanceof Frame)
                d = new HyperlinkDialog((Frame) w);
            else if (w instanceof Dialog)
                d = new HyperlinkDialog((Dialog) w);
        }

        return d;
    }

    protected ImageDialog createImageDialog() {
        Component c = getCurrentEditor();
        ImageDialog d = null;

        if (c != null) {
            Window w = SwingUtilities.getWindowAncestor(c);
            if (w instanceof Frame)
                d = new ImageDialog((Frame) w);
            else if (w instanceof Dialog)
                d = new ImageDialog((Dialog) w);
        }

        return d;
    }

    protected TablePropertiesDialog createTablePropertiesDialog() {
        Component c = getCurrentEditor();
        TablePropertiesDialog d = null;

        if (c != null) {
            Window w = SwingUtilities.getWindowAncestor(c);
            if (w instanceof Frame)
                d = new TablePropertiesDialog((Frame) w);
            else if (w instanceof Dialog)
                d = new TablePropertiesDialog((Dialog) w);
        }

        return d;
    }

    protected ListDialog createListDialog() {
        Component c = getCurrentEditor();
        ListDialog d = null;

        if (c != null) {
            Window w = SwingUtilities.getWindowAncestor(c);
            if (w instanceof Frame)
                d = new ListDialog((Frame) w);
            else if (w instanceof Dialog)
                d = new ListDialog((Dialog) w);
        }

        return d;
    }

    protected ElementStyleDialog createStyleDialog() {
        Component c = getCurrentEditor();
        ElementStyleDialog d = null;
        
        if (c != null) {
            Window w = SwingUtilities.getWindowAncestor(c);
            if (w instanceof Frame)
                d = new ElementStyleDialog((Frame) w);
            else if (w instanceof Dialog)
                d = new ElementStyleDialog((Dialog) w);
        }

        return d;
    }

    private void replace(Element elem, String html) {
        HTMLDocument document = null;
        try {
            document = (HTMLDocument) elem.getDocument();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        CompoundUndoManager.beginCompoundEdit(document);
        try {
            document.setOuterHTML(elem, html);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            CompoundUndoManager.endCompoundEdit(document);
        }
    }

    @Override
    protected void updateWysiwygContextState(JEditorPane ed) {
        int t = ELEM_PROPS;
        Element elem = elementAtCaretPosition(ed);
        if (elem != null) t = getElementType(elem);
        putValue(NAME, PROPS[t]);
    }

    private int getElementType(Element elem) {
        AttributeSet att = elem.getAttributes();
        String name = att.getAttribute(StyleConstants.NameAttribute).toString();

        //is it an image?
        if (name.equals("img")) return IMG_PROPS;

        //is it a link?
        for (Enumeration<?> ee = att.getAttributeNames(); ee.hasMoreElements(); ) {
            if (ee.nextElement().toString().equals("a")) return LINK_PROPS;
        }

        //is it a list?
        if (HTMLUtils.getParent(elem, HTML.Tag.UL) != null) return LIST_PROPS;
        if (HTMLUtils.getParent(elem, HTML.Tag.OL) != null) return LIST_PROPS;

        //is it a table?
        if (HTMLUtils.getParent(elem, HTML.Tag.TD) != null) return TABLE_PROPS;

        //return the default
        return ELEM_PROPS;
    }

    /**
     * Computes the (inline or block) element at the focused editor's caret position
     *
     * @return the element, or null of the element cant be retrieved
     */
    private Element elementAtCaretPosition(JTextComponent ed) {
        if (ed == null) return null;

        HTMLDocument doc = (HTMLDocument) ed.getDocument();
        int caret = ed.getCaretPosition();

        Element elem = doc.getParagraphElement(caret);
        HTMLDocument.BlockElement blockElem = (HTMLDocument.BlockElement) elem;
        return blockElem.positionToElement(caret);
    }

    /* (non-Javadoc)
     * @see net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction#sourceEditPerformed(java.awt.event.ActionEvent, javax.swing.JEditorPane)
     */
    @Override
    protected void sourceEditPerformed(ActionEvent e, JTextComponent editor) {
        // Does nothing
    }
}
