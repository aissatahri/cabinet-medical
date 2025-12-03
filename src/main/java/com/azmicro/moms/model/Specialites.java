/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */

public class Specialites {
    private int specialiteID;
    private String nomSpecialite;

    public Specialites() {}

    public Specialites(int specialiteID, String nomSpecialite) {
        this.specialiteID = specialiteID;
        this.nomSpecialite = nomSpecialite;
    }

    public int getSpecialiteID() {
        return specialiteID;
    }

    public void setSpecialiteID(int specialiteID) {
        this.specialiteID = specialiteID;
    }

    public String getNomSpecialite() {
        return nomSpecialite;
    }

    public void setNomSpecialite(String nomSpecialite) {
        this.nomSpecialite = nomSpecialite;
    }

    @Override
    public String toString() {
        return "Specialites{" +
                "specialiteID=" + specialiteID +
                ", nomSpecialite='" + nomSpecialite + '\'' +
                '}';
    }
}

