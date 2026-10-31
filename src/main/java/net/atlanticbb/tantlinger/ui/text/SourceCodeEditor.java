package net.atlanticbb.tantlinger.ui.text;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * HTML source editor backed by RSyntaxTextArea.
 */
public class SourceCodeEditor extends RSyntaxTextArea {
    private static final long serialVersionUID = 1L;

    public SourceCodeEditor() {
        super();
        setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        setCodeFoldingEnabled(true);
        setAntiAliasingEnabled(true);
        setBracketMatchingEnabled(true);
        setAnimateBracketMatching(true);
        setHighlightCurrentLine(true);
        setOpaque(true);
        setFont(new Font("Default", Font.PLAIN, 12));
        setTabSize(4);
        
        try (InputStream in = getClass().getResourceAsStream("syntax.properties")) {
            if (in != null) {
                Properties theme = new Properties();
                theme.load(in);
                setTheme(theme);
            }
        } catch (IOException ignored) {
            // Keep RSyntaxTextArea's defaults if the optional theme is unavailable.
        }
    }

    /**
     * Applies the legacy editor text and caret colors from a properties theme.
     */
    public void setTheme(Properties props) {
        setTheme(props, "default");
    }

    public void setTheme(Properties props, String prefix) {
        Color foreground = parseColor(props.getProperty(prefix + ".text.fg"));
        if (foreground != null) setForeground(foreground);

        Color background = parseColor(props.getProperty(prefix + ".text.bg"));
        if (background != null) setBackground(background);

        Color caret = parseColor(props.getProperty(prefix + ".caret.fg"));
        if (caret != null) setCaretColor(caret);

        Color line = parseColor(props.getProperty(prefix + ".linehighlight.fg"));
        if (line != null) setCurrentLineHighlightColor(line);

        Color bracket = parseColor(props.getProperty(prefix + ".brackethighlight.fg"));
        if (bracket != null) setMatchedBracketBGColor(bracket);

        Color selection = parseColor(props.getProperty(prefix + ".selectioncolor.bg"));
        if (selection != null) setSelectionColor(selection);

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
        Style style = getSyntaxScheme().getStyle(tokenType);

        Color foreground = parseColor(props.getProperty(prefix + "." + key + ".fg"));
        if (foreground != null) style.foreground = foreground;

        Color background = parseColor(props.getProperty(prefix + "." + key + ".bg"));
        if (background != null) style.background = background;

        String font = props.getProperty(prefix + "." + key + ".font", "");
        int flags = Font.PLAIN;
        if (font.contains("bold")) flags |= Font.BOLD;
        if (font.contains("italic")) flags |= Font.ITALIC;
        style.font = getFont().deriveFont(flags);
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
