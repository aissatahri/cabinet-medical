/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */

public class TypeAnalyse {
    private int idTypeAnalyse;
    private String nomAnalyseFr;
    private String nomAnalyseEn;
    private String description;
    private String codeAnalyseFr;
    private String codeAnalyseEn;

    // Constructors
    public TypeAnalyse() {}

    public TypeAnalyse(int idTypeAnalyse, String nomAnalyseFr, String nomAnalyseEn, String description, String codeAnalyseFr, String codeAnalyseEn) {
        this.idTypeAnalyse = idTypeAnalyse;
        this.nomAnalyseFr = nomAnalyseFr;
        this.nomAnalyseEn = nomAnalyseEn;
        this.description = description;
        this.codeAnalyseFr = codeAnalyseFr;
        this.codeAnalyseEn = codeAnalyseEn;
    }

    // Getters and Setters
    public int getIdTypeAnalyse() {
        return idTypeAnalyse;
    }

    public void setIdTypeAnalyse(int idTypeAnalyse) {
        this.idTypeAnalyse = idTypeAnalyse;
    }

    public String getNomAnalyseFr() {
        return nomAnalyseFr;
    }

    public void setNomAnalyseFr(String nomAnalyseFr) {
        this.nomAnalyseFr = nomAnalyseFr;
    }

    public String getNomAnalyseEn() {
        return nomAnalyseEn;
    }

    public void setNomAnalyseEn(String nomAnalyseEn) {
        this.nomAnalyseEn = nomAnalyseEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeAnalyseFr() {
        return codeAnalyseFr;
    }

    public void setCodeAnalyseFr(String codeAnalyseFr) {
        this.codeAnalyseFr = codeAnalyseFr;
    }

    public String getCodeAnalyseEn() {
        return codeAnalyseEn;
    }

    public void setCodeAnalyseEn(String codeAnalyseEn) {
        this.codeAnalyseEn = codeAnalyseEn;
    }

    @Override
    public String toString() {
        return "TypeAnalyse{" + "idTypeAnalyse=" + idTypeAnalyse + ", nomAnalyseFr=" + nomAnalyseFr + ", nomAnalyseEn=" + nomAnalyseEn + ", description=" + description + ", codeAnalyseFr=" + codeAnalyseFr + ", codeAnalyseEn=" + codeAnalyseEn + '}';
    }
    
    
}
