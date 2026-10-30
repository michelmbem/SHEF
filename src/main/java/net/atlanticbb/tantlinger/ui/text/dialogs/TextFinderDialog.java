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
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

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
    protected JTextField txtFind1;
    protected JTextField txtFind2;
    protected Document docFind;
    protected Document docReplace;
    protected ButtonModel modelWord;
    protected ButtonModel modelCase;
    protected ButtonModel modelUp;
    protected ButtonModel modelDown;
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

    private void init(JTextComponent tc, int index) {
        setJTextComponent(tc);

        tabbedPane = new JTabbedPane(SwingConstants.TOP);

        // "Find" panel
        JPanel p1 = new JPanel(new BorderLayout());

        JPanel pc1 = new JPanel(new BorderLayout());

        JPanel pf = new JPanel();
        pf.setLayout(new DialogLayout(20, 5));
        pf.setBorder(new EmptyBorder(8, 5, 8, 0));
        pf.add(new JLabel(I18N.str("find_what")));

        txtFind1 = new JTextField();
        docFind = txtFind1.getDocument();
        pf.add(txtFind1);
        pc1.add(pf, BorderLayout.CENTER);
        popupManager.registerJTextComponent(txtFind1);

        JPanel po = new JPanel(new GridLayout(2, 2, 8, 2));
        po.setBorder(new TitledBorder(new EtchedBorder(), I18N.str("options")));

        JCheckBox chkWord = new JCheckBox(I18N.str("whole_words_only"));
        chkWord.setMnemonic('w');
        modelWord = chkWord.getModel();
        po.add(chkWord);

        ButtonGroup bg = new ButtonGroup();
        JRadioButton rdUp = new JRadioButton(I18N.str("search_up"));
        rdUp.setMnemonic('u');
        modelUp = rdUp.getModel();
        bg.add(rdUp);
        po.add(rdUp);

        JCheckBox chkCase = new JCheckBox(I18N.str("match_case"));
        chkCase.setMnemonic('c');
        modelCase = chkCase.getModel();
        po.add(chkCase);

        JRadioButton rdDown = new JRadioButton(I18N.str("search_down"), true);
        rdDown.setMnemonic('d');
        modelDown = rdDown.getModel();
        bg.add(rdDown);
        po.add(rdDown);
        pc1.add(po, BorderLayout.PAGE_END);

        p1.add(pc1, BorderLayout.CENTER);

        JPanel p01 = new JPanel(new FlowLayout());
        JPanel p = new JPanel(new GridLayout(2, 1, 2, 8));

        ActionListener findAction = e -> findNext(false, true);

        JButton btFind = new JButton(I18N.str("find_next"));
        btFind.addActionListener(findAction);
        btFind.setMnemonic('f');
        p.add(btFind);

        ActionListener closeAction = e -> setVisible(false);

        JButton btClose = new JButton(I18N.str("close"));
        btClose.addActionListener(closeAction);
        btClose.setDefaultCapable(true);
        p.add(btClose);
        p01.add(p);
        p1.add(p01, BorderLayout.EAST);

        tabbedPane.addTab(I18N.str("find"), p1);

        // "Replace" panel
        JPanel p2 = new JPanel(new BorderLayout());

        JPanel pc2 = new JPanel(new BorderLayout());

        JPanel pc = new JPanel();
        pc.setLayout(new DialogLayout(20, 5));
        pc.setBorder(new EmptyBorder(8, 5, 8, 0));

        pc.add(new JLabel(I18N.str("find_what")));
        txtFind2 = new JTextField();
        txtFind2.setDocument(docFind);
        pc.add(txtFind2);
        popupManager.registerJTextComponent(txtFind2);

        pc.add(new JLabel(I18N.str("replace")));
        JTextField txtReplace = new JTextField();
        docReplace = txtReplace.getDocument();
        pc.add(txtReplace);
        pc2.add(pc, BorderLayout.CENTER);
        popupManager.registerJTextComponent(txtReplace);

        po = new JPanel(new GridLayout(2, 2, 8, 2));
        po.setBorder(new TitledBorder(new EtchedBorder(), I18N.str("options")));

        chkWord = new JCheckBox(I18N.str("whole_words_only"));
        chkWord.setMnemonic('w');
        chkWord.setModel(modelWord);
        po.add(chkWord);

        bg = new ButtonGroup();
        rdUp = new JRadioButton(I18N.str("search_up"));
        rdUp.setMnemonic('u');
        rdUp.setModel(modelUp);
        bg.add(rdUp);
        po.add(rdUp);

        chkCase = new JCheckBox(I18N.str("match_case"));
        chkCase.setMnemonic('c');
        chkCase.setModel(modelCase);
        po.add(chkCase);

        rdDown = new JRadioButton(I18N.str("search_down"), true);
        rdDown.setMnemonic('d');
        rdDown.setModel(modelDown);
        bg.add(rdDown);
        po.add(rdDown);
        pc2.add(po, BorderLayout.PAGE_END);

        p2.add(pc2, BorderLayout.CENTER);

        JPanel p02 = new JPanel(new FlowLayout());
        p = new JPanel(new GridLayout(3, 1, 2, 8));

        ActionListener replaceAction = e -> findNext(true, true);

        JButton btReplace = new JButton(I18N.str("replace"));
        btReplace.addActionListener(replaceAction);
        btReplace.setMnemonic('r');
        p.add(btReplace);

        ActionListener replaceAllAction = e -> {
            int counter = 0;
            while (true) {
                int result = findNext(true, false);
                if (result < 0)    // error
                    return;
                else if (result == 0)    // no more
                    break;
                counter++;
            }
            JOptionPane.showMessageDialog(owner,
                    counter + " " + I18N.str("replacements_prompt"), "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        };

        JButton btReplaceAll = new JButton(I18N.str("replace_all"));
        btReplaceAll.addActionListener(replaceAllAction);
        btReplaceAll.setMnemonic('a');
        p.add(btReplaceAll);

        btClose = new JButton(I18N.str("close"));
        btClose.addActionListener(closeAction);
        btClose.setDefaultCapable(true);
        p.add(btClose);
        p02.add(p);
        p2.add(p02, BorderLayout.EAST);

        // Make button columns the same size
        p01.setPreferredSize(p02.getPreferredSize());

        tabbedPane.addTab(I18N.str("replace"), p2);

        tabbedPane.setSelectedIndex(index);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        WindowListener flst = new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                //searchIndex = -1;
                if (tabbedPane.getSelectedIndex() == 0)
                    if (!txtFind1.hasFocus())
                        txtFind1.requestFocusInWindow();
                    else if (!txtFind2.hasFocus())
                        txtFind2.requestFocusInWindow();

            }

            public void windowDeactivated(WindowEvent e) {
                searchData = null;
            }
        };

        addWindowListener(flst);

        pack();
        setResizable(false);
    }

    public JTextComponent getJTextComponent() {
        return monitor;
    }

    public void setJTextComponent(JTextComponent tc) {
        monitor = tc;
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
            key = docFind.getText(0, docFind.getLength());
        } catch (BadLocationException ignored) {
        }

        if (key.isEmpty()) {
            warning(I18N.str("no_target_prompt"));
            return -1;
        }

        if (modelWord.isSelected()) {
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
                replacement = docReplace.getText(0, docReplace.getLength());
            } catch (BadLocationException ignored) {
            }
        }

        if (modelUp.isSelected() != searchUp)
            searchUp = modelUp.isSelected();

        String searchQuery;
        try {
            searchQuery = monitor.getDocument().getText(
                    0, monitor.getDocument().getLength());
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }

        if (!modelCase.isSelected()) {
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

            if (modelWord.isSelected()) {
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

    private static class DialogLayout implements LayoutManager {
        protected static final int COMP_TWO_COL = 0;
        protected static final int COMP_BIG = 1;
        protected static final int COMP_BUTTON = 2;

        protected int mDivider = -1;
        protected int mHGap = 10;
        protected int mVGap = 5;
        protected List<Component> components = new ArrayList<>();

        public DialogLayout() {
        }

        public DialogLayout(int hGap, int vGap) {
            mHGap = hGap;
            mVGap = vGap;
        }

        public void addLayoutComponent(String name, Component comp) {
            // Does nothing
        }

        public void removeLayoutComponent(Component comp) {
            // Does nothing
        }

        public Dimension preferredLayoutSize(Container parent) {
            components.clear();

            int w = 0;
            int h = 0;
            int type = -1;

            for (int k = 0; k < parent.getComponentCount(); k++) {
                Component comp = parent.getComponent(k);
                int newType = getLayoutType(comp);

                if (k == 0) type = newType;

                if (type != newType) {
                    Dimension d = preferredLayoutSize(components, type);
                    w = Math.max(w, d.width);
                    h += d.height + mVGap;
                    components.clear();
                    type = newType;
                }

                components.add(comp);
            }

            Dimension d = preferredLayoutSize(components, type);
            w = Math.max(w, d.width);
            h += d.height;

            Insets insets = parent.getInsets();
            return new Dimension(
                    w + insets.left + insets.right,
                    h + insets.top + insets.bottom);
        }

        protected Dimension preferredLayoutSize(List<Component> list, int type) {
            int w = 0;
            int h = 0;

            switch (type) {
                case COMP_TWO_COL:
                    int divider = getDivider(list);
                    for (int k = 1; k < list.size(); k += 2) {
                        Component comp = list.get(k);
                        Dimension d = comp.getPreferredSize();
                        w = Math.max(w, d.width);
                        h += d.height + mVGap;
                    }

                    h -= mVGap;
                    return new Dimension(divider + w, h);
                case COMP_BIG:
                    for (Component comp : list) {
                        Dimension d = comp.getPreferredSize();
                        w = Math.max(w, d.width);
                        h += d.height + mVGap;
                    }

                    h -= mVGap;
                    return new Dimension(w, h);
                case COMP_BUTTON:
                    Dimension d = getMaxDimension(list);
                    w = d.width + mHGap;
                    h = d.height;
                    return new Dimension(w * list.size() - mHGap, h);
            }

            throw new IllegalArgumentException("Illegal type " + type);
        }

        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        public void layoutContainer(Container parent) {
            components.clear();
            int type = -1;

            Insets insets = parent.getInsets();
            int w = parent.getWidth() - insets.left - insets.right;
            int x = insets.left;
            int y = insets.top;

            for (int k = 0; k < parent.getComponentCount(); k++) {
                Component comp = parent.getComponent(k);
                int newType = getLayoutType(comp);

                if (k == 0) type = newType;

                if (type != newType) {
                    y = layoutComponents(components, type, x, y, w);
                    components.clear();
                    type = newType;
                }

                components.add(comp);
            }

            components.clear();
        }

        protected int layoutComponents(List<Component> list, int type, int x, int y, int w) {
            switch (type) {
                case COMP_TWO_COL:
                    int divider = getDivider(list);
                    for (int k = 1; k < list.size(); k += 2) {
                        Component comp1 = list.get(k - 1);
                        Component comp2 = list.get(k);
                        Dimension d = comp2.getPreferredSize();

                        comp1.setBounds(x, y, divider, d.height);
                        comp2.setBounds(x + divider, y, w - divider, d.height);
                        y += d.height + mVGap;
                    }

                    return y;
                case COMP_BIG:
                    for (Component comp : list) {
                        Dimension d = comp.getPreferredSize();
                        comp.setBounds(x, y, w, d.height);
                        y += d.height + mVGap;
                    }

                    return y;
                case COMP_BUTTON:
                    Dimension d = getMaxDimension(list);
                    int ww = d.width * list.size() + mHGap * (list.size() - 1);
                    int xx = x + Math.max(0, (w - ww) / 2);

                    for (Component comp : list) {
                        comp.setBounds(xx, y, d.width, d.height);
                        xx += d.width + mHGap;
                    }

                    return y + d.height;
            }

            throw new IllegalArgumentException("Illegal type " + type);
        }

        public int getHGap() {
            return mHGap;
        }

        public int getVGap() {
            return mVGap;
        }

        public int getDivider() {
            return mDivider;
        }

        public void setDivider(int divider) {
            if (divider > 0) mDivider = divider;
        }

        protected int getDivider(List<Component> list) {
            if (mDivider > 0) return mDivider;

            int divider = 0;
            for (int k = 0; k < list.size(); k += 2) {
                Component comp = list.get(k);
                Dimension d = comp.getPreferredSize();
                divider = Math.max(divider, d.width);
            }

            divider += mHGap;
            return divider;
        }

        protected Dimension getMaxDimension(List<Component> list) {
            int w = 0;
            int h = 0;

            for (Component comp : list) {
                Dimension d = comp.getPreferredSize();
                w = Math.max(w, d.width);
                h = Math.max(h, d.height);
            }

            return new Dimension(w, h);
        }

        protected int getLayoutType(Component comp) {
            if (comp instanceof AbstractButton)
                return COMP_BUTTON;
            else if (comp instanceof JPanel ||
                    comp instanceof JScrollPane ||
                    comp instanceof JTabbedPane)
                return COMP_BIG;
            else
                return COMP_TWO_COL;
        }
    }
}
