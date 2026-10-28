/*
 * Created on Dec 23, 2005
 *
 */
package net.atlanticbb.tantlinger.ui.text.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CellAttributesPanel extends HTMLAttributeEditorPanel {

    private static final long serialVersionUID = 1L;
    
    private AlignmentAttributesPanel alignPanel = null;
    private SizeAttributesPanel sizePanel = null;
    private JCheckBox dontWrapCB = null;
    private BGColorPanel bgColorPanel = null;
    private JPanel spanPanel = null;
    private JCheckBox colSpanCB = null;
    private JCheckBox rowSpanCB = null;
    private JSpinner colSpanField = null;
    private JSpinner rowSpanField = null;
    private JPanel expansionPanel = null;

    /**
     * This is the default constructor
     */
    public CellAttributesPanel() {
        this(new HashMap<>());
    }

    public CellAttributesPanel(Map<String, String> attr) {
        super(attr);
        initialize();
        alignPanel.setAttributes(getAttributes());
        sizePanel.setAttributes(getAttributes());
        updateComponentsFromAttribs();
    }

    public void updateComponentsFromAttribs() {
        alignPanel.updateComponentsFromAttribs();
        sizePanel.updateComponentsFromAttribs();

        if (attribs.containsKey("colspan")) {
            colSpanCB.setSelected(true);
            colSpanField.setEnabled(true);

            try {
                colSpanField.getModel().setValue(
                        Integer.valueOf(attribs.get("colspan").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            colSpanCB.setSelected(false);
            colSpanField.setEnabled(false);
        }

        if (attribs.containsKey("rowspan")) {
            rowSpanCB.setSelected(true);
            rowSpanField.setEnabled(true);
            
            try {
                rowSpanField.getModel().setValue(
                        Integer.valueOf(attribs.get("rowspan").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            rowSpanCB.setSelected(false);
            rowSpanField.setEnabled(false);
        }

        if (attribs.containsKey("bgcolor")) {
            bgColorPanel.setSelected(true);
            bgColorPanel.setColor(attribs.get("bgcolor").toString());
        } else {
            bgColorPanel.setSelected(false);
        }

        dontWrapCB.setSelected(attribs.containsKey("nowrap"));
    }


    public void updateAttribsFromComponents() {
        alignPanel.updateAttribsFromComponents();
        sizePanel.updateAttribsFromComponents();

        if (dontWrapCB.isSelected())
            attribs.put("nowrap", "nowrap");
        else
            attribs.remove("nowrap");

        if (bgColorPanel.isSelected())
            attribs.put("bgcolor", bgColorPanel.getColor());
        else
            attribs.remove("bgcolor");

        if (colSpanCB.isSelected())
            attribs.put("colspan", colSpanField.getModel().getValue().toString());
        else
            attribs.remove("colspan");

        if (rowSpanCB.isSelected())
            attribs.put("rowspan", rowSpanField.getModel().getValue().toString());
        else
            attribs.remove("rowspan");
    }

    @Override
    public void setAttributes(Map<String, String> attr) {
        alignPanel.setAttributes(attr);
        sizePanel.setAttributes(attr);
        super.setAttributes(attr);
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        GridBagConstraints gbc31 = new GridBagConstraints();
        gbc31.gridx = 0;
        gbc31.gridwidth = 3;
        gbc31.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc31.fill = GridBagConstraints.HORIZONTAL;
        gbc31.weighty = 1.0;
        gbc31.gridy = 3;

        GridBagConstraints gbc21 = new GridBagConstraints();
        gbc21.gridx = 0;
        gbc21.gridheight = 2;
        gbc21.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc21.fill = GridBagConstraints.HORIZONTAL;
        gbc21.insets = new Insets(0, 0, 0, 5);
        gbc21.gridy = 1;

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 1;
        gbc3.gridwidth = 2;
        gbc3.anchor = GridBagConstraints.NORTHWEST;
        gbc3.gridy = 2;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 1;
        gbc2.anchor = GridBagConstraints.SOUTHWEST;
        gbc2.insets = new Insets(0, 0, 5, 0);
        gbc2.gridwidth = 2;
        gbc2.gridy = 1;

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 1;
        gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc1.weightx = 1.0;
        gbc1.insets = new Insets(0, 0, 5, 0);
        gbc1.gridy = 0;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridwidth = 1;
        gbc.gridy = 0;

        setLayout(new GridBagLayout());
        setSize(420, 200);
        setPreferredSize(new Dimension(410, 200));
        add(getAlignPanel(), gbc);
        add(getSizePanel(), gbc1);
        add(getDontWrapCB(), gbc2);
        add(getBgColorPanel(), gbc3);
        add(getSpanPanel(), gbc21);
        add(getExpansionPanel(), gbc31);


    }

    /**
     * This method initializes alignPanel
     *
     * @return javax.swing.JPanel
     */
    private AlignmentAttributesPanel getAlignPanel() {
        if (alignPanel == null) {
            alignPanel = new AlignmentAttributesPanel();
            alignPanel.setPreferredSize(new Dimension(180, 95));

        }
        return alignPanel;
    }

    /**
     * This method initializes sizePanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getSizePanel() {
        if (sizePanel == null) {
            sizePanel = new SizeAttributesPanel();
        }
        return sizePanel;
    }

    /**
     * This method initializes dontWrapCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getDontWrapCB() {
        if (dontWrapCB == null) {
            dontWrapCB = new JCheckBox();
            dontWrapCB.setText(i18n.str("dont_wrap_text"));
        }
        return dontWrapCB;
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
     * This method initializes spanPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getSpanPanel() {
        if (spanPanel == null) {
            GridBagConstraints gbc7 = new GridBagConstraints();
            gbc7.fill = GridBagConstraints.NONE;
            gbc7.gridy = 1;
            gbc7.weightx = 1.0;
            gbc7.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc7.gridx = 1;
            GridBagConstraints gbc6 = new GridBagConstraints();
            gbc6.fill = GridBagConstraints.NONE;
            gbc6.gridy = 0;
            gbc6.weightx = 0.0;
            gbc6.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc6.gridx = 1;
            GridBagConstraints gbc5 = new GridBagConstraints();
            gbc5.gridx = 0;
            gbc5.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc5.gridheight = 1;
            gbc5.insets = new Insets(0, 0, 0, 0);
            gbc5.gridy = 1;
            GridBagConstraints gbc4 = new GridBagConstraints();
            gbc4.gridx = 0;
            gbc4.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc4.insets = new Insets(0, 0, 5, 0);
            gbc4.gridy = 0;
            spanPanel = new JPanel();
            spanPanel.setLayout(new GridBagLayout());
            spanPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, i18n.str("span"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
            spanPanel.add(getColSpanCB(), gbc4);
            spanPanel.add(getRowSpanCB(), gbc5);
            spanPanel.add(getColSpanField(), gbc6);
            spanPanel.add(getRowSpanField(), gbc7);
        }
        return spanPanel;
    }

    /**
     * This method initializes colSpanCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getColSpanCB() {
        if (colSpanCB == null) {
            colSpanCB = new JCheckBox();
            colSpanCB.setText(i18n.str("colspan"));
            colSpanCB.setPreferredSize(new Dimension(85, 25));
            colSpanCB.addItemListener(e -> colSpanField.setEnabled(colSpanCB.isSelected()));
        }
        return colSpanCB;
    }

    /**
     * This method initializes rowSpanCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getRowSpanCB() {
        if (rowSpanCB == null) {
            rowSpanCB = new JCheckBox();
            rowSpanCB.setText(i18n.str("rowspan"));
            rowSpanCB.addItemListener(e -> rowSpanField.setEnabled(rowSpanCB.isSelected()));
        }
        return rowSpanCB;
    }

    /**
     * This method initializes colSpanField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getColSpanField() {
        if (colSpanField == null) {
            colSpanField = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        }
        return colSpanField;
    }

    /**
     * This method initializes rowSpanField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getRowSpanField() {
        if (rowSpanField == null) {
            rowSpanField = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        }
        return rowSpanField;
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

}  //  @jve:decl-index=0:visual-constraint="42,7"
