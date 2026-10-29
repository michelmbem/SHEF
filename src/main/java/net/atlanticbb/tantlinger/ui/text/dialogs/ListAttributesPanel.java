package net.atlanticbb.tantlinger.ui.text.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        if (Objects.equals(typeCombo.getSelectedItem(), UL)) {
            for (String ulTypeLabel : UL_TYPE_LABELS) {
                styleCombo.addItem(ulTypeLabel);
            }
            startAtCB.setEnabled(false);
            startAtField.setEnabled(false);
        } else {
            for (String olTypeLabel : OL_TYPE_LABELS) {
                styleCombo.addItem(olTypeLabel);
            }
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
        final int anchor = GridBagConstraints.BASELINE_LEADING;
        final int fill = GridBagConstraints.HORIZONTAL;
        final Insets leftInsets = new Insets(0, 0, 5, 5);
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
        gbc1.insets = rightInsets;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        gbc2.anchor = anchor;
        gbc2.fill = fill;
        gbc2.insets = leftInsets;

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = gbc3.gridy = 1;
        gbc3.anchor = anchor;
        gbc3.fill = fill;
        gbc3.insets = rightInsets;

        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.gridx = 0;
        gbc4.gridy = 2;
        gbc4.anchor = anchor;
        gbc4.fill = fill;
        gbc4.insets = leftInsets;

        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.gridx = 1;
        gbc5.gridy = 2;
        gbc5.anchor = anchor;
        gbc5.fill = fill;
        gbc5.insets = rightInsets;

        typeLabel = new JLabel(i18n.str("list_type"));

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {0.0, 1.0};
        gbl.rowWeights = new double[] {0.0, 0.0, 0.0};

        setLayout(gbl);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(typeLabel, gbc);
        add(getTypeCombo(), gbc1);
        add(getStyleCB(), gbc2);
        add(getStyleCombo(), gbc3);
        add(getStartAtCB(), gbc4);
        add(getStartAtField(), gbc5);
    }

    public void updateComponentsFromAttribs() {
        if (attribs.containsKey("type")) {
            styleCB.setSelected(true);
            styleCombo.setEnabled(true);
            styleCombo.setSelectedIndex(
                    getIndexForStyle(attribs.get("type")));
        } else {
            styleCB.setSelected(false);
            styleCombo.setEnabled(false);
        }

        if (attribs.containsKey("start")) {
            startAtCB.setSelected(true);
            startAtField.setEnabled(true);

            try {
                int n = Integer.parseInt(attribs.get("start"));
                startAtField.getModel().setValue(n);
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

}
