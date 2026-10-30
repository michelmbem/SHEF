package net.atlanticbb.tantlinger.ui.text.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel for editing the size of a table cell
 *
 * @author Bob Tantlinger
 *
 */
public class SizeAttributesPanel extends HTMLAttributeEditorPanel {
    private static final long serialVersionUID = 1L;
    private static final String[] MEASUREMENTS = {"percent", "pixels"};

    private JCheckBox widthCB = null;
    private JCheckBox heightCB = null;
    private JSpinner widthField = null;
    private JSpinner heightField = null;
    private JComboBox<String> wMeasurementCombo = null;
    private JComboBox<String> hMeasurementCombo = null;

    public SizeAttributesPanel() {
        this(new HashMap<>());
    }

    public SizeAttributesPanel(Map<String, String> attr) {
        super(attr);
        initialize();
        updateComponentsFromAttribs();
    }

    public void updateComponentsFromAttribs() {
        if (attribs.containsKey("width")) {
            widthCB.setSelected(true);

            String w = attribs.get("width");
            if (w.endsWith("%"))
                w = w.substring(0, w.length() - 1);
            else
                wMeasurementCombo.setSelectedIndex(1);

            try {
                widthField.getModel().setValue(w);
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

            String h = attribs.get("height");
            if (h.endsWith("%"))
                h = h.substring(0, h.length() - 1);
            else
                hMeasurementCombo.setSelectedIndex(1);

            try {
                heightField.getModel().setValue(h);
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
            if (wMeasurementCombo.getSelectedIndex() == 0) w += "%";
            attribs.put("width", w);
        } else
            attribs.remove("width");

        if (heightCB.isSelected()) {
            String h = heightField.getModel().getValue().toString();
            if (hMeasurementCombo.getSelectedIndex() == 0) h += "%";
            attribs.put("height", h);
        } else
            attribs.remove("height");
    }

    public void setComponentStates(Map<String, String> attribs) {
        if (attribs.containsKey("width")) {
            widthCB.setSelected(true);

            String w = attribs.get("width");
            if (w.endsWith("%"))
                w = w.substring(0, w.length() - 1);
            else
                wMeasurementCombo.setSelectedIndex(1);

            try {
                widthField.getModel().setValue(w);
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

        if (attribs.containsKey("height")) {
            heightCB.setSelected(true);

            String h = attribs.get("height");
            if (h.endsWith("%"))
                h = h.substring(0, h.length() - 1);
            else
                hMeasurementCombo.setSelectedIndex(1);

            try {
                heightField.getModel().setValue(h);
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
        gbc1.insets = leftInsets;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 2;
        gbc2.gridy = 0;
        gbc2.anchor = anchor;
        gbc2.fill = fill;
        gbc2.insets = rightInsets;

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 0;
        gbc3.gridy = 1;
        gbc3.anchor = anchor;
        gbc3.fill = fill;
        gbc3.insets = leftInsets;

        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.gridx = gbc4.gridy = 1;
        gbc4.anchor = anchor;
        gbc4.fill = fill;
        gbc4.insets = leftInsets;

        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.gridx = 2;
        gbc5.gridy = 1;
        gbc5.anchor = anchor;
        gbc5.fill = fill;
        gbc5.insets = rightInsets;

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {0.0, 1.0, 1.0};
        gbl.rowWeights = new double[] {0.0, 0.0};

        setLayout(gbl);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(null, i18n.str("size")),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        add(getWidthCB(), gbc);
        add(getWidthField(), gbc1);
        add(getWMeasurementCombo(), gbc2);
        add(getHeightCB(), gbc3);
        add(getHeightField(), gbc4);
        add(getHMeasurementCombo(), gbc5);
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
                wMeasurementCombo.setEnabled(widthCB.isSelected());
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
            heightCB.addItemListener(e -> {
                heightField.setEnabled(heightCB.isSelected());
                hMeasurementCombo.setEnabled(heightCB.isSelected());
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
    private JComboBox<String> getWMeasurementCombo() {
        if (wMeasurementCombo == null) {
            wMeasurementCombo = new JComboBox<>(MEASUREMENTS);
        }

        return wMeasurementCombo;
    }

    /**
     * This method initializes hMeasurementCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getHMeasurementCombo() {
        if (hMeasurementCombo == null) {
            hMeasurementCombo = new JComboBox<>(MEASUREMENTS);
        }

        return hMeasurementCombo;
    }

}
