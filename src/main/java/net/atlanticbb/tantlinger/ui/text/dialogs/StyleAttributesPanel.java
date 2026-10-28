package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.ui.text.TextEditPopupManager;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class StyleAttributesPanel extends HTMLAttributeEditorPanel {
    private static final long serialVersionUID = 1L;
    private JLabel classLabel = null;
    private JLabel idLabel = null;
    private JTextField classField = null;
    private JTextField idField = null;

    /**
     * This method initializes
     *
     */
    public StyleAttributesPanel() {
        this(new Hashtable());
    }

    public StyleAttributesPanel(Hashtable attr) {
        super();
        initialize();
        setAttributes(attr);
        this.updateComponentsFromAttribs();
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.fill = GridBagConstraints.HORIZONTAL;
        gbc4.gridy = 1;
        gbc4.weightx = 1.0;
        gbc4.insets = new Insets(0, 0, 5, 0);
        gbc4.gridx = 1;
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.gridy = 0;
        gbc3.weightx = 1.0;
        gbc3.insets = new Insets(0, 0, 5, 0);
        gbc3.weighty = 0.0;
        gbc3.gridx = 1;
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc1.insets = new Insets(0, 0, 5, 5);
        gbc1.gridy = 1;
        idLabel = new JLabel();
        idLabel.setText(i18n.str("id"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridy = 0;
        classLabel = new JLabel();
        classLabel.setText(i18n.str("class"));
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(210, 60));
        this.setPreferredSize(new Dimension(210, 60));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.add(classLabel, gbc);
        this.add(idLabel, gbc1);
        this.add(getClassField(), gbc3);
        this.add(getIdField(), gbc4);

        TextEditPopupManager popupMan = TextEditPopupManager.getInstance();
        popupMan.registerJTextComponent(classField);
        popupMan.registerJTextComponent(idField);

    }

    public void updateComponentsFromAttribs() {
        if (attribs.containsKey("class"))
            classField.setText(attribs.get("class").toString());
        else
            classField.setText("");

        if (attribs.containsKey("id"))
            idField.setText(attribs.get("id").toString());
        else
            idField.setText("");
    }

    public void updateAttribsFromComponents() {
        if (!classField.getText().equals(""))
            attribs.put("class", classField.getText());
        else
            attribs.remove("class");

        if (!idField.getText().equals(""))
            attribs.put("id", idField.getText());
        else
            attribs.remove("id");

    }

    /**
     * This method initializes classField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getClassField() {
        if (classField == null) {
            classField = new JTextField();
        }
        return classField;
    }

    /**
     * This method initializes idField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getIdField() {
        if (idField == null) {
            idField = new JTextField();
        }
        return idField;
    }

}
