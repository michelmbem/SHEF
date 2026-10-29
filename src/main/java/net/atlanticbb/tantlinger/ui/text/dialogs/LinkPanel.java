package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.ui.text.TextEditPopupManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LinkPanel extends HTMLAttributeEditorPanel {
    private static final long serialVersionUID = 1L;

    private JPanel hlinkPanel = null;
    private JLabel urlLabel = null;
    private JLabel textLabel = null;
    private JTextField urlField = null;
    private JTextField textField = null;
    private HTMLAttributeEditorPanel linkAttrPanel = null;
    private boolean urlFieldEnabled;

    /**
     * This is the default constructor
     */
    public LinkPanel() {
        this(true);
    }

    public LinkPanel(boolean urlFieldEnabled) {
        this(new HashMap<>(), urlFieldEnabled);
    }

    public LinkPanel(Map<String, String> attr, boolean urlFieldEnabled) {
        super();
        this.urlFieldEnabled = urlFieldEnabled;
        initialize();
        setAttributes(attr);
        updateComponentsFromAttribs();
    }

    @Override
    public void updateComponentsFromAttribs() {
        linkAttrPanel.updateComponentsFromAttribs();
        urlField.setText(attribs.getOrDefault("href", ""));
    }

    @Override
    public void updateAttribsFromComponents() {
        linkAttrPanel.updateAttribsFromComponents();
        attribs.put("href", urlField.getText());
    }

    @Override
    public void setAttributes(Map<String, String> at) {
        super.setAttributes(at);
        linkAttrPanel.setAttributes(attribs);
    }

    public String getLinkText() {
        return textField.getText();
    }

    public void setLinkText(String text) {
        textField.setText(text);
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        setLayout(new BorderLayout(5, 5));
        add(getHlinkPanel(), BorderLayout.PAGE_START);
        add(getLinkAttrPanel(), BorderLayout.CENTER);

        TextEditPopupManager popupMan = TextEditPopupManager.getInstance();
        popupMan.registerJTextComponent(urlField);
        popupMan.registerJTextComponent(textField);
    }

    /**
     * This method initializes hlinkPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getHlinkPanel() {
        if (hlinkPanel == null) {
            final int anchor = GridBagConstraints.BASELINE_LEADING;
            final int fill = GridBagConstraints.HORIZONTAL;
            final Insets leftInsets = new Insets(0, 0, 5, 5);
            final Insets rightInsets = new Insets(0, 0, 5, 0);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx =  gbc.gridy = 0;
            gbc.anchor = anchor;
            gbc.fill = fill;
            gbc.insets = leftInsets;

            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 0;
            gbc1.gridy = 1;
            gbc1.anchor = anchor;
            gbc1.fill = fill;
            gbc1.insets = rightInsets;

            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 1;
            gbc2.gridy = 0;
            gbc2.anchor = anchor;
            gbc2.fill = fill;
            gbc2.insets = leftInsets;

            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = gbc3.gridy = 1;
            gbc3.anchor = anchor;
            gbc3.fill = fill;
            gbc3.insets = rightInsets;

            urlLabel = new JLabel();
            urlLabel.setText(i18n.str("url"));

            textLabel = new JLabel();
            textLabel.setText(i18n.str("text"));

            GridBagLayout gbl = new GridBagLayout();
            gbl.columnWeights = new double[] {0.0, 1.0};
            gbl.rowWeights = new double[] {0.0, 0.0};

            hlinkPanel = new JPanel();
            hlinkPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(null, i18n.str("link")),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            hlinkPanel.setLayout(gbl);
            hlinkPanel.add(urlLabel, gbc);
            hlinkPanel.add(textLabel, gbc1);
            hlinkPanel.add(getUrlField(), gbc2);
            hlinkPanel.add(getTextField(), gbc3);
        }

        return hlinkPanel;
    }

    /**
     * This method initializes urlField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getUrlField() {
        if (urlField == null) {
            urlField = new JTextField();
            urlField.setEditable(urlFieldEnabled);
        }

        return urlField;
    }

    /**
     * This method initializes textField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTextField() {
        if (textField == null) {
            textField = new JTextField();
        }

        return textField;
    }

    private JPanel getLinkAttrPanel() {
        if (linkAttrPanel == null) {
            linkAttrPanel = new LinkAttributesPanel();
        }

        return linkAttrPanel;
    }

}
