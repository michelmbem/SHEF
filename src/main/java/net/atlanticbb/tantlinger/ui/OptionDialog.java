package net.atlanticbb.tantlinger.ui;

import javax.swing.*;
import java.awt.*;

public class OptionDialog extends StandardDialog {
    private static final long serialVersionUID = 1L;
    private JPanel internalContentPane;
    private Container contentPane;

    public OptionDialog(Frame parent, String headerTitle, String desc, Icon icon) {
        super(parent, headerTitle, BUTTONS_RIGHT);
        init(headerTitle, desc, icon);
    }

    public OptionDialog(Dialog parent, String headerTitle, String desc, Icon icon) {
        super(parent, headerTitle, BUTTONS_RIGHT);
        init(headerTitle, desc, icon);
    }

    private void init(String title, String desc, Icon icon) {
        internalContentPane = new JPanel(new BorderLayout());
        HeaderPanel hp = new HeaderPanel();
        hp.setTitle(title);
        hp.setDescription(desc);
        hp.setIcon(icon);
        internalContentPane.add(hp, BorderLayout.PAGE_START);

        super.setContentPane(internalContentPane);
    }

    @Override
    public Container getContentPane() {
        return contentPane;
    }

    @Override
    public void setContentPane(Container c) {
        contentPane = c;
        internalContentPane.add(c, BorderLayout.CENTER);
    }
}
