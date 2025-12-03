package com.azmicro.moms.util;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 * Service pour la validation et le formatage des champs médicaux
 */
public class MedicalFieldValidator {

    /**
     * Configure un champ de pression artérielle avec formatage automatique (XXX/XX)
     */
    public static void setupPressionArterielleField(TextField tfPression) {
        tfPression.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < oldValue.length()) {
                return; // Backspace pressed
            }

            String cleanText = newValue.replaceAll("[^\\d]", "");

            if (cleanText.length() > 5) {
                cleanText = cleanText.substring(0, 5);
            }

            String formattedText;
            if (cleanText.length() > 3) {
                formattedText = cleanText.substring(0, 3) + "/" + cleanText.substring(3);
            } else {
                formattedText = cleanText;
            }

            if (!formattedText.equals(tfPression.getText())) {
                int caretPosition = tfPression.getCaretPosition();
                tfPression.setText(formattedText);

                if (caretPosition <= 3) {
                    tfPression.positionCaret(caretPosition);
                } else if (caretPosition <= formattedText.length()) {
                    tfPression.positionCaret(caretPosition + 1);
                } else {
                    tfPression.positionCaret(formattedText.length());
                }
            }

            if (cleanText.length() == 5) {
                int systolic = Integer.parseInt(cleanText.substring(0, 3));
                int diastolic = Integer.parseInt(cleanText.substring(3));
                String classification = MedicalCalculationService.classifyBloodPressure(systolic, diastolic);
                tfPression.setTooltip(new Tooltip(classification));
            } else {
                tfPression.setTooltip(null);
            }
        });
    }

    /**
     * Configure un champ de fréquence (limite à 2 chiffres)
     */
    public static void setupFrequenceField(TextField tfFrequence, int maxLength) {
        tfFrequence.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < oldValue.length()) {
                return;
            }

            String cleanText = newValue.replaceAll("[^\\d]", "");

            if (cleanText.length() > maxLength) {
                cleanText = cleanText.substring(0, maxLength);
            }

            if (!cleanText.equals(tfFrequence.getText())) {
                tfFrequence.setText(cleanText);
                tfFrequence.positionCaret(cleanText.length());
            }
        });
    }

    /**
     * Remplace les virgules par des points pour les nombres décimaux
     */
    public static double parseDecimal(String text) {
        return Double.parseDouble(text.replace(",", "."));
    }

    /**
     * Remplace les virgules par des points pour les entiers
     */
    public static int parseInt(String text) {
        return Integer.parseInt(text.replace(",", "."));
    }
}
