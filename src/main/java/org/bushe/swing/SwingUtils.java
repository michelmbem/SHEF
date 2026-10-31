/*
 * Copyright 2005 Bushe Enterprises, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Decompiled from sam.jar using CFR 0.152. This reconstructed source
 * may differ from the original source formatting and comments.
 */

package org.bushe.swing;

import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class SwingUtils {
    static /* synthetic */ Class class$0;

    public static Icon getIcon(String imagePath) {
        if (imagePath != null && !imagePath.equals("")) {
            URL url;
            Class<?> clazz = class$0;
            if (clazz == null) {
                try {
                    clazz = class$0 = Class.forName("org.bushe.swing.SwingUtils");
                }
                catch (ClassNotFoundException classNotFoundException) {
                    throw new NoClassDefFoundError(classNotFoundException.getMessage());
                }
            }
            if ((url = clazz.getResource(imagePath)) != null) {
                return new ImageIcon(url);
            }
        }
        return null;
    }
}

