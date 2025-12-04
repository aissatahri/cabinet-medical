package com.azmicro.moms.model;

/**
 * Représente une imagerie avec ses détails
 */
public class ImagerieTemplate {
    private String typeImagerie;
    private String description;

    public ImagerieTemplate(String typeImagerie, String description) {
        this.typeImagerie = typeImagerie;
        this.description = description;
    }

    public String getTypeImagerie() {
        return typeImagerie;
    }

    public void setTypeImagerie(String typeImagerie) {
        this.typeImagerie = typeImagerie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullDescription() {
        if (description != null && !description.isEmpty()) {
            return typeImagerie + " - " + description;
        }
        return typeImagerie;
    }
}
