package com.azmicro.moms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un template complet d'ordonnance
 */
public class PrescriptionTemplate {
    private String nom;
    private String description;
    private String icone;
    private List<MedicamentTemplate> medicaments;

    public PrescriptionTemplate(String nom, String description, String icone) {
        this.nom = nom;
        this.description = description;
        this.icone = icone;
        this.medicaments = new ArrayList<>();
    }

    public void addMedicament(String nom, String posologie, String duree, String instructions) {
        medicaments.add(new MedicamentTemplate(nom, posologie, duree, instructions));
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public List<MedicamentTemplate> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(List<MedicamentTemplate> medicaments) {
        this.medicaments = medicaments;
    }
    
    /**
     * Retourne les templates pré-configurés
     */
    public static List<PrescriptionTemplate> getDefaultTemplates() {
        List<PrescriptionTemplate> templates = new ArrayList<>();
        
        // Template Infection
        PrescriptionTemplate infection = new PrescriptionTemplate(
            "Infection Standard",
            "Traitement antibiotique standard avec protection gastrique",
            "fas-capsules"
        );
        infection.addMedicament(
            "Amoxicilline 1g",
            "1 comprimé 3 fois par jour",
            "7 jours",
            "À prendre après les repas"
        );
        infection.addMedicament(
            "Probiotiques",
            "1 gélule 1 fois par jour",
            "14 jours",
            "À distance des antibiotiques (2h)"
        );
        templates.add(infection);
        
        // Template Grippe
        PrescriptionTemplate grippe = new PrescriptionTemplate(
            "Grippe / Rhume",
            "Traitement symptomatique standard",
            "fas-thermometer-half"
        );
        grippe.addMedicament(
            "Paracétamol 1g",
            "1 comprimé 3 fois par jour si besoin",
            "5 jours maximum",
            "Respecter 6h entre les prises"
        );
        grippe.addMedicament(
            "Vitamine C 500mg",
            "1 comprimé 1 fois par jour",
            "7 jours",
            "Le matin avec un grand verre d'eau"
        );
        templates.add(grippe);
        
        // Template Inflammation
        PrescriptionTemplate inflammation = new PrescriptionTemplate(
            "Douleur / Inflammation",
            "Anti-inflammatoire avec protection gastrique",
            "fas-fire"
        );
        inflammation.addMedicament(
            "Ibuprofène 400mg",
            "1 comprimé 2 à 3 fois par jour",
            "5 jours",
            "À prendre au cours des repas"
        );
        inflammation.addMedicament(
            "Oméprazole 20mg",
            "1 gélule 1 fois par jour",
            "5 jours",
            "Le matin à jeun"
        );
        templates.add(inflammation);
        
        // Template HTA
        PrescriptionTemplate hta = new PrescriptionTemplate(
            "Hypertension - Suivi",
            "Traitement antihypertenseur classique",
            "fas-heartbeat"
        );
        hta.addMedicament(
            "Amlodipine 5mg",
            "1 comprimé 1 fois par jour",
            "1 mois (renouvellement)",
            "Le matin, même heure chaque jour"
        );
        templates.add(hta);
        
        // Template Diabète
        PrescriptionTemplate diabete = new PrescriptionTemplate(
            "Diabète - Suivi",
            "Traitement antidiabétique oral",
            "fas-syringe"
        );
        diabete.addMedicament(
            "Metformine 850mg",
            "1 comprimé 2 fois par jour",
            "1 mois (renouvellement)",
            "Au milieu des repas (matin et soir)"
        );
        templates.add(diabete);
        
        // Template Allergie
        PrescriptionTemplate allergie = new PrescriptionTemplate(
            "Allergie / Urticaire",
            "Antihistaminique standard",
            "fas-pills"
        );
        allergie.addMedicament(
            "Cétirizine 10mg",
            "1 comprimé 1 fois par jour",
            "7 jours",
            "Le soir au coucher"
        );
        templates.add(allergie);
        
        return templates;
    }
}
