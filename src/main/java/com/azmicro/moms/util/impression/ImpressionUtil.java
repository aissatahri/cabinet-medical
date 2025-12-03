/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.util.impression;

import com.azmicro.moms.model.Analyse;
import com.azmicro.moms.model.ConsultationActe;
import com.azmicro.moms.model.Imagerie;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.Prescriptions;
import java.util.List;
import java.io.FileNotFoundException;

/**
 *
 * @author Aissa
 */
public class ImpressionUtil {

    public static <T> void imprimerDocument(TypeImpression type, List<T> listeElements, Patient patient, Medecin medecin) throws FileNotFoundException {
        switch (type) {
            case ORDONNANCE:
                imprimerOrdonnance(patient, (List<Prescriptions>) listeElements, medecin);
                break;
            case ORDONNANCE_BILAN:
                imprimerOrdonnanceBilan(patient, (List<Analyse>) listeElements, medecin);
                break;
            case ORDONNANCE_IMAGERIE:
                imprimerOrdonnanceImagerie(patient, (List<Imagerie>) listeElements, medecin);
                break;
            case CERTIFICAT_MEDICAL:
                imprimerCertificatMedical(patient, listeElements, medecin);
                break;
            
            default:
                System.out.println("Type d'impression inconnu.");
                break;
        }
    }

    private static void imprimerOrdonnance(Patient patient, List<Prescriptions> prescriptions, Medecin medecin) throws FileNotFoundException {
        // Logique pour imprimer la liste des prescriptions
        System.out.println("Impression des prescriptions : " + prescriptions.size());
        // Générer le PDF et récupérer le chemin du fichier
        String pdfPath = PdfGenerator.generatePrescriptionPdf("Détails de la consultation", patient, prescriptions, medecin);

        // Ouvrir le PDF
        PdfViewer.openPdf(pdfPath);

    }

    private static void imprimerOrdonnanceBilan(Patient patient, List<Analyse> bilans, Medecin medecin) throws FileNotFoundException {
        // Logique pour imprimer la liste des bilans
        System.out.println("Impression des bilans : " + bilans.size());
        // Générer le PDF et récupérer le chemin du fichier
        String pdfPath = PdfGenerator.generateAnalysePdf("Détails de la consultation", patient, bilans, medecin);

        // Ouvrir le PDF
        PdfViewer.openPdf(pdfPath);
    }

    private static void imprimerOrdonnanceImagerie(Patient patient, List<Imagerie> imageries, Medecin medecin) throws FileNotFoundException {
        // Logique pour imprimer la liste des imageries
        System.out.println("Impression des imageries : " + imageries.size());
        // Générer le PDF et récupérer le chemin du fichier
        String pdfPath = PdfGenerator.generateImageriePdf("Détails de la consultation", patient, imageries, medecin);

        // Ouvrir le PDF
        PdfViewer.openPdf(pdfPath);
    }

    private static <T> void imprimerCertificatMedical(Patient patient, List<T> listeElements, Medecin medecin) throws FileNotFoundException {
        // Logique pour imprimer un certificat médical
        System.out.println("Impression du certificat médical.");
        // Générer le PDF et récupérer le chemin du fichier
        String pdfPath = PdfGenerator.generateCertificatPdf("Détails de la consultation", patient, listeElements, medecin);

        // Ouvrir le PDF
        PdfViewer.openPdf(pdfPath);
    }

    public static void imprimerFacture(TypeImpression typeImpression, List<ConsultationActe> selectedConsultationActes, Patient patient, Medecin medecin) throws FileNotFoundException {
    // Logique pour imprimer une facture avec plusieurs actes
    System.out.println("Impression de la facture avec plusieurs actes.");

    // Générer le PDF et récupérer le chemin du fichier
    String pdfPath = PdfGenerator.generateFacturePdf("Détails de la consultation", patient, selectedConsultationActes, medecin);

    // Ouvrir le PDF
    PdfViewer.openPdf(pdfPath);
}

}
