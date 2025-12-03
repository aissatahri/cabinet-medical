/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.util;

/**
 *
 * @author Aissa
 */
import javafx.util.StringConverter;
import com.azmicro.moms.model.TypeAnalyse;

public class TypeAnalyseStringConverter extends StringConverter<TypeAnalyse> {
    @Override
    public String toString(TypeAnalyse typeAnalyse) {
        return typeAnalyse != null ? typeAnalyse.getNomAnalyseFr() : "";
    }

    @Override
    public TypeAnalyse fromString(String string) {
        // Implement this method if you need to parse strings back to TypeAnalyse objects
        return null;
    }
}
