package com.azmicro.moms.util;

import com.azmicro.moms.model.Patient;

/**
 * Service pour les calculs médicaux
 */
public class MedicalCalculationService {

    /**
     * Calcule l'IMC (Indice de Masse Corporelle)
     * @param poids en kg
     * @param taille en mètres
     * @return IMC calculé
     */
    public static double calculateIMC(double poids, double taille) {
        if (taille <= 0) {
            return 0.0;
        }
        return poids / (taille * taille);
    }

    /**
     * Classifie la pression artérielle
     */
    public static String classifyBloodPressure(int systolic, int diastolic) {
        if (systolic < 120 && diastolic < 80) {
            return "Pression artérielle optimale";
        } else if (systolic >= 120 && systolic <= 129 && diastolic >= 80 && diastolic <= 84) {
            return "Pression artérielle normale";
        } else if (systolic >= 130 && systolic <= 139 && diastolic >= 85 && diastolic <= 89) {
            return "Pression artérielle normale haute";
        } else if (systolic >= 140 && systolic <= 159 && diastolic >= 90 && diastolic <= 99) {
            return "Hypertension de stade 1 (légère)";
        } else if (systolic >= 160 && systolic <= 179 && diastolic >= 100 && diastolic <= 109) {
            return "Hypertension de stade 2 (modérée)";
        } else if (systolic >= 180 && diastolic >= 110) {
            return "Hypertension de stade 3 (sévère)";
        } else if (systolic >= 140 && diastolic < 90) {
            return "Hypertension systolique isolée";
        } else {
            return "Classification non disponible";
        }
    }

    /**
     * Classifie la saturation en oxygène
     */
    public static String classifyOxygenSaturation(int spo2) {
        if (spo2 >= 95) {
            return "Saturation normale";
        } else if (spo2 >= 91) {
            return "Hypoxémie légère";
        } else if (spo2 >= 86) {
            return "Hypoxémie modérée";
        } else {
            return "Hypoxémie sévère";
        }
    }

    /**
     * Classifie la glycémie à jeun (en g/L)
     */
    public static String classifyGlycemiaFastingInGl(double glycemyLevel) {
        if (glycemyLevel < 0.7) {
            return "Glycémie inférieure à la normale";
        } else if (glycemyLevel <= 0.99) {
            return "Glycémie normale à jeun";
        } else if (glycemyLevel <= 1.25) {
            return "Pré-diabète à jeun";
        } else {
            return "Diabète à jeun";
        }
    }

    /**
     * Classifie la fréquence respiratoire selon l'âge
     */
    public static String classifyRespiratoryRate(int frequence, Patient patient) {
        int ageInMonths = patient.getAgeInMonths();
        int ageInYears = patient.getAgeInYears();

        if (ageInMonths <= 12) {
            if (frequence >= 30 && frequence <= 60) {
                return "Fréquence respiratoire normale pour un nourrisson";
            }
        } else if (ageInYears <= 3) {
            if (frequence >= 24 && frequence <= 40) {
                return "Fréquence respiratoire normale pour un jeune enfant";
            }
        } else if (ageInYears <= 6) {
            if (frequence >= 22 && frequence <= 34) {
                return "Fréquence respiratoire normale pour un enfant d'âge préscolaire";
            }
        } else if (ageInYears <= 12) {
            if (frequence >= 18 && frequence <= 30) {
                return "Fréquence respiratoire normale pour un enfant d'âge scolaire";
            }
        } else if (ageInYears <= 18) {
            if (frequence >= 12 && frequence <= 20) {
                return "Fréquence respiratoire normale pour un adolescent";
            }
        } else {
            if (frequence >= 12 && frequence <= 20) {
                return "Fréquence respiratoire normale pour un adulte";
            }
        }

        if (frequence > 20) {
            return "Tachypnée (fréquence respiratoire élevée)";
        } else if (frequence < 12) {
            return "Bradypnée (fréquence respiratoire basse)";
        } else {
            return "Classification non disponible";
        }
    }

    /**
     * Obtient l'interprétation de la fréquence cardiaque
     */
    public static String getHeartRateInterpretation(int age, String sexe, int frequenceCardiaque) {
        if (sexe.equalsIgnoreCase("Masculin")) {
            if (age >= 18 && age <= 25) {
                if (frequenceCardiaque >= 49 && frequenceCardiaque <= 55) return "Athlète";
                if (frequenceCardiaque >= 56 && frequenceCardiaque <= 61) return "Excellente";
                if (frequenceCardiaque >= 62 && frequenceCardiaque <= 65) return "Bonne";
                if (frequenceCardiaque >= 66 && frequenceCardiaque <= 69) return "Au-dessus de la moyenne";
                if (frequenceCardiaque >= 70 && frequenceCardiaque <= 73) return "Moyenne";
                if (frequenceCardiaque >= 74 && frequenceCardiaque <= 81) return "En dessous de la moyenne";
                if (frequenceCardiaque >= 82) return "Mauvaise";
            }
        } else if (sexe.equalsIgnoreCase("Féminin")) {
            if (age >= 18 && age <= 25) {
                if (frequenceCardiaque >= 56 && frequenceCardiaque <= 60) return "Athlète";
                if (frequenceCardiaque >= 61 && frequenceCardiaque <= 65) return "Excellente";
                if (frequenceCardiaque >= 66 && frequenceCardiaque <= 69) return "Bonne";
                if (frequenceCardiaque >= 70 && frequenceCardiaque <= 73) return "Au-dessus de la moyenne";
                if (frequenceCardiaque >= 74 && frequenceCardiaque <= 78) return "Moyenne";
                if (frequenceCardiaque >= 79 && frequenceCardiaque <= 84) return "En dessous de la moyenne";
                if (frequenceCardiaque >= 85) return "Mauvaise";
            }
        }
        return "Interprétation non disponible";
    }

    /**
     * Formate et classifie l'IMC
     */
    public static IMCClassification classifyIMC(double imc) {
        String interpretation;
        String styleColor;

        if (imc < 18.5) {
            interpretation = "Insuffisance pondérale (maigreur)";
            styleColor = "blue";
        } else if (imc >= 18.5 && imc < 25) {
            interpretation = "Corpulence normale";
            styleColor = "green";
        } else if (imc >= 25 && imc < 30) {
            interpretation = "Surpoids";
            styleColor = "orange";
        } else if (imc >= 30 && imc < 35) {
            interpretation = "Obésité modérée";
            styleColor = "darkorange";
        } else if (imc >= 35 && imc < 40) {
            interpretation = "Obésité sévère";
            styleColor = "red";
        } else {
            interpretation = "Obésité morbide ou massive";
            styleColor = "darkred";
        }

        return new IMCClassification(interpretation, styleColor);
    }

    /**
     * Classe pour encapsuler la classification de l'IMC
     */
    public static class IMCClassification {
        private final String interpretation;
        private final String styleColor;

        public IMCClassification(String interpretation, String styleColor) {
            this.interpretation = interpretation;
            this.styleColor = styleColor;
        }

        public String getInterpretation() {
            return interpretation;
        }

        public String getStyleColor() {
            return styleColor;
        }

        public String getStyle() {
            return "-fx-text-fill: " + styleColor + ";";
        }
    }
}
