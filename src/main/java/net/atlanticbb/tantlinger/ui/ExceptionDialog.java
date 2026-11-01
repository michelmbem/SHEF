package net.atlanticbb.tantlinger.ui;

import net.atlanticbb.tantlinger.i18n.I18n;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionDialog extends JDialog {
    private static final I18n i18n = I18n.getInstance("net.atlanticbb.tantlinger.ui");
    private static final long serialVersionUID = 1L;
    private static final int PREFERRED_WIDTH = 450;
    private static final String DEFAULT_TITLE = i18n.str("error");

    private JPanel jContentPane = null;
    private JLabel iconLabel = null;
    private JLabel titleLabel = null;
    private JLabel msgLabel = null;
    private JPanel buttonPanel = null;
    private JButton okButton = null;
    private JButton detailsButton = null;
    private JScrollPane scrollPane = null;
    private JTextArea textArea = null;
    private JSeparator separator = null;

    public ExceptionDialog() {
        super();
        init(new Exception());
    }

    public ExceptionDialog(Frame owner, Throwable th) {
        super(owner, DEFAULT_TITLE);
        init(th);
    }

    public ExceptionDialog(Dialog owner, Throwable th) {
        super(owner, DEFAULT_TITLE);
        init(th);
    }

    private void init(Throwable th) {
        setModal(true);
        initialize();
        setThrowable(th);
        showDetails(false);
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        setContentPane(getJContentPane());
        getRootPane().setDefaultButton(getOkButton());
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = gbc.gridy = 0;
            gbc.gridheight = 3;
            gbc.anchor = GridBagConstraints.PAGE_START;

            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 1;
            gbc1.gridy = 0;
            gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc1.fill = GridBagConstraints.HORIZONTAL;
            gbc1.insets = new Insets(0, 10, 5, 5);

            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 2;
            gbc2.gridy = 0;
            gbc2.gridheight = 3;
            gbc2.anchor = GridBagConstraints.PAGE_START;
            gbc2.fill = GridBagConstraints.HORIZONTAL;
            gbc2.insets = new Insets(0, 0, 10, 0);

            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = gbc3.gridy = 1;
            gbc3.gridheight = 2;
            gbc3.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc3.fill = GridBagConstraints.BOTH;
            gbc3.insets = new Insets(0, 10, 10, 5);

            GridBagConstraints gbc4 = new GridBagConstraints();
            gbc4.gridx = 0;
            gbc4.gridy = 3;
            gbc4.gridwidth = 3;
            gbc4.fill = GridBagConstraints.BOTH;

            GridBagConstraints gbc5 = new GridBagConstraints();
            gbc5.gridx = 0;
            gbc5.gridy = 4;
            gbc5.gridwidth = 3;
            gbc5.fill = GridBagConstraints.BOTH;

            iconLabel = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));
            titleLabel = new JLabel(i18n.str("error_prompt"));
            msgLabel = new JLabel();

            Font titleFont = titleLabel.getFont();
            titleLabel.setFont(titleFont.deriveFont(Font.BOLD, titleFont.getSize() + 2f));

            GridBagLayout gbl = new GridBagLayout();
            gbl.columnWeights = new double[] {0.0, 1.0, 0.0};
            gbl.rowWeights = new double[] {0.0, 0.0, 0.0, 1.0, 0.0};

            jContentPane = new JPanel(gbl);
            jContentPane.setBorder(BorderFactory.createEmptyBorder(12, 5, 10, 5));
            jContentPane.add(iconLabel, gbc);
            jContentPane.add(titleLabel, gbc1);
            jContentPane.add(getButtonPanel(), gbc2);
            jContentPane.add(msgLabel, gbc3);
            jContentPane.add(getSeparator(), gbc4);
            jContentPane.add(getScrollPane(), gbc5);
        }

        return jContentPane;
    }

    /**
     * This method initializes buttonPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridLayout gl = new GridLayout(2, 1, 0, 5);
            buttonPanel = new JPanel(gl);
            buttonPanel.add(getOkButton());
            buttonPanel.add(getDetailsButton());
        }

        return buttonPanel;
    }

    /**
     * This method initializes okButton
     *
     * @return javax.swing.JButton
     */
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton(i18n.str("ok"));
            okButton.addActionListener(e -> dispose());
        }

        return okButton;
    }

    /**
     * This method initializes detailsButton
     *
     * @return javax.swing.JButton
     */
    private JButton getDetailsButton() {
        if (detailsButton == null) {
            detailsButton = new JButton(i18n.str("_details"));
            detailsButton.addActionListener(e -> toggleDetails());
        }

        return detailsButton;
    }

    private void toggleDetails() {
        showDetails(!isDetailsVisible());
    }

    public void showDetails(boolean b) {
        if (b) {
            detailsButton.setText(i18n.str("details_"));
            scrollPane.setVisible(true);
            separator.setVisible(false);
        } else {
            detailsButton.setText(i18n.str("_details"));
            scrollPane.setVisible(false);
            separator.setVisible(true);
        }

        setResizable(true);
        pack();
        setResizable(false);
    }

    public boolean isDetailsVisible() {
        return scrollPane.isVisible();
    }

    public void setThrowable(Throwable th) {
        String msg = i18n.str("no_message_given");
        if (th.getLocalizedMessage() != null && !th.getLocalizedMessage().isEmpty())
            msg = th.getLocalizedMessage();
        msgLabel.setText(msg);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        th.printStackTrace(new PrintStream(bout));
        String stackTrace = bout.toString();

        textArea.setText(stackTrace);
        textArea.setCaretPosition(0);
    }

    /**
     * @return a JSeparator that gets swapped out with the detail pane.
     */
    private JSeparator getSeparator() {
        if (separator == null) {
            separator = new JSeparator();
            separator.setPreferredSize(new Dimension(PREFERRED_WIDTH, 3));
        }

        return separator;
    }

    /**
     * This method initializes scrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setPreferredSize(new Dimension(PREFERRED_WIDTH, 200));
            scrollPane.setViewportView(getTextArea());
        }

        return scrollPane;
    }

    /**
     * This method initializes textArea
     *
     * @return javax.swing.JTextArea
     */
    private JTextArea getTextArea() {
        if (textArea == null) {
            textArea = new JTextArea();
            textArea.setTabSize(2);
            textArea.setEditable(false);
            textArea.setOpaque(false);
        }

        return textArea;
    }
}
