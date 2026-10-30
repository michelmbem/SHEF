package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.OptionDialog;
import net.atlanticbb.tantlinger.ui.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class NewTableDialog extends OptionDialog {
    private static final long serialVersionUID = 1L;
    private static final I18n I18N = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");
    private static final Icon ICON = UIUtils.getIcon(UIUtils.X48, "table.png");

    private final LayoutPanel layoutPanel = new LayoutPanel();
    private TableAttributesPanel propsPanel;

    public NewTableDialog(Frame parent) {
        super(parent, I18N.str("new_table"), I18N.str("new_table_desc"), ICON);
        init();
    }

    public NewTableDialog(Dialog parent) {
        super(parent, I18N.str("new_table"), I18N.str("new_table_desc"), ICON);
        init();
    }

    private void init() {
        //default attribs
        Map<String, String> attribs = new HashMap<>();
        attribs.put("border", "1");
        attribs.put("width", "100%");

        propsPanel = new TableAttributesPanel();
        propsPanel.setAttributes(attribs);
        propsPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(I18N.str("properties")),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(layoutPanel, BorderLayout.PAGE_START);
        mainPanel.add(propsPanel, BorderLayout.CENTER);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setContentPane(mainPanel);
        setSize(new Dimension(330, 380));
        setResizable(false);
    }

    public String getHTML() {
        Map<String, String> attribs = propsPanel.getAttributes();
        StringBuilder html = new StringBuilder("<table");

        for (Map.Entry<String, String> entry : attribs.entrySet()) {
            html.append(' ').append(entry.getKey()).append("=\"")
                    .append(entry.getValue()).append("\"");
        }

        html.append(">\n");

        int numRows = layoutPanel.getRows();
        int numCols = layoutPanel.getColumns();

        for (int row = 0; row < numRows; ++row) {
            html.append("\t<tr>\n");
            html.append("\t\t<td>\n\t\t</td>\n".repeat(Math.max(0, numCols)));
            html.append("\t</tr>\n");
        }

        return html + "</table>\n";
    }

    private static class LayoutPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private JLabel rowsLabel = null;
        private JLabel colsLabel = null;
        private int iRows;
        private int iCols;
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
            iRows = Math.max(r, 1);
            iCols = Math.max(c, 1);
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
            final int anchor = GridBagConstraints.BASELINE_LEADING;
            final int fill = GridBagConstraints.HORIZONTAL;
            final Insets leftInsets = new Insets(0, 0, 5, 5);
            final Insets middleInsets = new Insets(0, 10, 5, 5);
            final Insets rightInsets = new Insets(0, 0, 5, 0);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = gbc.gridy = 0;
            gbc.anchor = anchor;
            gbc.fill = fill;
            gbc.insets = leftInsets;

            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 1;
            gbc1.gridy = 0;
            gbc1.anchor = anchor;
            gbc1.fill = fill;
            gbc1.insets = leftInsets;

            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 2;
            gbc2.gridy = 0;
            gbc2.anchor = anchor;
            gbc2.fill = fill;
            gbc2.insets = middleInsets;

            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = 3;
            gbc3.gridy = 0;
            gbc3.anchor = anchor;
            gbc3.fill = fill;
            gbc3.insets = rightInsets;

            rowsLabel = new JLabel(I18N.str("rows"));
            colsLabel = new JLabel(I18N.str("columns"));

            GridBagLayout gbl = new GridBagLayout();
            gbl.columnWeights = new double[] {0.0, 1.0, 0.0, 1.0};
            gbl.rowWeights = new double[] {0.0};

            setLayout(gbl);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(null, I18N.str("layout")),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            add(rowsLabel, gbc);
            add(getRowsField(), gbc1);
            add(colsLabel, gbc2);
            add(getColsField(), gbc3);
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
