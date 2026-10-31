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

package org.bushe.swing.action;

import java.util.Map;

public interface ContextAware {
    Map getContext();

    void setContext(Map var1);

    void clearContext();

    void putContextValue(Object var1, Object var2);

    Object getContextValue(Object var1);
}

