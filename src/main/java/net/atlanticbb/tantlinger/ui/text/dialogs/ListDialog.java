/*
 * Created on Jan 18, 2006
 *
 */
package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.OptionDialog;
import net.atlanticbb.tantlinger.ui.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ListDialog extends OptionDialog {
    public static final int UNORDERED = ListAttributesPanel.UL_LIST;
    public static final int ORDERED = ListAttributesPanel.OL_LIST;
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final I18n i18n = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");
    private static Icon icon = UIUtils.getIcon(UIUtils.X48, "categories.png"); //$NON-NLS-1$
    private static String title = i18n.str("list_properties"); //$NON-NLS-1$
    private static String desc = i18n.str("list_properties_desc"); //$NON-NLS-1$

    private ListAttributesPanel listAttrPanel;

    public ListDialog(Frame parent) {
        super(parent, title, desc, icon);
        init();
    }

    public ListDialog(Dialog parent) {
        super(parent, title, desc, icon);
        init();
    }

    private void init() {
        listAttrPanel = new ListAttributesPanel();
        setContentPane(listAttrPanel);
        pack();
        setSize(220, getHeight());
        setResizable(false);
    }

    public int getListType() {
        return listAttrPanel.getListType();
    }

    public void setListType(int t) {
        listAttrPanel.setListType(t);
    }

    public Map getListAttributes() {
        return listAttrPanel.getAttributes();
    }

    public void setListAttributes(Map attr) {
        listAttrPanel.setAttributes(attr);
    }
}
