/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Aissa
 */
public class ConsultationActe {
    private int idConsultationActe;
    private Acte acte;
    private Consultations consultation;
    
    

    public ConsultationActe() {
    }

    public ConsultationActe(int idConsultationActe, Acte acte, Consultations consultation) {
        this.idConsultationActe = idConsultationActe;
        this.acte = acte;
        this.consultation = consultation;
    }

    // Getters et Setters
    public int getIdConsultationActe() {
        return idConsultationActe;
    }

    public void setIdConsultationActe(int idConsultationActe) {
        this.idConsultationActe = idConsultationActe;
    }

    public Acte getActe() {
        return acte;
    }

    public void setActe(Acte acte) {
        this.acte = acte;
    }

    public Consultations getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultations consultation) {
        this.consultation = consultation;
    }
    
    private BooleanProperty selected = new SimpleBooleanProperty(false); // Propriété sélectionnée

    // Constructeurs, getters et setters...

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public String toString() {
        return "ConsultationActe{" + "idConsultationActe=" + idConsultationActe + ", acte=" + acte + ", consultation=" + consultation + '}';
    }
    
    
}

