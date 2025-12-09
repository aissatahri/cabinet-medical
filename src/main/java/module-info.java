module com.azmicro.moms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.prefs;
    
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires atlantafx.base;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.web;
    requires org.apache.logging.log4j;
    requires java.desktop; // Nécessaire pour utiliser Desktop.getDesktop().open(
    requires mysql.connector.j;
    requires javafx.swing;
    requires kernel;
    requires layout;
    requires jfxtras.common;
    requires jfxtras.controls;
    
    
    
    
// Open specific packages for reflection as needed
    opens com.azmicro.moms to javafx.fxml;
    opens com.azmicro.moms.controller to javafx.fxml;
    opens com.azmicro.moms.controller.assistante to javafx.fxml;
    opens com.azmicro.moms.controller.medecin to javafx.fxml;
    opens com.azmicro.moms.controller.bilan to javafx.fxml;
    opens com.azmicro.moms.controller.imagerie to javafx.fxml;
    opens com.azmicro.moms.controller.patient to javafx.fxml;
    opens com.azmicro.moms.controller.prescription to javafx.fxml;
    opens com.azmicro.moms.controller.acte to javafx.fxml;
    opens com.azmicro.moms.model to javafx.base;
    
    // Export packages to be used by other modules
    exports com.azmicro.moms;
    exports com.azmicro.moms.controller;
    exports com.azmicro.moms.controller.assistante;
    exports com.azmicro.moms.controller.medecin;
    exports com.azmicro.moms.controller.bilan;
    exports com.azmicro.moms.controller.imagerie;
    exports com.azmicro.moms.controller.patient;
    exports com.azmicro.moms.controller.prescription;
    exports com.azmicro.moms.controller.acte;
    exports com.azmicro.moms.util;
    exports com.azmicro.moms.dao;
    exports com.azmicro.moms.service;
    exports com.azmicro.moms.model;
    
    
}
