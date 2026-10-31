package net.atlanticbb.tantlinger.ui.text;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.Properties;

/**
 * HTML source editor backed by RSyntaxTextArea.
 */
public class SourceCodeEditor extends JEditorPane {
    private static final long serialVersionUID = 1L;
    private final RSyntaxTextArea textArea;

    public SourceCodeEditor() {
        super();
        setLayout(new BorderLayout());

        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);
        textArea.setBracketMatchingEnabled(true);
        textArea.setAnimateBracketMatching(true);
        textArea.setHighlightCurrentLine(true);
        textArea.setHighlightCurrentLine(true);
        textArea.setOpaque(true);
        textArea.setFont(new Font("Default", Font.PLAIN, 12));
        textArea.setTabSize(4);
        add(textArea, BorderLayout.CENTER);
        setFocusable(false);

        Properties theme = new Properties();

        try (InputStream in = getClass().getResourceAsStream("syntax.properties")) {
            if (in != null) {
                theme.load(in);
                setTheme(theme);
            }
        } catch (java.io.IOException ignored) {
            // Keep RSyntaxTextArea's defaults if the optional theme is unavailable.
        }
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }

    @Override
    public Document getDocument() {
        return textArea == null ? super.getDocument() : textArea.getDocument();
    }

    @Override
    public void setDocument(Document doc) {
        if (textArea == null) super.setDocument(doc);
        else textArea.setDocument(doc);
    }

    @Override
    public String getText() {
        return textArea == null ? super.getText() : textArea.getText();
    }

    @Override
    public void setText(String text) {
        if (textArea == null) super.setText(text);
        else textArea.setText(text);
    }

    @Override
    public int getCaretPosition() {
        return textArea == null ? super.getCaretPosition() : textArea.getCaretPosition();
    }

    @Override
    public void setCaretPosition(int position) {
        if (textArea == null) super.setCaretPosition(position);
        else textArea.setCaretPosition(position);
    }

    @Override
    public int getSelectionStart() {
        return textArea == null ? super.getSelectionStart() : textArea.getSelectionStart();
    }

    @Override
    public int getSelectionEnd() {
        return textArea == null ? super.getSelectionEnd() : textArea.getSelectionEnd();
    }

    @Override
    public String getSelectedText() {
        return textArea == null ? super.getSelectedText() : textArea.getSelectedText();
    }

    @Override
    public void select(int start, int end) {
        if (textArea == null) super.select(start, end);
        else textArea.select(start, end);
    }

    @Override
    public void selectAll() {
        if (textArea == null) super.selectAll();
        else textArea.selectAll();
    }

    @Override
    public void replaceSelection(String content) {
        if (textArea == null) super.replaceSelection(content);
        else textArea.replaceSelection(content);
    }

    @Override
    public void copy() {
        if (textArea == null) super.copy();
        else textArea.copy();
    }

    @Override
    public void cut() {
        if (textArea == null) super.cut();
        else textArea.cut();
    }

    @Override
    public void paste() {
        if (textArea == null) super.paste();
        else textArea.paste();
    }

    @Override
    public boolean requestFocusInWindow() {
        return textArea == null ? super.requestFocusInWindow() : textArea.requestFocusInWindow();
    }

    @Override
    public void requestFocus() {
        if (textArea == null) super.requestFocus();
        else textArea.requestFocus();
    }

    @Override
    public void addCaretListener(CaretListener listener) {
        if (textArea == null) super.addCaretListener(listener);
        else textArea.addCaretListener(listener);
    }

    @Override
    public void removeCaretListener(CaretListener listener) {
        if (textArea == null) super.removeCaretListener(listener);
        else textArea.removeCaretListener(listener);
    }

    @Override
    public void addFocusListener(FocusListener listener) {
        if (textArea == null) super.addFocusListener(listener);
        else textArea.addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(FocusListener listener) {
        if (textArea == null) super.removeFocusListener(listener);
        else textArea.removeFocusListener(listener);
    }

    @Override
    public void addMouseListener(MouseListener listener) {
        if (textArea == null) super.addMouseListener(listener);
        else textArea.addMouseListener(listener);
    }

    @Override
    public void removeMouseListener(MouseListener listener) {
        if (textArea == null) super.removeMouseListener(listener);
        else textArea.removeMouseListener(listener);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (textArea != null) textArea.setFont(font);
    }

    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        if (textArea != null) textArea.setForeground(color);
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        if (textArea != null) textArea.setBackground(color);
    }

    @Override
    public void setCaretColor(Color color) {
        super.setCaretColor(color);
        if (textArea != null) textArea.setCaretColor(color);
    }

    /**
     * Applies the legacy editor text and caret colors from a properties theme.
     */
    public void setTheme(Properties props) {
        setTheme(props, "default");
    }

    public void setTheme(Properties props, String prefix) {
        Color foreground = parseColor(props.getProperty(prefix + ".text.fg"));
        Color background = parseColor(props.getProperty(prefix + ".text.bg"));
        Color caret = parseColor(props.getProperty(prefix + ".caret.fg"));
        Color line = parseColor(props.getProperty(prefix + ".linehighlight.fg"));
        Color bracket = parseColor(props.getProperty(prefix + ".brackethighlight.fg"));
        if (foreground != null) textArea.setForeground(foreground);
        if (background != null) textArea.setBackground(background);
        if (caret != null) textArea.setCaretColor(caret);
        if (line != null) textArea.setCurrentLineHighlightColor(line);
        if (bracket != null) textArea.setMatchedBracketBGColor(bracket);
        Color selection = parseColor(props.getProperty(prefix + ".selectioncolor.bg"));
        if (selection != null) textArea.setSelectionColor(selection);
        setStyle(props, prefix, TokenTypes.COMMENT_EOL, "comment1");
        setStyle(props, prefix, TokenTypes.COMMENT_MULTILINE, "comment2");
        setStyle(props, prefix, TokenTypes.COMMENT_DOCUMENTATION, "comment3");
        setStyle(props, prefix, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "literal1");
        setStyle(props, prefix, TokenTypes.LITERAL_CHAR, "literal2");
        setStyle(props, prefix, TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "digit");
        setStyle(props, prefix, TokenTypes.RESERVED_WORD, "keyword1");
        setStyle(props, prefix, TokenTypes.RESERVED_WORD_2, "keyword2");
        setStyle(props, prefix, TokenTypes.FUNCTION, "function");
        setStyle(props, prefix, TokenTypes.OPERATOR, "operator");
        setStyle(props, prefix, TokenTypes.MARKUP_TAG_NAME, "markup");
        setStyle(props, prefix, TokenTypes.MARKUP_TAG_ATTRIBUTE, "label");
        setStyle(props, prefix, TokenTypes.ERROR_IDENTIFIER, "invalid");
    }

    private void setStyle(Properties props, String prefix, int tokenType, String key) {
        Style style = textArea.getSyntaxScheme().getStyle(tokenType);
        Color foreground = parseColor(props.getProperty(prefix + "." + key + ".fg"));
        Color background = parseColor(props.getProperty(prefix + "." + key + ".bg"));
        if (foreground != null) style.foreground = foreground;
        if (background != null) style.background = background;
        String font = props.getProperty(prefix + "." + key + ".font", "");
        int flags = Font.PLAIN;
        if (font.contains("bold")) flags |= Font.BOLD;
        if (font.contains("italic")) flags |= Font.ITALIC;
        style.font = textArea.getFont().deriveFont(flags);
    }

    private Color parseColor(String value) {
        if (value == null) return null;
        try {
            if (value.length() == 6) return new Color(Integer.parseInt(value, 16));
            if (value.length() == 8) return new Color((int) Long.parseLong(value, 16), true);
        } catch (NumberFormatException ignored) {
        }
        return null;
    }
}
