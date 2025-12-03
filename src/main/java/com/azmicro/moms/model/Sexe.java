/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */

/**
 * Représente le sexe d'un utilisateur.
 * Peut être soit F (Féminin) ou M (Masculin).
 * 
 * <p>Utilisation :</p>
 * <pre>
 *     Sexe sexe = Sexe.F;
 *     System.out.println(sexe.getDescription()); // Imprimera "Féminin"
 * </pre>
 */
public enum Sexe {
    F("Féminin"),
    M("Masculin");

    private final String description;

    Sexe(String description) {
        this.description = description;
    }

    /**
     * Retourne la description textuelle du sexe.
     * 
     * @return la description du sexe.
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
