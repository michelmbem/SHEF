/*
 *  ===================================================
 *  This program contains code from the book "Swing"
 *  2nd Edition by Matthew Robinson and Pavel Vorobiev
 *  http://www.spindoczine.com/sbe
 *  ===================================================
 *
 * This class (Mostly) comes from the manning "Swing" book
 * by Matthew Robinson and Pavel Vorobiev.  I'd come across it
 * and only after i started writing my own finder/replacer..
 * So, rather than waste time reinventing the wheel,
 * I modified it to fit my needs.
 * -BT
 *
 */
package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.TextEditPopupManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A find and replace dialog for JTextComponents
 *
 */
public class TextFinderDialog extends JDialog {
    public static final int FIND = 0;
    public static final int REPLACE = 1;
    protected static final char[] WORD_SEPARATORS = {
            ' ', '\t', '\n', '\r', '\f', '.', ',', ':', '-', '(', ')',
            '[', ']', '{', '}', '<', '>', '/', '|', '\\', '\'', '\"'};
    private static final long serialVersionUID = 1L;
    private static final I18n I18N = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");
    private static final String TITLE = I18N.str("find_and_replace");

    private final ImageIcon warnIcon = UIUtils.getIcon(UIUtils.X48, "error.png");
    private final TextEditPopupManager popupManager = TextEditPopupManager.getInstance();

    protected Frame owner;
    protected JTextComponent monitor;
    protected JTabbedPane tabbedPane;
    protected JTextField findField1;
    protected JTextField findField2;
    protected Document findDoc;
    protected Document replaceDoc;
    protected ButtonModel wordModel;
    protected ButtonModel caseModel;
    protected ButtonModel searchUpModel;
    protected ButtonModel searchDownModel;
    protected boolean searchUp = false;
    protected String searchData;

    public TextFinderDialog(Frame owner, JTextComponent tc, int index) {
        super(owner, TITLE, false);
        init(tc, index);
    }

    public TextFinderDialog(Dialog owner, JTextComponent tc, int index) {
        super(owner, TITLE, false);
        init(tc, index);
    }

    private void init(JTextComponent textComponent, int index) {
        setMonitor(textComponent);
        createTabbedPane(index);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        addWindowListener(new FindReplaceWindowListener());
        pack();
        setSize(550, 225);
        setResizable(false);
    }

    private void createTabbedPane(int index) {
        tabbedPane = new JTabbedPane(SwingConstants.TOP);
        tabbedPane.addTab(I18N.str("find"), createFindPanel());
        tabbedPane.addTab(I18N.str("replace"), createReplacePanel());
        tabbedPane.setSelectedIndex(index);
    }

    private JPanel createFindPanel() {
        findField1 = new JTextField();
        findDoc = findField1.getDocument();
        popupManager.registerJTextComponent(findField1);

        JCheckBox matchWordCheck = new JCheckBox(I18N.str("whole_words_only"));
        matchWordCheck.setMnemonic('w');
        wordModel = matchWordCheck.getModel();

        JCheckBox matchCaseCheck = new JCheckBox(I18N.str("match_case"));
        matchCaseCheck.setMnemonic('c');
        caseModel = matchCaseCheck.getModel();

        JRadioButton searchUpRadio = new JRadioButton(I18N.str("search_up"));
        searchUpRadio.setMnemonic('u');
        searchUpModel = searchUpRadio.getModel();

        JRadioButton searchDownRadio = new JRadioButton(I18N.str("search_down"), true);
        searchDownRadio.setMnemonic('d');
        searchDownModel = searchDownRadio.getModel();

        ButtonGroup optionGroup = new ButtonGroup();
        optionGroup.add(searchUpRadio);
        optionGroup.add(searchDownRadio);

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {0.0, 1.0};
        gbl.rowWeights = new double[] {0.0, 1.0};

        JPanel fieldPane = new JPanel(gbl);
        fieldPane.setBorder(new EmptyBorder(8, 5, 8, 0));

        GridBagConstraints gbc = new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0);

        fieldPane.add(new JLabel(I18N.str("find_what")), gbc);

        gbc.gridx++;
        gbc.insets = new Insets(0, 5, 0, 0);
        fieldPane.add(findField1, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        fieldPane.add(new JLabel(), gbc);

        JPanel optionPane = new JPanel(new GridLayout(2, 2, 8, 2));
        optionPane.setBorder(new TitledBorder(new EtchedBorder(), I18N.str("options")));
        optionPane.add(matchWordCheck);
        optionPane.add(matchCaseCheck);
        optionPane.add(searchUpRadio);
        optionPane.add(searchDownRadio);

        JPanel entryPane = new JPanel(new BorderLayout());
        entryPane.add(fieldPane, BorderLayout.CENTER);
        entryPane.add(optionPane, BorderLayout.PAGE_END);

        JButton findButton = new JButton(I18N.str("find_next"));
        findButton.setMnemonic('f');
        findButton.addActionListener(e -> findNext(false, true));

        JButton closeButton = new JButton(I18N.str("close"));
        closeButton.addActionListener(e -> setVisible(false));
        closeButton.setDefaultCapable(true);

        JPanel sidePane = new JPanel();
        sidePane.setLayout(new BoxLayout(sidePane, BoxLayout.PAGE_AXIS));
        sidePane.add(findButton);
        sidePane.add(closeButton);
        sidePane.add(Box.createGlue());
        stretchContentHorizontally(sidePane);

        JPanel findPanel = new JPanel(new BorderLayout());
        findPanel.add(entryPane, BorderLayout.CENTER);
        findPanel.add(sidePane, BorderLayout.LINE_END);
        return findPanel;
    }

    private JPanel createReplacePanel() {
        findField2 = new JTextField();
        findField2.setDocument(findDoc);
        popupManager.registerJTextComponent(findField2);

        JTextField replaceField = new JTextField();
        replaceDoc = replaceField.getDocument();
        popupManager.registerJTextComponent(replaceField);

        JCheckBox matchWordCheck = new JCheckBox(I18N.str("whole_words_only"));
        matchWordCheck.setMnemonic('w');
        wordModel = matchWordCheck.getModel();

        JCheckBox matchCaseCheck = new JCheckBox(I18N.str("match_case"));
        matchCaseCheck.setMnemonic('c');
        caseModel = matchCaseCheck.getModel();

        JRadioButton searchUpRadio = new JRadioButton(I18N.str("search_up"));
        searchUpRadio.setMnemonic('u');
        searchUpModel = searchUpRadio.getModel();

        JRadioButton searchDownRadio = new JRadioButton(I18N.str("search_down"), true);
        searchDownRadio.setMnemonic('d');
        searchDownModel = searchDownRadio.getModel();

        ButtonGroup optionGroup = new ButtonGroup();
        optionGroup.add(searchUpRadio);
        optionGroup.add(searchDownRadio);

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWeights = new double[] {0.0, 1.0};
        gbl.rowWeights = new double[] {0.0, 0.0, 1.0};

        JPanel fieldPane = new JPanel(gbl);
        fieldPane.setBorder(new EmptyBorder(8, 5, 8, 0));

        GridBagConstraints gbc = new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.BASELINE_LEADING, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 5, 0), 0, 0);

        fieldPane.add(new JLabel(I18N.str("find_what")), gbc);

        gbc.gridx++;
        gbc.insets = new Insets(0, 5, 5, 0);
        fieldPane.add(findField2, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        fieldPane.add(new JLabel(I18N.str("replace")), gbc);

        gbc.gridx++;
        gbc.insets = new Insets(0, 5, 0, 0);
        fieldPane.add(replaceField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        fieldPane.add(new JLabel(), gbc);

        JPanel optionPane = new JPanel(new GridLayout(2, 2, 8, 2));
        optionPane.setBorder(new TitledBorder(new EtchedBorder(), I18N.str("options")));
        optionPane.add(matchWordCheck);
        optionPane.add(matchCaseCheck);
        optionPane.add(searchUpRadio);
        optionPane.add(searchDownRadio);

        JPanel entryPane = new JPanel(new BorderLayout());
        entryPane.add(fieldPane, BorderLayout.CENTER);
        entryPane.add(optionPane, BorderLayout.PAGE_END);

        JButton replaceButton = new JButton(I18N.str("replace"));
        replaceButton.setMnemonic('r');
        replaceButton.addActionListener(e -> findNext(true, true));

        JButton replaceAllButton = new JButton(I18N.str("replace_all"));
        replaceAllButton.setMnemonic('a');
        replaceAllButton.addActionListener(e -> replaceAll());

        JButton closeButton = new JButton(I18N.str("close"));
        closeButton.addActionListener(e -> setVisible(false));
        closeButton.setDefaultCapable(true);

        JPanel sidePane = new JPanel();
        sidePane.setLayout(new BoxLayout(sidePane, BoxLayout.PAGE_AXIS));
        sidePane.add(replaceButton);
        sidePane.add(replaceAllButton);
        sidePane.add(closeButton);
        sidePane.add(Box.createGlue());
        stretchContentHorizontally(sidePane);

        JPanel replacePanel = new JPanel(new BorderLayout());
        replacePanel.add(entryPane, BorderLayout.CENTER);
        replacePanel.add(sidePane, BorderLayout.LINE_END);
        return replacePanel;
    }

    private static void stretchContentHorizontally(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                Dimension prefSize = jc.getPreferredSize();
                jc.setMaximumSize(new Dimension(Integer.MAX_VALUE, prefSize.height));
            }
        }
    }

    public JTextComponent getMonitor() {
        return monitor;
    }

    public void setMonitor(JTextComponent textComponent) {
        monitor = textComponent;
        if (!monitor.hasFocus())
            monitor.requestFocusInWindow();
    }

    public int getSelectedIndex() {
        return tabbedPane.getSelectedIndex();
    }

    public void setSelectedIndex(int index) {
        tabbedPane.setSelectedIndex(index);
    }

    public void show(int index) {
        setSelectedIndex(index);
        setLocationRelativeTo(owner);
        setVisible(true);
        if (!monitor.hasFocus())
            monitor.requestFocusInWindow();
    }

    public int findNext(boolean doReplace, boolean showWarnings) {
        int pos = monitor.getCaretPosition();

        String key = "";
        try {
            key = findDoc.getText(0, findDoc.getLength());
        } catch (BadLocationException ignored) {
        }

        if (key.isEmpty()) {
            warning(I18N.str("no_target_prompt"));
            return -1;
        }

        if (wordModel.isSelected()) {
            for (char wordSeparator : WORD_SEPARATORS) {
                if (key.indexOf(wordSeparator) >= 0) {
                    warning(I18N.str("illegal_character_prompt") +
                            " '" + wordSeparator + "'");
                    return -1;
                }
            }
        }

        String replacement = "";
        if (doReplace) {
            try {
                replacement = replaceDoc.getText(0, replaceDoc.getLength());
            } catch (BadLocationException ignored) {
            }
        }

        if (searchUpModel.isSelected() != searchUp)
            searchUp = searchUpModel.isSelected();

        String searchQuery;
        try {
            searchQuery = monitor.getDocument().getText(
                    0, monitor.getDocument().getLength());
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }

        if (!caseModel.isSelected()) {
            searchQuery = searchQuery.toLowerCase();
            key = key.toLowerCase();
        }

        int index;
        while (true) {
            if (!searchUp)
                index = searchQuery.indexOf(key, pos);
            else
                index = searchQuery.lastIndexOf(key, pos - 1);

            if (index < 0 || index >= searchQuery.length()) {
                if (showWarnings)
                    warning(I18N.str("text_not_found"));
                return 0;
            }

            if (wordModel.isSelected()) {
                boolean s1 = index > 0;
                boolean b1 = s1 && !isSeparator(searchQuery.charAt(index - 1));
                boolean s2 = (index + key.length()) < searchQuery.length();
                boolean b2 = s2 && !isSeparator(searchQuery.charAt(index + key.length()));

                if (b1 || b2) {
                    if (!searchUp && s2) {
                        pos = index + key.length();
                        continue;
                    }

                    if (searchUp && s1) {
                        pos = index;
                        continue;
                    }

                    if (showWarnings)
                        warning(I18N.str("text_not_found"));
                    return 0;
                }
            }

            break;
        }

        if (doReplace) {
            setSelection(index, index + key.length(), searchUp);
            monitor.replaceSelection(replacement);
            setSelection(index, index + replacement.length(), searchUp);

        } else
            setSelection(index, index + key.length(), searchUp);

        return 1;
    }

    public void replaceAll() {
        int counter = 0;
        while (true) {
            int result = findNext(true, false);
            if (result < 0) return;
            if (result == 0) break;
            counter++;
        }

        JOptionPane.showMessageDialog(owner,
                counter + " " + I18N.str("replacements_prompt"), "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void setSelection(int xStart, int xFinish, boolean moveUp) {
        if (moveUp) {
            monitor.setCaretPosition(xFinish);
            monitor.moveCaretPosition(xStart);
        } else {
            monitor.setCaretPosition(xStart);
            monitor.moveCaretPosition(xFinish);
        }
    }

    protected boolean isSeparator(char ch) {
        for (char wordSeparator : WORD_SEPARATORS)
            if (ch == wordSeparator)
                return true;
        return false;
    }

    protected void warning(String message) {
        JOptionPane.showMessageDialog(owner,
                message, TITLE, JOptionPane.INFORMATION_MESSAGE, warnIcon);
    }

    private class FindReplaceWindowListener extends WindowAdapter {
        @Override
        public void windowActivated(WindowEvent e) {
            String selectedText = monitor.getSelectedText();
            boolean hasSelection = selectedText != null && !selectedText.isBlank();

            if (tabbedPane.getSelectedIndex() == 0) {
                if (hasSelection) {
                    findField1.setText(selectedText);
                    findField1.selectAll();
                }

                if (!findField1.hasFocus()) {
                    findField1.requestFocusInWindow();
                }
            } else {
                if (hasSelection) {
                    findField2.setText(selectedText);
                    findField2.selectAll();
                }

                if (!findField2.hasFocus()) {
                    findField2.requestFocusInWindow();
                }
            }
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            searchData = null;
        }
    }
}
