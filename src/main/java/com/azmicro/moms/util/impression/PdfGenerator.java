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
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.Imagerie;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.Prescriptions;
import com.azmicro.moms.model.RendezVous;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Properties;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
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
        
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String dateSt = date.format(new Date());
        String pdfPath = patientDir.getPath() + "/ordonnance_" + dateStr + ".pdf";
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A5);

        // Set margins
        document.setMargins(140f, 30, 50f, 30);

        float fontSize = 9f;

        // En-tête avec date
        Paragraph dateParagraph = new Paragraph(dateSt)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize + 1)
                .setBold()
                .setMarginBottom(15);
        document.add(dateParagraph);

        // Informations patient - Encadré
        Table patientTable = new Table(UnitValue.createPercentArray(new float[]{1}));
        patientTable.setWidth(UnitValue.createPercentValue(100));
        
        Cell patientCell = new Cell()
                .add(new Paragraph()
                    .add(new Text(patient.getNom().toUpperCase() + " " + patient.getPrenom())
                        .setFontSize(14)
                        .setBold())
                    .add(new Text("\n" + patient.getAge() + " ans")
                        .setFontSize(fontSize))
                    .add(new Text(patient.getSexe() != null ? " • " + patient.getSexe() : "")
                        .setFontSize(fontSize)))
                .setBackgroundColor(new DeviceRgb(240, 248, 255))
                .setPadding(10)
                .setBorder(new SolidBorder(new DeviceRgb(22, 160, 133), 1));
        
        patientTable.addCell(patientCell);
        document.add(patientTable);
        
        // Espacement
        document.add(new Paragraph("\n").setFontSize(8));

        // Liste des prescriptions
        int index = 1;
        for (Prescriptions prescription : prescriptions) {
            // Clean description - extract ONLY the Instructions line, ignore Posologie and Durée lines
            String description = "";
            if (prescription.getDescription() != null && !prescription.getDescription().trim().isEmpty()) {
                String fullDesc = prescription.getDescription();
                String[] descLines = fullDesc.split("\n");
                
                for (String line : descLines) {
                    line = line.trim();
                    // Only keep the Instructions line
                    if (line.startsWith("Instructions:")) {
                        description = line.substring("Instructions:".length()).trim();
                        break; // Stop after finding instructions
                    }
                }
            }
            
            // Clean dose by removing prefix and any embedded duplicates
            String dose = prescription.getDose() != null ? prescription.getDose() : "";
            if (dose.startsWith("Posologie:")) {
                dose = dose.substring("Posologie:".length()).trim();
            }
            // Remove any "• Posologie: ..." that might be embedded
            if (dose.contains("• Posologie:")) {
                dose = dose.substring(0, dose.indexOf("• Posologie:")).trim();
            }
            // Remove any "Posologie: ..." that might be embedded  
            if (dose.contains("Posologie:")) {
                dose = dose.substring(0, dose.indexOf("Posologie:")).trim();
            }
            // Remove any "• Durée: ..." that might be embedded
            if (dose.contains("• Durée:")) {
                dose = dose.substring(0, dose.indexOf("• Durée:")).trim();
            }
            // Remove any "Durée: ..." that might be embedded
            if (dose.contains("Durée:")) {
                dose = dose.substring(0, dose.indexOf("Durée:")).trim();
            }
            // Remove any "• Instructions: ..." that might be embedded
            if (dose.contains("• Instructions:")) {
                dose = dose.substring(0, dose.indexOf("• Instructions:")).trim();
            }
            // Remove any "Instructions: ..." that might be embedded
            if (dose.contains("Instructions:")) {
                dose = dose.substring(0, dose.indexOf("Instructions:")).trim();
            }
            
            // Clean duree by removing prefix if exists
            String duree = prescription.getDuree() != null ? prescription.getDuree() : "";
            if (duree.startsWith("Durée:")) {
                duree = duree.substring("Durée:".length()).trim();
            }

            // Numéro et nom du médicament
            Paragraph medNamePara = new Paragraph()
                    .add(new Text(index + ". ")
                        .setFontSize(fontSize + 1)
                        .setBold()
                        .setFontColor(new DeviceRgb(22, 160, 133)))
                    .add(new Text(prescription.getMedicament().getNomMedicament())
                        .setFontSize(fontSize + 2)
                        .setBold())
                    .add(new Text(" " + prescription.getMedicament().getFormeDosage())
                        .setFontSize(fontSize)
                        .setItalic())
                    .setMarginBottom(3);
            document.add(medNamePara);

            // Construire la posologie complète sur une seule ligne
            StringBuilder posologyText = new StringBuilder();
            posologyText.append(dose);
            
            // Ajouter la durée seulement si elle n'est pas vide et pas déjà dans dose
            if (!duree.trim().isEmpty() && !dose.toLowerCase().contains(duree.toLowerCase())) {
                if (posologyText.length() > 0) {
                    posologyText.append(" • pdt ");
                }
                posologyText.append(duree);
            }
            
            Paragraph posologyPara = new Paragraph()
                    .add(new Text("   ➤ ")
                        .setFontColor(new DeviceRgb(52, 152, 219)))
                    .add(new Text(posologyText.toString())
                        .setFontSize(fontSize + 1));
            
            // Ajouter les instructions sur la même ligne si elles existent
            if (!description.isEmpty()) {
                posologyPara.add(new Text("  •  ")
                        .setFontColor(new DeviceRgb(241, 196, 15)))
                    .add(new Text(description)
                        .setFontSize(fontSize)
                        .setItalic()
                        .setFontColor(new DeviceRgb(52, 73, 94)));
            }
            
            posologyPara.setMarginLeft(10).setMarginBottom(5);
            document.add(posologyPara);

            // Ligne de séparation entre les médicaments
            if (index < prescriptions.size()) {
                document.add(new Paragraph("")
                    .setMarginTop(3)
                    .setMarginBottom(6)
                    .setBorderBottom(new SolidBorder(new DeviceRgb(236, 240, 241), 1)));
            }
            
            index++;
        }

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/demande_imagerie_" + dateStr.replace("/", "-") + ".pdf";
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A5);

        // Set top margin to 4 cm (113.39 points), bottom margin to 48.24f, left and right to 20
        document.setMargins(140f, 30, 50f, 30);

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

        // Add imageries with improved formatting
        int index = 1;
        for (Imagerie imagerie : imageries) {
            String displayText = imagerie.getTypeImagerie().getCodeImagerieFr() + " - " + imagerie.getTypeImagerie().getNomImagerieFr();
            Paragraph imagerieParagraph = new Paragraph()
                    .add(new Text(index + ". ")
                        .setFontSize(fontSize + 2)
                        .setBold()
                        .setFontColor(new DeviceRgb(22, 160, 133)))
                    .add(new Text(displayText)
                        .setFontSize(fontSize + 2))
                    .setMarginLeft(10)
                    .setMarginBottom(5);
            document.add(imagerieParagraph);
            index++;
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/demande_analyse_" + dateStr.replace("/", "-") + ".pdf";
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A5);

        // Set top margin to 4 cm (113.39 points), bottom margin to 48.24f, left and right to 20
        document.setMargins(140f, 30, 50f, 30);

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

        // Add analyses with improved formatting
        int index = 1;
        for (Analyse analyse : bilans) {
            String displayText = analyse.getTypeAnalyse().getCodeAnalyseFr() + " - " + analyse.getTypeAnalyse().getNomAnalyseFr();
            Paragraph analyseParagraph = new Paragraph()
                    .add(new Text(index + ". ")
                        .setFontSize(fontSize + 2)
                        .setBold()
                        .setFontColor(new DeviceRgb(22, 160, 133)))
                    .add(new Text(displayText)
                        .setFontSize(fontSize + 2))
                    .setMarginLeft(10)
                    .setMarginBottom(5);
            document.add(analyseParagraph);
            index++;
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

        document.add(new Paragraph("")
            .setBorderBottom(new SolidBorder(new DeviceRgb(0, 0, 0), 1))
            .setMarginBottom(10));
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(new Date());
        String uniqueTimeStamp = "_" + System.currentTimeMillis(); // Ajouter un horodatage unique
        String pdfPath = patientDir.getPath() + "/facture_" + dateStr.replace("/", "-") + uniqueTimeStamp + ".pdf";

        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A5);
        // Réduire les marges pour maximiser l'espace sur une seule page
        document.setMargins(80, 20, 30, 20);

        float fontSize = 7f;

        // Titre "Facture" centré et date sur la même ligne (compact)
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.setBorder(Border.NO_BORDER);
        
        Cell titleCell = new Cell().add(new Paragraph("Facture")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(fontSize + 4))
                .setBorder(Border.NO_BORDER);
        
        Cell dateCell = new Cell().add(new Paragraph("Date : " + dateStr)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize + 1))
                .setBorder(Border.NO_BORDER);
        
        headerTable.addCell(titleCell);
        headerTable.addCell(dateCell);
        document.add(headerTable);

        // Ligne séparatrice compacte
        document.add(new Paragraph("")
            .setBorderBottom(new SolidBorder(new DeviceRgb(0, 0, 0), 1))
            .setMarginTop(3)
            .setMarginBottom(5));

        // Informations du patient compactes
        Paragraph patientInfo = new Paragraph("Patient : " + patient.getNom() + " " + patient.getPrenom() + " - Âge : " + patient.getAge() + " ans")
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(fontSize + 1)
                .setMarginBottom(8);
        document.add(patientInfo);

        // Créer un tableau compact
        Table table = new Table(new float[]{3, 1});  // Description plus large, montant plus étroit
        table.setWidth(UnitValue.createPercentValue(100));
        table.setFontSize(fontSize);

        // En-têtes du tableau
        table.addCell(new Cell().add(new Paragraph("Description").setBold()).setPadding(3));
        table.addCell(new Cell().add(new Paragraph("Montant (DH)").setBold()).setPadding(3));

        // Calculer le montant total
        double totalMontant = 0;

        // Ajouter chaque acte à la facture avec padding réduit
        for (ConsultationActe acte : selectedConsultationActes) {
            table.addCell(new Cell().add(new Paragraph(acte.getActe().getNomActe())).setPadding(3));
            Cell montantCell = new Cell().add(new Paragraph(String.valueOf(acte.getActe().getPrix())))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setPadding(3);
            table.addCell(montantCell);
            totalMontant += acte.getActe().getPrix();
        }

        // Ligne de total
        table.addCell(new Cell().add(new Paragraph("Total").setBold())
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5)
                .setBackgroundColor(new DeviceRgb(240, 240, 240)));
        table.addCell(new Cell().add(new Paragraph(String.format("%.2f DH", totalMontant)).setBold())
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5)
                .setBackgroundColor(new DeviceRgb(240, 240, 240)));

        document.add(table);

        // Espace flexible avant la signature (s'adapte à l'espace disponible)
        document.add(new Paragraph("\n").setMarginTop(15));

        // Signature et cachet compacts
        Paragraph signature = new Paragraph("Signature du médecin : _____________________")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize)
                .setMarginTop(10);
        document.add(signature);

        Paragraph stamp = new Paragraph("[Cachet du médecin]")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize)
                .setMarginTop(5);
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

    public static String generateFicheSoinsLocauxPdf(Consultations consultation,
                                                     List<Prescriptions> traitementEnCours,
                                                     List<Prescriptions> traitementSortie,
                                                     List<RendezVous> rendezVousLies,
                                                     List<com.azmicro.moms.model.HistoriqueMedical> antecedents,
                                                     Patient patient,
                                                     Medecin medecin) throws FileNotFoundException {
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/fiche_soins_locaux_" + dateStr + ".pdf";

        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(60, 36, 50, 36);

        float labelSize = 10f;
        float valueSize = 10f;
        SimpleDateFormat dateHuman = new SimpleDateFormat("dd/MM/yyyy");

        // Header date
        document.add(new Paragraph("Date : " + dateHuman.format(new Date()))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(labelSize));

        // Title
        document.add(new Paragraph("FICHE DE SOINS LOCAUX")
            .setTextAlignment(TextAlignment.CENTER)
            .setBold()
            .setFontSize(14)
            .setMarginBottom(8));

        // Consultation date under title, aligned left
        if (consultation.getDateConsultation() != null) {
            document.add(new Paragraph("Date de consultation : " + consultation.getDateConsultation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .setTextAlignment(TextAlignment.LEFT)
            .setFontSize(labelSize)
            .setMarginBottom(10));
        }

        // Patient info table 2 cols
        Table info = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
        info.addCell(makeLabelValue("Nom :", patient.getNom() + " " + patient.getPrenom(), labelSize, valueSize));
        info.addCell(makeLabelValue("CIN :", safe(""), labelSize, valueSize));
        info.addCell(makeLabelValue("Date de naissance :", safeDate(patient.getDateNaissance(), dateHuman), labelSize, valueSize));
        info.addCell(makeLabelValue("Couverture sanitaire :", safe(patient.getCouvertureSanitaire()), labelSize, valueSize));
        info.addCell(makeLabelValue("Adresse :", safe(patient.getAdresse()), labelSize, valueSize));
        info.addCell(makeLabelValue("Numéro de téléphone :", safe(patient.getTelephone()), labelSize, valueSize));
        info.addCell(makeLabelValue("Profession :", safe(patient.getProfession()), labelSize, valueSize));
        info.addCell(makeLabelValue("", "", labelSize, valueSize));
        document.add(info.setMarginBottom(10));

        // Motif en plein format gauche
        document.add(makeLabelValueMultiline("Motif de consultation :", safe(consultation.getSymptome()), labelSize, valueSize));
        document.add(new Paragraph(""));

        document.add(makeSectionWithBullets("ATCD :", formatAntecedents(antecedents), labelSize, valueSize));
        document.add(makeSectionWithBullets("Traitement en cours :", formatPrescriptions(traitementEnCours), labelSize, valueSize));

        // Exam section
        document.add(new Paragraph("Examen :").setBold().setFontSize(labelSize + 1).setMarginTop(12).setMarginBottom(6));
        Table exam = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1})).useAllAvailableWidth();
        exam.addCell(makeLabelValue("TA bras droit :", safe(consultation.getPressionDroite()), labelSize, valueSize));
        exam.addCell(makeLabelValue("TA bras gauche :", safe(consultation.getPression()), labelSize, valueSize));
        exam.addCell(makeLabelValue("FC :", formatInt(consultation.getFrequencequardiaque()), labelSize, valueSize));
        exam.addCell(makeLabelValue("SaO2 :", formatInt(consultation.getSaO()), labelSize, valueSize));
        exam.addCell(makeLabelValue("Glycémie :", formatDouble(consultation.getGlycimie()), labelSize, valueSize));
        exam.addCell(makeLabelValue("Poids :", formatDouble(consultation.getPoids()), labelSize, valueSize));
        exam.addCell(makeLabelValue("Température :", formatDouble(consultation.getTemperature()), labelSize, valueSize));
        exam.addCell(makeLabelValue("", "", labelSize, valueSize));
        document.add(exam.setMarginBottom(6));

        // Clinique en plein format gauche
        document.add(makeLabelValueMultiline("Clinique :", safe(consultation.getExamenClinique()), labelSize, valueSize));
        document.add(new Paragraph(""));

        document.add(makeSectionWithBullets("ECG :", "", labelSize, valueSize));
        document.add(makeSectionWithBullets("ETT :", "", labelSize, valueSize));
        document.add(makeSectionWithBullets("Traitement de sortie :", formatPrescriptions(traitementSortie), labelSize, valueSize));
        document.add(makeSectionWithBullets("Remarques :", "", labelSize, valueSize));
        document.add(makeSectionWithBullets("Prochains RDV :", "", labelSize, valueSize));

        // Rendez-vous (sans répétition de titre)
        if (rendezVousLies != null && !rendezVousLies.isEmpty()) {
            Table rdvTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1})).useAllAvailableWidth();
            for (RendezVous rdv : rendezVousLies) {
                String date = rdv.getDate() != null ? rdv.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
                String heures = (rdv.getHourStart() != null ? rdv.getHourStart().toString() : "")
                        + (rdv.getHourEnd() != null ? " - " + rdv.getHourEnd() : "");
                String titre = safe(rdv.getTitre());
                rdvTable.addCell(makeLabelValue("Date :", date, labelSize, valueSize));
                rdvTable.addCell(makeLabelValue("Heure :", heures, labelSize, valueSize));
                rdvTable.addCell(makeLabelValue("Objet :", titre, labelSize, valueSize));
            }
            document.add(rdvTable.setMarginBottom(10));
        }

        document.close();
        return pdfPath;
    }

    private static Cell makeLabelValue(String label, String value, float labelSize, float valueSize) {
        Paragraph p = new Paragraph()
            .add(new Text(label).setBold().setFontSize(labelSize))
            .add(new Text(" " + (value != null ? value : ""))).setFontSize(valueSize);
        return new Cell().add(p).setBorder(Border.NO_BORDER);
    }

    private static Cell makeLabelValueMultiline(String label, String value, float labelSize, float valueSize) {
        Paragraph p = new Paragraph()
            .add(new Text(label).setBold().setFontSize(labelSize))
            .add(new Text("\n" + (value != null ? value : "")).setFontSize(valueSize));
        return new Cell().add(p).setBorder(Border.NO_BORDER);
    }

    private static Paragraph makeSectionWithBullets(String label, String value, float labelSize, float valueSize) {
        Paragraph p = new Paragraph()
                .add(new Text(label).setBold().setFontSize(labelSize))
                .add(new Text("\n").setFontSize(valueSize));

        if (value != null && !value.isBlank()) {
            p.add(new Text(value).setFontSize(valueSize));
        }

        return p.setMarginBottom(8);
    }

    private static String safe(String value) {
        return value != null ? value : "";
    }

    private static String safeDate(LocalDate date, SimpleDateFormat fmt) {
        try {
            return date != null ? fmt.format(java.sql.Date.valueOf(date)) : "";
        } catch (Exception e) {
            return "";
        }
    }

    private static String formatInt(Integer v) {
        return v != null && v > 0 ? String.valueOf(v) : "";
    }

    private static String formatDouble(Double v) {
        return v != null && v > 0 ? String.valueOf(v) : "";
    }

    private static String formatPrescriptions(List<Prescriptions> prescriptions) {
        if (prescriptions == null || prescriptions.isEmpty()) {
            return "";
        }
        List<String> lines = prescriptions.stream()
                .map(PdfGenerator::formatPrescriptionLine)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        if (lines.isEmpty()) {
            return "";
        }

        return lines.stream().map(s -> "• " + s).collect(Collectors.joining("\n"));
    }

    private static String formatAntecedents(List<com.azmicro.moms.model.HistoriqueMedical> antecedents) {
        if (antecedents == null || antecedents.isEmpty()) {
            return "-";
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return antecedents.stream()
                .map(a -> {
                    String date = a.getDate() != null ? a.getDate().format(fmt) : "";
                    String type = a.getType() != null ? a.getType().name() : "";
                    String desc = a.getDescription() != null ? a.getDescription() : "";
                        return Arrays.stream(new String[]{date, type, desc})
                            .filter(s -> s != null && !s.isBlank())
                            .collect(Collectors.joining(" - "));
                })
                .filter(s -> s != null && !s.isBlank())
                .map(s -> "• " + s)
                .collect(Collectors.joining("\n"));
    }

    private static String cleanPosologieText(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace('\u00A0', ' ')
                .replaceAll("(?i)\\bposologie[:]?\\s*", "")
                .replaceAll("(?i)\\binstructions?[:]?\\s*", "")
                .replaceAll("(?i)\\bdur(?:e|\\u00e9)e[:]?\\s*", "")
                .replaceAll("(?i)\\bduree[:]?\\s*", "")
                .replaceAll("•", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static String cleanDureeText(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace('\u00A0', ' ')
                .replaceAll("(?i)\\bdur(?:e|\\u00e9)e[:]?\\s*", "")
                .replaceAll("(?i)\\bduree[:]?\\s*", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static final java.util.regex.Pattern DURATION_PATTERN = java.util.regex.Pattern.compile("(?i)\\b\\d+\\s*(?:j|jr|jrs|jour|jours)\\b");
    private static final java.util.regex.Pattern FREQ_PATTERN = java.util.regex.Pattern.compile("(?i)\\b\\d+\\s*x\\s*/\\s*jour\\b");

    private static void collectSegments(String raw, LinkedHashSet<String> acc) {
        if (raw == null || raw.isBlank()) return;
        String normalized = raw.replace('\u00A0', ' ').replaceAll("\\s+", " ").trim();

        // Split on hyphen separators while preserving values like "3x/jour"
        for (String token : normalized.split("\\s*-\\s*")) {
            String t = token.trim();
            if (t.isEmpty()) continue;

            // Extract frequency parts to avoid duplicates like "3x/jour" twice
            java.util.regex.Matcher f = FREQ_PATTERN.matcher(t);
            while (f.find()) {
                String freq = f.group().replaceAll("\\s+", "").toLowerCase();
                if (!freq.isEmpty()) acc.add(freq);
            }
            t = f.replaceAll("").trim();

            // Extract duration parts inside the token to avoid duplicates like "3x/jour 7 jours"
            java.util.regex.Matcher m = DURATION_PATTERN.matcher(t);
            while (m.find()) {
                String d = m.group().trim();
                if (!d.isEmpty()) acc.add(d);
            }
            // Remove duration segments from the token
            t = m.replaceAll("").trim();
            if (!t.isEmpty()) acc.add(t);
        }
    }

    private static String formatPrescriptionLine(Prescriptions p) {
        if (p == null) {
            return "";
        }

        String med = p.getMedicament() != null ? safe(p.getMedicament().getNomMedicament()) : "";
        String dose = cleanPosologieText(safe(p.getDose()));
        String duree = cleanDureeText(safe(p.getDuree()));
        if (!duree.isEmpty()) {
            dose = dose.replaceAll("(?i)\\b" + java.util.regex.Pattern.quote(duree) + "\\b", "").trim();
        }
        dose = dose.replaceAll("(?i)\\b\\d+\\s*(?:j|jr|jrs|jour|jours)\\b", "").trim();
        String desc = cleanPosologieText(safe(p.getDescription()));

        LinkedHashSet<String> parts = new LinkedHashSet<>();
        if (!med.isEmpty()) parts.add(med);
        collectSegments(dose, parts);
        collectSegments(duree, parts);
        collectSegments(desc, parts);

        // Normalize to avoid duplicates like "3x/jour" reappearing with slight spacing differences
        LinkedHashMap<String, String> ordered = new LinkedHashMap<>();
        for (String part : parts) {
            String key = part.toLowerCase().replaceAll("\\s+", "").trim();
            if (!ordered.containsKey(key)) {
                ordered.put(key, part);
            }
        }

        return ordered.values().stream().collect(Collectors.joining(" - "));
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

        document.add(new Paragraph("")
            .setBorderBottom(new SolidBorder(new DeviceRgb(0, 0, 0), 1))
            .setMarginBottom(10));
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
