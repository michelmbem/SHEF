package net.atlanticbb.tantlinger.ui.text.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RowAttributesPanel extends HTMLAttributeEditorPanel {
    private static final long serialVersionUID = 1L;

    private AlignmentAttributesPanel alignPanel = null;
    private BGColorPanel bgColorPanel = null;
    private JPanel expansionPanel = null;

    /**
     * This is the default constructor
     */
    public RowAttributesPanel() {
        this(new HashMap<>());
    }

    public RowAttributesPanel(Map<String, String> attr) {
        super(attr);
        initialize();
        alignPanel.setAttributes(getAttributes());
        updateComponentsFromAttribs();
    }

    public void updateComponentsFromAttribs() {
        if (attribs.containsKey("bgcolor")) {
            bgColorPanel.setSelected(true);
            bgColorPanel.setColor(attribs.get("bgcolor"));
        }

        alignPanel.updateComponentsFromAttribs();
    }

    public void updateAttribsFromComponents() {
        if (bgColorPanel.isSelected())
            attribs.put("bgcolor", bgColorPanel.getColor());
        else
            attribs.remove("bgcolor");

        alignPanel.updateAttribsFromComponents();
    }

    public void setComponentStates(Map<String, String> attribs) {
        if (attribs.containsKey("bgcolor")) {
            bgColorPanel.setSelected(true);
            bgColorPanel.setColor(attribs.get("bgcolor"));
        }

        alignPanel.setComponentStates(attribs);
    }

    @Override
    public void setAttributes(Map<String, String> attr) {
        alignPanel.setAttributes(attr);
        super.setAttributes(attr);
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        final int anchor = GridBagConstraints.BASELINE_LEADING;
        final int fill = GridBagConstraints.HORIZONTAL;
        final Insets insets = new Insets(0, 0, 5, 0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.insets = insets;

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 1;
        gbc1.anchor = anchor;
        gbc1.fill = fill;
        gbc1.insets = insets;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 2;
        gbc2.fill = GridBagConstraints.BOTH;

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {1.0};
        gbl.rowWeights = new double[] {0.0, 0.0, 1.0};

        setLayout(gbl);
        add(getAlignPanel(), gbc);
        add(getBgColorPanel(), gbc1);
        add(getExpansionPanel(), gbc2);
    }

    /**
     * This method initializes alignPanel
     *
     * @return javax.swing.JPanel
     */
    private AlignmentAttributesPanel getAlignPanel() {
        if (alignPanel == null) {
            alignPanel = new AlignmentAttributesPanel();
        }

        return alignPanel;
    }

    /**
     * This method initializes bgColorPanel
     *
     * @return javax.swing.JPanel
     */
    private BGColorPanel getBgColorPanel() {
        if (bgColorPanel == null) {
            bgColorPanel = new BGColorPanel();
        }

        return bgColorPanel;
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
