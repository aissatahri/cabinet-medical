package com.azmicro.moms.util;

import com.azmicro.moms.model.Consultations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * Utilitaire pour configurer les ListView
 */
public class ListViewConfigurator {

    /**
     * Configure et initialise une ListView avec des consultations
     * Affiche les dates de consultation et sélectionne automatiquement la dernière
     */
    public static void configureConsultationsListView(ListView<Consultations> listView, 
                                                      List<Consultations> consultations) {
        // Créer une liste observable
        ObservableList<Consultations> consultationsList = FXCollections.observableArrayList(consultations);

        // Configurer le cell factory pour afficher les dates
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Consultations item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDateConsultation().toString());
                }
            }
        });

        // Initialiser avec les consultations
        listView.setItems(consultationsList);

        // Sélectionner automatiquement le dernier élément
        if (!consultationsList.isEmpty()) {
            listView.getSelectionModel().selectLast();
            listView.scrollTo(consultationsList.size() - 1);
        }
    }

    /**
     * Sélectionne automatiquement le dernier élément d'une ListView
     */
    public static <T> void selectLastItem(ListView<T> listView) {
        ObservableList<T> items = listView.getItems();
        if (!items.isEmpty()) {
            listView.getSelectionModel().selectLast();
            listView.scrollTo(items.size() - 1);
        }
    }
}
