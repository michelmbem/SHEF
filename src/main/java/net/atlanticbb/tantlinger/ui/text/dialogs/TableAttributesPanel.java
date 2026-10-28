package net.atlanticbb.tantlinger.ui.text.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TableAttributesPanel extends HTMLAttributeEditorPanel {

    private static final long serialVersionUID = 1L;
    private static final String[] ALIGNMENTS = {"left", "center", "right"};
    private static final String[] MEASUREMENTS = {"percent", "pixels"};

    private JCheckBox widthCB = null;
    private JSpinner widthField = null;
    private JComboBox<String> widthCombo = null;
    private JCheckBox alignCB = null;
    private JCheckBox cellSpacingCB = null;
    private JSpinner cellSpacingField = null;
    private JCheckBox borderCB = null;
    private JSpinner borderField = null;
    private JCheckBox cellPaddingCB = null;
    private JSpinner cellPaddingField = null;
    private JComboBox<String> alignCombo = null;
    private BGColorPanel bgPanel = null;
    private JPanel expansionPanel = null;

    /**
     * This is the default constructor
     */
    public TableAttributesPanel() {
        this(new HashMap<>());
    }

    public TableAttributesPanel(Map<String, String> attribs) {
        super(attribs);
        initialize();
        updateComponentsFromAttribs();
    }

    @Override
    public void updateComponentsFromAttribs() {
        if (attribs.containsKey("width")) {
            widthCB.setSelected(true);
            String w = attribs.get("width");
            if (w.endsWith("%"))
                w = w.substring(0, w.length() - 1);
            else
                widthCombo.setSelectedIndex(1);
            widthField.setEnabled(true);

            try {
                widthField.getModel().setValue(Integer.valueOf(w));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            widthCB.setSelected(false);
            widthField.setEnabled(false);
            widthCombo.setEnabled(false);
        }

        if (attribs.containsKey("align")) {
            alignCB.setSelected(true);
            alignCombo.setEnabled(true);
            alignCombo.setSelectedItem(attribs.get("align"));
        } else {
            alignCB.setSelected(false);
            alignCombo.setEnabled(false);
        }

        if (attribs.containsKey("border")) {
            borderCB.setSelected(true);
            borderField.setEnabled(true);

            try {
                borderField.getModel().setValue(
                        Integer.valueOf(attribs.get("border")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            borderCB.setSelected(false);
            borderField.setEnabled(false);
        }

        if (attribs.containsKey("cellpadding")) {
            cellPaddingCB.setSelected(true);
            cellPaddingField.setEnabled(true);

            try {
                cellPaddingField.getModel().setValue(
                        Integer.valueOf(attribs.get("cellpadding")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cellPaddingCB.setSelected(false);
            cellPaddingField.setEnabled(false);
        }

        if (attribs.containsKey("cellspacing")) {
            cellSpacingCB.setSelected(true);
            cellSpacingField.setEnabled(true);
            try {
                cellSpacingField.getModel().setValue(
                        Integer.valueOf(attribs.get("cellspacing")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cellSpacingCB.setSelected(false);
            cellSpacingField.setEnabled(false);
        }

        if (attribs.containsKey("bgcolor")) {
            bgPanel.setSelected(true);
            bgPanel.setColor(attribs.get("bgcolor"));
        } else {
            bgPanel.setSelected(false);
        }
    }

    public void updateAttribsFromComponents() {
        if (widthCB.isSelected()) {
            String w = widthField.getModel().getValue().toString();
            if (widthCombo.getSelectedIndex() == 0) w += "%";
            attribs.put("width", w);
        } else {
            attribs.remove("width");
        }

        if (alignCB.isSelected())
            attribs.put("align", alignCombo.getSelectedItem().toString());
        else
            attribs.remove("align");

        if (borderCB.isSelected())
            attribs.put("border",
                    borderField.getModel().getValue().toString());
        else
            attribs.remove("border");

        if (cellSpacingCB.isSelected())
            attribs.put("cellspacing",
                    cellSpacingField.getModel().getValue().toString());
        else
            attribs.remove("cellspacing");

        if (cellPaddingCB.isSelected())
            attribs.put("cellpadding",
                    cellPaddingField.getModel().getValue().toString());
        else
            attribs.remove("cellpadding");

        if (bgPanel.isSelected())
            attribs.put("bgcolor", bgPanel.getColor());
        else
            attribs.remove("bgcolor");
    }

    public void setComponentStates(Map<String, String> attribs) {
        if (attribs.containsKey("width")) {
            widthCB.setSelected(true);
            String w = attribs.get("width");

            if (w.endsWith("%"))
                w = w.substring(0, w.length() - 1);
            else
                widthCombo.setSelectedIndex(1);

            try {
                widthField.getModel().setValue(new Integer(w));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            widthCB.setSelected(false);
            widthField.setEnabled(false);
            widthCombo.setEnabled(false);
        }

        if (attribs.containsKey("align")) {
            alignCB.setSelected(true);
            alignCombo.setSelectedItem(attribs.get("align"));
        } else {
            alignCB.setSelected(false);
            alignCombo.setEnabled(false);
        }

        if (attribs.containsKey("border")) {
            borderCB.setSelected(true);
            try {
                borderField.getModel().setValue(
                        Integer.valueOf(attribs.get("border")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            borderCB.setSelected(false);
            borderField.setEnabled(false);
        }

        if (attribs.containsKey("cellpadding")) {
            cellPaddingCB.setSelected(true);
            try {
                cellPaddingField.getModel().setValue(
                        Integer.valueOf(attribs.get("cellpadding")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cellPaddingCB.setSelected(false);
            cellPaddingField.setEnabled(false);
        }

        if (attribs.containsKey("cellspacing")) {
            cellSpacingCB.setSelected(true);
            try {
                cellSpacingField.getModel().setValue(
                        Integer.valueOf(attribs.get("cellspacing")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cellSpacingCB.setSelected(false);
            cellSpacingField.setEnabled(false);
        }

        if (attribs.containsKey("bgcolor")) {
            bgPanel.setSelected(true);
            bgPanel.setColor(attribs.get("bgcolor"));
        } else {
            bgPanel.setSelected(false);
        }
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        final int anchor = GridBagConstraints.BASELINE_LEADING;
        final int fill = GridBagConstraints.HORIZONTAL;
        final Insets leftInsets = new Insets(0, 0, 5, 5);
        final Insets rightInsets = new Insets(0, 0, 5, 0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
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
        gbc2.insets = rightInsets;

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 0;
        gbc3.gridy = 1;
        gbc3.anchor = anchor;
        gbc3.fill = fill;
        gbc3.insets = leftInsets;

        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.gridx = 1;
        gbc4.gridy = 1;
        gbc4.anchor = anchor;
        gbc4.fill = fill;
        gbc4.insets = leftInsets;

        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.gridx = 2;
        gbc5.gridy = 1;
        gbc5.anchor = anchor;
        gbc5.fill = fill;
        gbc5.insets = leftInsets;

        GridBagConstraints gbc6 = new GridBagConstraints();
        gbc6.gridx = 3;
        gbc6.gridy = 1;
        gbc6.anchor = anchor;
        gbc6.fill = fill;
        gbc6.insets = rightInsets;

        GridBagConstraints gbc7 = new GridBagConstraints();
        gbc7.gridx = 0;
        gbc7.gridy = 2;
        gbc7.anchor = anchor;
        gbc7.fill = fill;
        gbc7.insets = leftInsets;

        GridBagConstraints gbc8 = new GridBagConstraints();
        gbc8.gridx = 1;
        gbc8.gridy = 2;
        gbc8.anchor = anchor;
        gbc8.fill = fill;
        gbc8.insets = leftInsets;

        GridBagConstraints gbc9 = new GridBagConstraints();
        gbc9.gridx = 2;
        gbc9.gridy = 2;
        gbc9.anchor = anchor;
        gbc9.fill = fill;
        gbc9.insets = leftInsets;

        GridBagConstraints gbc10 = new GridBagConstraints();
        gbc10.gridx = 3;
        gbc10.gridy = 2;
        gbc10.anchor = anchor;
        gbc10.fill = fill;
        gbc10.insets = rightInsets;

        GridBagConstraints gbc11 = new GridBagConstraints();
        gbc11.gridx = 0;
        gbc11.gridy = 3;
        gbc11.gridwidth = 4;
        gbc11.anchor = anchor;

        GridBagConstraints gbc12 = new GridBagConstraints();
        gbc12.gridx = 0;
        gbc12.gridy = 4;
        gbc12.gridwidth = 4;
        gbc12.anchor = anchor;
        gbc12.fill = GridBagConstraints.BOTH;

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {0.0, 1.0, 0.0, 1.0};
        gbl.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};

        setLayout(gbl);
        add(getWidthCB(), gbc);
        add(getWidthField(), gbc1);
        add(getWidthCombo(), gbc2);
        add(getAlignCB(), gbc3);
        add(getCellSpacingCB(), gbc5);
        add(getCellSpacingField(), gbc6);
        add(getBorderCB(), gbc7);
        add(getBorderField(), gbc8);
        add(getCellPaddingCB(), gbc9);
        add(getCellPaddingField(), gbc10);
        add(getAlignCombo(), gbc4);
        add(getBGPanel(), gbc11);
        add(getExpansionPanel(), gbc12);
    }

    /**
     * This method initializes widthCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getWidthCB() {
        if (widthCB == null) {
            widthCB = new JCheckBox();
            widthCB.setText(i18n.str("width"));
            widthCB.addItemListener(e -> {
                widthField.setEnabled(widthCB.isSelected());
                widthCombo.setEnabled(widthCB.isSelected());
            });
        }

        return widthCB;
    }

    /**
     * This method initializes widthField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getWidthField() {
        if (widthField == null) {
            widthField = new JSpinner(new SpinnerNumberModel(100, 1, 999, 1));
        }

        return widthField;
    }

    /**
     * This method initializes widthCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getWidthCombo() {
        if (widthCombo == null) {
            widthCombo = new JComboBox<>(MEASUREMENTS);
        }

        return widthCombo;
    }

    /**
     * This method initializes alignCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getAlignCB() {
        if (alignCB == null) {
            alignCB = new JCheckBox();
            alignCB.setText(i18n.str("align"));
            alignCB.addItemListener(e -> alignCombo.setEnabled(alignCB.isSelected()));
        }

        return alignCB;
    }

    /**
     * This method initializes cellSpacingCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getCellSpacingCB() {
        if (cellSpacingCB == null) {
            cellSpacingCB = new JCheckBox();
            cellSpacingCB.setText(i18n.str("cellspacing"));
            cellSpacingCB.addItemListener(e ->
                    cellSpacingField.setEnabled(cellSpacingCB.isSelected()));
        }

        return cellSpacingCB;
    }

    /**
     * This method initializes cellSpacingField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getCellSpacingField() {
        if (cellSpacingField == null) {
            cellSpacingField = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
        }

        return cellSpacingField;
    }

    /**
     * This method initializes borderCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBorderCB() {
        if (borderCB == null) {
            borderCB = new JCheckBox();
            borderCB.setText(i18n.str("border"));
            borderCB.addItemListener(e -> borderField.setEnabled(borderCB.isSelected()));
        }

        return borderCB;
    }

    /**
     * This method initializes borderField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getBorderField() {
        if (borderField == null) {
            borderField = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
        }

        return borderField;
    }

    /**
     * This method initializes cellPaddingCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getCellPaddingCB() {
        if (cellPaddingCB == null) {
            cellPaddingCB = new JCheckBox();
            cellPaddingCB.setText(i18n.str("cellpadding"));
            cellPaddingCB.addItemListener(e ->
                    cellPaddingField.setEnabled(cellPaddingCB.isSelected()));
        }

        return cellPaddingCB;
    }

    /**
     * This method initializes cellPaddingField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getCellPaddingField() {
        if (cellPaddingField == null) {
            cellPaddingField = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
        }

        return cellPaddingField;
    }

    /**
     * This method initializes alignCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getAlignCombo() {
        if (alignCombo == null) {
            alignCombo = new JComboBox<>(ALIGNMENTS);
        }

        return alignCombo;
    }

    /**
     * This method initializes tempPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getBGPanel() {
        if (bgPanel == null) {
            bgPanel = new BGColorPanel();
        }

        return bgPanel;
    }

    /**
     * This method initializes expansionPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getExpansionPanel() {
        if (expansionPanel == null) {
            expansionPanel = new JPanel();
        }

        return expansionPanel;
    }

}
