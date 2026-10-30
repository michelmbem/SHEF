package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.ui.text.TextEditPopupManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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
        this(new HashMap<>());
    }

    public StyleAttributesPanel(Map<String, String> attr) {
        super();
        initialize();
        setAttributes(attr);
        updateComponentsFromAttribs();
    }

    /**
     * This method initializes this
     *
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
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        gbc2.anchor = anchor;
        gbc2.fill = fill;
        gbc2.insets = leftInsets;

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = gbc3.gridy = 1;
        gbc3.anchor = anchor;
        gbc3.fill = fill;
        gbc3.insets = rightInsets;

        classLabel = new JLabel(i18n.str("class"));
        idLabel = new JLabel(i18n.str("id"));

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {0.0, 1.0};
        gbl.rowWeights = new double[] {0.0, 0.0};

        setLayout(gbl);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(classLabel, gbc);
        add(idLabel, gbc2);
        add(getClassField(), gbc1);
        add(getIdField(), gbc3);

        TextEditPopupManager popupMan = TextEditPopupManager.getInstance();
        popupMan.registerJTextComponent(classField);
        popupMan.registerJTextComponent(idField);
    }

    public void updateComponentsFromAttribs() {
        classField.setText(attribs.getOrDefault("class", ""));
        idField.setText(attribs.getOrDefault("id", ""));
    }

    public void updateAttribsFromComponents() {
        if (classField.getText().isEmpty()) {
            attribs.remove("class");
        } else {
            attribs.put("class", classField.getText());
        }

        if (idField.getText().isEmpty()) {
            attribs.remove("id");
        } else {
            attribs.put("id", idField.getText());
        }
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
