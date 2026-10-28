/*
 * Created on Dec 21, 2005
 *
 */
package net.atlanticbb.tantlinger.ui.text.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;


/**
 * Panel for editing the size of a table cell
 *
 * @author Bob Tantlinger
 *
 */
public class SizeAttributesPanel extends HTMLAttributeEditorPanel {
    private static final long serialVersionUID = 1L;

    private static final String MEASUREMENTS[] = {"percent", "pixels"};

    private JCheckBox widthCB = null;
    private JCheckBox heightCB = null;
    private JSpinner widthField = null;
    private JSpinner heightField = null;
    private JComboBox wMeasurementCombo = null;
    private JComboBox hMeasurementCombo = null;

    public SizeAttributesPanel() {
        this(new Hashtable());
    }

    public SizeAttributesPanel(Hashtable attr) {
        super(attr);
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
                wMeasurementCombo.setSelectedIndex(1);
            try {
                widthField.getModel().setValue(new Integer(w));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            wMeasurementCombo.setEnabled(true);
            widthField.setEnabled(true);
        } else {
            widthCB.setSelected(false);
            widthField.setEnabled(false);
            wMeasurementCombo.setEnabled(false);
        }

        if (attribs.containsKey("height"))
        {
            heightCB.setSelected(true);
            String h = attribs.get("height").toString();
            if (h.endsWith("%"))
                h = h.substring(0, h.length() - 1);
            else
                hMeasurementCombo.setSelectedIndex(1);
            try {
                heightField.getModel().setValue(new Integer(h));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            hMeasurementCombo.setEnabled(true);
            heightField.setEnabled(true);
        } else {
            heightCB.setSelected(false);
            heightField.setEnabled(false);
            hMeasurementCombo.setEnabled(false);
        }
    }


    public void updateAttribsFromComponents() {
        if (widthCB.isSelected()) {
            String w = widthField.getModel().getValue().toString();
            if (wMeasurementCombo.getSelectedIndex() == 0)
                w += "%";
            attribs.put("width", w);
        } else
            attribs.remove("width");

        if (heightCB.isSelected()) {
            String h = heightField.getModel().getValue().toString();
            if (hMeasurementCombo.getSelectedIndex() == 0)
                h += "%";
            attribs.put("height", h);
        } else
            attribs.remove("height");
    }


    public void setComponentStates(Hashtable attribs) {
        if (attribs.containsKey("width"))
        {
            widthCB.setSelected(true);
            String w = attribs.get("width").toString();
            if (w.endsWith("%"))
                w = w.substring(0, w.length() - 1);
            else
                wMeasurementCombo.setSelectedIndex(1);
            try {
                widthField.getModel().setValue(new Integer(w));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            wMeasurementCombo.setEnabled(true);
            widthField.setEnabled(true);
        } else {
            widthCB.setSelected(false);
            widthField.setEnabled(false);
            wMeasurementCombo.setEnabled(false);
        }

        if (attribs.containsKey("height"))
        {
            heightCB.setSelected(true);
            String h = attribs.get("height").toString();
            if (h.endsWith("%"))
                h = h.substring(0, h.length() - 1);
            else
                hMeasurementCombo.setSelectedIndex(1);
            try {
                heightField.getModel().setValue(new Integer(h));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            hMeasurementCombo.setEnabled(true);
            heightField.setEnabled(true);
        } else {
            heightCB.setSelected(false);
            heightField.setEnabled(false);
            hMeasurementCombo.setEnabled(false);
        }
    }


    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.fill = GridBagConstraints.NONE;
        gbc5.gridy = 1;
        gbc5.weightx = 0.0;
        gbc5.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc5.ipadx = 0;
        gbc5.gridx = 2;
        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.fill = GridBagConstraints.NONE;
        gbc4.gridy = 0;
        gbc4.weightx = 1.0;
        gbc4.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc4.insets = new Insets(0, 0, 5, 0);
        gbc4.gridx = 2;
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.fill = GridBagConstraints.NONE;
        gbc3.gridy = 1;
        gbc3.weightx = 0.0;
        gbc3.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc3.insets = new Insets(0, 0, 0, 5);
        gbc3.ipadx = 0;
        gbc3.gridx = 1;
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.NONE;
        gbc2.gridy = 0;
        gbc2.weightx = 0.0;
        gbc2.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc2.insets = new Insets(0, 0, 0, 5);
        gbc2.ipadx = 0;
        gbc2.gridx = 1;
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc1.insets = new Insets(0, 0, 0, 5);
        gbc1.gridy = 1;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridy = 0;

        this.setLayout(new GridBagLayout());
        this.setSize(215, 95);
        this.setPreferredSize(new Dimension(215, 95));
        this.setMaximumSize(getPreferredSize());
        this.setMinimumSize(getPreferredSize());
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, i18n.str("size"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null), BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        this.add(getWidthCB(), gbc);
        this.add(getHeightCB(), gbc1);
        this.add(getWidthField(), gbc2);
        this.add(getHeightField(), gbc3);
        this.add(getWMeasurementCombo(), gbc4);
        this.add(getHMeasurementCombo(), gbc5);
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

            widthCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    widthField.setEnabled(widthCB.isSelected());
                    wMeasurementCombo.setEnabled(widthCB.isSelected());
                }
            });
        }
        return widthCB;
    }

    /**
     * This method initializes heightCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getHeightCB() {
        if (heightCB == null) {
            heightCB = new JCheckBox();
            heightCB.setText(i18n.str("height"));

            heightCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    heightField.setEnabled(heightCB.isSelected());
                    hMeasurementCombo.setEnabled(heightCB.isSelected());
                }
            });
        }
        return heightCB;
    }

    /**
     * This method initializes widthField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getWidthField() {
        if (widthField == null) {

            widthField = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        }

        return widthField;
    }

    /**
     * This method initializes heightField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getHeightField() {
        if (heightField == null) {

            heightField = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        }

        return heightField;
    }

    /**
     * This method initializes wMeasurementCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getWMeasurementCombo() {
        if (wMeasurementCombo == null) {
            wMeasurementCombo = new JComboBox(MEASUREMENTS);

        }
        return wMeasurementCombo;
    }

    /**
     * This method initializes hMeasurementCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getHMeasurementCombo() {
        if (hMeasurementCombo == null) {
            hMeasurementCombo = new JComboBox(MEASUREMENTS);

        }
        return hMeasurementCombo;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
