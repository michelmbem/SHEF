package net.atlanticbb.tantlinger.ui.text.actions;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;

/**
 * Action suitable for when wysiwyg or source context does not matter.
 *
 * @author Bob Tantlinger
 *
 */
public abstract class BasicEditAction extends HTMLTextEditAction {

    /**
     * @param name
     */
    protected BasicEditAction(String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction#sourceEditPerformed(java.awt.event.ActionEvent, javax.swing.JEditorPane)
     */
    protected final void sourceEditPerformed(ActionEvent e, JTextComponent editor) {
        doEdit(e, editor);
    }

    /* (non-Javadoc)
     * @see net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction#wysiwygEditPerformed(java.awt.event.ActionEvent, javax.swing.JEditorPane)
     */
    protected final void wysiwygEditPerformed(ActionEvent e, JEditorPane editor) {
        doEdit(e, editor);
    }

    protected abstract void doEdit(ActionEvent e, JTextComponent editor);

    protected void updateContextState(JTextComponent editor) {
    }

    @Override
    protected final void updateWysiwygContextState(JEditorPane wysEditor) {
        updateContextState(wysEditor);
    }

    @Override
    protected final void updateSourceContextState(JTextComponent srcEditor) {
        updateContextState(srcEditor);
    }
}
