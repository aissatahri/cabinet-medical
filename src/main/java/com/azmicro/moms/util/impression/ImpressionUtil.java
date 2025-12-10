/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.util.impression;

import com.azmicro.moms.model.Analyse;
import com.azmicro.moms.model.CompteRenduETT;
import com.azmicro.moms.model.ConsultationActe;
import com.azmicro.moms.model.Imagerie;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.Prescriptions;
import java.util.List;
import java.io.FileNotFoundException;
import javafx.stage.Window;

/**
 *
 * @author Aissa
 */
public class ImpressionUtil {

    public static <T> void imprimerDocument(TypeImpression type, List<T> listeElements, Patient patient, Medecin medecin, Window parentWindow) throws FileNotFoundException {
        switch (type) {
            case ORDONNANCE:
                imprimerOrdonnance(patient, (List<Prescriptions>) listeElements, medecin, parentWindow);
                break;
            case ORDONNANCE_BILAN:
                imprimerOrdonnanceBilan(patient, (List<Analyse>) listeElements, medecin, parentWindow);
                break;
            case ORDONNANCE_IMAGERIE:
                imprimerOrdonnanceImagerie(patient, (List<Imagerie>) listeElements, medecin, parentWindow);
                break;
            case CERTIFICAT_MEDICAL:
                imprimerCertificatMedical(patient, listeElements, medecin, parentWindow);
                break;
            
            default:
                System.out.println("Type d'impression inconnu.");
                break;
        }
    }

    private static void imprimerOrdonnance(Patient patient, List<Prescriptions> prescriptions, Medecin medecin, Window parentWindow) throws FileNotFoundException {
        // Logique pour imprimer la liste des prescriptions
        System.out.println("Impression des prescriptions : " + prescriptions.size());
        // Générer le PDF et récupérer le chemin du fichier
        String pdfPath = PdfGenerator.generatePrescriptionPdf("Détails de la consultation", patient, prescriptions, medecin);

        // Afficher le message de succès et ouvrir le PDF
        PdfSuccessDialog.showSuccessAndOpenPdf(pdfPath, parentWindow, "Ordonnance");
    }

    private static void imprimerOrdonnanceBilan(Patient patient, List<Analyse> bilans, Medecin medecin, Window parentWindow) throws FileNotFoundException {
        // Logique pour imprimer la liste des bilans
        System.out.println("Impression des bilans : " + bilans.size());
        // Générer le PDF et récupérer le chemin du fichier
        String pdfPath = PdfGenerator.generateAnalysePdf("Détails de la consultation", patient, bilans, medecin);

        // Afficher le message de succès et ouvrir le PDF
        PdfSuccessDialog.showSuccessAndOpenPdf(pdfPath, parentWindow, "Ordonnance d'analyse");
    }

    private static void imprimerOrdonnanceImagerie(Patient patient, List<Imagerie> imageries, Medecin medecin, Window parentWindow) throws FileNotFoundException {
        // Logique pour imprimer la liste des imageries
        System.out.println("Impression des imageries : " + imageries.size());
        // Générer le PDF et récupérer le chemin du fichier
        String pdfPath = PdfGenerator.generateImageriePdf("Détails de la consultation", patient, imageries, medecin);

        // Afficher le message de succès et ouvrir le PDF
        PdfSuccessDialog.showSuccessAndOpenPdf(pdfPath, parentWindow, "Ordonnance d'imagerie");
    }

    private static <T> void imprimerCertificatMedical(Patient patient, List<T> listeElements, Medecin medecin, Window parentWindow) throws FileNotFoundException {
        // Logique pour imprimer un certificat médical
        System.out.println("Impression du certificat médical.");
        // Générer le PDF et récupérer le chemin du fichier
        String pdfPath = PdfGenerator.generateCertificatPdf("Détails de la consultation", patient, listeElements, medecin);

        // Afficher le message de succès et ouvrir le PDF
        PdfSuccessDialog.showSuccessAndOpenPdf(pdfPath, parentWindow, "Certificat médical");
    }

    public static void imprimerFacture(TypeImpression typeImpression, List<ConsultationActe> selectedConsultationActes, Patient patient, Medecin medecin, Window parentWindow) throws FileNotFoundException {
    // Logique pour imprimer une facture avec plusieurs actes
    System.out.println("Impression de la facture avec plusieurs actes.");

    // Générer le PDF et récupérer le chemin du fichier
    String pdfPath = PdfGenerator.generateFacturePdf("Détails de la consultation", patient, selectedConsultationActes, medecin);

    // Afficher le message de succès et ouvrir le PDF
    PdfSuccessDialog.showSuccessAndOpenPdf(pdfPath, parentWindow, "Facture");
}

    /**
     * Méthode pour imprimer un compte rendu d'Échocardiographie Transthoracique (ETT)
     */
    public static void imprimerCompteRenduETT(CompteRenduETT compteRendu, Medecin medecin, Window parentWindow) throws FileNotFoundException {
        System.out.println("Impression du compte rendu ETT.");
        
        // Générer le PDF et récupérer le chemin du fichier
        String pdfPath = PdfGenerator.generateCompteRenduETTPdf(compteRendu, medecin);
        
        // Afficher le message de succès et ouvrir le PDF
        PdfSuccessDialog.showSuccessAndOpenPdf(pdfPath, parentWindow, "Compte Rendu ETT");
    }

}
