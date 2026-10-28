package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.OptionDialog;
import net.atlanticbb.tantlinger.ui.UIUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class TablePropertiesDialog extends OptionDialog {
    private static final long serialVersionUID = 1L;

    private static final I18n i18n = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");

    private static Icon icon = UIUtils.getIcon(UIUtils.X48, "table.png");
    private static String title = i18n.str("table_properties");
    private static String desc = i18n.str("table_properties_desc");

    private TableAttributesPanel tableProps = new TableAttributesPanel();
    private RowAttributesPanel rowProps = new RowAttributesPanel();
    private CellAttributesPanel cellProps = new CellAttributesPanel();

    public TablePropertiesDialog(Frame parent) {
        super(parent, title, desc, icon);
        init();
    }

    public TablePropertiesDialog(Dialog parent) {
        super(parent, title, desc, icon);
        init();
    }

    private void init() {
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        Border titleBorder = BorderFactory.createTitledBorder(i18n.str("table_properties"));

        tableProps.setBorder(BorderFactory.createCompoundBorder(emptyBorder, titleBorder));
        rowProps.setBorder(emptyBorder);
        cellProps.setBorder(emptyBorder);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add(tableProps, i18n.str("table"));
        tabs.add(rowProps, i18n.str("row"));
        tabs.add(cellProps, i18n.str("cell"));

        setContentPane(tabs);
        setSize(440, 375);
        setResizable(false);
    }

    public void setRowAttributes(Map at) {
        rowProps.setAttributes(at);
    }

    public Map getTableAttributes() {
        return tableProps.getAttributes();
    }

    public void setTableAttributes(Map at) {
        tableProps.setAttributes(at);
    }

    public Map getRowAttribures() {
        return rowProps.getAttributes();
    }

    public Map getCellAttributes() {
        return cellProps.getAttributes();
    }

    public void setCellAttributes(Map at) {
        cellProps.setAttributes(at);
    }
}
