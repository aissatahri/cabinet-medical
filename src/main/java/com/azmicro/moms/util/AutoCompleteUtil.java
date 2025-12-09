package com.azmicro.moms.util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for auto-completion functionality
 */
public class AutoCompleteUtil {
    
    /**
     * Setup auto-completion on a ComboBox
     * The ComboBox becomes editable and filters items based on user input
     * 
     * @param comboBox The ComboBox to make auto-completable
     * @param items The list of items to filter
     */
    public static <T> void setupAutoComplete(ComboBox<T> comboBox, ObservableList<T> items) {
        comboBox.setEditable(true);
        
        FilteredList<T> filteredItems = new FilteredList<>(items, p -> true);
        comboBox.setItems(filteredItems);
        
        comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (comboBox.getSelectionModel().getSelectedItem() != null) {
                return; // Item was selected, don't filter
            }
            
            String filter = newValue == null ? "" : newValue.toLowerCase();
            
            filteredItems.setPredicate(item -> {
                if (filter.isEmpty()) {
                    return true;
                }
                
                String itemString = item.toString().toLowerCase();
                return itemString.contains(filter);
            });
            
            if (!filteredItems.isEmpty() && !comboBox.isShowing()) {
                comboBox.show();
            }
        });
        
        // When user focuses on the combo, show all items
        comboBox.setOnMouseClicked(event -> {
            if (!comboBox.isShowing()) {
                filteredItems.setPredicate(p -> true);
                comboBox.show();
            }
        });
    }
    
    /**
     * Setup auto-completion with custom toString function
     * Useful for complex objects where you want to search by a specific property
     * 
     * @param comboBox The ComboBox to make auto-completable
     * @param items The list of items to filter
     * @param toStringFunction Function to convert item to searchable string
     */
    public static <T> void setupAutoComplete(ComboBox<T> comboBox, ObservableList<T> items, 
                                            java.util.function.Function<T, String> toStringFunction) {
        comboBox.setEditable(true);
        
        FilteredList<T> filteredItems = new FilteredList<>(items, p -> true);
        comboBox.setItems(filteredItems);
        
        comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (comboBox.getSelectionModel().getSelectedItem() != null) {
                return;
            }
            
            String filter = newValue == null ? "" : newValue.toLowerCase();
            
            filteredItems.setPredicate(item -> {
                if (filter.isEmpty()) {
                    return true;
                }
                
                String itemString = toStringFunction.apply(item).toLowerCase();
                return itemString.contains(filter);
            });
            
            if (!filteredItems.isEmpty() && !comboBox.isShowing()) {
                comboBox.show();
            }
        });
        
        comboBox.setOnMouseClicked(event -> {
            if (!comboBox.isShowing()) {
                filteredItems.setPredicate(p -> true);
                comboBox.show();
            }
        });
    }
    
    /**
     * Setup auto-completion for TextField with suggestions list
     * Shows a dropdown with matching suggestions as user types
     * 
     * @param textField The TextField to add auto-complete
     * @param suggestions List of suggestion strings
     */
    public static void setupTextFieldAutoComplete(TextField textField, List<String> suggestions) {
        // This would require a custom implementation with a Popup
        // For now, we'll use the ComboBox approach which is more standard in JavaFX
        // If you need TextField auto-complete, we can implement it with a Popup later
    }
}
