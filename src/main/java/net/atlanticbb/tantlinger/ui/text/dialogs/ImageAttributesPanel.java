/*
 * Created on Jan 14, 2006
 *
 */
package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.ui.text.TextEditPopupManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;


public class ImageAttributesPanel extends HTMLAttributeEditorPanel {
    private static final long serialVersionUID = 1L;

    private static final String ALIGNMENTS[] =
            {
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
    private JComboBox alignCombo = null;
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
            imgUrlField.setText(attribs.get("src").toString());

        if (attribs.containsKey("alt"))
        {
            altTextCB.setSelected(true);
            altTextField.setEditable(true);
            altTextField.setText(attribs.get("alt").toString());
        } else {
            altTextCB.setSelected(false);
            altTextField.setEditable(false);
        }

        if (attribs.containsKey("width"))
        {
            widthCB.setSelected(true);
            widthField.setEnabled(true);
            try {
                widthField.getModel().setValue(
                        new Integer(attribs.get("width").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            widthCB.setSelected(false);
            widthField.setEnabled(false);
        }

        if (attribs.containsKey("height"))
        {
            heightCB.setSelected(true);
            heightField.setEnabled(true);
            try {
                heightField.getModel().setValue(
                        new Integer(attribs.get("height").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            heightCB.setSelected(false);
            heightField.setEnabled(false);
        }

        if (attribs.containsKey("hspace"))
        {
            hSpaceCB.setSelected(true);
            hSpaceField.setEnabled(true);
            try {
                hSpaceField.getModel().setValue(
                        new Integer(attribs.get("hspace").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            hSpaceCB.setSelected(false);
            hSpaceField.setEnabled(false);
        }

        if (attribs.containsKey("vspace"))
        {
            vSpaceCB.setSelected(true);
            vSpaceField.setEnabled(true);
            try {
                vSpaceField.getModel().setValue(
                        new Integer(attribs.get("vspace").toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            vSpaceCB.setSelected(false);
            vSpaceField.setEnabled(false);
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

        if (attribs.containsKey("align"))
        {
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
     *
     * @return void
     */
    private void initialize() {

        GridBagConstraints gbc22 = new GridBagConstraints();
        gbc22.gridx = 2;
        gbc22.gridwidth = 2;
        gbc22.anchor = GridBagConstraints.CENTER;
        gbc22.fill = GridBagConstraints.HORIZONTAL;
        gbc22.weighty = 1.0;
        gbc22.gridy = 0;
        GridBagConstraints gbc21 = new GridBagConstraints();
        gbc21.gridx = 0;
        gbc21.gridwidth = 2;
        gbc21.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc21.fill = GridBagConstraints.HORIZONTAL;
        gbc21.weighty = 1.0;
        gbc21.gridy = 3;
        GridBagConstraints gbc16 = new GridBagConstraints();
        gbc16.gridx = 0;
        gbc16.gridwidth = 2;
        gbc16.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc16.gridy = 2;
        GridBagConstraints gbc15 = new GridBagConstraints();
        gbc15.fill = GridBagConstraints.HORIZONTAL;
        gbc15.gridy = 1;
        gbc15.weightx = 1.0;
        gbc15.insets = new Insets(0, 0, 10, 0);
        gbc15.gridwidth = 1;
        gbc15.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc15.gridx = 1;
        GridBagConstraints gbc14 = new GridBagConstraints();
        gbc14.fill = GridBagConstraints.HORIZONTAL;
        gbc14.gridy = 0;
        gbc14.weightx = 1.0;
        gbc14.insets = new Insets(0, 0, 5, 0);
        gbc14.gridwidth = 1;
        gbc14.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc14.gridx = 1;
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc1.insets = new Insets(0, 0, 10, 5);
        gbc1.gridy = 1;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridy = 0;
        imgUrlLabel = new JLabel();
        imgUrlLabel.setText(i18n.str("image_url"));

        selectFileButton = new JButton("Select Image File...");
        selectFileButton.addActionListener(evt -> {

            JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
            File selected;
            chooser.setDialogTitle("Select Image File");
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
            chooser.setAcceptAllFileFilterUsed(false);
            int rc = chooser.showDialog(ImageAttributesPanel.this, "Select");
            if (rc == JFileChooser.APPROVE_OPTION) {
                selected = chooser.getSelectedFile();
                if (selected == null)
                    return;

                imgUrlField.setText("file:" + selected.getAbsolutePath());

            }

        });

        this.setLayout(new GridBagLayout());
        //this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Image Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null), BorderFactory.createEmptyBorder(5,5,5,5)));
        this.add(imgUrlLabel, gbc);
        this.add(selectFileButton, gbc22);
        this.add(getAltTextCB(), gbc1);
        this.add(getImgUrlField(), gbc14);
        this.add(getAltTextField(), gbc15);
        this.add(getAttribPanel(), gbc16);
        this.add(getSpacerPanel(), gbc21);
        this.setSize(700, 300);

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
            altTextCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    altTextField.setEditable(altTextCB.isSelected());
                }
            });
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
            widthCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    widthField.setEnabled(widthCB.isSelected());
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
                }
            });
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
            borderCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    borderField.setEnabled(borderCB.isSelected());
                }
            });
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
            //widthField.setColumns(4);
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
            //heightField.setColumns(4);
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
            //borderField.setColumns(4);
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
            vSpaceCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    vSpaceField.setEnabled(vSpaceCB.isSelected());
                }
            });
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
            hSpaceCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    hSpaceField.setEnabled(hSpaceCB.isSelected());
                }
            });
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
            alignCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    alignCombo.setEnabled(alignCB.isSelected());
                }
            });
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
            //vSpaceField.setColumns(4);
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
            //hSpaceField.setColumns(4);
        }
        return hSpaceField;
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
            GridBagConstraints gbc13 = new GridBagConstraints();
            gbc13.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc13.insets = new Insets(0, 0, 10, 0);
            gbc13.gridx = 3;
            gbc13.gridy = 2;
            gbc13.weightx = 1.0;
            gbc13.fill = GridBagConstraints.NONE;
            GridBagConstraints gbc12 = new GridBagConstraints();
            gbc12.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc12.insets = new Insets(0, 0, 10, 0);
            gbc12.gridx = 3;
            gbc12.gridy = 1;
            gbc12.weightx = 0.0;
            gbc12.fill = GridBagConstraints.NONE;
            GridBagConstraints gbc11 = new GridBagConstraints();
            gbc11.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc11.insets = new Insets(0, 0, 5, 0);
            gbc11.gridx = 3;
            gbc11.gridy = 0;
            gbc11.weightx = 0.0;
            gbc11.fill = GridBagConstraints.NONE;
            GridBagConstraints gbc10 = new GridBagConstraints();
            gbc10.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc10.gridx = 2;
            gbc10.gridy = 2;
            gbc10.insets = new Insets(0, 0, 10, 5);
            GridBagConstraints gbc9 = new GridBagConstraints();
            gbc9.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc9.gridx = 2;
            gbc9.gridy = 1;
            gbc9.insets = new Insets(0, 0, 10, 5);
            GridBagConstraints gbc8 = new GridBagConstraints();
            gbc8.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc8.gridx = 2;
            gbc8.gridy = 0;
            gbc8.insets = new Insets(0, 0, 5, 5);
            GridBagConstraints gbc7 = new GridBagConstraints();
            gbc7.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc7.insets = new Insets(0, 0, 10, 10);
            gbc7.gridx = 1;
            gbc7.gridy = 2;
            gbc7.weightx = 0.0;
            gbc7.fill = GridBagConstraints.NONE;
            GridBagConstraints gbc6 = new GridBagConstraints();
            gbc6.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc6.insets = new Insets(0, 0, 10, 10);
            gbc6.gridx = 1;
            gbc6.gridy = 1;
            gbc6.weightx = 0.0;
            gbc6.fill = GridBagConstraints.NONE;
            GridBagConstraints gbc5 = new GridBagConstraints();
            gbc5.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc5.insets = new Insets(0, 0, 5, 10);
            gbc5.gridx = 1;
            gbc5.gridy = 0;
            gbc5.weightx = 0.0;
            gbc5.fill = GridBagConstraints.NONE;
            GridBagConstraints gbc4 = new GridBagConstraints();
            gbc4.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc4.gridx = 0;
            gbc4.gridy = 2;
            gbc4.insets = new Insets(0, 0, 10, 5);
            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc3.gridx = 0;
            gbc3.gridy = 1;
            gbc3.insets = new Insets(0, 0, 10, 5);
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc2.gridx = 0;
            gbc2.gridy = 0;
            gbc2.insets = new Insets(0, 0, 5, 5);
            attribPanel = new JPanel();
            attribPanel.setLayout(new GridBagLayout());
            attribPanel.add(getWidthCB(), gbc2);
            attribPanel.add(getHeightCB(), gbc3);
            attribPanel.add(getBorderCB(), gbc4);
            attribPanel.add(getWidthField(), gbc5);
            attribPanel.add(getHeightField(), gbc6);
            attribPanel.add(getBorderField(), gbc7);
            attribPanel.add(getVSpaceCB(), gbc8);
            attribPanel.add(getHSpaceCB(), gbc9);
            attribPanel.add(getAlignCB(), gbc10);
            attribPanel.add(getVSpaceField(), gbc11);
            attribPanel.add(getHSpaceField(), gbc12);
            attribPanel.add(getAlignCombo(), gbc13);
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

}  //  @jve:decl-index=0:visual-constraint="10,10"
