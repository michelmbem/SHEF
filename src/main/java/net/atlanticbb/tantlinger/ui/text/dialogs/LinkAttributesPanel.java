package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.ui.text.TextEditPopupManager;

import javax.swing.*;
import java.awt.*;

public class LinkAttributesPanel extends HTMLAttributeEditorPanel {
    private static final long serialVersionUID = 1L;
    private static final String NEW_WIN = "New Window";
    private static final String SAME_WIN = "Same Window";
    private static final String SAME_FRAME = "Same Frame";
    private static final String TARGET_LABELS[] =
            {NEW_WIN, SAME_WIN, SAME_FRAME};
    private static final String TARGETS[] = {"_blank", "_top", "_self"};
    private JCheckBox nameCB = null;
    private JCheckBox titleCB = null;
    private JCheckBox openInCB = null;
    private JTextField nameField = null;
    private JTextField titleField = null;
    private JComboBox openInCombo = null;
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
        GridBagConstraints gbc6 = new GridBagConstraints();
        gbc6.gridx = 0;
        gbc6.fill = GridBagConstraints.HORIZONTAL;
        gbc6.weighty = 1.0;
        gbc6.weightx = 0.0;
        gbc6.anchor = GridBagConstraints.NORTHWEST;
        gbc6.gridwidth = 2;
        gbc6.gridy = 3;
        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.fill = GridBagConstraints.NONE;
        gbc5.gridy = 2;
        gbc5.weightx = 1.0;
        gbc5.insets = new Insets(0, 0, 5, 0);
        gbc5.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc5.gridx = 1;
        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.fill = GridBagConstraints.HORIZONTAL;
        gbc4.gridy = 1;
        gbc4.weightx = 1.0;
        gbc4.insets = new Insets(0, 0, 5, 0);
        gbc4.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc4.gridx = 1;
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.gridy = 0;
        gbc3.weightx = 1.0;
        gbc3.insets = new Insets(0, 0, 5, 0);
        gbc3.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc3.gridx = 1;
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc2.insets = new Insets(0, 0, 5, 5);
        gbc2.gridy = 2;
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc1.insets = new Insets(0, 0, 5, 5);
        gbc1.gridy = 1;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(320, 118));
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, i18n.str("attributes"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        this.add(getNameCB(), gbc);
        this.add(getTitleCB(), gbc1);
        this.add(getOpenInCB(), gbc2);
        this.add(getNameField(), gbc3);
        this.add(getTitleField(), gbc4);
        this.add(getOpenInCombo(), gbc5);
        this.add(getSpacerPanel(), gbc6);

        TextEditPopupManager.getInstance().registerJTextComponent(nameField);
        TextEditPopupManager.getInstance().registerJTextComponent(titleField);

    }

    public void updateComponentsFromAttribs() {
        if (attribs.containsKey("name"))
        {
            nameCB.setSelected(true);
            nameField.setEditable(true);
            nameField.setText(attribs.get("name").toString());
        } else {
            nameCB.setSelected(false);
            nameField.setEditable(false);
        }

        if (attribs.containsKey("title"))
        {
            titleCB.setSelected(true);
            titleField.setEditable(true);
            titleField.setText(attribs.get("title").toString());
        } else {
            titleCB.setSelected(false);
            titleField.setEditable(false);
        }

        if (attribs.containsKey("target"))
        {
            openInCB.setSelected(true);
            String val = attribs.get("target").toString();
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
            nameCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    nameField.setEditable(nameCB.isSelected());
                }
            });
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
            titleCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    titleField.setEditable(titleCB.isSelected());
                }
            });
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
            openInCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    openInCombo.setEnabled(openInCB.isSelected());
                }
            });
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
    private JComboBox getOpenInCombo() {
        if (openInCombo == null) {
            openInCombo = new JComboBox(TARGET_LABELS);
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
