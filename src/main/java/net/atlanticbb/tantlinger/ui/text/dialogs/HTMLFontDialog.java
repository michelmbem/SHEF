package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.UIUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class HTMLFontDialog extends HTMLOptionDialog {
    private static final long serialVersionUID = 1L;
    private static final I18n I18N = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");
    private static final Integer[] SIZES = { 8, 10, 12, 14, 18, 24, 36 };
    private static final Icon ICON = UIUtils.getIcon(UIUtils.X48, "fontsize.png");
    private static final String TITLE = I18N.str("font");
    private static final String DESC = I18N.str("font_desc");

    private JPanel jContentPane = null;
    private JLabel fontLabel = null;
    private JComboBox<String> fontCombo = null;
    private JComboBox<Integer> sizeCombo = null;
    private JPanel stylePanel = null;
    private JCheckBox boldCB = null;
    private JCheckBox italicCB = null;
    private JCheckBox ulCB = null;
    private JPanel previewPanel = null;
    private JLabel previewLabel = null;
    private JPanel spacerPanel = null;
    private String text = "";

    public HTMLFontDialog(Frame parent, String text) {
        super(parent, TITLE, DESC, ICON);
        initialize(text);
    }

    public HTMLFontDialog(Dialog parent, String text) {
        super(parent, TITLE, DESC, ICON);
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
        return (int) sizeCombo.getSelectedItem();
    }

    public void setFontSize(int size) {
        sizeCombo.setSelectedItem(size);
        updatePreview();
    }

    public String getHTML() {
        StringBuilder html = new StringBuilder("<font ");
        html.append("name=\"").append(fontCombo.getSelectedItem()).append("\" ");
        html.append("size=\"").append(sizeCombo.getSelectedItem()).append("\">");

        if (boldCB.isSelected()) html.append("<b>");
        if (italicCB.isSelected()) html.append("<i>");
        if (ulCB.isSelected()) html.append("<u>");

        html.append(text);

        if (boldCB.isSelected()) html.append("</b>");
        if (italicCB.isSelected()) html.append("</i>");
        if (ulCB.isSelected()) html.append("</u>");

        return html.append("</font>").toString();
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
        int size = (int) sizeCombo.getSelectedItem();
        Font f = new Font(font, style, size);
        previewLabel.setFont(f);
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
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
            gbc2.insets = rightInsets;

            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = 0;
            gbc3.gridy = 1;
            gbc3.gridwidth = 3;
            gbc3.anchor = anchor;
            gbc3.fill = fill;

            fontLabel = new JLabel();
            fontLabel.setText(I18N.str("font"));

            GridBagLayout gbl = new GridBagLayout();
            gbl.columnWeights = new double[] {0.0, 1.0, 1.0};
            gbl.rowWeights = new double[] {0.0, 0.0};

            jContentPane = new JPanel(gbl);
            jContentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            jContentPane.add(fontLabel, gbc);
            jContentPane.add(getFontCombo(), gbc1);
            jContentPane.add(getSizeCombo(), gbc2);
            jContentPane.add(getStylePanel(), gbc3);

            sizeCombo.setSelectedItem(previewLabel.getFont().getSize());
        }

        return jContentPane;
    }

    /**
     * This method initializes fontCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<String> getFontCombo() {
        if (fontCombo == null) {
            java.util.List<String> fonts = new ArrayList<>();
            fonts.add("Default");
            fonts.add("serif");
            fonts.add("sans-serif");
            fonts.add("monospaced");

            GraphicsEnvironment gEnv =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            Collections.addAll(fonts, gEnv.getAvailableFontFamilyNames());

            fontCombo = new JComboBox<>(fonts.toArray(String[]::new));
            fontCombo.addItemListener(e -> updatePreview());
        }

        return fontCombo;
    }

    /**
     * This method initializes sizeCombo
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox<Integer> getSizeCombo() {
        if (sizeCombo == null) {
            sizeCombo = new JComboBox<>(SIZES);
            sizeCombo.setSelectedItem(12);
            sizeCombo.addItemListener(e -> updatePreview());
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
            gbc1.gridheight = 4;
            gbc1.fill = GridBagConstraints.BOTH;
            gbc1.insets = rightInsets;

            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 0;
            gbc2.gridy = 1;
            gbc2.anchor = anchor;
            gbc2.fill = fill;
            gbc2.insets = leftInsets;

            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = 0;
            gbc3.gridy = 2;
            gbc3.anchor = anchor;
            gbc3.fill = fill;
            gbc3.insets = leftInsets;

            GridBagConstraints gbc4 = new GridBagConstraints();
            gbc4.gridx = 0;
            gbc4.gridy = 3;
            gbc4.fill = GridBagConstraints.BOTH;
            gbc4.insets = leftInsets;

            GridBagLayout gbl = new GridBagLayout();
            gbl.columnWeights = new double[] {0.0, 1.0};
            gbl.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0};

            stylePanel = new JPanel(gbl);
            stylePanel.add(getBoldCB(), gbc);
            stylePanel.add(getPreviewPanel(), gbc1);
            stylePanel.add(getItalicCB(), gbc2);
            stylePanel.add(getUlCB(), gbc3);
            stylePanel.add(getSpacerPanel(), gbc4);
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
            boldCB.setText(I18N.str("bold"));
            boldCB.addItemListener(e -> updatePreview());
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
            italicCB.setText(I18N.str("italic"));
            italicCB.addItemListener(e -> updatePreview());
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
            ulCB.setText(I18N.str("underline"));
            ulCB.addItemListener(e -> updatePreview());
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
            previewLabel = new JLabel("AaBbYyZz");
            previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            previewLabel.setVerticalAlignment(SwingConstants.CENTER);

            JPanel spacer = new JPanel(new BorderLayout());
            spacer.setBackground(Color.WHITE);
            spacer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            spacer.add(previewLabel, BorderLayout.CENTER);

            previewPanel = new JPanel(new BorderLayout());
            previewPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(null, I18N.str("preview")),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(5, 5, 5, 5),
                            BorderFactory.createBevelBorder(BevelBorder.LOWERED))));

            Dimension fixedSize = new Dimension(90, 100);
            previewPanel.setPreferredSize(fixedSize);
            previewPanel.setMaximumSize(fixedSize);
            previewPanel.setMinimumSize(fixedSize);
            previewPanel.add(spacer, BorderLayout.CENTER);
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
