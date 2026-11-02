package net.atlanticbb.tantlinger.shef;

import net.atlanticbb.tantlinger.io.IOUtils;
import net.atlanticbb.tantlinger.ui.ImageHandlers;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * A demo application for {@link HTMLEditorPane}
 *
 * @author Bob Tantlinger
 */
public class Demo {
    private static final String HTMLSNIP = "/net/atlanticbb/tantlinger/shef/htmlsnip.txt";
    private static Demo demo;
    private final JFrame frame;
    private final HTMLEditorPane editor;

    public Demo() {
        editor = new HTMLEditorPane();

        try (InputStream in = Demo.class.getResourceAsStream(HTMLSNIP)) {
            editor.setText(IOUtils.read(in));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(editor.getEditMenu());
        menuBar.add(editor.getFormatMenu());
        menuBar.add(editor.getInsertMenu());

        frame = new JFrame();
        frame.setContentPane(editor);
        frame.setJMenuBar(menuBar);
        frame.setTitle("HTML Editor Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        // Configure the handler before creating or displaying any HTMLEditorPane
        // so all pasted and selected images are intercepted by this handler.
        ImageHandlers.setCurrent(new ImageHandlers.DataURL());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {
        }

        SwingUtilities.invokeAndWait(() -> demo = new Demo());

        do {
            Thread.sleep(2000);
        } while (demo.frame.isVisible());

        demo.printHtml();
    }

    private void printHtml() {
        System.out.println(editor.getText());
    }
}
