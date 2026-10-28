package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.OptionDialog;
import net.atlanticbb.tantlinger.ui.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class NewTableDialog extends OptionDialog {
    private static final long serialVersionUID = 1L;

    private static final I18n i18n = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");
    private static Icon icon = UIUtils.getIcon(UIUtils.X48, "table.png");
    private LayoutPanel layoutPanel = new LayoutPanel();
    private TableAttributesPanel propsPanel;

    public NewTableDialog(Frame parent) {
        super(parent, i18n.str("new_table"), i18n.str("new_table_desc"), icon);
        init();
    }

    public NewTableDialog(Dialog parent) {
        super(parent, i18n.str("new_table"), i18n.str("new_table_desc"), icon);
        init();
    }

    private void init() {
        //default attribs
        Hashtable ht = new Hashtable();
        ht.put("border", "1");
        ht.put("width", "100%");
        propsPanel = new TableAttributesPanel();
        propsPanel.setAttributes(ht);

        propsPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(i18n.str("properties")),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(layoutPanel, BorderLayout.NORTH);
        mainPanel.add(propsPanel, BorderLayout.CENTER);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        setSize(new Dimension(330, 380));
        setResizable(false);
    }

    public String getHTML() {
        String html = "<table";
        Map attribs = propsPanel.getAttributes();

        for (Iterator e = attribs.keySet().iterator(); e.hasNext(); ) {
            String key = e.next().toString();
            String val = attribs.get(key).toString();
            html += ' ' + key + "=\"" + val + "\"";
        }

        html += ">\n";

        int numRows = layoutPanel.getRows();
        int numCols = layoutPanel.getColumns();
        for (int row = 1; row <= numRows; row++) {
            html += "<tr>\n";
            for (int col = 1; col <= numCols; col++) {
                html += "\t<td>\n</td>\n";
            }
            html += "</tr>\n";
        }

        return html + "</table>";
    }

    private class LayoutPanel extends JPanel {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private JLabel rowsLabel = null;
        private JLabel colsLabel = null;
        private int iRows, iCols;
        private JSpinner rowsField = null;
        private JSpinner colsField = null;

        /**
         * This is the default constructor
         */
        public LayoutPanel() {
            this(1, 1);
        }

        public LayoutPanel(int r, int c) {
            super();
            iRows = (r > 0) ? r : 1;
            iCols = (c > 0) ? c : 1;
            initialize();
        }

        public int getRows() {
            return Integer.parseInt(rowsField.getModel().getValue().toString());
        }

        public int getColumns() {
            return Integer.parseInt(colsField.getModel().getValue().toString());
        }

        /**
         * This method initializes this
         */
        private void initialize() {
            GridBagConstraints gbc7 = new GridBagConstraints();
            gbc7.fill = GridBagConstraints.NONE;
            gbc7.gridy = 0;
            gbc7.weightx = 1.0;
            gbc7.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc7.gridx = 3;
            GridBagConstraints gbc6 = new GridBagConstraints();
            gbc6.fill = GridBagConstraints.NONE;
            gbc6.gridy = 0;
            gbc6.weightx = 0.0;
            gbc6.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc6.insets = new Insets(0, 0, 0, 15);
            gbc6.gridx = 1;
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 2;
            gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc1.insets = new Insets(0, 0, 0, 5);
            gbc1.gridy = 0;
            colsLabel = new JLabel();
            colsLabel.setText(i18n.str("columns"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc.insets = new Insets(0, 0, 0, 5);
            gbc.weighty = 0.0;
            gbc.gridy = 0;
            rowsLabel = new JLabel();
            rowsLabel.setText(i18n.str("rows"));
            this.setLayout(new GridBagLayout());
            this.setSize(330, 60);
            this.setPreferredSize(new Dimension(330, 60));
            //this.setMaximumSize(this.getPreferredSize());
            this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, i18n.str("layout"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            this.add(rowsLabel, gbc);
            this.add(colsLabel, gbc1);
            this.add(getRowsField(), gbc6);
            this.add(getColsField(), gbc7);
        }

        /**
         * This method initializes rowsField
         *
         * @return javax.swing.JSpinner
         */
        private JSpinner getRowsField() {
            if (rowsField == null) {
                rowsField = new JSpinner(new SpinnerNumberModel(iRows, 1, 999, 1));
            }
            return rowsField;
        }

        /**
         * This method initializes colsField
         *
         * @return javax.swing.JSpinner
         */
        private JSpinner getColsField() {
            if (colsField == null) {
                colsField = new JSpinner(new SpinnerNumberModel(iCols, 1, 999, 1));
            }
            return colsField;
        }
    }

}
