package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.OptionDialog;
import net.atlanticbb.tantlinger.ui.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ElementStyleDialog extends OptionDialog {
    private static final long serialVersionUID = 1L;
    private static final I18n I18N = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");
    private static final Icon ICON = UIUtils.getIcon(UIUtils.X48, "pencil.png");
    private static final String TITLE = I18N.str("element_style");
    private static final String DESC = I18N.str("element_style_desc");

    private StyleAttributesPanel stylePanel;

    public ElementStyleDialog(Frame parent) {
        super(parent, TITLE, DESC, ICON);
        init();
    }

    public ElementStyleDialog(Dialog parent) {
        super(parent, TITLE, DESC, ICON);
        init();
    }

    private void init() {
        stylePanel = new StyleAttributesPanel();
        setContentPane(stylePanel);
        pack();
        setSize(300, getHeight());
        setResizable(false);
    }

    public Map<String, String> getStyleAttributes() {
        return stylePanel.getAttributes();
    }

    public void setStyleAttributes(Map<String, String> attr) {
        stylePanel.setAttributes(attr);
    }
}
