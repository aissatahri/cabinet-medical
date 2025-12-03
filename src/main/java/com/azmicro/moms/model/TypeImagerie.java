/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */
public class TypeImagerie {
    private int idTypeImagerie;
    private String nomImagerieFr;
    private String nomImagerieEn;
    private String description;
    private String codeImagerieFr;
    private String codeImagerieEn;

    public TypeImagerie() {}

    public TypeImagerie(int idTypeImagerie, String nomImagerieFr, String nomImagerieEn, String description, String codeImagerieFr, String codeImagerieEn) {
        this.idTypeImagerie = idTypeImagerie;
        this.nomImagerieFr = nomImagerieFr;
        this.nomImagerieEn = nomImagerieEn;
        this.description = description;
        this.codeImagerieFr = codeImagerieFr;
        this.codeImagerieEn = codeImagerieEn;
    }

    public int getIdTypeImagerie() {
        return idTypeImagerie;
    }

    public void setIdTypeImagerie(int idTypeImagerie) {
        this.idTypeImagerie = idTypeImagerie;
    }

    public String getNomImagerieFr() {
        return nomImagerieFr;
    }

    public void setNomImagerieFr(String nomImagerieFr) {
        this.nomImagerieFr = nomImagerieFr;
    }

    public String getNomImagerieEn() {
        return nomImagerieEn;
    }

    public void setNomImagerieEn(String nomImagerieEn) {
        this.nomImagerieEn = nomImagerieEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeImagerieFr() {
        return codeImagerieFr;
    }

    public void setCodeImagerieFr(String codeImagerieFr) {
        this.codeImagerieFr = codeImagerieFr;
    }

    public String getCodeImagerieEn() {
        return codeImagerieEn;
    }

    public void setCodeImagerieEn(String codeImagerieEn) {
        this.codeImagerieEn = codeImagerieEn;
    }

    @Override
    public String toString() {
        return "TypeImagerie{" + 
               "idTypeImagerie=" + idTypeImagerie + 
               ", nomImagerieFr='" + nomImagerieFr + '\'' + 
               ", nomImagerieEn='" + nomImagerieEn + '\'' + 
               ", description='" + description + '\'' + 
               ", codeImagerieFr='" + codeImagerieFr + '\'' + 
               ", codeImagerieEn='" + codeImagerieEn + '\'' + 
               '}';
    }
}

