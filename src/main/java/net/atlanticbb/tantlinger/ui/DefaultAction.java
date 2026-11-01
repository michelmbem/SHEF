package net.atlanticbb.tantlinger.ui;

import org.bushe.swing.action.BasicAction;
import org.bushe.swing.action.EnabledUpdater;

import javax.swing.*;

public class DefaultAction extends BasicAction implements EnabledUpdater {
    private static final long serialVersionUID = 1L;

    public DefaultAction() {
        this(null);
    }

    public DefaultAction(String id) {
        this(id, null);
    }

    public DefaultAction(String id, Icon icon) {
        this(id, null, null, icon);
    }

    public DefaultAction(String id, Integer mnemonic, KeyStroke accelerator, Icon icon) {
        this(id, id, id, mnemonic, accelerator, icon);
    }

    public DefaultAction(String id, String shortDesc, String longDesc,
                         Integer mnemonic, KeyStroke accelerator, Icon icon) {

        this(id, id, id, shortDesc, longDesc, mnemonic, accelerator, icon);
    }

    public DefaultAction(String id, String actionName, String actionCommandName, String shortDesc,
                         String longDesc, Integer mnemonic, KeyStroke accelerator, Icon icon) {

        this(id, actionName, actionCommandName, shortDesc, longDesc,
                mnemonic, accelerator, icon, false, true);
    }

    public DefaultAction(String id, String actionName, String actionCommandName,
                         String shortDesc, String longDesc, Integer mnemonic, KeyStroke accelerator,
                         Icon icon, boolean toolbarShowsText, boolean menuShowsIcon) {

        super(id, actionName, actionCommandName, shortDesc, longDesc, mnemonic,
                accelerator, icon, toolbarShowsText, menuShowsIcon);
    }

    public boolean updateEnabled() {
        updateEnabledState();
        return isEnabled();
    }

    public boolean shouldBeEnabled(Action action) {
        return shouldBeEnabled();
    }

    /**
     * Catch all for anything thrown in the execute() method.
     * This implementation shows an ExceptionDialog.
     * (non-Javadoc)
     *
     * @see org.bushe.swing.action.BasicAction#actionPerformedCatch(java.lang.Throwable)
     */
    @Override
    protected void actionPerformedCatch(Throwable t) {
        t.printStackTrace();
    }
}
