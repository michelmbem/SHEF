/*
 * Created on Jan 24, 2006
 *
 */
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
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final I18n i18n = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs"); //$NON-NLS-1$

    private static Icon icon = UIUtils.getIcon(UIUtils.X48, "copyright.png"); //$NON-NLS-1$
    private static String title = i18n.str("special_character"); //$NON-NLS-1$
    private static String desc = i18n.str("special_character_desc"); //$NON-NLS-1$

    private Font plainFont = new Font("Dialog", Font.PLAIN, 12); //$NON-NLS-1$
    private Font rollFont = new Font("Dialog", Font.BOLD, 14); //$NON-NLS-1$

    private MouseListener mouseHandler = new MouseHandler();
    private ActionListener buttonHandler = new ButtonHandler();


    private boolean insertEntity;

    private JTextComponent editor;

    public SpecialCharDialog(Frame parent, JTextComponent ed) {
        super(parent, title);
        editor = ed;
        init();
    }

    public SpecialCharDialog(Dialog parent, JTextComponent ed) {
        super(parent, title);
        editor = ed;
        init();
    }

    private void init() {
        JPanel headerPanel = new HeaderPanel(title, desc, icon);

        JPanel charPanel = new JPanel(new GridLayout(8, 12, 2, 2));
        charPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 160; i <= 255; i++) {
            String ent = "&#" + i + ";"; //$NON-NLS-1$ //$NON-NLS-2$
            JButton chLabel = new JButton(Entities.HTML32.unescape(ent));
            chLabel.setFont(plainFont);
            chLabel.setOpaque(true);
            chLabel.setToolTipText(ent);
            chLabel.setBackground(Color.white);
            chLabel.setHorizontalAlignment(SwingConstants.CENTER);
            chLabel.setVerticalAlignment(SwingConstants.CENTER);
            chLabel.addActionListener(buttonHandler);
            chLabel.addMouseListener(mouseHandler);
            chLabel.setMargin(new Insets(0, 0, 0, 0));
            charPanel.add(chLabel);
        }

        JButton close = new JButton(i18n.str("close")); //$NON-NLS-1$
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(close);
        this.getRootPane().setDefaultButton(close);

        //selectedLabel.setBorder(pressedBorder);
        //setContentPane(charPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(charPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        //setSize(414, 340);
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
        public void mouseEntered(MouseEvent e) {
            JButton l = (JButton) e.getComponent();
            l.setFont(rollFont);
            //l.setForeground(Color.BLUE);
        }

        public void mouseExited(MouseEvent e) {
            JButton l = (JButton) e.getComponent();
            l.setFont(plainFont);
            //l.setForeground(Color.BLACK);

        }
    }

    private class ButtonHandler implements ActionListener {

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            JButton l = (JButton) e.getSource();
            if (editor != null) {
                if (!editor.hasFocus())
                    editor.requestFocusInWindow();
                if (insertEntity)
                    editor.replaceSelection(l.getToolTipText());
                else {
                    editor.replaceSelection(l.getText());
                }
            }
        }

    }


}
