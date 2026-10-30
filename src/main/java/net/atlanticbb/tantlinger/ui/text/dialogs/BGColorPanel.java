package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class BGColorPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final I18n i18n = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");

    private JCheckBox bgColorCB = null;
    private JPanel colorPanel = null;
    private JButton colorButton = null;
    private Color selColor = Color.WHITE;

    /**
     * This is the default constructor
     */
    public BGColorPanel() {
        super();
        initialize();
    }

    public boolean isSelected() {
        return bgColorCB.isSelected();
    }

    public void setSelected(boolean sel) {
        bgColorCB.setSelected(sel);
        colorButton.setEnabled(sel);
    }

    public String getColor() {
        return HTMLUtils.colorToHex(selColor);
    }

    public void setColor(String hexColor) {
        selColor = HTMLUtils.stringToColor(hexColor);
        colorPanel.setBackground(selColor);
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 5);
        
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 1;
        gbc1.gridy = 0;
        gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc1.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 2;
        gbc2.gridy = 0;
        gbc2.fill = GridBagConstraints.BOTH;

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 3;
        gbc3.gridy = 0;
        gbc3.fill = GridBagConstraints.BOTH;

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {0.0, 0.0, 0.0, 1.0};
        gbl.rowWeights = new double[] {0.0};

        setLayout(gbl);
        add(getBgColorCB(), gbc);
        add(getColorPanel(), gbc1);
        add(getColorButton(), gbc2);
        add(new JLabel(), gbc3);

        colorButton.setEnabled(bgColorCB.isSelected());
    }

    /**
     * This method initializes bgColorCB
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBgColorCB() {
        if (bgColorCB == null) {
            bgColorCB = new JCheckBox();
            bgColorCB.setText(i18n.str("background"));

            bgColorCB.addItemListener(e -> {
                colorButton.setEnabled(bgColorCB.isSelected());

                if (bgColorCB.isSelected())
                    colorPanel.setBackground(selColor);
                else
                    colorPanel.setBackground(getBackground());
            });
        }
        
        return bgColorCB;
    }

    /**
     * This method initializes colorPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getColorPanel() {
        if (colorPanel == null) {
            colorPanel = new JPanel();
            colorPanel.setPreferredSize(new Dimension(50, 20));
            colorPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
        
        return colorPanel;
    }

    /**
     * This method initializes colorButton
     *
     * @return javax.swing.JButton
     */
    private JButton getColorButton() {
        if (colorButton == null) {
            colorButton = new JButton(UIUtils.getIcon(UIUtils.X16, "color.png"));
            colorButton.setToolTipText(i18n.str("choose_color"));
            colorButton.addActionListener(e -> {
                Color c = JColorChooser.showDialog(
                        BGColorPanel.this, i18n.str("color"), selColor);

                if (c != null) {
                    selColor = c;
                    colorPanel.setBackground(c);
                    colorPanel.setToolTipText(HTMLUtils.colorToHex(c));
                }
            });
        }

        return colorButton;
    }

}
