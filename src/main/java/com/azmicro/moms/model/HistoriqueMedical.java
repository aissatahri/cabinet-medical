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

/**
 * Représente l'historique médical d'un patient.
 */

public class HistoriqueMedical {

    private int historiqueID;
    private Patient patient;
    private Type type;
    private String description;
    private LocalDate date;
    private String note;

    public HistoriqueMedical() {}

    public HistoriqueMedical(int historiqueID, Patient patient, Type type, String description, LocalDate date, String note) {
        this.historiqueID = historiqueID;
        this.patient = patient;
        this.type = type;
        this.description = description;
        this.date = date;
        this.note = note;
    }

    public int getHistoriqueID() {
        return historiqueID;
    }

    public void setHistoriqueID(int historiqueID) {
        this.historiqueID = historiqueID;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "HistoriqueMedical{" +
                "historiqueID=" + historiqueID +
                ", patient=" + patient +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", note='" + note + '\'' +
                '}';
    }
}