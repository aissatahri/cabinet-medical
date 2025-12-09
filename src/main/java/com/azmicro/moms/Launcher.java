package com.azmicro.moms;

/**
 * Classe de lancement pour le JAR exécutable.
 * Cette classe ne doit PAS étendre Application pour éviter les problèmes
 * de JavaFX avec les modules lors de l'exécution en tant que JAR.
 */
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}
