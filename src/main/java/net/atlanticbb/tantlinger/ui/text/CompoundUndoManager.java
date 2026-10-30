package net.atlanticbb.tantlinger.ui.text;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.UIUtils;
import org.bushe.swing.action.ActionManager;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.TextAction;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

/**
 * Manages compound undoable edits.
 * <p>
 * Before an undoable edit happens on a particular document, you should call
 * the static method CompoundUndoManager.beginCompoundEdit(doc)
 * <p>
 * Conversely after an undoable edit happens on a particular document,
 * you shoulc call the static method CompoundUndoManager.beginCompoundEdit(doc)
 * <p>
 * For either of these methods to work, you must add an instance of
 * CompoundUndoManager as a document listener... e.g
 * <p>
 * doc.addUndoableEditListener(new CompoundUndoManager(doc, new UndoManager());
 * <p>
 * Note that each CompoundUndoManager should have its own UndoManager.
 *
 * @author Bob Tantlinger
 */
public class CompoundUndoManager implements UndoableEditListener {
    private static final I18n I18N = I18n.getInstance("net.atlanticbb.tantlinger.ui.text");
    private static final java.util.List<RegistryRecord> REGISTRY = new LinkedList<>();

    /**
     * Static undo action that works across all documents
     * with a CompoundUndoManager registered as an UndoableEditListener
     */
    public static Action UNDO = new UndoAction();

    /**
     * Static undo action that works across all documents
     * with a CompoundUndoManager registered as an UndoableEditListener
     */
    public static Action REDO = new RedoAction();

    private final UndoManager manager;

    private CompoundEdit compoundEdit = null;
    private Document document = null;

    /**
     * Creates a new CompoundUndoManager
     *
     * @param document The {@link Document} to monitor
     * @param manager  The {@link UndoManager} to use for this document
     */
    public CompoundUndoManager(Document document, UndoManager manager) {
        this.manager = manager;
        this.document = document;
        registerDocument(this.document, this, this.manager);
    }

    /**
     * Creates a new CompoundUndoManager
     *
     * @param document The {@link Document} to monitor
     */
    public CompoundUndoManager(Document document) {
        this(document, new UndoManager());
    }

    protected static void registerDocument(
            Document document, CompoundUndoManager instance, UndoManager manager) {

        REGISTRY.add(new RegistryRecord(document, instance, manager));
    }

    /**
     * Gets the undo manager for a document that has a CompoundUndoManager
     * as an UndoableEditListener
     *
     * @param document The {@link Document} for which to get an undo manager
     * @return The registered {@link UndoManager} for the document
     */
    public static Optional<UndoManager> getUndoManagerForDocument(Document document) {
        return REGISTRY.stream()
                .filter(rec -> rec.document == document)
                .findFirst()
                .map(rec -> rec.manager);
    }

    /**
     * Notifies the CompoundUndoManager for the specified Document that
     * a compound edit is about to begin.
     *
     * @param document The {@link Document} on which a compound edit is about to start
     */
    public static void beginCompoundEdit(Document document) {
        REGISTRY.stream()
                .filter(rec -> rec.document == document)
                .findFirst()
                .map(rec -> rec.instance)
                .ifPresent(inst -> inst.beginCompoundEdit());
    }

    /**
     * Notifies the CompoundUndoManager for the specified Document that
     * a compound edit is complete.
     *
     * @param document The {@link Document} on which a compound edit is ending
     */
    public static void endCompoundEdit(Document document) {
        REGISTRY.stream()
                .filter(rec -> rec.document == document)
                .findFirst()
                .map(rec -> rec.instance)
                .ifPresent(inst -> inst.endCompoundEdit());
    }

    /**
     * Updates the enabled states of the UNDO and REDO actions
     * for the specified document
     *
     * @param document The {@link Document} on which to update the UNDO/REDO states
     */
    public static void updateUndo(Document document) {
        getUndoManagerForDocument(document).ifPresent(um -> {
            UNDO.setEnabled(um.canUndo());
            REDO.setEnabled(um.canRedo());
        });
    }

    /**
     * Discards all edits for the specified Document
     *
     * @param document The {@link Document} on which to discard edits
     */
    public static void discardAllEdits(Document document) {
        getUndoManagerForDocument(document).ifPresent(um -> {
            um.discardAllEdits();
            UNDO.setEnabled(um.canUndo());
            REDO.setEnabled(um.canRedo());
        });
    }

    public void undoableEditHappened(UndoableEditEvent evt) {
        UndoableEdit edit = evt.getEdit();
        if (compoundEdit != null) {
            compoundEdit.addEdit(edit);
        } else {
            manager.addEdit(edit);
            updateUndo(document);
        }
    }

    protected void beginCompoundEdit() {
        compoundEdit = new CompoundEdit();
    }

    protected void endCompoundEdit() {
        if (compoundEdit != null) {
            compoundEdit.end();
            manager.addEdit(compoundEdit);
            updateUndo(document);
        }

        compoundEdit = null;
    }

    static final class RegistryRecord {
        Document document;
        CompoundUndoManager instance;
        UndoManager manager;

        RegistryRecord(Document document, CompoundUndoManager instance, UndoManager manager) {
            this.document = document;
            this.instance = instance;
            this.manager = manager;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) return true;
            if (other == null) return false;
            if (other instanceof RegistryRecord) {
                RegistryRecord rec = (RegistryRecord) other;
                return Objects.equals(document, rec.document) &&
                        Objects.equals(instance, rec.instance) &&
                        Objects.equals(manager, rec.manager);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(document, instance, manager);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() +
                    "[document=" + document +
                    ",instance=" + instance +
                    ",manager=" + manager +
                    "]";
        }
    }

    static class UndoAction extends TextAction {
        private static final long serialVersionUID = 1L;

        public UndoAction() {
            super(I18N.str("undo"));
            putValue(Action.SMALL_ICON, UIUtils.getIcon(UIUtils.X16, "undo.png"));
            putValue(ActionManager.LARGE_ICON, UIUtils.getIcon(UIUtils.X24, "undo.png"));
            putValue(MNEMONIC_KEY, I18N.mnem("undo"));

            setEnabled(false);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                    KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            putValue(SHORT_DESCRIPTION, getValue(NAME));
        }

        public void actionPerformed(ActionEvent e) {
            Document doc = getTextComponent(e).getDocument();
            getUndoManagerForDocument(doc).ifPresent(um -> {
                try {
                    um.undo();
                } catch (CannotUndoException ex) {
                    ex.printStackTrace();
                } finally {
                    updateUndo(doc);
                }
            });
        }
    }

    static class RedoAction extends TextAction {
        private static final long serialVersionUID = 1L;

        public RedoAction() {
            super(I18N.str("redo"));
            putValue(Action.SMALL_ICON, UIUtils.getIcon(UIUtils.X16, "redo.png"));
            putValue(ActionManager.LARGE_ICON, UIUtils.getIcon(UIUtils.X24, "redo.png"));
            putValue(MNEMONIC_KEY, I18N.mnem("redo"));

            setEnabled(false);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                    KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            putValue(SHORT_DESCRIPTION, getValue(NAME));
        }

        public void actionPerformed(ActionEvent e) {
            Document doc = getTextComponent(e).getDocument();
            getUndoManagerForDocument(doc).ifPresent(um -> {
                try {
                    um.redo();
                } catch (CannotUndoException ex) {
                    ex.printStackTrace();
                } finally {
                    updateUndo(doc);
                }
            });
        }
    }
}