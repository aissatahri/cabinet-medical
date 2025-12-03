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

public class Imagerie {
    private int imagerieID;
    private int consultationID;
    private TypeImagerie typeImagerie;
    private LocalDate dateImagerie;
    private String resultat;
    private String description;

    public Imagerie() {}

    public Imagerie(int radiographieID, int consultationID, TypeImagerie typeRadiographie, LocalDate dateRadiographie, String resultat, String description) {
        this.imagerieID = radiographieID;
        this.consultationID = consultationID;
        this.typeImagerie = typeRadiographie;
        this.dateImagerie = dateRadiographie;
        this.resultat = resultat;
        this.description = description;
    }

    public int getImagerieID() {
        return imagerieID;
    }

    public void setImagerieID(int imagerieID) {
        this.imagerieID = imagerieID;
    }

    public int getConsultationID() {
        return consultationID;
    }

    public void setConsultationID(int consultationID) {
        this.consultationID = consultationID;
    }

    public TypeImagerie getTypeImagerie() {
        return typeImagerie;
    }

    public void setTypeImagerie(TypeImagerie typeImagerie) {
        this.typeImagerie = typeImagerie;
    }

    public LocalDate getDateImagerie() {
        return dateImagerie;
    }

    public void setDateImagerie(LocalDate dateImagerie) {
        this.dateImagerie = dateImagerie;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Imagerie{" + "imagerieID=" + imagerieID + ", consultationID=" + consultationID + ", typeImagerie=" + typeImagerie + ", dateImagerie=" + dateImagerie + ", resultat=" + resultat + ", description=" + description + '}';
    }

    
}

