package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.HeaderPanel;
import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.Entities;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;

public class SpecialCharDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final I18n I18N = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");
    private static final Icon ICON = UIUtils.getIcon(UIUtils.X48, "copyright.png");
    private static final String TITLE = I18N.str("special_character");
    private static final String DESC = I18N.str("special_character_desc");

    private final Font plainFont = new Font("Dialog", Font.PLAIN, 12);
    private final Font rollFont = new Font("Dialog", Font.BOLD, 14);
    private final MouseListener mouseHandler = new MouseHandler();
    private final ActionListener buttonHandler = new ButtonHandler();

    private boolean insertEntity;
    private JTextComponent editor;

    public SpecialCharDialog(Frame parent, JTextComponent ed) {
        super(parent, TITLE);
        editor = ed;
        init();
    }

    public SpecialCharDialog(Dialog parent, JTextComponent ed) {
        super(parent, TITLE);
        editor = ed;
        init();
    }

    private void init() {
        JPanel headerPanel = new HeaderPanel(TITLE, DESC, ICON);

        JPanel charPanel = new JPanel(new GridLayout(8, 12, 2, 2));
        charPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 160; i <= 255; i++) {
            String ent = "&#" + i + ";";
            JButton chButton = new JButton(Entities.HTML32.unescape(ent));
            chButton.setFont(plainFont);
            chButton.setOpaque(true);
            chButton.setToolTipText(ent);
            chButton.setBackground(Color.WHITE);
            chButton.setHorizontalAlignment(SwingConstants.CENTER);
            chButton.setVerticalAlignment(SwingConstants.CENTER);
            chButton.addActionListener(buttonHandler);
            chButton.addMouseListener(mouseHandler);
            chButton.setMargin(new Insets(0, 0, 0, 0));
            chButton.setPreferredSize(new Dimension(24, 24));
            charPanel.add(chButton);
        }

        JButton closeButton = new JButton(I18N.str("close"));
        closeButton.addActionListener(e -> setVisible(false));
        getRootPane().setDefaultButton(closeButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.PAGE_START);
        getContentPane().add(charPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
    }

    public JTextComponent getJTextComponent() {
        return editor;
    }

    public void setJTextComponent(JTextComponent ed) {
        editor = ed;
    }

    /**
     * @return the insertEntity
     */
    public boolean isInsertEntity() {
        return insertEntity;
    }

    /**
     * @param insertEntity the insertEntity to set
     */
    public void setInsertEntity(boolean insertEntity) {
        this.insertEntity = insertEntity;
    }

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            JButton button = (JButton) e.getComponent();
            button.setFont(rollFont);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JButton button = (JButton) e.getComponent();
            button.setFont(plainFont);
        }
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();

            if (editor != null) {
                if (!editor.hasFocus())
                    editor.requestFocusInWindow();
                if (insertEntity)
                    editor.replaceSelection(button.getToolTipText());
                else {
                    editor.replaceSelection(button.getText());
                }
            }
        }
    }

}
