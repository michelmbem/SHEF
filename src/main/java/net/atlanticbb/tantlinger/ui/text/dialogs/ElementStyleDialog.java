package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.OptionDialog;
import net.atlanticbb.tantlinger.ui.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ElementStyleDialog extends OptionDialog {
    private static final long serialVersionUID = 1L;

    private static final I18n i18n = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");

    private static Icon icon = UIUtils.getIcon(UIUtils.X48, "pencil.png");
    private static String title = i18n.str("element_style");
    private static String desc = i18n.str("element_style_desc");

    private StyleAttributesPanel stylePanel;

    public ElementStyleDialog(Frame parent) {
        super(parent, title, desc, icon);
        init();
    }

    public ElementStyleDialog(Dialog parent) {
        super(parent, title, desc, icon);
        init();
    }

    private void init() {
        stylePanel = new StyleAttributesPanel();
        setContentPane(stylePanel);
        pack();
        setSize(300, getHeight());
        setResizable(false);
    }

    public Map getStyleAttributes() {
        return stylePanel.getAttributes();
    }

    public void setStyleAttributes(Map attr) {
        stylePanel.setAttributes(attr);
    }
}
