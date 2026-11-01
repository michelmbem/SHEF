package net.atlanticbb.tantlinger.ui.text.actions;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.DefaultAction;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.ActionEvent;

/**
 * @author Bob Tantlinger
 *
 */
public abstract class HTMLTextEditAction extends DefaultAction {
    public static final String EDITOR = "editor";
    public static final int DISABLED = -1;
    public static final int WYSIWYG = 0;
    public static final int SOURCE = 1;
    protected static final I18n i18n = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.actions");

    protected HTMLTextEditAction(String name) {
        super(name);
        addShouldBeEnabledDelegate(a -> getEditMode() != DISABLED);
        updateEnabledState();
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void execute(ActionEvent e) {
        if (getEditMode() == WYSIWYG)
            wysiwygEditPerformed(e, (JEditorPane) getCurrentEditor());
        else if (getEditMode() == SOURCE)
            sourceEditPerformed(e, getCurrentEditor());
    }

    public int getEditMode() {
        JTextComponent ed = getCurrentEditor();
        if (ed == null) return DISABLED;
        if (ed instanceof JEditorPane &&
                ed.getDocument() instanceof HTMLDocument &&
                ((JEditorPane) ed).getEditorKit() instanceof HTMLEditorKit)
            return WYSIWYG;
        return SOURCE;
    }

    protected JTextComponent getCurrentEditor() {
        try {
            return (JTextComponent) getContextValue(EDITOR);
        } catch (ClassCastException ignored) {
        }

        return null;
    }

    @Override
    protected void contextChanged() {
        if (getEditMode() == WYSIWYG)
            updateWysiwygContextState((JEditorPane) getCurrentEditor());
        else if (getEditMode() == SOURCE)
            updateSourceContextState(getCurrentEditor());
    }

    protected void updateWysiwygContextState(JEditorPane wysEditor) {
    }

    protected void updateSourceContextState(JTextComponent srcEditor) {
    }

    protected abstract void wysiwygEditPerformed(ActionEvent e, JEditorPane editor);

    protected abstract void sourceEditPerformed(ActionEvent e, JTextComponent editor);

}
