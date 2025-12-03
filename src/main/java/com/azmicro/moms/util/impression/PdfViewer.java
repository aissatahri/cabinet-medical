/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.util.impression;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Aissa
 */
public class PdfViewer {
    
    public static void openPdf(String pdfPath) {
        try {
            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("Le fichier PDF n'existe pas.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
