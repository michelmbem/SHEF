package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.ui.OptionDialog;

import javax.swing.*;
import java.awt.*;

/**
 * An abstract OptionDialog for HTML editor dialog boxes.
 * <p>
 * Subclasses should implement dialogs for inserting HTML elements
 * such as tables, links, images, etc.
 * </p>
 *
 * @author Bob Tantlinger
 */
public abstract class HTMLOptionDialog extends OptionDialog {
    protected HTMLOptionDialog(Frame parent, String title, String desc, Icon ico) {
        super(parent, title, desc, ico);
    }

    protected HTMLOptionDialog(Dialog parent, String title, String desc, Icon ico) {
        super(parent, title, desc, ico);
    }

    /**
     * Gets the generated HTML from the dialog
     *
     * @return the HTML
     */
    public abstract String getHTML();
}
