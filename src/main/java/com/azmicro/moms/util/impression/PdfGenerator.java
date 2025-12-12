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
import com.azmicro.moms.model.CompteRenduETT;
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
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

public class PdfGenerator {
    // A5 en orientation Portrait : utiliser PageSize.A5 standard ISO
    // PageSize.A5 = 420 x 595 points (valeurs arrondies standard reconnues par tous les systèmes)
    private static com.itextpdf.kernel.geom.PageSize getExactA5PageSize() {
        // Utiliser PageSize.A5 standard mais vérifier l'orientation
        // A5 portrait DOIT avoir largeur < hauteur (420 x 595)
        PageSize a5 = PageSize.A5;
        
        // S'assurer que c'est en portrait (largeur < hauteur)
        if (a5.getWidth() > a5.getHeight()) {
            // Si en paysage, créer manuellement en portrait
            return new PageSize(420, 595);
        }
        
        return a5;
    }
    
    /**
     * Configure un PdfDocument avec le format A5 et les métadonnées nécessaires
     * pour une reconnaissance automatique par les imprimantes
     */
    private static void configureA5Document(PdfDocument pdfDoc) {
        PageSize a5 = getExactA5PageSize();
        pdfDoc.setDefaultPageSize(a5);
        
        // Ajouter les préférences du viewer pour forcer le format A5
        PdfViewerPreferences viewerPreferences = new PdfViewerPreferences();
        viewerPreferences.setPrintScaling(PdfViewerPreferences.PdfViewerPreferencesConstants.NONE);
        pdfDoc.getCatalog().setViewerPreferences(viewerPreferences);
    }

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

    private static class FontConfig {
        final String titleFamily; final float titleSize;
        final String textFamily; final float textSize;
        final String ordFamily; final float ordSize;
        final String formFamily; final float formSize;
        FontConfig(String titleFamily, float titleSize, String textFamily, float textSize,
                   String ordFamily, float ordSize, String formFamily, float formSize) {
            this.titleFamily = titleFamily; this.titleSize = titleSize;
            this.textFamily = textFamily; this.textSize = textSize;
            this.ordFamily = ordFamily; this.ordSize = ordSize;
            this.formFamily = formFamily; this.formSize = formSize;
        }
    }

    private static FontConfig loadFontConfig() {
        Properties properties = new Properties();
        String configFilePath = System.getProperty("user.home") + "/app-config/config.properties";
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                properties.load(input);
            } catch (IOException e) {
                // ignore
            }
        }
        String titleFamily = properties.getProperty("pdf.font.title.family", "Helvetica");
        float titleSize = parseFloat(properties.getProperty("pdf.font.title.size", "16"), 16f);
        String textFamily = properties.getProperty("pdf.font.text.family", "Cambria");
        float textSize = parseFloat(properties.getProperty("pdf.font.text.size", "12"), 12f);
        String ordFamily = properties.getProperty("pdf.font.ord.family", "Times New Roman");
        float ordSize = parseFloat(properties.getProperty("pdf.font.ord.size", "12"), 12f);
        String formFamily = properties.getProperty("pdf.font.form.family", "Arial");
        float formSize = parseFloat(properties.getProperty("pdf.font.form.size", "11"), 11f);
        return new FontConfig(titleFamily, titleSize, textFamily, textSize, ordFamily, ordSize, formFamily, formSize);
    }

    private static float parseFloat(String s, float def) {
        try { return Float.parseFloat(s); } catch (Exception e) { return def; }
    }

    private static PdfFont resolveConfiguredFont(String family) {
        // Try project fonts directory first
        String base = System.getProperty("user.dir");
        String[] candidates = new String[] {
            base + "/src/main/resources/com/azmicro/moms/fonts/" + mapFamilyToFile(family),
            base + "/target/classes/com/azmicro/moms/fonts/" + mapFamilyToFile(family),
            base + "/src/main/resources/fonts/" + mapFamilyToFile(family),
            base + "/target/classes/fonts/" + mapFamilyToFile(family),
            // Windows system fonts
            "C:/Windows/Fonts/" + mapFamilyToFile(family)
        };
        for (String path : candidates) {
            try {
                File f = new File(path);
                if (f.exists()) {
                    return PdfFontFactory.createFont(path, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                }
            } catch (Exception ignored) {}
        }
        // Fallback: return null so iText uses its default font (typically Helvetica)
        return null;
    }

    private static String mapFamilyToFile(String family) {
        if (family == null) return "";
        String f = family.trim().toLowerCase();
        if (f.contains("helvetica")) return ""; // will fallback to StandardFonts.HELVETICA
        if (f.contains("calibri")) return "calibri.ttf";
        if (f.contains("cambria")) return "cambria.ttf";
        if (f.contains("book antiqu")) return "BOOKANT.TTF";
        if (f.contains("times")) return "times.ttf"; // may vary per system
        if (f.contains("arial")) return "arial.ttf";
        if (f.contains("segoe")) return "segoeui.ttf";
        if (f.contains("garamond")) return "garamond.ttf"; // common name; also try GARA.TTF
        return "arial.ttf"; // default fallback name
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
        
        // Configurer le format A5 avec métadonnées pour reconnaissance automatique
        configureA5Document(pdfDoc);
        
        Document document = new Document(pdfDoc, getExactA5PageSize());

        // Set margins
        document.setMargins(140f, 30, 50f, 30);

        FontConfig fc = loadFontConfig();
        float fontSize = fc.ordSize; // ordonnance body size
        PdfFont pdfFont = resolveConfiguredFont(fc.ordFamily);
        if (pdfFont != null) {
            document.setFont(pdfFont);
        }

        // En-tête avec date
        Paragraph dateParagraph = new Paragraph(dateSt)
            .setTextAlignment(TextAlignment.RIGHT)
            .setFontSize(fc.titleSize)
                .setBold()
                .setMarginBottom(15);
        document.add(dateParagraph);

        // Informations patient - Encadré
        Table patientTable = new Table(UnitValue.createPercentArray(new float[]{1}));
        patientTable.setWidth(UnitValue.createPercentValue(100));
        
        Cell patientCell = new Cell()
                .add(new Paragraph()
                    .add(new Text(patient.getNom().toUpperCase() + " " + patient.getPrenom())
                        .setFontSize(fc.titleSize)
                        .setBold())
                    .add(new Text("\n" + patient.getAge() + " ans")
                        .setFontSize(fontSize))
                    .add(new Text(formatAgeDescriptor(patient))
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
            // Clean description - handle both structured (with prefix) and free text modes
            String description = "";
            if (prescription.getDescription() != null && !prescription.getDescription().trim().isEmpty()) {
                String fullDesc = prescription.getDescription();
                String[] descLines = fullDesc.split("\n");
                
                boolean foundInstructionsPrefix = false;
                for (String line : descLines) {
                    line = line.trim();
                    // Only keep the Instructions line if prefix exists
                    if (line.startsWith("Instructions:")) {
                        description = line.substring("Instructions:".length()).trim();
                        foundInstructionsPrefix = true;
                        break; // Stop after finding instructions
                    }
                }
                
                // Si aucun préfixe trouvé, c'est du texte libre - prendre tout
                if (!foundInstructionsPrefix) {
                    description = fullDesc.trim();
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
                        .setFontSize(fc.titleSize)
                        .setBold()
                        .setFontColor(new DeviceRgb(22, 160, 133)))
                    .add(new Text(prescription.getMedicament().getNomMedicament())
                        .setFontSize(fc.titleSize)
                        .setBold())
                    .add(new Text(" " + prescription.getMedicament().getFormeDosage())
                        .setFontSize(fontSize)
                        .setItalic())
                    .setMarginBottom(3);
            document.add(medNamePara);

            // Construire la posologie complète sur une seule ligne
            StringBuilder posologyText = new StringBuilder();
            
            // Si dose et duree sont vides, c'est du texte libre stocké dans description
            if (dose.trim().isEmpty() && duree.trim().isEmpty() && !description.isEmpty()) {
                // Mode texte libre : afficher la description comme posologie
                posologyText.append(description);
                description = ""; // Vider description pour ne pas la dupliquer
            } else {
                // Mode structuré : construire dose + durée
                posologyText.append(dose);
                
                // Ajouter la durée seulement si elle n'est pas vide et pas déjà dans dose
                if (!duree.trim().isEmpty() && !dose.toLowerCase().contains(duree.toLowerCase())) {
                    if (posologyText.length() > 0) {
                        posologyText.append(" • pdt ");
                    }
                    posologyText.append(duree);
                }
            }
            
            Paragraph posologyPara = new Paragraph()
                    // Use a widely supported bullet to avoid missing glyphs
                    .add(new Text("   • ")
                        .setFontColor(new DeviceRgb(52, 152, 219)))
                    .add(new Text(posologyText.toString())
                        .setFontSize(fontSize));
            
            // Ajouter les instructions sur la même ligne si elles existent (mode structuré uniquement)
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

    private static String formatAgeDescriptor(Patient patient) {
        try {
            Integer age = patient.getAge();
            com.azmicro.moms.model.Sexe sexe = patient.getSexe();
            if (age == null || age <= 0) {
                return ""; // Unknown age: avoid descriptor
            }
            String s = sexe != null ? sexe.name().toLowerCase() : "";
            boolean isFemale = s.startsWith("fem") || s.equals("f") || s.contains("girl") || s.contains("fille");

            if (age < 12) {
                return " • enfant";
            } else if (age < 18) {
                return isFemale ? " • jeune fille" : " • jeune garçon";
            } else {
                if (s.isEmpty()) return ""; // If sex unknown, skip
                return isFemale ? " • femme" : " • homme";
            }
        } catch (Exception e) {
            return "";
        }
    }

    private static String formatSexLabel(Patient patient) {
        try {
            Integer age = patient.getAge();
            com.azmicro.moms.model.Sexe sexe = patient.getSexe();
            if (age == null || age <= 0) {
                return sexe != null ? sexe.getDescription().toLowerCase() : "Non renseigné";
            }
            if (age < 12) {
                return "enfant";
            } else if (age < 18) {
                return sexe == com.azmicro.moms.model.Sexe.F ? "jeune fille" : "jeune garçon";
            } else {
                return sexe == com.azmicro.moms.model.Sexe.F ? "femme" : "homme";
            }
        } catch (Exception e) {
            return sexeFallback(patient);
        }
    }

    private static String sexeFallback(Patient patient) {
        try {
            com.azmicro.moms.model.Sexe s = patient.getSexe();
            return s != null ? s.getDescription().toLowerCase() : "Non renseigné";
        } catch (Exception e) {
            return "Non renseigné";
        }
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
        String uniqueTimeStamp = "_" + System.currentTimeMillis();
        String pdfPath = patientDir.getPath() + "/demande_imagerie_" + dateStr.replace("/", "-") + uniqueTimeStamp + ".pdf";
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        configureA5Document(pdfDoc);
        Document document = new Document(pdfDoc, getExactA5PageSize());

        // Set top margin to 4 cm (113.39 points), bottom margin to 48.24f, left and right to 20
        document.setMargins(140f, 30, 50f, 30);

        FontConfig fc = loadFontConfig();
        float fontSize = fc.textSize - 7f < 8f ? 8f : fc.textSize - 7f; // keep imagerie/analyse compact
        PdfFont textFont = resolveConfiguredFont(fc.textFamily);
        PdfFont titleFont = resolveConfiguredFont(fc.titleFamily);
        if (textFont != null) {
            document.setFont(textFont);
        }

        // Create a table with 2 columns for title and date
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        table.setWidth(PageSize.A5.getWidth() - 40); // Adjust table width to fit within margins

        // Title cell
        Cell titleCell = new Cell()
            .add(new Paragraph("Demande d'Imagerie")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(fc.titleSize))
                .setBorder(Border.NO_BORDER);
        titleCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        if (titleFont != null) { titleCell.setFont(titleFont); }

        // Date cell
        Cell dateCell = new Cell()
            .add(new Paragraph(dateStr)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fc.titleSize))
                .setBorder(Border.NO_BORDER);
        dateCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        if (titleFont != null) { dateCell.setFont(titleFont); }

        table.addCell(titleCell);
        table.addCell(dateCell);

        document.add(table);

        // Add empty paragraph to create space between date and patient info (1 cm space)
        document.add(new Paragraph("\n").setFontSize(12f));

        // Create the name and age text with different font sizes
        Text nameText = new Text(patient.getNom() + " " + patient.getPrenom())
                .setFontSize(fc.titleSize) // Bold and configured size for the name
                .setBold();

        Text ageText = new Text(" (" + patient.getAge() + " ans)")
                .setFontSize(fontSize);  // Smaller font size for age

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
                        .setFontSize(fc.titleSize)
                        .setBold()
                        .setFontColor(new DeviceRgb(22, 160, 133)))
                    .add(new Text(displayText)
                        .setFontSize(fc.titleSize))
                    .setMarginLeft(10)
                    .setMarginBottom(5);
            document.add(imagerieParagraph);
            index++;
        }

        document.add(new Paragraph("\n").setFontSize(fc.titleSize));
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
        String uniqueTimeStamp = "_" + System.currentTimeMillis();
        String pdfPath = patientDir.getPath() + "/demande_analyse_" + dateStr.replace("/", "-") + uniqueTimeStamp + ".pdf";
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        configureA5Document(pdfDoc);
        Document document = new Document(pdfDoc, getExactA5PageSize());

        // Set top margin to 4 cm (113.39 points), bottom margin to 48.24f, left and right to 20
        document.setMargins(140f, 30, 50f, 30);

        FontConfig fc = loadFontConfig();
        float fontSize = fc.textSize - 7f < 8f ? 8f : fc.textSize - 7f;
        PdfFont textFont = resolveConfiguredFont(fc.textFamily);
        PdfFont titleFont = resolveConfiguredFont(fc.titleFamily);
        if (textFont != null) {
            document.setFont(textFont);
        }

        // Create a table with 2 columns for title and date
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        table.setWidth(PageSize.A5.getWidth() - 40); // Adjust table width to fit within margins

        // Title cell
        Cell titleCell = new Cell()
            .add(new Paragraph("Demande d'Analyse")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(fc.titleSize))
                .setBorder(Border.NO_BORDER);
        titleCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        if (titleFont != null) { titleCell.setFont(titleFont); }

        // Date cell
        Cell dateCell = new Cell()
            .add(new Paragraph(dateStr)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fc.titleSize))
                .setBorder(Border.NO_BORDER);
        dateCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        if (titleFont != null) { dateCell.setFont(titleFont); }

        table.addCell(titleCell);
        table.addCell(dateCell);

        document.add(table);

        // Add empty paragraph to create space between date and patient info (1 cm space)
        document.add(new Paragraph("\n").setFontSize(12f));

        // Create the name and age text with different font sizes
        Text nameText = new Text(patient.getNom() + " " + patient.getPrenom())
                .setFontSize(fc.titleSize) // Bold and configured size for the name
                .setBold();

        Text ageText = new Text(" (" + patient.getAge() + " ans)")
                .setFontSize(fontSize);  // Smaller font size for age

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
                        .setFontSize(fc.titleSize)
                        .setBold()
                        .setFontColor(new DeviceRgb(22, 160, 133)))
                    .add(new Text(displayText)
                        .setFontSize(fc.titleSize))
                    .setMarginLeft(10)
                    .setMarginBottom(5);
            document.add(analyseParagraph);
            index++;
        }

        document.add(new Paragraph("\n").setFontSize(fc.titleSize));
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
        configureA5Document(pdfDoc);
        Document document = new Document(pdfDoc, getExactA5PageSize());
        // Compact margins similar to generic certificat
        document.setMargins(90f, 24f, 30f, 24f);

        FontConfig fc = loadFontConfig();
        PdfFont textFont = resolveConfiguredFont(fc.textFamily);
        PdfFont titleFont = resolveConfiguredFont(fc.titleFamily);
        if (textFont != null) {
            document.setFont(textFont);
        }
        float fontSize = fc.textSize;

        // Titre "Certificat Médical" et date
        Paragraph title = new Paragraph("Certificat Médical")
            .setTextAlignment(TextAlignment.CENTER)
            .setBold()
            .setFontSize(fc.titleSize);
        if (titleFont != null) { title.setFont(titleFont); }

        Paragraph dateParagraph = new Paragraph("Date : " + dateStr)
                .setTextAlignment(TextAlignment.RIGHT)
            .setFontSize(fc.titleSize);
        if (titleFont != null) { dateParagraph.setFont(titleFont); }

        document.add(new Paragraph("").setMarginTop(6));
        document.add(title);
        document.add(dateParagraph);

        document.add(new Paragraph("")
            .setBorderBottom(new SolidBorder(new DeviceRgb(0, 0, 0), 1))
            .setMarginBottom(6));

        // Informations du patient
        Paragraph patientInfo = new Paragraph("Patient : " + patient.getNom() + " " + patient.getPrenom() + "\n"
            + "Âge : " + patient.getAge() + " ans")
                .setTextAlignment(TextAlignment.LEFT)
            .setFontSize(fc.textSize);
        document.add(patientInfo);

        document.add(new Paragraph("\n").setFontSize(fontSize));

        // Ajouter chaque élément du certificat (utilisé de manière générique)
        for (T element : listeElements) {
                Paragraph elementParagraph = new Paragraph()
                    .setFontSize(fc.textSize)
                    .add("\t" + element.toString())
                    .setMarginBottom(4);
            document.add(elementParagraph);
        }

            document.add(new Paragraph("").setMarginTop(8));

        // Signature et cachet du médecin
        Paragraph signature = new Paragraph("Signature du médecin : _____________________")
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
        configureA5Document(pdfDoc);
        Document document = new Document(pdfDoc, getExactA5PageSize());
        // Réduire les marges pour maximiser l'espace sur une seule page
        document.setMargins(80, 20, 30, 20);

        FontConfig fc = loadFontConfig();
        PdfFont textFont = resolveConfiguredFont(fc.textFamily);
        PdfFont titleFont = resolveConfiguredFont(fc.titleFamily);
        if (textFont != null) {
            document.setFont(textFont);
        }
        float fontSize = fc.textSize;

        // Titre "Facture" centré et date sur la même ligne (compact)
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.setBorder(Border.NO_BORDER);
        
        Cell titleCell = new Cell().add(new Paragraph("Facture")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
            .setFontSize(fc.titleSize))
                .setBorder(Border.NO_BORDER);
        if (titleFont != null) { titleCell.setFont(titleFont); }
        
        Cell dateCell = new Cell().add(new Paragraph("Date : " + dateStr)
                .setTextAlignment(TextAlignment.RIGHT)
            .setFontSize(fc.titleSize))
                .setBorder(Border.NO_BORDER);
        if (titleFont != null) { dateCell.setFont(titleFont); }
        
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
            .setFontSize(fc.titleSize)
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

        FontConfig fc = loadFontConfig();
        PdfFont formFont = resolveConfiguredFont(fc.formFamily);
        PdfFont titleFont = resolveConfiguredFont(fc.titleFamily);
        if (formFont != null) {
            document.setFont(formFont);
        }

        float labelSize = fc.formSize;
        float valueSize = fc.formSize;
        SimpleDateFormat dateHuman = new SimpleDateFormat("dd/MM/yyyy");

        // Header date
        document.add(new Paragraph("Date : " + dateHuman.format(new Date()))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(labelSize));

        // Title
        Paragraph ficheTitle = new Paragraph("FICHE DE SOINS LOCAUX")
            .setTextAlignment(TextAlignment.CENTER)
            .setBold()
            .setFontSize(fc.titleSize)
            .setMarginBottom(8);
        if (titleFont != null) { ficheTitle.setFont(titleFont); }
        document.add(ficheTitle);

        // Consultation date under title, aligned left
        if (consultation.getDateConsultation() != null) {
            Paragraph dateConsultPara = new Paragraph()
                .add(new Text("Date de consultation : ").setBold().setFontSize(labelSize).setFontColor(new DeviceRgb(0, 0, 255)))
                .add(new Text(consultation.getDateConsultation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setFontSize(labelSize));
            document.add(dateConsultPara.setTextAlignment(TextAlignment.LEFT).setMarginBottom(10));
        }

        // Patient info table 2 cols
        Table info = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
        info.addCell(makeLabelValueBlue("Nom :", patient.getNom() + " " + patient.getPrenom(), labelSize, valueSize));
        info.addCell(makeLabelValueBlue("CIN :", safe(""), labelSize, valueSize));
        info.addCell(makeLabelValueBlue("Date de naissance :", safeDate(patient.getDateNaissance(), dateHuman), labelSize, valueSize));
        info.addCell(makeLabelValueBlue("Couverture sanitaire :", safe(patient.getCouvertureSanitaire()), labelSize, valueSize));
        info.addCell(makeLabelValueBlue("Adresse :", safe(patient.getAdresse()), labelSize, valueSize));
        info.addCell(makeLabelValueBlue("Numéro de téléphone :", safe(patient.getTelephone()), labelSize, valueSize));
        info.addCell(makeLabelValueBlue("Profession :", safe(patient.getProfession()), labelSize, valueSize));
        info.addCell(makeLabelValue("", "", labelSize, valueSize));
        document.add(info.setMarginBottom(10));

        // Motif en plein format gauche
        document.add(makeLabelValueMultilineBlue("Motif de consultation :", safe(consultation.getSymptome()), labelSize, valueSize));
        document.add(new Paragraph(""));

        document.add(makeSectionWithBulletsBlue("ATCD :", formatAntecedents(antecedents), labelSize, valueSize));
        document.add(makeSectionWithBulletsBlue("Traitement en cours :", formatPrescriptions(traitementEnCours), labelSize, valueSize));

        // Exam section
        document.add(new Paragraph("Examen :").setBold().setFontSize(labelSize + 1).setFontColor(new DeviceRgb(0, 0, 255)).setMarginTop(12).setMarginBottom(6));
        Table exam = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1})).useAllAvailableWidth();
        exam.addCell(makeLabelValueRed("TA bras gauche :", safe(consultation.getPression()), labelSize, valueSize));
        exam.addCell(makeLabelValueRed("TA bras droit :", safe(consultation.getPressionDroite()), labelSize, valueSize));
        exam.addCell(makeLabelValueRed("FC :", formatInt(consultation.getFrequencequardiaque()), labelSize, valueSize));
        exam.addCell(makeLabelValueRed("SaO2 :", formatInt(consultation.getSaO()), labelSize, valueSize));
        exam.addCell(makeLabelValueRed("Glycémie :", formatDouble(consultation.getGlycimie()), labelSize, valueSize));
        exam.addCell(makeLabelValueRed("Poids :", formatDouble(consultation.getPoids()), labelSize, valueSize));
        exam.addCell(makeLabelValueRed("Température :", formatDouble(consultation.getTemperature()), labelSize, valueSize));
        exam.addCell(makeLabelValue("", "", labelSize, valueSize));
        document.add(exam.setMarginBottom(6));

        // Clinique en plein format gauche
        document.add(makeLabelValueMultilineRed("Clinique :", safe(consultation.getExamenClinique()), labelSize, valueSize));
        document.add(new Paragraph(""));

        document.add(makeSectionWithBulletsRed("ECG :", safe(consultation.getEcg()), labelSize, valueSize));
        document.add(makeSectionWithBulletsRed("ETT :", safe(consultation.getEtt()), labelSize, valueSize));
        document.add(makeSectionWithBulletsBlue("Traitement de sortie :", formatPrescriptions(traitementSortie), labelSize, valueSize));
        document.add(makeSectionWithBulletsBlue("Remarques :", "", labelSize, valueSize));
        document.add(makeSectionWithBulletsBlue("Prochains RDV :", "", labelSize, valueSize));

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

    private static Cell makeLabelValueRed(String label, String value, float labelSize, float valueSize) {
        Paragraph p = new Paragraph()
            .add(new Text(label).setBold().setFontSize(labelSize).setFontColor(new DeviceRgb(255, 0, 0)))
            .add(new Text(" " + (value != null ? value : ""))).setFontSize(valueSize);
        return new Cell().add(p).setBorder(Border.NO_BORDER);
    }

    private static Cell makeLabelValueBlue(String label, String value, float labelSize, float valueSize) {
        Paragraph p = new Paragraph()
            .add(new Text(label).setBold().setFontSize(labelSize).setFontColor(new DeviceRgb(0, 0, 255)))
            .add(new Text(" " + (value != null ? value : ""))).setFontSize(valueSize);
        return new Cell().add(p).setBorder(Border.NO_BORDER);
    }

    private static Cell makeLabelValueMultiline(String label, String value, float labelSize, float valueSize) {
        Paragraph p = new Paragraph()
            .add(new Text(label).setBold().setFontSize(labelSize))
            .add(new Text("\n" + (value != null ? value : "")).setFontSize(valueSize));
        return new Cell().add(p).setBorder(Border.NO_BORDER);
    }

    private static Cell makeLabelValueMultilineBlue(String label, String value, float labelSize, float valueSize) {
        Paragraph p = new Paragraph()
            .add(new Text(label).setBold().setFontSize(labelSize).setFontColor(new DeviceRgb(0, 0, 255)))
            .add(new Text("\n" + (value != null ? value : "")).setFontSize(valueSize));
        return new Cell().add(p).setBorder(Border.NO_BORDER);
    }

    private static Cell makeLabelValueMultilineRed(String label, String value, float labelSize, float valueSize) {
        Paragraph p = new Paragraph()
            .add(new Text(label).setBold().setFontSize(labelSize).setFontColor(new DeviceRgb(255, 0, 0)))
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

    private static Paragraph makeSectionWithBulletsRed(String label, String value, float labelSize, float valueSize) {
        Paragraph p = new Paragraph()
                .add(new Text(label).setBold().setFontSize(labelSize).setFontColor(new DeviceRgb(255, 0, 0)))
                .add(new Text("\n").setFontSize(valueSize));

        if (value != null && !value.isBlank()) {
            p.add(new Text(value).setFontSize(valueSize));
        }

        return p.setMarginBottom(8);
    }

    private static Paragraph makeSectionWithBulletsBlue(String label, String value, float labelSize, float valueSize) {
        Paragraph p = new Paragraph()
                .add(new Text(label).setBold().setFontSize(labelSize).setFontColor(new DeviceRgb(0, 0, 255)))
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
        String pdfPath = patientDir.getPath() + "/lettre_reference_" + dateStr + ".pdf";

        // Utiliser le format A4 pour la lettre de référence au lieu de A5
        return generateLettreOrientationA4(pdfPath, "Lettre de Référence", lettreText, patient, medecin);
    }

    private static String generateLettreOrientationA4(String pdfPath, String titre, String contenu, Patient patient, Medecin medecin) {
        try {
            System.out.println("DEBUG: Génération Lettre de Référence avec PageSize.A4 - Largeur: " + PageSize.A4.getWidth() + " Hauteur: " + PageSize.A4.getHeight());
            PdfWriter writer = new PdfWriter(pdfPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDoc, PageSize.A4);
            // Marges réduites pour une seule page A4
            document.setMargins(50f, 40f, 40f, 40f);
            FontConfig fc = loadFontConfig();
            PdfFont textFont = resolveConfiguredFont(fc.textFamily);
            PdfFont titleFont = resolveConfiguredFont(fc.titleFamily);
        if (textFont != null) {
            document.setFont(textFont);
        }
        float titleSize = fc.titleSize;
        float textSize = fc.textSize;

        // Titre du document centré (pas de header médecin)
        Paragraph titleParagraph = new Paragraph("Lettre de Référence")
                .setFont(titleFont != null ? titleFont : document.getPdfDocument().getDefaultFont())
                .setFontSize(titleSize + 2)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15f);
        document.add(titleParagraph);

        // Date du jour
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Paragraph dateParagraph = new Paragraph("Fait à Oujda, le " + sdf.format(new Date()))
                .setFont(textFont != null ? textFont : document.getPdfDocument().getDefaultFont())
                .setFontSize(textSize - 1)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(15f);
        document.add(dateParagraph);

        // Informations patient
        if (patient != null) {
            Paragraph patientInfo = new Paragraph()
                    .setFont(textFont != null ? textFont : document.getPdfDocument().getDefaultFont())
                    .setFontSize(textSize - 1)
                    .setMarginBottom(15f);
            
            patientInfo.add(new Text("Concernant : ").setBold());
            patientInfo.add(patient.getNom() + " " + patient.getPrenom() + "\n");
            
            if (patient.getDateNaissance() != null) {
                patientInfo.add(new Text("Né(e) le : ").setBold());
                patientInfo.add(patient.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            
            document.add(patientInfo);
        }

        // Contenu de la lettre (texte compact)
        String[] paragraphes = contenu.split("\n\n");
        for (String para : paragraphes) {
            if (para.trim().isEmpty()) continue;
            Paragraph p = new Paragraph(para.trim())
                    .setFont(textFont != null ? textFont : document.getPdfDocument().getDefaultFont())
                    .setFontSize(textSize - 1)
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setMarginBottom(8f)
                    .setFirstLineIndent(15f);
            document.add(p);
        }

        // Signature compacte
        document.add(new Paragraph("")
                .setMarginTop(20f));
        
        Paragraph signatureParagraph = new Paragraph()
                .setFont(textFont != null ? textFont : document.getPdfDocument().getDefaultFont())
                .setFontSize(textSize - 1)
                .setTextAlignment(TextAlignment.RIGHT);
        
        signatureParagraph.add(new Text("Signature et cachet\n\n").setBold());
        if (medecin != null) {
            signatureParagraph.add("Dr. " + medecin.getNom() + " " + medecin.getPrenom());
        }
        
        document.add(signatureParagraph);

        document.close();
        System.out.println("Lettre de référence A4 générée : " + pdfPath);
        return pdfPath;
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération de la lettre de référence: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Génère un PDF pour un compte rendu d'Échocardiographie Transthoracique (ETT)
     */
    public static String generateCompteRenduETTPdf(CompteRenduETT compteRendu, Medecin medecin) throws FileNotFoundException {
        Patient patient = compteRendu.getPatient();
        String outputDirectory = getOutputDirectory();
        String patientFolderName = patient.getNom() + "_" + patient.getPrenom();
        File patientDir = new File(outputDirectory + "/" + patientFolderName);
        if (!patientDir.exists()) {
            patientDir.mkdirs();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateStr = dateFormat.format(new Date());
        String pdfPath = patientDir.getPath() + "/compte_rendu_ett_" + dateStr + ".pdf";
        
        System.out.println("DEBUG: Génération compte rendu ETT - PDF path: " + pdfPath);
        
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(50f, 36f, 50f, 36f);
        
        FontConfig fc = loadFontConfig();
        PdfFont textFont = resolveConfiguredFont(fc.textFamily);
        PdfFont titleFont = resolveConfiguredFont(fc.titleFamily);
        if (textFont != null) {
            document.setFont(textFont);
        }
        float fontSize = fc.textSize;

        // En-tête laissé vide : le papier contient déjà un en-tête imprimé.
        // Conserver un espace vide équivalent à l'en-tête + séparation pour l'alignement
        Paragraph medecinHeaderPlaceholder = new Paragraph("")
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(25); // espace équivalent à l'en-tête (10) + séparation (15)
        document.add(medecinHeaderPlaceholder);
        
        // Remplacer la ligne horizontale par un espace vide conservant le même espacement
        document.add(new Paragraph("")
            .setMarginBottom(15));
        
        // Titre du document
        Paragraph title = new Paragraph("COMPTE RENDU")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(fc.titleSize + 2)
                .setUnderline()
                .setMarginTop(10)
                .setMarginBottom(5);
        if (titleFont != null) { 
            title.setFont(titleFont); 
        }
        document.add(title);
        
        Paragraph subtitle = new Paragraph("Échocardiographie Transthoracique (ETT)")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(fontSize + 2)
                .setBold()
                .setMarginBottom(15);
        if (titleFont != null) { 
            subtitle.setFont(titleFont); 
        }
        document.add(subtitle);
        
        // Ligne de séparation
        document.add(new Paragraph("")
            .setBorderBottom(new SolidBorder(new DeviceRgb(0, 0, 0), 1))
            .setMarginBottom(10));

        // Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateExamen = compteRendu.getDateCreation().format(formatter);
        Paragraph dateParagraph = new Paragraph("Date de l'examen : " + dateExamen)
                .setFontSize(fontSize)
                .setMarginBottom(15);
        document.add(dateParagraph);

        // Informations patient
        Paragraph patientTitle = new Paragraph("Informations du Patient")
                .setBold()
                .setFontSize(fontSize + 1)
                .setMarginBottom(5);
        if (titleFont != null) { 
            patientTitle.setFont(titleFont); 
        }
        document.add(patientTitle);
        
        String nameWithSex = patient.getNom() + " " + patient.getPrenom() + " (" + formatSexLabel(patient) + ")";

        String patientInfo = String.format(
            "Nom : %s\n" +
            "Date de naissance : %s\n" +
            "Âge : %s ans",
            nameWithSex,
            patient.getDateNaissance() != null ? patient.getDateNaissance().format(formatter) : "Non renseigné",
            patient.getAge()
        );
        
        Paragraph patientInfoPara = new Paragraph(patientInfo)
                .setFontSize(fontSize)
                .setMarginBottom(20);
        document.add(patientInfoPara);
        
        // Ligne de séparation
        document.add(new Paragraph("")
            .setBorderBottom(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f))
            .setMarginBottom(15));

        // Contenu du compte rendu
        Paragraph compteRenduTitle = new Paragraph("Compte Rendu de l'Examen")
                .setBold()
                .setFontSize(fontSize + 1)
                .setMarginBottom(10);
        if (titleFont != null) { 
            compteRenduTitle.setFont(titleFont); 
        }
        document.add(compteRenduTitle);
        
        Paragraph contenuParagraph = new Paragraph(compteRendu.getContenu())
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setFontSize(fontSize)
                .setMarginBottom(30);
        document.add(contenuParagraph);

        // Section Conclusion (si fournie)
        String conclusion = compteRendu.getConclusion();
        if (conclusion != null && !conclusion.trim().isEmpty()) {
            Paragraph conclusionTitle = new Paragraph("Conclusion")
                .setBold()
                .setFontSize(fontSize + 1)
                .setMarginBottom(8);
            if (titleFont != null) { conclusionTitle.setFont(titleFont); }
            document.add(conclusionTitle);

            Paragraph conclusionPara = new Paragraph(conclusion)
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setFontSize(fontSize)
                .setItalic()
                .setMarginBottom(30);
            document.add(conclusionPara);
        }

        // Signature
        Paragraph signature = new Paragraph("Signature et cachet du médecin")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize)
                .setMarginTop(30);
        document.add(signature);

        Paragraph medecinInfo = new Paragraph("Dr " + medecin.getNom() + " " + medecin.getPrenom())
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize)
                .setBold();
        if (titleFont != null) { 
            medecinInfo.setFont(titleFont); 
        }
        document.add(medecinInfo);
        
        Paragraph specialite = new Paragraph(medecin.getSpecialite() != null ? 
                medecin.getSpecialite().getNomSpecialite() : "")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize - 1)
                .setItalic();
        document.add(specialite);

        document.close();
        System.out.println("DEBUG: Compte rendu ETT généré avec succès: " + pdfPath);
        return pdfPath;
    }

    private static String generateGenericCertificat(String pdfPath, String titre, String contenu, Patient patient, Medecin medecin) throws FileNotFoundException {
        System.out.println("DEBUG: Génération PDF avec PageSize.A5 - Largeur: " + PageSize.A5.getWidth() + " Hauteur: " + PageSize.A5.getHeight());
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        configureA5Document(pdfDoc);
        Document document = new Document(pdfDoc, getExactA5PageSize());
        // Compact margins to keep everything on a single A5 page
        document.setMargins(90f, 24f, 30f, 24f);
        FontConfig fc = loadFontConfig();
        PdfFont textFont = resolveConfiguredFont(fc.textFamily);
        PdfFont titleFont = resolveConfiguredFont(fc.titleFamily);
        if (textFont != null) {
            document.setFont(textFont);
        }
        float fontSize = fc.textSize;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(new Date());

        // Titre
        Paragraph title = new Paragraph(titre)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
            .setFontSize(fc.titleSize)
                .setUnderline();
        if (titleFont != null) { title.setFont(titleFont); }

        // Minimal spacing before title
        document.add(new Paragraph("").setMarginTop(6));
        document.add(title);
        // Verify actual page size of the first page after content is added
        try {
            com.itextpdf.kernel.geom.PageSize defaultSize = pdfDoc.getDefaultPageSize();
            System.out.println("DEBUG: pdfDoc default page size = " + defaultSize.getWidth() + " x " + defaultSize.getHeight());
        } catch (Exception ignored) {}
        
        // Date
        Paragraph dateParagraph = new Paragraph("Date : " + dateStr)
                .setTextAlignment(TextAlignment.RIGHT)
            .setFontSize(fontSize);
        if (titleFont != null) { dateParagraph.setFont(titleFont); }
        document.add(dateParagraph);

        document.add(new Paragraph("")
            .setBorderBottom(new SolidBorder(new DeviceRgb(0, 0, 0), 1))
            .setMarginBottom(6));

        // Contenu du certificat
        Paragraph contenuParagraph = new Paragraph(contenu)
            .setTextAlignment(TextAlignment.LEFT)
            .setFontSize(fontSize)
            .setMarginTop(6)
            .setMarginBottom(6);
        document.add(contenuParagraph);
        // Compact spacing before signature
        document.add(new Paragraph("").setMarginTop(8));

        // Signature
        Paragraph signature = new Paragraph("Signature et cachet du médecin")
            .setTextAlignment(TextAlignment.RIGHT)
            .setFontSize(fontSize);
        document.add(signature);

        Paragraph medecinInfo = new Paragraph("Dr " + medecin.getNom() + " " + medecin.getPrenom())
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(fontSize)
                .setBold();
        document.add(medecinInfo);

        // Log first page size before closing
        try {
            if (pdfDoc.getNumberOfPages() > 0) {
                com.itextpdf.kernel.geom.Rectangle firstPageSize = pdfDoc.getFirstPage().getPageSize();
                System.out.println("DEBUG: First page size = " + firstPageSize.getWidth() + " x " + firstPageSize.getHeight());
            }
        } catch (Exception ignored) {}

        document.close();
        return pdfPath;
    }
}
