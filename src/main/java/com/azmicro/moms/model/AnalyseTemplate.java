package com.azmicro.moms.model;

/**
 * Représente une analyse avec ses détails
 */
public class AnalyseTemplate {
    private String typeAnalyse;
    private String description;

    public AnalyseTemplate(String typeAnalyse, String description) {
        this.typeAnalyse = typeAnalyse;
        this.description = description;
    }

    public String getTypeAnalyse() {
        return typeAnalyse;
    }

    public void setTypeAnalyse(String typeAnalyse) {
        this.typeAnalyse = typeAnalyse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullDescription() {
        if (description != null && !description.isEmpty()) {
            return typeAnalyse + " - " + description;
        }
        return typeAnalyse;
    }
}
