/*
 * Created on Jan 13, 2006
 *
 */
package net.atlanticbb.tantlinger.ui.text.actions;

import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;
import net.atlanticbb.tantlinger.ui.text.dialogs.ImageDialog;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTML;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * Action which desplays a dialog to insert an image
 *
 * @author Bob Tantlinger
 *
 */
public class HTMLImageAction extends HTMLTextEditAction {
    private static final long serialVersionUID = 1L;


    public HTMLImageAction() {
        super(i18n.str("image_"));
        putValue(SMALL_ICON, UIUtils.getIcon(UIUtils.X16, "image.png"));
        putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
    }

    protected void sourceEditPerformed(ActionEvent e, JEditorPane editor) {
        ImageDialog d = createDialog(editor);
        d.setLocationRelativeTo(d.getParent());
        d.setVisible(true);
        if (d.hasUserCancelled()) return;

        editor.requestFocusInWindow();
        editor.replaceSelection(d.getHTML());
    }

    protected void wysiwygEditPerformed(ActionEvent e, JEditorPane editor) {
        ImageDialog d = createDialog(editor);
        d.setLocationRelativeTo(d.getParent());
        d.setVisible(true);
        if (d.hasUserCancelled()) return;

        String tagText = d.getHTML();
        if (editor.getCaretPosition() == editor.getDocument().getLength())
            tagText += "&nbsp;";

        editor.replaceSelection("");
        HTML.Tag tag = HTML.Tag.IMG;
        if (tagText.startsWith("<a"))
            tag = HTML.Tag.A;

        HTMLUtils.insertHTML(tagText, tag, editor);
    }


    protected ImageDialog createDialog(JTextComponent ed) {
        Window w = SwingUtilities.getWindowAncestor(ed);
        ImageDialog d = null;

        if (w instanceof Frame)
            d = new ImageDialog((Frame) w);
        else if (w instanceof Dialog)
            d = new ImageDialog((Dialog) w);

        return d;
    }

}
