package net.atlanticbb.tantlinger.ui.text.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A panel for editing table alignment attributes
 *
 * @author Bob Tantlinger
 *
 */
public class AlignmentAttributesPanel extends HTMLAttributeEditorPanel {
    private static final long serialVersionUID = 1L;
    private static final String[] VERT_ALIGNMENTS = {"top", "middle", "bottom"};
    private static final String[] HORIZ_ALIGNMENTS = {
            "left", "center", "right", "justify"
    };

    private JCheckBox vAlignCB = null;
    private JCheckBox hAlignCB = null;
    private JComboBox<String> vLocCombo = null;
    private JComboBox<String> hLocCombo = null;

    public AlignmentAttributesPanel() {
        this(new HashMap<>());
    }

    public AlignmentAttributesPanel(Map<String, String> attr) {
        super(attr);
        initialize();
        updateComponentsFromAttribs();
    }

    public void updateComponentsFromAttribs() {
        setComponentStates(attribs);
    }

    public void updateAttribsFromComponents() {
        if (vAlignCB.isSelected())
            attribs.put("valign", vLocCombo.getSelectedItem().toString());
        else
            attribs.remove("valign");

        if (hAlignCB.isSelected())
            attribs.put("align", hLocCombo.getSelectedItem().toString());
        else
            attribs.remove("align");
    }

    public void setComponentStates(Map<String, String> attribs) {
        if (attribs.containsKey("align"))
        {
            hAlignCB.setSelected(true);
            hLocCombo.setEnabled(true);
            hLocCombo.setSelectedItem(attribs.get("align"));
        } else {
            hAlignCB.setSelected(false);
            hLocCombo.setEnabled(false);
        }

        if (attribs.containsKey("valign"))
        {
            vAlignCB.setSelected(true);
            vLocCombo.setEnabled(true);
            vLocCombo.setSelectedItem(attribs.get("valign"));
        } else {
            vAlignCB.setSelected(false);
            vLocCombo.setEnabled(false);
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
        gbc1.gridx = 0;
        gbc1.gridy = 1;
        gbc1.anchor = anchor;
        gbc1.fill = fill;
        gbc1.insets = leftInsets;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        gbc2.anchor = anchor;
        gbc2.fill = fill;
        gbc2.insets = rightInsets;

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = gbc3.gridy = 1;
        gbc3.anchor = anchor;
        gbc3.fill = fill;
        gbc3.insets = rightInsets;

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {0.0, 1.0};
        gbl.rowWeights = new double[] {0.0, 0.0};

        setLayout(gbl);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(i18n.str("content_alignment")),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        add(getVAlignCB(), gbc);
        add(getHAlignCB(), gbc1);
        add(getVLocCombo(), gbc2);
        add(getHLocCombo(), gbc3);
    }

    /**
     * This method initializes vAlignCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getVAlignCB() {
        if (vAlignCB == null) {
            vAlignCB = new JCheckBox();
            vAlignCB.setText(i18n.str("vertical"));
            vAlignCB.addActionListener(e -> vLocCombo.setEnabled(vAlignCB.isSelected()));
        }

        return vAlignCB;
    }

    /**
     * This method initializes hAlignCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getHAlignCB() {
        if (hAlignCB == null) {
            hAlignCB = new JCheckBox();
            hAlignCB.setText(i18n.str("horizontal"));
            hAlignCB.addActionListener(e -> hLocCombo.setEnabled(hAlignCB.isSelected()));
        }

        return hAlignCB;
    }

    /**
     * This method initializes vLocCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getVLocCombo() {
        if (vLocCombo == null) {
            vLocCombo = new JComboBox<>(VERT_ALIGNMENTS);
        }

        return vLocCombo;
    }

    /**
     * This method initializes hLocCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getHLocCombo() {
        if (hLocCombo == null) {
            hLocCombo = new JComboBox<>(HORIZ_ALIGNMENTS);

        }

        return hLocCombo;
    }

}
