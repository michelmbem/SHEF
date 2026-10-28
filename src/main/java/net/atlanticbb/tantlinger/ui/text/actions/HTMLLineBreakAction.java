/*
 * Created on Feb 25, 2005
 *
 */
package net.atlanticbb.tantlinger.ui.text.actions;

import net.atlanticbb.tantlinger.ui.UIUtils;

import javax.swing.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;


/**
 *
 */
public class HTMLLineBreakAction extends HTMLTextEditAction {
    private static final long serialVersionUID = 1L;

    public HTMLLineBreakAction() {
        super(i18n.str("line_break"));
        putValue(MNEMONIC_KEY, i18n.mnem("line_break"));
        putValue(SMALL_ICON, UIUtils.getIcon(UIUtils.X16, "br.png"));
        putValue(ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(Event.ENTER, InputEvent.SHIFT_MASK));
        putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
    }


    @Override
    protected void sourceEditPerformed(ActionEvent e, JEditorPane editor) {
        editor.replaceSelection("<br>\n");
    }

    @Override
    protected void wysiwygEditPerformed(ActionEvent e, JEditorPane editor) {
        HTMLDocument document = (HTMLDocument) editor.getDocument();
        int pos = editor.getCaretPosition();

        String elName = document.getParagraphElement(pos).getName();
        HTML.Tag tag = HTML.getTag(elName);
        if (elName.equalsIgnoreCase("P-IMPLIED"))
            tag = HTML.Tag.IMPLIED;

        HTMLEditorKit.InsertHTMLTextAction hta =
                new HTMLEditorKit.InsertHTMLTextAction(
                        "insertBR",
                        "<br>",
                        tag,
                        HTML.Tag.BR);
        hta.actionPerformed(e);
    }
}
