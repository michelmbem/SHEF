/*
 * Created on Jan 13, 2006
 *
 */
package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.ui.text.TextEditPopupManager;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
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

    /**
     * @param attr
     */
    public LinkPanel(boolean urlFieldEnabled) {
        this(new Hashtable(), urlFieldEnabled);
    }

    public LinkPanel(Hashtable attr, boolean urlFieldEnabled) {
        super();
        this.urlFieldEnabled = urlFieldEnabled;
        initialize();
        setAttributes(attr);
        updateComponentsFromAttribs();
    }


    public void updateComponentsFromAttribs() {
        linkAttrPanel.updateComponentsFromAttribs();
        if (attribs.containsKey("href"))
            urlField.setText(attribs.get("href").toString());
        else
            urlField.setText("");
    }

    public void updateAttribsFromComponents() {
        linkAttrPanel.updateAttribsFromComponents();
        attribs.put("href", urlField.getText());
    }

    public void setAttributes(Map at) {
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
     *
     * @return void
     */
    private void initialize() {
        this.setLayout(new BorderLayout(5, 5));
        this.setSize(328, 218);
        this.add(getHlinkPanel(), java.awt.BorderLayout.NORTH);
        this.add(getLinkAttrPanel(), BorderLayout.CENTER);

        TextEditPopupManager popupMan = TextEditPopupManager.getInstance();//new TextEditPopupManager();
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
            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.fill = GridBagConstraints.HORIZONTAL;
            gbc3.gridy = 1;
            gbc3.weightx = 1.0;
            gbc3.gridx = 1;
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.fill = GridBagConstraints.HORIZONTAL;
            gbc2.gridy = 0;
            gbc2.weightx = 1.0;
            gbc2.insets = new Insets(0, 0, 5, 0);
            gbc2.gridx = 1;
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 0;
            gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc1.insets = new Insets(0, 0, 0, 5);
            gbc1.gridy = 1;
            textLabel = new JLabel();
            textLabel.setText(i18n.str("text"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc.insets = new Insets(0, 0, 0, 5);
            gbc.gridy = 0;
            urlLabel = new JLabel();
            urlLabel.setText(i18n.str("url"));
            hlinkPanel = new JPanel();
            hlinkPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, i18n.str("link"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            hlinkPanel.setLayout(new GridBagLayout());
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
            //urlField.setEditable(true);
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

}  //  @jve:decl-index=0:visual-constraint="29,25"
