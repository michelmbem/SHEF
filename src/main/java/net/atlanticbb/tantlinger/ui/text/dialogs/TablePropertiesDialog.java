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
    private static final I18n I18N = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");
    private static final Icon ICON = UIUtils.getIcon(UIUtils.X48, "table.png");
    private static final String TITLE = I18N.str("table_properties");
    private static final String DESC = I18N.str("table_properties_desc");

    private final TableAttributesPanel tableProps = new TableAttributesPanel();
    private final RowAttributesPanel rowProps = new RowAttributesPanel();
    private final CellAttributesPanel cellProps = new CellAttributesPanel();

    public TablePropertiesDialog(Frame parent) {
        super(parent, TITLE, DESC, ICON);
        init();
    }

    public TablePropertiesDialog(Dialog parent) {
        super(parent, TITLE, DESC, ICON);
        init();
    }

    private void init() {
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        Border titleBorder = BorderFactory.createTitledBorder(I18N.str("table_properties"));

        tableProps.setBorder(BorderFactory.createCompoundBorder(emptyBorder, titleBorder));
        rowProps.setBorder(emptyBorder);
        cellProps.setBorder(emptyBorder);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add(tableProps, I18N.str("table"));
        tabs.add(rowProps, I18N.str("row"));
        tabs.add(cellProps, I18N.str("cell"));

        setContentPane(tabs);
        setSize(525, 375);
        setResizable(false);
    }

    public void setRowAttributes(Map<String, String> at) {
        rowProps.setAttributes(at);
    }

    public Map<String, String> getTableAttributes() {
        return tableProps.getAttributes();
    }

    public void setTableAttributes(Map<String, String> at) {
        tableProps.setAttributes(at);
    }

    public Map<String, String> getRowAttributes() {
        return rowProps.getAttributes();
    }

    public Map<String, String> getCellAttributes() {
        return cellProps.getAttributes();
    }

    public void setCellAttributes(Map<String, String> at) {
        cellProps.setAttributes(at);
    }
}
