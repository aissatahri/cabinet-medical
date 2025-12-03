/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */
public enum ModePaiement {
    CARTE("Carte"),
    ESPECES("Espèces"),
    CHEQUE("Chèque");

    private final String label;

    ModePaiement(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static ModePaiement fromString(String label) {
        for (ModePaiement mode : ModePaiement.values()) {
            if (mode.label.equalsIgnoreCase(label)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("No enum constant with label " + label);
    }
    
    

}

