package net.atlanticbb.tantlinger.ui.text.dialogs;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ImageDialog extends HTMLOptionDialog {
    private static final long serialVersionUID = 1L;
    private static final I18n I18N = I18n.getInstance("net.atlanticbb.tantlinger.ui.text.dialogs");
    private static final Icon ICON = UIUtils.getIcon(UIUtils.X48, "image.png");
    private static final String TITLE = I18N.str("image");
    private static final String DESC = I18N.str("image_desc");

    private ImagePanel imagePanel;

    public ImageDialog(Frame parent) {
        super(parent, TITLE, DESC, ICON);
        init();
    }

    public ImageDialog(Dialog parent) {
        super(parent, TITLE, DESC, ICON);
        init();
    }

    private void init() {
        imagePanel = new ImagePanel();
        setContentPane(imagePanel);
        setSize(600, 400);
        setResizable(false);
    }

    public Map<String, String> getImageAttributes() {
        return imagePanel.getAttributes();
    }

    public void setImageAttributes(Map<String, String> attrs) {
        imagePanel.setAttributes(attrs);
    }

    private String createImgAttributes(Map<String, String> attrs) {
        StringBuilder html = new StringBuilder();

        for (Map.Entry<String, String> attr : attrs.entrySet()) {
            String name = attr.getKey();
            if (!(name.equals("a") || name.equals("name"))) {
                html.append(" ").append(name).append("=\"")
                        .append(attr.getValue()).append("\"");
            }
        }

        return html.toString();
    }

    public String getHTML() {
        Map<String, String> imgAttr = imagePanel.getAttributes();
        boolean hasLink = imgAttr.containsKey("a");

        StringBuilder html = new StringBuilder();
        if (hasLink) html.append("<a ").append(imgAttr.get("a")).append(">");
        html.append("<img").append(createImgAttributes(imgAttr)).append(">");
        if (hasLink) html.append("</a>");

        return html.toString();
    }

}
