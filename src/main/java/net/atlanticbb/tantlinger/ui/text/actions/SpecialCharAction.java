package net.atlanticbb.tantlinger.ui.text.actions;

import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.dialogs.SpecialCharDialog;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SpecialCharAction extends BasicEditAction {
    private static final long serialVersionUID = 1L;
    SpecialCharDialog dialog;

    public SpecialCharAction() {
        super(i18n.str("special_character_"));
        putValue(SMALL_ICON, UIUtils.getIcon(UIUtils.X16, "copyright.png"));
        putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
    }

    protected void doEdit(ActionEvent e, JTextComponent ed) {
        Component c = SwingUtilities.getWindowAncestor(ed);
        if (dialog == null) {
            if (c instanceof Frame) {
                dialog = new SpecialCharDialog((Frame) c, ed);
            } else if (c instanceof Dialog) {
                dialog = new SpecialCharDialog((Dialog) c, ed);
            } else
                return;
        }

        dialog.setInsertEntity(getEditMode() == SOURCE);
        if (!dialog.isVisible()) {
            dialog.setLocationRelativeTo(c);
            dialog.setVisible(true);
        }
    }

    @Override
    protected void updateContextState(JTextComponent editor) {
        if (dialog != null) {
            dialog.setInsertEntity(getEditMode() == SOURCE);
            dialog.setJTextComponent(editor);
        }
    }

}
