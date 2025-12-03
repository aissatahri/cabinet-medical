/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.util.impression;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Analyse;
import com.azmicro.moms.model.ConsultationActe;
import com.azmicro.moms.model.Imagerie;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.Prescriptions;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.properties.VerticalAlignment;

public class PdfGenerator {

    private static String getOutputDirectory() {
        // Charger le répertoire de sortie à partir des propriétés de configuration
        Properties properties = new Properties();
        String configFilePath = System.getProperty("user.home") + "/app-config/config.properties";
        File configFile = new File(configFilePath);

        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                properties.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return properties.getProperty("output.directory", "D:/output");
    }

    public static String generatePrescriptionPdf(String consultationDetails, Patient patient, List<Prescriptions> prescriptions, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStr = dateFormat.format(new Date());
        
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyy");
        String dateSt = date.format(new Date());
        String pdfPath = patientDir.getPath() + "/ordonnance_" + dateStr + ".pdf";
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A5);

        // Set top margin to 4 cm (113.39 points), bottom margin to 48.24f, left and right to 20
        document.setMargins(140f, 40, 48.24f, 20);

        float fontSize = 8f;

        // Date paragraph, positioned at 4 cm from the top
        Paragraph dateParagraph = new Paragraph(" " + dateSt)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize + 2)
                .setBold();

        // Add date at 4 cm from the top
        document.add(dateParagraph);

        // Add empty paragraph to create space between date and patient info (1 cm space)
        document.add(new Paragraph("").setFontSize(12f));

        // Create the name and age text with different font sizes
        Text nameText = new Text(patient.getNom() + " " + patient.getPrenom())
                .setFontSize(16) // Bold and size 16 for the name
                .setBold();

        Text ageText = new Text(" (" + patient.getAge() + " ans)")
                .setFontSize(fontSize + 3);  // Smaller font size for age

        // Combine the name and age into one paragraph
        Paragraph patientInfo = new Paragraph()
                .add(nameText)
                .add(ageText)
                .setMarginLeft(85.04f) // Set left margin to 3 cm
                .setTextAlignment(TextAlignment.LEFT);

        // Add the patient info paragraph to the document
        document.add(patientInfo);

        document.add(new Paragraph("\n").setFontSize(fontSize));

        for (Prescriptions prescription : prescriptions) {
            String description = (prescription.getDescription() != null) ? prescription.getDescription() : "";

            // Création d'un paragraphe pour le nom et la forme de dosage du médicament avec une puce
            Paragraph prescriptionParagraph = new Paragraph()
                    .setFontSize(fontSize + 2)
                    .add("- " + prescription.getMedicament().getNomMedicament() + " " + prescription.getMedicament().getFormeDosage());

            // Création d'un nouveau paragraphe pour la dose et la durée
            Paragraph doseDurationParagraph = new Paragraph()
                    .setFontSize(fontSize + 2)
                    .add(" - " + prescription.getDose() + " " + prescription.getDuree())
                    .setMarginLeft(50); // Décalage à gauche pour simuler une tabulation

            // Ajout du paragraphe pour la description (si elle est présente)
            Paragraph descriptionParagraph = new Paragraph()
                    .setFontSize(fontSize + 2)
                    .setMarginLeft(50); // Décalage à gauche pour simuler une tabulation
            if (!description.isEmpty()) {
                descriptionParagraph.add(" - " + description);
            }

            // Ajout des paragraphes au document dans l'ordre voulu
            document.add(prescriptionParagraph);
            document.add(doseDurationParagraph);
            if (!description.isEmpty()) {
                document.add(descriptionParagraph);
            }
        }

//        document.add(new Paragraph("\n").setFontSize(fontSize + 2));
//        document.add(new Paragraph("\n").setFontSize(48.24f + 30));
//
//        // Add signature
//        Paragraph signature = new Paragraph("Signature du médecin : _____________________\n\n")
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontSize(fontSize);
//        document.add(signature);
//
//        // Add stamp
//        Paragraph stamp = new Paragraph("[Cachet du médecin]")
//                .setTextAlignment(TextAlignment.RIGHT)
//                .setFontSize(fontSize);
//        document.add(stamp);
        document.close();

        return pdfPath;
    }

    public static String generateImageriePdf(String consultationDetails, Patient patient, List<Imagerie> imageries, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/demande_imagerie_" + dateStr + ".pdf";
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A5);

        // Set top margin to 4 cm (113.39 points), bottom margin to 48.24f, left and right to 20
        document.setMargins(113.39f, 20, 48.24f, 20);

        float fontSize = 8f;

        // Create a table with 2 columns for title and date
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        table.setWidth(PageSize.A5.getWidth() - 40); // Adjust table width to fit within margins

        // Title cell
        Cell titleCell = new Cell()
                .add(new Paragraph("Demande d'Imagerie")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBold()
                        .setFontSize(fontSize + 4))
                .setBorder(Border.NO_BORDER);
        titleCell.setVerticalAlignment(VerticalAlignment.MIDDLE);

        // Date cell
        Cell dateCell = new Cell()
                .add(new Paragraph(dateStr)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(fontSize + 2))
                .setBorder(Border.NO_BORDER);
        dateCell.setVerticalAlignment(VerticalAlignment.MIDDLE);

        table.addCell(titleCell);
        table.addCell(dateCell);

        document.add(table);

        // Add empty paragraph to create space between date and patient info (1 cm space)
        document.add(new Paragraph("\n").setFontSize(12f));

        // Create the name and age text with different font sizes
        Text nameText = new Text(patient.getNom() + " " + patient.getPrenom())
                .setFontSize(16) // Bold and size 16 for the name
                .setBold();

        Text ageText = new Text(" (" + patient.getAge() + " ans)")
                .setFontSize(fontSize + 3);  // Smaller font size for age

        // Combine the name and age into one paragraph
        Paragraph patientInfo = new Paragraph()
                .add(nameText)
                .add(ageText)
                .setMarginLeft(85.04f) // Set left margin to 3 cm
                .setTextAlignment(TextAlignment.LEFT);

        // Add the patient info paragraph to the document
        document.add(patientInfo);

        document.add(new Paragraph("\n").setFontSize(fontSize));

        // Add imageries
        for (Imagerie imagerie : imageries) {
            Paragraph imagerieParagraph = new Paragraph()
                    .setFontSize(fontSize + 2)
                    .add("\t - " + imagerie.getTypeImagerie().getCodeImagerieFr());
            document.add(imagerieParagraph);
        }

        document.add(new Paragraph("\n").setFontSize(fontSize + 2));
        document.add(new Paragraph("\n").setFontSize(48.24f + 30));

        // Add signature
        Paragraph signature = new Paragraph("Signature du médecin : _____________________\n\n")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize);
        document.add(signature);

        // Add stamp
        Paragraph stamp = new Paragraph("[Cachet du médecin]")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize);
        document.add(stamp);

        document.close();

        return pdfPath;
    }

    public static String generateAnalysePdf(String consultationDetails, Patient patient, List<Analyse> bilans, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/demande_analyse_" + dateStr + ".pdf";
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A5);

        // Set top margin to 4 cm (113.39 points), bottom margin to 48.24f, left and right to 20
        document.setMargins(113.39f, 20, 48.24f, 20);

        float fontSize = 8f;

        // Create a table with 2 columns for title and date
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        table.setWidth(PageSize.A5.getWidth() - 40); // Adjust table width to fit within margins

        // Title cell
        Cell titleCell = new Cell()
                .add(new Paragraph("Demande d'Analyse")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBold()
                        .setFontSize(fontSize + 4))
                .setBorder(Border.NO_BORDER);
        titleCell.setVerticalAlignment(VerticalAlignment.MIDDLE);

        // Date cell
        Cell dateCell = new Cell()
                .add(new Paragraph(dateStr)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(fontSize + 2))
                .setBorder(Border.NO_BORDER);
        dateCell.setVerticalAlignment(VerticalAlignment.MIDDLE);

        table.addCell(titleCell);
        table.addCell(dateCell);

        document.add(table);

        // Add empty paragraph to create space between date and patient info (1 cm space)
        document.add(new Paragraph("\n").setFontSize(12f));

        // Create the name and age text with different font sizes
        Text nameText = new Text(patient.getNom() + " " + patient.getPrenom())
                .setFontSize(16) // Bold and size 16 for the name
                .setBold();

        Text ageText = new Text(" (" + patient.getAge() + " ans)")
                .setFontSize(fontSize + 3);  // Smaller font size for age

        // Combine the name and age into one paragraph
        Paragraph patientInfo = new Paragraph()
                .add(nameText)
                .add(ageText)
                .setMarginLeft(85.04f) // Set left margin to 3 cm
                .setTextAlignment(TextAlignment.LEFT);

        // Add the patient info paragraph to the document
        document.add(patientInfo);

        document.add(new Paragraph("\n").setFontSize(fontSize));

        // Add analyses
        for (Analyse analyse : bilans) {
            Paragraph analyseParagraph = new Paragraph()
                    .setFontSize(fontSize + 2)
                    .add("\t - " + analyse.getTypeAnalyse().getCodeAnalyseFr());
            document.add(analyseParagraph);
        }

        document.add(new Paragraph("\n").setFontSize(fontSize + 2));
        document.add(new Paragraph("\n").setFontSize(48.24f + 30));

        // Add signature
        Paragraph signature = new Paragraph("Signature du médecin : _____________________\n\n")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize);
        document.add(signature);

        // Add stamp
        Paragraph stamp = new Paragraph("[Cachet du médecin]")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize);
        document.add(stamp);

        document.close();

        return pdfPath;
    }

    public static <T> String generateCertificatPdf(String consultationDetails, Patient patient, List<T> listeElements, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/certificat_medical_" + dateStr + ".pdf";
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A5);
        document.setMargins(127, 20, 48.24f, 20);

        float fontSize = 8f;

        // Titre "Certificat Médical" et date
        Paragraph title = new Paragraph("Certificat Médical")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(fontSize + 4);

        Paragraph dateParagraph = new Paragraph("Date : " + dateStr)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize + 2);

        document.add(new Paragraph("\n").setFontSize(fontSize));
        document.add(title);
        document.add(dateParagraph);

        document.add(new LineSeparator(new CustomLineDrawer(1f, new DeviceRgb(0, 0, 0))));
        document.add(new Paragraph("\n").setFontSize(fontSize));

        // Informations du patient
        Paragraph patientInfo = new Paragraph("Patient : " + patient.getNom() + " " + patient.getPrenom() + "\n"
                + "Âge : " + patient.getAge() + " ans")
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(fontSize + 3);
        document.add(patientInfo);

        document.add(new Paragraph("\n").setFontSize(fontSize));

        // Ajouter chaque élément du certificat (utilisé de manière générique)
        for (T element : listeElements) {
            Paragraph elementParagraph = new Paragraph()
                    .setFontSize(fontSize + 2)
                    .add("\t" + element.toString());
            document.add(elementParagraph);
        }

        document.add(new Paragraph("\n").setFontSize(fontSize + 2));

        float spaceAboveFooter = 48.24f + 30;
        document.add(new Paragraph("\n").setFontSize(spaceAboveFooter));

        // Signature et cachet du médecin
        Paragraph signature = new Paragraph("Signature du médecin : _____________________\n\n")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize);
        document.add(signature);

        Paragraph stamp = new Paragraph("[Cachet du médecin]")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize);
        document.add(stamp);

        document.close();
        return pdfPath;
    }

    public static String generateFacturePdf(String consultationDetails, Patient patient, List<ConsultationActe> selectedConsultationActes, Medecin medecin) throws FileNotFoundException {
        // Créer le dossier du patient basé sur son nom
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        // Générer la date et un horodatage pour le nom du fichier
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
        String dateStr = dateFormat.format(new Date());
        String uniqueTimeStamp = "_" + System.currentTimeMillis(); // Ajouter un horodatage unique
        String pdfPath = patientDir.getPath() + "/facture_" + dateStr + uniqueTimeStamp + ".pdf";

        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A5);
        document.setMargins(127, 20, 48.24f, 20);

        float fontSize = 8f;

        // Titre "Facture" centré et date alignée à droite sur la même ligne
        Paragraph title = new Paragraph("Facture")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(fontSize + 4);

        Paragraph dateParagraph = new Paragraph("Date : " + dateStr)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize + 2);

        document.add(new Paragraph("\n").setFontSize(fontSize));
        document.add(title);
        document.add(dateParagraph);

        // Ajouter une ligne séparatrice
        LineSeparator lineSeparator = new LineSeparator(new CustomLineDrawer(1f, new DeviceRgb(0, 0, 0)));
        document.add(lineSeparator);

        document.add(new Paragraph("\n").setFontSize(fontSize));

        // Informations du patient
        Paragraph patientInfo = new Paragraph("Patient : " + patient.getNom() + " " + patient.getPrenom() + "\n"
                + "Âge : " + patient.getAge() + " ans")
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(fontSize + 3);
        document.add(patientInfo);

        document.add(new Paragraph("\n").setFontSize(fontSize));

        // Créer un tableau qui s'ajuste à la largeur de la fenêtre
        Table table = new Table(new float[]{1, 1});  // Deux colonnes de même largeur
        table.setWidth(UnitValue.createPercentValue(100));  // 100% de la largeur disponible
        table.setFontSize(fontSize + 3);

        table.addCell(new Cell().add(new Paragraph("Description")));
        table.addCell(new Cell().add(new Paragraph("Montant (DH)")));

        // Calculer le montant total
        double totalMontant = 0;

        // Ajouter chaque acte à la facture
        for (ConsultationActe acte : selectedConsultationActes) {
            table.addCell(new Cell().add(new Paragraph(acte.getActe().getNomActe())));
            // Aligner le montant à droite
            Cell montantCell = new Cell().add(new Paragraph(String.valueOf(acte.getActe().getPrix())))
                    .setTextAlignment(TextAlignment.RIGHT);
            table.addCell(montantCell);

            // Ajouter au total
            totalMontant += acte.getActe().getPrix();
        }

        // Ajouter la ligne de total au tableau
        table.addCell(new Cell().add(new Paragraph("Total"))
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(new Paragraph(String.format("%.2f DH", totalMontant)))
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT));

        document.add(table);

        document.add(new Paragraph("\n").setFontSize(fontSize + 2));

        // Ajouter de l'espace pour positionner la signature et le cachet près de la marge inférieure
        float spaceAboveFooter = 48.24f + 30; // Ajuster pour positionner la signature et le cachet au-dessus de la marge inférieure
        document.add(new Paragraph("\n").setFontSize(spaceAboveFooter));

        // Signature
        Paragraph signature = new Paragraph("Signature du médecin : _____________________\n\n")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize);
        document.add(signature);

        // Placeholder pour le cachet du médecin
        Paragraph stamp = new Paragraph("[Cachet du médecin]")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize);
        document.add(stamp);

        // Fermer le document
        document.close();

        return pdfPath;
    }

    // Méthodes spécifiques pour chaque type de certificat avec noms de fichiers uniques
    
    public static String generateCertificatConsultationPdf(String certificatText, Patient patient, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/certificat_consultation_" + dateStr + ".pdf";
        
        return generateGenericCertificat(pdfPath, "Certificat Médical de Consultation", certificatText, patient, medecin);
    }

    public static String generateCertificatAptitudeSportivePdf(String certificatText, Patient patient, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/certificat_aptitude_sportive_" + dateStr + ".pdf";
        
        return generateGenericCertificat(pdfPath, "Certificat d'Aptitude Sportive", certificatText, patient, medecin);
    }

    public static String generateCertificatArretScolairePdf(String certificatText, Patient patient, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/certificat_arret_scolaire_" + dateStr + ".pdf";
        
        return generateGenericCertificat(pdfPath, "Certificat d'Arrêt Scolaire", certificatText, patient, medecin);
    }

    public static String generateCertificatArretTravailPdf(String certificatText, Patient patient, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/certificat_arret_travail_" + dateStr + ".pdf";
        
        return generateGenericCertificat(pdfPath, "Certificat d'Arrêt de Travail", certificatText, patient, medecin);
    }

    public static String generateCertificatMaladieChroniquePdf(String certificatText, Patient patient, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/certificat_maladie_chronique_" + dateStr + ".pdf";
        
        return generateGenericCertificat(pdfPath, "Certificat Maladie Chronique", certificatText, patient, medecin);
    }

    public static String generateFicheSoinsLocauxPdf(String ficheText, Patient patient, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/fiche_soins_locaux_" + dateStr + ".pdf";
        
        return generateGenericCertificat(pdfPath, "Fiche de Soins Locaux", ficheText, patient, medecin);
    }

    public static String generateLettreOrientationPdf(String lettreText, Patient patient, Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/lettre_orientation_" + dateStr + ".pdf";
        
        return generateGenericCertificat(pdfPath, "Lettre d'Orientation", lettreText, patient, medecin);
    }

    private static String generateGenericCertificat(String pdfPath, String titre, String contenu, Patient patient, Medecin medecin) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(127, 40, 48.24f, 40);

        float fontSize = 10f;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(new Date());

        // Titre
        Paragraph title = new Paragraph(titre)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(fontSize + 6)
                .setUnderline();

        document.add(new Paragraph("\n").setFontSize(fontSize));
        document.add(title);
        
        // Date
        Paragraph dateParagraph = new Paragraph("Date : " + dateStr)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize);
        document.add(dateParagraph);

        document.add(new LineSeparator(new CustomLineDrawer(1f, new DeviceRgb(0, 0, 0))));
        document.add(new Paragraph("\n").setFontSize(fontSize));

        // Contenu du certificat
        Paragraph contenuParagraph = new Paragraph(contenu)
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(fontSize);
        document.add(contenuParagraph);

        document.add(new Paragraph("\n\n").setFontSize(fontSize));

        // Signature
        Paragraph signature = new Paragraph("Signature et cachet du médecin\n\n\n\n")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize);
        document.add(signature);

        Paragraph medecinInfo = new Paragraph("Dr " + medecin.getNom() + " " + medecin.getPrenom())
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize)
                .setBold();
        document.add(medecinInfo);

        document.close();
        return pdfPath;
    }
}
