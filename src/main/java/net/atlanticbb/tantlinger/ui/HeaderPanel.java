package net.atlanticbb.tantlinger.ui;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel titleLabel = null;
    private JLabel msgLabel = null;
    private JLabel iconLabel = null;

    /**
     * This is the default constructor
     */
    public HeaderPanel() {
        super();
        initialize();
    }

    public HeaderPanel(String title, String desc, Icon ico) {
        super();
        initialize();
        setTitle(title);
        setDescription(desc);
        setIcon(ico);

    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setDescription(String desc) {
        msgLabel.setText(desc);
    }

    public void setIcon(Icon icon) {
        iconLabel.setIcon(icon);
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 1;
        gbc3.gridheight = 2;
        gbc3.insets = new Insets(0, 5, 0, 10);
        gbc3.gridy = 0;
        iconLabel = new JLabel();
        iconLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.insets = new Insets(2, 25, 0, 0);
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.weightx = 1.0;
        gbc2.weighty = 1.0;
        gbc2.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc2.gridy = 1;
        msgLabel = new JLabel();
        msgLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        msgLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(6, 10, 0, 0);
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        titleLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        this.setLayout(new GridBagLayout());
        this.setSize(360, 56);
        this.setPreferredSize(new Dimension(360, 56));
        this.add(titleLabel, gbc);
        this.add(msgLabel, gbc2);
        this.add(iconLabel, gbc3);

        setBorder(BorderFactory.createLineBorder(Color.black, 1));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle bounds = getBounds();

        // Set Paint for filling Shape
        Color blue = new Color(153, 204, 255);

        Paint gradientPaint = new GradientPaint(bounds.width * 0.5f,
                bounds.y, Color.white, bounds.width, 0f, blue);
        g2.setPaint(gradientPaint);
        g2.fillRect(0, 0, bounds.width, bounds.height);

    }

}
