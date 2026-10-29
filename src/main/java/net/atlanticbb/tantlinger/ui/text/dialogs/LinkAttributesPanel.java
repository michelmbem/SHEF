package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.ui.text.TextEditPopupManager;

import javax.swing.*;
import java.awt.*;

public class LinkAttributesPanel extends HTMLAttributeEditorPanel {
    private static final long serialVersionUID = 1L;
    private static final String NEW_WIN = "New Window";
    private static final String SAME_WIN = "Same Window";
    private static final String SAME_FRAME = "Same Frame";
    private static final String[] TARGET_LABELS = {NEW_WIN, SAME_WIN, SAME_FRAME};
    private static final String[] TARGETS = {"_blank", "_top", "_self"};

    private JCheckBox nameCB = null;
    private JCheckBox titleCB = null;
    private JCheckBox openInCB = null;
    private JTextField nameField = null;
    private JTextField titleField = null;
    private JComboBox<String> openInCombo = null;
    private JPanel spacerPanel = null;

    /**
     * This method initializes
     *
     */
    public LinkAttributesPanel() {
        super();
        initialize();
        updateComponentsFromAttribs();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        nameCB.setEnabled(b);
        titleCB.setEnabled(b);
        openInCB.setEnabled(b);
        nameField.setEditable(nameCB.isSelected() && b);
        titleField.setEditable(titleCB.isSelected() && b);
        openInCombo.setEnabled(openInCB.isSelected() && b);
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
        gbc3.gridx = 1;
        gbc3.gridy = 1;
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

        GridBagConstraints gbc6 = new GridBagConstraints();
        gbc6.gridx = 0;
        gbc6.gridy = 3;
        gbc6.gridwidth = 2;
        gbc6.fill = GridBagConstraints.BOTH;

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {0.0, 1.0};
        gbl.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};

        setLayout(gbl);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(null, i18n.str("attributes")),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(getNameCB(), gbc);
        add(getTitleCB(), gbc2);
        add(getOpenInCB(), gbc4);
        add(getNameField(), gbc1);
        add(getTitleField(), gbc3);
        add(getOpenInCombo(), gbc5);
        add(getSpacerPanel(), gbc6);

        TextEditPopupManager.getInstance().registerJTextComponent(nameField);
        TextEditPopupManager.getInstance().registerJTextComponent(titleField);
    }

    public void updateComponentsFromAttribs() {
        if (attribs.containsKey("name")) {
            nameCB.setSelected(true);
            nameField.setEditable(true);
            nameField.setText(attribs.get("name"));
        } else {
            nameCB.setSelected(false);
            nameField.setEditable(false);
        }

        if (attribs.containsKey("title")) {
            titleCB.setSelected(true);
            titleField.setEditable(true);
            titleField.setText(attribs.get("title"));
        } else {
            titleCB.setSelected(false);
            titleField.setEditable(false);
        }

        if (attribs.containsKey("target")) {
            openInCB.setSelected(true);
            String val = attribs.get("target");
            openInCombo.setEnabled(true);

            for (int i = 0; i < TARGETS.length; i++) {
                if (val.equals(TARGETS[i])) {
                    openInCombo.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            openInCB.setSelected(false);
            openInCombo.setEnabled(false);
        }
    }

    public void updateAttribsFromComponents() {
        if (openInCB.isSelected())
            attribs.put("target", TARGETS[openInCombo.getSelectedIndex()]);
        else
            attribs.remove("target");

        if (titleCB.isSelected())
            attribs.put("title", titleField.getText());
        else
            attribs.remove("title");

        if (nameCB.isSelected())
            attribs.put("name", nameField.getText());
        else
            attribs.remove("name");
    }

    /**
     * This method initializes nameCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getNameCB() {
        if (nameCB == null) {
            nameCB = new JCheckBox();
            nameCB.setText(i18n.str("name"));
            nameCB.addItemListener(e -> nameField.setEditable(nameCB.isSelected()));
        }

        return nameCB;
    }

    /**
     * This method initializes titleCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getTitleCB() {
        if (titleCB == null) {
            titleCB = new JCheckBox();
            titleCB.setText(i18n.str("title"));
            titleCB.addItemListener(e -> titleField.setEditable(titleCB.isSelected()));
        }

        return titleCB;
    }

    /**
     * This method initializes openInCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getOpenInCB() {
        if (openInCB == null) {
            openInCB = new JCheckBox();
            openInCB.setText(i18n.str("open_in"));
            openInCB.addItemListener(e -> openInCombo.setEnabled(openInCB.isSelected()));
        }

        return openInCB;
    }

    /**
     * This method initializes nameField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getNameField() {
        if (nameField == null) {
            nameField = new JTextField();
        }

        return nameField;
    }

    /**
     * This method initializes titleField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTitleField() {
        if (titleField == null) {
            titleField = new JTextField();
        }

        return titleField;
    }

    /**
     * This method initializes openInCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getOpenInCombo() {
        if (openInCombo == null) {
            openInCombo = new JComboBox<>(TARGET_LABELS);
        }

        return openInCombo;
    }

    /**
     * This method initializes spacerPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getSpacerPanel() {
        if (spacerPanel == null) {
            spacerPanel = new JPanel();
        }

        return spacerPanel;
    }

}
