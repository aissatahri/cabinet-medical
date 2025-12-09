package com.azmicro.moms.util;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import java.util.regex.Pattern;

/**
 * Utility class for form validation with real-time visual feedback
 */
public class ValidationUtil {
    
    // Regex patterns
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(06|05|07)\\d{8}$|^(06|05|07)\\d{2}-\\d{6}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    // Colors for validation feedback
    private static final String VALID_BORDER = "-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;";
    private static final String INVALID_BORDER = "-fx-border-color: #e74c3c; -fx-border-width: 2px; -fx-border-radius: 4px;";
    private static final String DEFAULT_BORDER = "-fx-border-color: transparent;";
    
    /**
     * Validate phone number in real-time
     * Accepts formats: 0612345678 or 0612-345678
     */
    public static boolean validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * Validate email address
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Setup real-time phone validation on a TextField
     * @param textField The TextField to validate
     * @param errorLabel Optional label to show error message (can be null)
     */
    public static void setupPhoneValidation(TextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                textField.setStyle(DEFAULT_BORDER);
                if (errorLabel != null) {
                    errorLabel.setText("");
                    errorLabel.setVisible(false);
                }
                return;
            }
            
            if (validatePhone(newValue)) {
                textField.setStyle(VALID_BORDER);
                if (errorLabel != null) {
                    errorLabel.setText("✓ Numéro valide");
                    errorLabel.setTextFill(Color.web("#27ae60"));
                    errorLabel.setVisible(true);
                }
            } else {
                textField.setStyle(INVALID_BORDER);
                if (errorLabel != null) {
                    errorLabel.setText("⚠ Format attendu: 06XX-XXXXXX ou 05XX-XXXXXX");
                    errorLabel.setTextFill(Color.web("#e74c3c"));
                    errorLabel.setVisible(true);
                }
            }
        });
    }
    
    /**
     * Setup real-time email validation on a TextField
     * @param textField The TextField to validate
     * @param errorLabel Optional label to show error message (can be null)
     */
    public static void setupEmailValidation(TextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                textField.setStyle(DEFAULT_BORDER);
                if (errorLabel != null) {
                    errorLabel.setText("");
                    errorLabel.setVisible(false);
                }
                return;
            }
            
            if (validateEmail(newValue)) {
                textField.setStyle(VALID_BORDER);
                if (errorLabel != null) {
                    errorLabel.setText("✓ Email valide");
                    errorLabel.setTextFill(Color.web("#27ae60"));
                    errorLabel.setVisible(true);
                }
            } else {
                textField.setStyle(INVALID_BORDER);
                if (errorLabel != null) {
                    errorLabel.setText("⚠ Format invalide (exemple@domaine.com)");
                    errorLabel.setTextFill(Color.web("#e74c3c"));
                    errorLabel.setVisible(true);
                }
            }
        });
    }
    
    /**
     * Format phone number automatically (add dash)
     * Converts 0612345678 to 0612-345678
     */
    public static String formatPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return phone;
        }
        
        String cleaned = phone.replaceAll("[^0-9]", "");
        
        if (cleaned.length() == 10 && cleaned.matches("^(06|05|07).*")) {
            return cleaned.substring(0, 4) + "-" + cleaned.substring(4);
        }
        
        return phone;
    }
    
    /**
     * Check if a string is not empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validate required field
     */
    public static void setupRequiredValidation(TextField textField, Label errorLabel, String fieldName) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // When field loses focus
            if (!newValue) {
                if (!isNotEmpty(textField.getText())) {
                    textField.setStyle(INVALID_BORDER);
                    if (errorLabel != null) {
                        errorLabel.setText("⚠ " + fieldName + " est obligatoire");
                        errorLabel.setTextFill(Color.web("#e74c3c"));
                        errorLabel.setVisible(true);
                    }
                } else {
                    textField.setStyle(VALID_BORDER);
                    if (errorLabel != null) {
                        errorLabel.setText("");
                        errorLabel.setVisible(false);
                    }
                }
            }
        });
    }
}
