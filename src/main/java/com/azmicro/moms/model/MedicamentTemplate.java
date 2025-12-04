package com.azmicro.moms.model;

/**
 * Représente un médicament avec sa posologie dans un template
 */
public class MedicamentTemplate {
    private String nomMedicament;
    private String posologie;
    private String duree;
    private String instructions;

    public MedicamentTemplate(String nomMedicament, String posologie, String duree, String instructions) {
        this.nomMedicament = nomMedicament;
        this.posologie = posologie;
        this.duree = duree;
        this.instructions = instructions;
    }

    public String getNomMedicament() {
        return nomMedicament;
    }

    public void setNomMedicament(String nomMedicament) {
        this.nomMedicament = nomMedicament;
    }

    public String getPosologie() {
        return posologie;
    }

    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getFullDescription() {
        return posologie + "\n" + duree + "\n" + instructions;
    }
}
