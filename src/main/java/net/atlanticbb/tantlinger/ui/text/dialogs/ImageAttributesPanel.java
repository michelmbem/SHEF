package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.TextEditPopupManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class ImageAttributesPanel extends HTMLAttributeEditorPanel {
    private static final long serialVersionUID = 1L;
    private static final String[] ALIGNMENTS = {
            "top", "middle", "bottom", "left", "right"
    };

    private JLabel imgUrlLabel = null;
    private JButton selectFileButton = null;
    private JCheckBox altTextCB = null;
    private JCheckBox widthCB = null;
    private JCheckBox heightCB = null;
    private JCheckBox borderCB = null;
    private JSpinner widthField = null;
    private JSpinner heightField = null;
    private JSpinner borderField = null;
    private JCheckBox vSpaceCB = null;
    private JCheckBox hSpaceCB = null;
    private JCheckBox alignCB = null;
    private JSpinner vSpaceField = null;
    private JSpinner hSpaceField = null;
    private JComboBox<String> alignCombo = null;
    private JTextField imgUrlField = null;
    private JTextField altTextField = null;
    private JPanel attribPanel = null;
    private JPanel spacerPanel = null;

    /**
     * This is the default constructor
     */
    public ImageAttributesPanel() {
        super();
        initialize();
        updateComponentsFromAttribs();
    }

    public void updateComponentsFromAttribs() {
        if (attribs.containsKey("src"))
            imgUrlField.setText(attribs.get("src"));

        if (attribs.containsKey("alt")) {
            altTextCB.setSelected(true);
            altTextField.setEditable(true);
            altTextField.setText(attribs.get("alt"));
        } else {
            altTextCB.setSelected(false);
            altTextField.setEditable(false);
        }

        if (attribs.containsKey("width")) {
            widthCB.setSelected(true);
            widthField.setEnabled(true);

            try {
                widthField.getModel().setValue(
                        Integer.valueOf(attribs.get("width")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            widthCB.setSelected(false);
            widthField.setEnabled(false);
        }

        if (attribs.containsKey("height")) {
            heightCB.setSelected(true);
            heightField.setEnabled(true);

            try {
                heightField.getModel().setValue(
                        Integer.valueOf(attribs.get("height")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            heightCB.setSelected(false);
            heightField.setEnabled(false);
        }

        if (attribs.containsKey("hspace")) {
            hSpaceCB.setSelected(true);
            hSpaceField.setEnabled(true);

            try {
                hSpaceField.getModel().setValue(
                        Integer.valueOf(attribs.get("hspace")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            hSpaceCB.setSelected(false);
            hSpaceField.setEnabled(false);
        }

        if (attribs.containsKey("vspace")) {
            vSpaceCB.setSelected(true);
            vSpaceField.setEnabled(true);

            try {
                vSpaceField.getModel().setValue(
                        Integer.valueOf(attribs.get("vspace")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            vSpaceCB.setSelected(false);
            vSpaceField.setEnabled(false);
        }

        if (attribs.containsKey("border")) {
            borderCB.setSelected(true);
            borderField.setEnabled(true);

            try {
                borderField.getModel().setValue(
                        Integer.valueOf(attribs.get("border")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            borderCB.setSelected(false);
            borderField.setEnabled(false);
        }

        if (attribs.containsKey("align")) {
            alignCB.setSelected(true);
            alignCombo.setEnabled(true);
            alignCombo.setSelectedItem(attribs.get("align"));
        } else {
            alignCB.setSelected(false);
            alignCombo.setEnabled(false);
        }
    }

    public void updateAttribsFromComponents() {
        attribs.put("src", imgUrlField.getText());

        if (altTextCB.isSelected())
            attribs.put("alt", altTextField.getText());
        else
            attribs.remove("alt");

        if (widthCB.isSelected())
            attribs.put("width", widthField.getModel().getValue().toString());
        else
            attribs.remove("width");

        if (heightCB.isSelected())
            attribs.put("height", heightField.getModel().getValue().toString());
        else
            attribs.remove("height");

        if (vSpaceCB.isSelected())
            attribs.put("vspace", vSpaceField.getModel().getValue().toString());
        else
            attribs.remove("vspace");

        if (hSpaceCB.isSelected())
            attribs.put("hspace", hSpaceField.getModel().getValue().toString());
        else
            attribs.remove("hspace");

        if (borderCB.isSelected())
            attribs.put("border", borderField.getModel().getValue().toString());
        else
            attribs.remove("border");

        if (alignCB.isSelected())
            attribs.put("align", alignCombo.getSelectedItem().toString());
        else
            attribs.remove("align");
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
        gbc1.insets = rightInsets;

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
        gbc4.gridx = 1;
        gbc4.gridy = 1;
        gbc4.gridwidth = 2;
        gbc4.anchor = anchor;
        gbc4.fill = fill;
        gbc4.insets = rightInsets;

        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.gridx = 0;
        gbc5.gridy = 2;
        gbc5.gridwidth = 3;
        gbc5.anchor = anchor;
        gbc5.fill = fill;
        gbc5.insets = rightInsets;

        GridBagConstraints gbc6 = new GridBagConstraints();
        gbc6.gridx = 0;
        gbc6.gridy = 3;
        gbc6.gridwidth = 3;
        gbc6.fill = GridBagConstraints.BOTH;

        imgUrlLabel = new JLabel(i18n.str("image_url"));

        selectFileButton = new JButton(UIUtils.getIcon(UIUtils.X16, "import.png"));
        selectFileButton.setToolTipText("Select an image file");
        selectFileButton.addActionListener(evt -> {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
            chooser.setDialogTitle("Select Image File");
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
            chooser.setAcceptAllFileFilterUsed(false);

            int rc = chooser.showDialog(ImageAttributesPanel.this, "Select");
            if (rc == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                if (selected != null) {
                    imgUrlField.setText("file:" + selected.getAbsolutePath());
                }
            }
        });

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {0.0, 1.0, 0.0};
        gbl.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};

        setLayout(gbl);
        add(imgUrlLabel, gbc);
        add(getImgUrlField(), gbc1);
        add(selectFileButton, gbc2);
        add(getAltTextCB(), gbc3);
        add(getAltTextField(), gbc4);
        add(getAttribPanel(), gbc5);
        add(getSpacerPanel(), gbc6);
        setSize(700, 300);

        TextEditPopupManager popupMan = TextEditPopupManager.getInstance();
        popupMan.registerJTextComponent(imgUrlField);
        popupMan.registerJTextComponent(altTextField);
        imgUrlField.setToolTipText("Filename (prefaced with \"file:\" or Link (prefaced with \"http://\")");
    }

    /**
     * This method initializes altTextCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getAltTextCB() {
        if (altTextCB == null) {
            altTextCB = new JCheckBox();
            altTextCB.setText(i18n.str("alt_text"));
            altTextCB.addItemListener(e -> altTextField.setEditable(altTextCB.isSelected()));
        }

        return altTextCB;
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
            widthCB.addItemListener(e -> widthField.setEnabled(widthCB.isSelected()));
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
            heightCB.addItemListener(e -> heightField.setEnabled(heightCB.isSelected()));
        }

        return heightCB;
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
            borderCB.addItemListener(e -> borderField.setEnabled(borderCB.isSelected()));
        }

        return borderCB;
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
     * This method initializes vSpaceCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getVSpaceCB() {
        if (vSpaceCB == null) {
            vSpaceCB = new JCheckBox();
            vSpaceCB.setText(i18n.str("vspace"));
            vSpaceCB.addItemListener(e -> vSpaceField.setEnabled(vSpaceCB.isSelected()));
        }

        return vSpaceCB;
    }

    /**
     * This method initializes hSpaceCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getHSpaceCB() {
        if (hSpaceCB == null) {
            hSpaceCB = new JCheckBox();
            hSpaceCB.setText(i18n.str("hspace"));
            hSpaceCB.addItemListener(e -> hSpaceField.setEnabled(hSpaceCB.isSelected()));
        }

        return hSpaceCB;
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
     * This method initializes vSpaceField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getVSpaceField() {
        if (vSpaceField == null) {
            vSpaceField = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        }

        return vSpaceField;
    }

    /**
     * This method initializes hSpaceField
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getHSpaceField() {
        if (hSpaceField == null) {
            hSpaceField = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        }

        return hSpaceField;
    }

    /**
     * This method initializes alignCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getAlignCombo() {
        if (alignCombo == null) {
            alignCombo = new JComboBox<>(ALIGNMENTS);
        }

        return alignCombo;
    }

    /**
     * This method initializes imgUrlField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getImgUrlField() {
        if (imgUrlField == null) {
            imgUrlField = new JTextField();
        }

        return imgUrlField;
    }

    /**
     * This method initializes altTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getAltTextField() {
        if (altTextField == null) {
            altTextField = new JTextField();
        }

        return altTextField;
    }

    /**
     * This method initializes attribPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getAttribPanel() {
        if (attribPanel == null) {
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
            gbc2.insets = leftInsets;

            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = 3;
            gbc3.gridy = 0;
            gbc3.anchor = anchor;
            gbc3.fill = fill;
            gbc3.insets = rightInsets;

            GridBagConstraints gbc4 = new GridBagConstraints();
            gbc4.gridx = 0;
            gbc4.gridy = 1;
            gbc4.anchor = anchor;
            gbc4.fill = fill;
            gbc4.insets = leftInsets;

            GridBagConstraints gbc5 = new GridBagConstraints();
            gbc5.gridx = 1;
            gbc5.gridy = 1;
            gbc5.anchor = anchor;
            gbc5.fill = fill;
            gbc5.insets = leftInsets;

            GridBagConstraints gbc6 = new GridBagConstraints();
            gbc6.gridx = 2;
            gbc6.gridy = 1;
            gbc6.anchor = anchor;
            gbc6.fill = fill;
            gbc6.insets = leftInsets;

            GridBagConstraints gbc7 = new GridBagConstraints();
            gbc7.gridx = 3;
            gbc7.gridy = 1;
            gbc7.anchor = anchor;
            gbc7.fill = fill;
            gbc7.insets = rightInsets;

            GridBagConstraints gbc8 = new GridBagConstraints();
            gbc8.gridx = 0;
            gbc8.gridy = 2;
            gbc8.anchor = anchor;
            gbc8.fill = fill;
            gbc8.insets = leftInsets;

            GridBagConstraints gbc9 = new GridBagConstraints();
            gbc9.gridx = 1;
            gbc9.gridy = 2;
            gbc9.anchor = anchor;
            gbc9.fill = fill;
            gbc9.insets = leftInsets;

            GridBagConstraints gbc10 = new GridBagConstraints();
            gbc10.gridx = 2;
            gbc10.gridy = 2;
            gbc10.anchor = anchor;
            gbc10.fill = fill;
            gbc10.insets = leftInsets;

            GridBagConstraints gbc11 = new GridBagConstraints();
            gbc11.gridx = 3;
            gbc11.gridy = 2;
            gbc11.anchor = anchor;
            gbc11.fill = fill;
            gbc11.insets = rightInsets;

            GridBagLayout gbl = new GridBagLayout();
            gbl.columnWeights = new double[] {0.0, 1.0, 0.0, 1.0};
            gbl.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

            attribPanel = new JPanel(gbl);
            attribPanel.add(getWidthCB(), gbc);
            attribPanel.add(getWidthField(), gbc1);
            attribPanel.add(getVSpaceCB(), gbc2);
            attribPanel.add(getVSpaceField(), gbc3);
            attribPanel.add(getHeightCB(), gbc4);
            attribPanel.add(getHeightField(), gbc5);
            attribPanel.add(getHSpaceCB(), gbc6);
            attribPanel.add(getHSpaceField(), gbc7);
            attribPanel.add(getBorderCB(), gbc8);
            attribPanel.add(getBorderField(), gbc9);
            attribPanel.add(getAlignCB(), gbc10);
            attribPanel.add(getAlignCombo(), gbc11);
        }

        return attribPanel;
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
