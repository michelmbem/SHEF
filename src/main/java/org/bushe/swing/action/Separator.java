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

public class Separator {
    private String id;
    private Number weight;
    private boolean lineVisible;

    public Separator() {
        this(null, null, true);
    }

    public Separator(Number weight) {
        this(null, weight, true);
    }

    public Separator(String id, Number weight, boolean lineVisible) {
        this.id = id;
        this.weight = weight;
        this.lineVisible = lineVisible;
    }

    public String getId() {
        return this.id;
    }

    public Number getWeight() {
        return this.weight;
    }

    public void setWeight(Number weight) {
        this.weight = weight;
    }

    public boolean isLineVisible() {
        return this.lineVisible;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Separator) {
            Separator s = (Separator)obj;
            if (this.id == null ? s.id != null : !this.id.equals(s.id)) {
                return false;
            }
            if (this.weight == null ? s.weight != null : !this.weight.equals(s.weight)) {
                return false;
            }
            return this.lineVisible == s.lineVisible;
        }
        return false;
    }
}

