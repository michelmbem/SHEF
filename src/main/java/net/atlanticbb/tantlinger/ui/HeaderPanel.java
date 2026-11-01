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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 1;
        gbc1.gridy = 0;
        gbc1.gridheight = 2;
        gbc1.insets = new Insets(5, 0, 5, 10);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        gbc2.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(0, 20, 5, 10);

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        titleLabel.setVerticalAlignment(SwingConstants.TOP);

        iconLabel = new JLabel();
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);

        msgLabel = new JLabel();
        msgLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        msgLabel.setVerticalAlignment(SwingConstants.TOP);

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {1.0, 0.0};
        gbl.rowWeights = new double[] {0.0, 1.0};

        setLayout(gbl);
        setBorder(BorderFactory.createLineBorder(Color.black, 1));
        add(titleLabel, gbc);
        add(iconLabel, gbc1);
        add(msgLabel, gbc2);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle bounds = getBounds();
        Color blue = new Color(153, 204, 255);
        Paint gradientPaint = new GradientPaint(bounds.width * 0.5f,
                bounds.y, Color.white, bounds.width, 0f, blue);

        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, bounds.width, bounds.height);
    }

}
