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
    private static final String DEFAULT_TITLE = i18n.str("error");  //  @jve:decl-index=0:

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

    /**
     * @param owner
     */
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
        this.setContentPane(getJContentPane());
        getRootPane().setDefaultButton(getOkButton());
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gbc4 = new GridBagConstraints();
            gbc4.gridx = 0;
            gbc4.gridwidth = 3;
            gbc4.fill = GridBagConstraints.HORIZONTAL;
            gbc4.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc4.insets = new Insets(0, 0, 0, 0);
            gbc4.gridy = 3;
            GridBagConstraints gbc11 = new GridBagConstraints();
            gbc11.fill = GridBagConstraints.BOTH;
            gbc11.gridy = 4;
            gbc11.weightx = 1.0;
            gbc11.weighty = 1.0;
            gbc11.gridwidth = 3;
            gbc11.insets = new Insets(0, 0, 0, 0);
            gbc11.gridx = 0;
            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = 2;
            gbc3.fill = GridBagConstraints.VERTICAL;
            gbc3.gridheight = 3;
            gbc3.insets = new Insets(0, 0, 10, 0);
            gbc3.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc3.gridy = 0;
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 1;
            gbc2.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc2.insets = new Insets(0, 20, 5, 5);
            gbc2.gridy = 1;
            msgLabel = new JLabel();
            msgLabel.setText("");
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 1;
            gbc1.anchor = GridBagConstraints.BASELINE_LEADING;
            gbc1.insets = new Insets(0, 10, 5, 5);
            gbc1.fill = GridBagConstraints.NONE;
            gbc1.weightx = 1.0;
            gbc1.gridy = 0;
            titleLabel = new JLabel();
            titleLabel.setText(i18n.str("error_prompt"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridheight = 3;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.weighty = 0.0;
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.gridy = 0;
            iconLabel = new JLabel();
            iconLabel.setText("");
            iconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.setBorder(BorderFactory.createEmptyBorder(12, 5, 10, 5));
            jContentPane.add(iconLabel, gbc);
            jContentPane.add(titleLabel, gbc1);
            jContentPane.add(msgLabel, gbc2);
            jContentPane.add(getButtonPanel(), gbc3);
            jContentPane.add(getScrollPane(), gbc11);
            jContentPane.add(getSeparator(), gbc4);
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
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(2);
            gridLayout.setHgap(0);
            gridLayout.setVgap(5);
            gridLayout.setColumns(1);
            buttonPanel = new JPanel();
            buttonPanel.setLayout(gridLayout);
            buttonPanel.add(getOkButton(), null);
            buttonPanel.add(getDetailsButton(), null);
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
            okButton = new JButton();
            okButton.setText(i18n.str("ok"));
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
            detailsButton = new JButton();
            detailsButton.setText(i18n.str("_details"));
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

        String stackTrace = new String(bout.toByteArray());
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
