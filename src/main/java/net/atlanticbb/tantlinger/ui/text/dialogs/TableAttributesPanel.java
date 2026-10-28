/*
 * Created on Dec 22, 2005
 *
 */
package net.atlanticbb.tantlinger.ui.text.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class TableAttributesPanel extends HTMLAttributeEditorPanel {

    private static final long serialVersionUID = 1L;
    private static final String ALIGNMENTS[] = {"left", "center", "right"};
    private static final String MEASUREMENTS[] = {"percent", "pixels"};

    private JCheckBox widthCB = null;
    private JSpinner widthField = null;
    private JComboBox widthCombo = null;
    private JCheckBox alignCB = null;
    private JCheckBox cellSpacingCB = null;
    private JSpinner cellSpacingField = null;
    private JCheckBox borderCB = null;
    private JSpinner borderField = null;
    private JCheckBox cellPaddingCB = null;
    private JSpinner cellPaddingField = null;
    private JComboBox alignCombo = null;
    private BGColorPanel bgPanel = null;
    private JPanel expansionPanel = null;


    /**
     * This is the default constructor
     */
    public TableAttributesPanel() {
        this(new Hashtable());
        //super();
        //initialize();
        //Hashtable ht = new Hashtable();
        //ht.put("width", "100%");
        //ht.put("border", "1");        
        //setComponentStates(ht);
    }

    public TableAttributesPanel(Hashtable attribs) {
        super(attribs);
        initialize();
        updateComponentsFromAttribs();
    }

    public void updateComponentsFromAttribs() {
        if (attribs.containsKey("width"))
        {
            widthCB.setSelected(true);
            String w = attribs.get("width").toString();
            if (w.endsWith("%"))
                w = w.substring(0, w.length() - 1);
            else
                widthCombo.setSelectedIndex(1);
            widthField.setEnabled(true);

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

        if (attribs.containsKey("align"))
        {
            alignCB.setSelected(true);
            alignCombo.setEnabled(true);
            alignCombo.setSelectedItem(attribs.get("align"));
        } else {
            alignCB.setSelected(false);
            alignCombo.setEnabled(false);
        }

        if (attribs.containsKey("border"))
        {
            borderCB.setSelected(true);
            borderField.setEnabled(true);
            try {
                borderField.getModel().setValue(
                        new Integer(attribs.get("border").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            borderCB.setSelected(false);
            borderField.setEnabled(false);
        }

        if (attribs.containsKey("cellpadding"))
        {
            cellPaddingCB.setSelected(true);
            cellPaddingField.setEnabled(true);
            try {
                cellPaddingField.getModel().setValue(
                        new Integer(attribs.get("cellpadding").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cellPaddingCB.setSelected(false);
            cellPaddingField.setEnabled(false);
        }

        if (attribs.containsKey("cellspacing"))
        {
            cellSpacingCB.setSelected(true);
            cellSpacingField.setEnabled(true);
            try {
                cellSpacingField.getModel().setValue(
                        new Integer(attribs.get("cellspacing").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cellSpacingCB.setSelected(false);
            cellSpacingField.setEnabled(false);
        }

        if (attribs.containsKey("bgcolor"))
        {
            bgPanel.setSelected(true);
            bgPanel.setColor(attribs.get("bgcolor").toString());
        } else {
            bgPanel.setSelected(false);
        }
    }

    public void updateAttribsFromComponents() {
        if (widthCB.isSelected()) {
            String w = widthField.getModel().getValue().toString();
            if (widthCombo.getSelectedIndex() == 0)
                w += "%";
            attribs.put("width", w);
        } else
            attribs.remove("width");

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

    public void setComponentStates(Hashtable attribs) {
        if (attribs.containsKey("width"))
        {
            widthCB.setSelected(true);
            String w = attribs.get("width").toString();
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


        if (attribs.containsKey("align"))
        {
            alignCB.setSelected(true);
            alignCombo.setSelectedItem(attribs.get("align"));
        } else {
            alignCB.setSelected(false);
            alignCombo.setEnabled(false);
        }

        if (attribs.containsKey("border"))
        {
            borderCB.setSelected(true);
            try {
                borderField.getModel().setValue(
                        new Integer(attribs.get("border").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            borderCB.setSelected(false);
            borderField.setEnabled(false);
        }

        if (attribs.containsKey("cellpadding"))
        {
            cellPaddingCB.setSelected(true);
            try {
                cellPaddingField.getModel().setValue(
                        new Integer(attribs.get("cellpadding").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cellPaddingCB.setSelected(false);
            cellPaddingField.setEnabled(false);
        }

        if (attribs.containsKey("cellspacing"))
        {
            cellSpacingCB.setSelected(true);
            try {
                cellSpacingField.getModel().setValue(
                        new Integer(attribs.get("cellspacing").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cellSpacingCB.setSelected(false);
            cellSpacingField.setEnabled(false);
        }

        if (attribs.containsKey("bgcolor"))
        {
            bgPanel.setSelected(true);
            bgPanel.setColor(attribs.get("bgcolor").toString());
        } else {
            bgPanel.setSelected(false);
        }

    }


    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        GridBagConstraints gbc12 = new GridBagConstraints();
        gbc12.gridx = 0;
        gbc12.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc12.gridwidth = 4;
        gbc12.fill = GridBagConstraints.HORIZONTAL;
        gbc12.weightx = 0.0;
        gbc12.weighty = 1.0;
        gbc12.gridy = 4;
        GridBagConstraints gbc11 = new GridBagConstraints();
        gbc11.gridx = 0;
        gbc11.gridwidth = 4;
        gbc11.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc11.weighty = 0.0;
        gbc11.gridy = 3;
        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.fill = GridBagConstraints.NONE;
        gbc4.gridy = 1;
        gbc4.weightx = 0.0;
        gbc4.gridwidth = 2;
        gbc4.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc4.insets = new Insets(0, 0, 5, 15);
        gbc4.gridx = 1;
        GridBagConstraints gbc10 = new GridBagConstraints();
        gbc10.fill = GridBagConstraints.NONE;
        gbc10.gridy = 2;
        gbc10.weightx = 1.0;
        gbc10.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc10.insets = new Insets(0, 0, 10, 0);
        gbc10.gridx = 4;
        GridBagConstraints gbc9 = new GridBagConstraints();
        gbc9.gridx = 3;
        gbc9.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc9.insets = new Insets(0, 0, 10, 3);
        gbc9.gridy = 2;
        GridBagConstraints gbc8 = new GridBagConstraints();
        gbc8.fill = GridBagConstraints.NONE;
        gbc8.gridy = 2;
        gbc8.weightx = 0.0;
        gbc8.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc8.insets = new Insets(0, 0, 10, 15);
        gbc8.gridx = 1;
        GridBagConstraints gbc7 = new GridBagConstraints();
        gbc7.gridx = 0;
        gbc7.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc7.insets = new Insets(0, 0, 10, 3);
        gbc7.gridy = 2;
        GridBagConstraints gbc6 = new GridBagConstraints();
        gbc6.fill = GridBagConstraints.NONE;
        gbc6.gridy = 1;
        gbc6.weightx = 1.0;
        gbc6.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc6.insets = new Insets(0, 0, 2, 0);
        gbc6.gridx = 4;
        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.gridx = 3;
        gbc5.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc5.insets = new Insets(0, 0, 2, 3);
        gbc5.gridy = 1;
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 0;
        gbc3.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc3.insets = new Insets(0, 0, 2, 3);
        gbc3.gridy = 1;
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.NONE;
        gbc2.gridy = 0;
        gbc2.weightx = 0.0;
        gbc2.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc2.gridwidth = 2;
        gbc2.insets = new Insets(0, 0, 10, 0);
        gbc2.gridx = 2;
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.fill = GridBagConstraints.NONE;
        gbc1.gridy = 0;
        gbc1.weightx = 0.0;
        gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc1.insets = new Insets(0, 0, 10, 0);
        gbc1.gridx = 1;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(0, 0, 10, 3);
        gbc.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(320, 140);
        this.setPreferredSize(new Dimension(320, 140));

        this.add(getWidthCB(), gbc);
        this.add(getWidthField(), gbc1);
        this.add(getWidthCombo(), gbc2);
        this.add(getAlignCB(), gbc3);
        this.add(getCellSpacingCB(), gbc5);
        this.add(getCellSpacingField(), gbc6);
        this.add(getBorderCB(), gbc7);
        this.add(getBorderField(), gbc8);
        this.add(getCellPaddingCB(), gbc9);
        this.add(getCellPaddingField(), gbc10);
        this.add(getAlignCombo(), gbc4);
        this.add(getBGPanel(), gbc11);
        this.add(getExpansionPanel(), gbc12);

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
    private JComboBox getWidthCombo() {
        if (widthCombo == null) {
            widthCombo = new JComboBox(MEASUREMENTS);
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
            cellSpacingCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    cellSpacingField.setEnabled(cellSpacingCB.isSelected());
                }
            });
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
            borderCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    borderField.setEnabled(borderCB.isSelected());
                }
            });
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
            cellPaddingCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    cellPaddingField.setEnabled(cellPaddingCB.isSelected());
                }
            });
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
    private JComboBox getAlignCombo() {
        if (alignCombo == null) {
            alignCombo = new JComboBox(ALIGNMENTS);
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

}  //  @jve:decl-index=0:visual-constraint="16,10"
