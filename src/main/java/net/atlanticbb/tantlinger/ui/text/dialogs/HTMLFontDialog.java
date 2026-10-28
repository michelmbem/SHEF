package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class HTMLFontDialog extends HTMLOptionDialog {
    private static final long serialVersionUID = 1L;

    private static final I18n i18n = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");
    private static final Integer SIZES[] =
            {
                    new Integer(8),
                    new Integer(10),
                    new Integer(12),
                    new Integer(14),
                    new Integer(18),
                    new Integer(24),
                    new Integer(36)
            };
    private static Icon icon = UIUtils.getIcon(UIUtils.X48, "fontsize.png");
    private static String title = i18n.str("font");
    private static String desc = i18n.str("font_desc");
    private JPanel jContentPane = null;
    private JLabel fontLabel = null;
    private JComboBox fontCombo = null;
    private JComboBox sizeCombo = null;
    private JPanel stylePanel = null;
    private JCheckBox boldCB = null;
    private JCheckBox italicCB = null;
    private JCheckBox ulCB = null;
    private JPanel previewPanel = null;
    private JLabel previewLabel = null;
    private JPanel spacerPanel = null;

    private String text = "";

    public HTMLFontDialog(Frame parent, String text) {
        super(parent, title, desc, icon);
        initialize(text);
    }

    public HTMLFontDialog(Dialog parent, String text) {
        super(parent, title, desc, icon);
        initialize(text);
    }

    public boolean isBold() {
        return boldCB.isSelected();
    }

    public void setBold(boolean b) {
        boldCB.setSelected(b);
        updatePreview();
    }

    public boolean isItalic() {
        return italicCB.isSelected();
    }

    public void setItalic(boolean b) {
        italicCB.setSelected(b);
        updatePreview();
    }

    public boolean isUnderline() {
        return ulCB.isSelected();
    }

    public void setUnderline(boolean b) {
        ulCB.setSelected(b);
        updatePreview();
    }

    public String getFontName() {
        return fontCombo.getSelectedItem().toString();
    }

    public void setFontName(String fn) {
        fontCombo.setSelectedItem(fn);
        updatePreview();
    }

    public int getFontSize() {
        Integer i = (Integer) sizeCombo.getSelectedItem();
        return i.intValue();
    }

    public void setFontSize(int size) {
        sizeCombo.setSelectedItem(new Integer(size));
        updatePreview();
    }

    public String getHTML() {
        String html = "<font ";
        html += "name=\"" + fontCombo.getSelectedItem() + "\" ";
        html += "size=\"" + (sizeCombo.getSelectedIndex() + 1) + "\">";
        if (boldCB.isSelected())
            html += "<b>";
        if (italicCB.isSelected())
            html += "<i>";
        if (ulCB.isSelected())
            html += "<u>";

        html += text;

        if (boldCB.isSelected())
            html += "</b>";
        if (italicCB.isSelected())
            html += "</i>";
        if (ulCB.isSelected())
            html += "</u>";

        html += "</font>";
        return html;
    }

    /**
     * This method initializes this
     */
    private void initialize(String text) {
        setContentPane(getJContentPane());
        pack();
        setSize(350, getHeight());
        setResizable(false);
        this.text = text;
    }

    private void updatePreview() {
        int style = Font.PLAIN;
        if (boldCB.isSelected())
            style += Font.BOLD;
        if (italicCB.isSelected())
            style += Font.ITALIC;

        if (ulCB.isSelected())
            previewLabel.setBorder(
                    BorderFactory.createMatteBorder(
                            0, 0, 1, 0, previewLabel.getForeground()));
        else
            previewLabel.setBorder(null);

        String font = fontCombo.getSelectedItem().toString();
        Integer size = SIZES[sizeCombo.getSelectedIndex()];
        Font f = new Font(font, style, size.intValue());
        previewLabel.setFont(f);

    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gbc21 = new GridBagConstraints();
            gbc21.gridx = 0;
            gbc21.gridwidth = 3;
            gbc21.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc21.fill = GridBagConstraints.HORIZONTAL;
            gbc21.insets = new Insets(5, 0, 0, 0);
            gbc21.gridy = 1;
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.fill = GridBagConstraints.NONE;
            gbc2.gridy = 0;
            gbc2.weightx = 1.0;
            gbc2.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc2.gridx = 2;
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.fill = GridBagConstraints.HORIZONTAL;
            gbc1.gridy = 0;
            gbc1.weightx = 1.0;
            gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc1.insets = new Insets(0, 0, 0, 5);
            gbc1.gridx = 1;
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.insets = new Insets(0, 0, 0, 5);
            gbc.gridy = 0;
            fontLabel = new JLabel();
            fontLabel.setText(i18n.str("font"));
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            jContentPane.add(fontLabel, gbc);
            jContentPane.add(getFontCombo(), gbc1);
            jContentPane.add(getSizeCombo(), gbc2);
            jContentPane.add(getStylePanel(), gbc21);

            sizeCombo.setSelectedItem(new Integer(previewLabel.getFont().getSize()));
        }
        return jContentPane;
    }

    /**
     * This method initializes fontCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getFontCombo() {
        if (fontCombo == null) {
            GraphicsEnvironment gEnv =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            String envfonts[] = gEnv.getAvailableFontFamilyNames();
            Vector fonts = new Vector();
            fonts.add("Default");
            fonts.add("serif");
            fonts.add("sans-serif");
            fonts.add("monospaced");
            for (int i = 0; i < envfonts.length; i++)
                fonts.add(envfonts[i]);

            fontCombo = new JComboBox(fonts);
            fontCombo.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    updatePreview();
                }
            });
        }
        return fontCombo;
    }

    /**
     * This method initializes sizeCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getSizeCombo() {
        if (sizeCombo == null) {
            sizeCombo = new JComboBox(SIZES);
            sizeCombo.setSelectedItem(new Integer(12));
            sizeCombo.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    updatePreview();
                }
            });
        }
        return sizeCombo;
    }

    /**
     * This method initializes stylePanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getStylePanel() {
        if (stylePanel == null) {
            GridBagConstraints gbc7 = new GridBagConstraints();
            gbc7.gridx = 0;
            gbc7.anchor = GridBagConstraints.NORTHWEST;
            gbc7.weighty = 1.0;
            gbc7.fill = GridBagConstraints.BOTH;
            gbc7.gridy = 3;
            GridBagConstraints gbc6 = new GridBagConstraints();
            gbc6.gridx = 1;
            gbc6.gridwidth = 1;
            gbc6.gridheight = 4;
            gbc6.fill = GridBagConstraints.BOTH;
            gbc6.weightx = 1.0;
            gbc6.weighty = 1.0;
            gbc6.anchor = GridBagConstraints.NORTHWEST;
            gbc6.gridy = 0;
            GridBagConstraints gbc5 = new GridBagConstraints();
            gbc5.gridx = 0;
            gbc5.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc5.insets = new Insets(0, 0, 0, 5);
            gbc5.weighty = 0.0;
            gbc5.gridy = 2;
            GridBagConstraints gbc4 = new GridBagConstraints();
            gbc4.gridx = 0;
            gbc4.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc4.insets = new Insets(0, 0, 0, 5);
            gbc4.gridy = 1;
            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = 0;
            gbc3.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc3.insets = new Insets(5, 0, 0, 5);
            gbc3.gridy = 0;
            stylePanel = new JPanel();
            stylePanel.setLayout(new GridBagLayout());
            stylePanel.add(getBoldCB(), gbc3);
            stylePanel.add(getItalicCB(), gbc4);
            stylePanel.add(getUlCB(), gbc5);
            stylePanel.add(getPreviewPanel(), gbc6);
            stylePanel.add(getSpacerPanel(), gbc7);
        }
        return stylePanel;
    }

    /**
     * This method initializes boldCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBoldCB() {
        if (boldCB == null) {
            boldCB = new JCheckBox();
            boldCB.setText(i18n.str("bold"));
            boldCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    updatePreview();
                }
            });
        }
        return boldCB;
    }

    /**
     * This method initializes italicCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getItalicCB() {
        if (italicCB == null) {
            italicCB = new JCheckBox();
            italicCB.setText(i18n.str("italic"));
            italicCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    updatePreview();
                }
            });
        }
        return italicCB;
    }

    /**
     * This method initializes ulCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getUlCB() {
        if (ulCB == null) {
            ulCB = new JCheckBox();
            ulCB.setText(i18n.str("underline"));
            ulCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    updatePreview();
                }
            });
        }
        return ulCB;
    }

    /**
     * This method initializes previewPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPreviewPanel() {
        if (previewPanel == null) {
            previewLabel = new JLabel();
            previewLabel.setText("AaBbYyZz");
            JPanel spacer = new JPanel(new FlowLayout(FlowLayout.LEFT));
            spacer.setBackground(Color.WHITE);
            spacer.add(previewLabel);
            previewPanel = new JPanel();
            previewPanel.setLayout(new BorderLayout());
            previewPanel.setBorder(BorderFactory.createCompoundBorder(null, BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, i18n.str("preview"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null), BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED)))));
            previewPanel.setPreferredSize(new Dimension(90, 100));
            previewPanel.setMaximumSize(previewPanel.getPreferredSize());
            previewPanel.setMinimumSize(previewPanel.getPreferredSize());
            previewPanel.add(spacer, null);
        }
        return previewPanel;
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
