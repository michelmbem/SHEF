/*
 * Created on Jan 18, 2006
 *
 */
package net.atlanticbb.tantlinger.ui.text.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ListAttributesPanel extends HTMLAttributeEditorPanel {
    public static final int UL_LIST = 0;
    public static final int OL_LIST = 1;
    private static final long serialVersionUID = 1L;
    private static final String UL = i18n.str("unordered_list");
    private static final String OL = i18n.str("ordered_list");
    private static final String[] LIST_TYPES = {UL, OL};

    private static final String[] OL_TYPES = {"1", "a", "A", "i", "I"};
    private static final String[] OL_TYPE_LABELS = {
            "1, 2, 3, ...",
            "a, b, c, ...",
            "A, B, C, ...",
            "i, ii, iii, ...",
            "I, II, III, ..."
    };

    private static final String[] UL_TYPES = {"disc", "square", "circle"};
    private static final String[] UL_TYPE_LABELS = {
            i18n.str("solid_circle"),
            i18n.str("solid_square"),
            i18n.str("open_circle")
    };

    private JLabel typeLabel = null;
    private JComboBox<String> typeCombo = null;
    private JComboBox<String> styleCombo = null;
    private JSpinner startAtField = null;
    private JCheckBox styleCB = null;
    private JCheckBox startAtCB = null;

    /**
     * This method initializes
     *
     */
    public ListAttributesPanel() {
        this(new HashMap<>());
    }

    public ListAttributesPanel(Map<String, String> at) {
        super();
        initialize();
        setAttributes(at);
        updateComponentsFromAttribs();
    }

    public int getListType() {
        return typeCombo.getSelectedIndex();
    }

    public void setListType(int t) {
        typeCombo.setSelectedIndex(t);
        updateForType();
    }

    private void updateForType() {
        styleCombo.removeAllItems();
        if (typeCombo.getSelectedItem().equals(UL)) {
            for (int i = 0; i < UL_TYPE_LABELS.length; i++)
                styleCombo.addItem(UL_TYPE_LABELS[i]);
            startAtCB.setEnabled(false);
            startAtField.setEnabled(false);
        } else {
            for (int i = 0; i < OL_TYPE_LABELS.length; i++)
                styleCombo.addItem(OL_TYPE_LABELS[i]);
            startAtCB.setEnabled(true);
            startAtField.setEnabled(startAtCB.isSelected());
        }
    }

    private int getIndexForStyle(String s) {
        if (typeCombo.getSelectedIndex() == UL_LIST) {
            for (int i = 0; i < UL_TYPES.length; i++)
                if (UL_TYPES[i].equals(s))
                    return i;
        } else {
            for (int i = 0; i < OL_TYPES.length; i++)
                if (OL_TYPES[i].equals(s))
                    return i;
        }

        return 0;
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc2.gridy = 2;

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc1.insets = new Insets(0, 0, 5, 5);
        gbc1.gridy = 1;

        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.fill = GridBagConstraints.NONE;
        gbc5.gridy = 2;
        gbc5.weightx = 1.0;
        gbc5.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc5.gridx = 1;

        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.fill = GridBagConstraints.HORIZONTAL;
        gbc4.gridy = 1;
        gbc4.weightx = 1.0;
        gbc4.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc4.insets = new Insets(0, 0, 5, 0);
        gbc4.gridx = 1;

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.gridy = 0;
        gbc3.weightx = 1.0;
        gbc3.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc3.insets = new Insets(0, 0, 5, 0);
        gbc3.gridx = 1;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridy = 0;

        typeLabel = new JLabel();
        typeLabel.setText(i18n.str("list_type"));
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(234, 159));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.add(typeLabel, gbc);
        this.add(getTypeCombo(), gbc3);
        this.add(getStyleCombo(), gbc4);
        this.add(getStartAtField(), gbc5);
        this.add(getStyleCB(), gbc1);
        this.add(getStartAtCB(), gbc2);
    }

    public void updateComponentsFromAttribs() {
        if (attribs.containsKey("type")) {
            styleCB.setSelected(true);
            styleCombo.setEnabled(true);
            int i = getIndexForStyle(attribs.get("type").toString());
            styleCombo.setSelectedIndex(i);
        } else {
            styleCB.setSelected(false);
            styleCombo.setEnabled(false);
        }

        if (attribs.containsKey("start")) {
            startAtCB.setSelected(true);
            startAtField.setEnabled(true);
            try {
                int n = Integer.parseInt(attribs.get("start").toString());
                startAtField.getModel().setValue(new Integer(n));
            } catch (Exception ignored) {
            }
        } else {
            startAtCB.setSelected(false);
            startAtField.setEnabled(false);
        }
    }

    public void updateAttribsFromComponents() {
        if (styleCB.isSelected()) {
            if (typeCombo.getSelectedIndex() == UL_LIST)
                attribs.put("type", UL_TYPES[styleCombo.getSelectedIndex()]);
            else
                attribs.put("type", OL_TYPES[styleCombo.getSelectedIndex()]);
        } else
            attribs.remove("type");

        if (startAtCB.isSelected())
            attribs.put("start", startAtField.getModel().getValue().toString());
        else
            attribs.remove("start");
    }

    /**
     * This method initializes typeCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getTypeCombo() {
        if (typeCombo == null) {
            typeCombo = new JComboBox<>(LIST_TYPES);
            typeCombo.addItemListener(e -> updateForType());
        }

        return typeCombo;
    }


    /**
     * This method initializes styleCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getStyleCombo() {
        if (styleCombo == null) {
            styleCombo = new JComboBox<>(UL_TYPE_LABELS);
        }

        return styleCombo;
    }

    /**
     * This method initializes startAtField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getStartAtField() {
        if (startAtField == null) {
            startAtField = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        }
        
        return startAtField;
    }

    /**
     * This method initializes styleCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getStyleCB() {
        if (styleCB == null) {
            styleCB = new JCheckBox();
            styleCB.setText(i18n.str("style"));
            styleCB.addItemListener(e -> styleCombo.setEnabled(styleCB.isSelected()));
        }

        return styleCB;
    }

    /**
     * This method initializes startAtCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getStartAtCB() {
        if (startAtCB == null) {
            startAtCB = new JCheckBox();
            startAtCB.setText(i18n.str("start_at"));
            startAtCB.addItemListener(e -> startAtField.setEnabled(startAtCB.isSelected()));
        }

        return startAtCB;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
