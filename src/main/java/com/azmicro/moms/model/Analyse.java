/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */

import java.time.LocalDate;

public class Analyse {
    private int analyseID;
    private int consultationID;
    private TypeAnalyse typeAnalyse;
    private String description;
    private LocalDate dateAnalyse;
    private String resultat;
    

    // Constructeurs
    public Analyse() {}

    public Analyse(int analyseID, int consultationID, TypeAnalyse typeAnalyse, String description, LocalDate dateAnalyse, String resultat) {
        this.analyseID = analyseID;
        this.consultationID = consultationID;
        this.typeAnalyse = typeAnalyse;
        this.description = description;
        this.dateAnalyse = dateAnalyse;
        this.resultat = resultat;
    }

    // Getters et Setters
    public int getAnalyseID() {
        return analyseID;
    }

    public void setAnalyseID(int analyseID) {
        this.analyseID = analyseID;
    }

    public int getConsultationID() {
        return consultationID;
    }

    public void setConsultationID(int consultationID) {
        this.consultationID = consultationID;
    }

    public TypeAnalyse getTypeAnalyse() {
        return typeAnalyse;
    }

    public void setTypeAnalyse(TypeAnalyse typeAnalyse) {
        this.typeAnalyse = typeAnalyse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateAnalyse() {
        return dateAnalyse;
    }

    public void setDateAnalyse(LocalDate dateAnalyse) {
        this.dateAnalyse = dateAnalyse;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    @Override
    public String toString() {
        return "Analyse{" + "analyseID=" + analyseID + ", consultationID=" + consultationID + ", typeAnalyse=" + typeAnalyse + ", description=" + description + ", dateAnalyse=" + dateAnalyse + ", resultat=" + resultat + '}';
    }
    
}
