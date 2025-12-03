# Guide de Démarrage Rapide - AtlantaFX

## 🎨 Comment tester AtlantaFX dans votre application

### Option 1 : Modification rapide de App.java (Recommandé)

Ajoutez simplement ces deux lignes dans votre `App.java` existant :

```java
import atlantafx.base.theme.PrimerLight;

@Override
public void start(Stage stage) throws IOException {
    logger.info("Starting application...");
    
    // ✨ AJOUTEZ CETTE LIGNE AVANT DE CRÉER LA SCÈNE
    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    
    if (!DatabaseUtil.databaseExists()) {
        logger.info("Database does not exist. Initializing...");
        DatabaseInitializer.initializeDatabase();
    }
    // ... reste du code inchangé
}
```

### Option 2 : Utiliser App_AtlantaFX.java

Un fichier exemple complet a été créé : `App_AtlantaFX.java`

Pour l'utiliser :

1. **Sauvegardez votre App.java actuel :**
   ```bash
   mv src/main/java/com/azmicro/moms/App.java src/main/java/com/azmicro/moms/App_backup.java
   ```

2. **Renommez App_AtlantaFX.java :**
   ```bash
   mv src/main/java/com/azmicro/moms/App_AtlantaFX.java src/main/java/com/azmicro/moms/App.java
   ```

3. **Compilez et lancez :**
   ```bash
   mvn clean javafx:run
   ```

## 🎭 Thèmes disponibles

### Thèmes Clairs

```java
// Thème moderne et épuré (recommandé pour médical)
Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

// Thème nordique élégant
Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());

// Style macOS moderne
Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
```

### Thèmes Sombres

```java
// Primer dark
Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

// Nord dark
Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());

// Cupertino dark
Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());

// Dracula (violet/rose)
Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
```

## 🎨 Appliquer le CSS personnalisé AtlantaFX

Un fichier CSS personnalisé a été créé : `atlantafx-custom.css`

Pour l'utiliser dans vos vues, ajoutez-le après le chargement FXML :

```java
Scene scene = new Scene(loadFXML("view/login-view"), 640, 520);

// Ajouter le CSS personnalisé
scene.getStylesheets().add(
    getClass().getResource("/com/azmicro/moms/css/atlantafx-custom.css").toExternalForm()
);

stage.setScene(scene);
```

## 📝 Utiliser les classes CSS d'AtlantaFX dans vos FXML

### Boutons avec styles

```xml
<!-- Bouton standard -->
<Button text="Sauvegarder" styleClass="button"/>

<!-- Bouton de succès (vert) -->
<Button text="Valider" styleClass="button, success"/>

<!-- Bouton de danger (rouge) -->
<Button text="Supprimer" styleClass="button, danger"/>

<!-- Bouton d'avertissement (orange) -->
<Button text="Attention" styleClass="button, warning"/>
```

### Cartes (Cards)

```xml
<VBox styleClass="card">
    <Label text="Titre de la carte" styleClass="title"/>
    <Label text="Contenu de la carte"/>
</VBox>
```

### Alertes

```xml
<!-- Info -->
<HBox styleClass="alert-info">
    <Label text="Information importante"/>
</HBox>

<!-- Succès -->
<HBox styleClass="alert-success">
    <Label text="Opération réussie"/>
</HBox>

<!-- Avertissement -->
<HBox styleClass="alert-warning">
    <Label text="Attention requise"/>
</HBox>

<!-- Danger -->
<HBox styleClass="alert-danger">
    <Label text="Erreur détectée"/>
</HBox>
```

## 🔧 Personnaliser les couleurs

Dans votre fichier CSS, vous pouvez redéfinir les variables AtlantaFX :

```css
.root {
    /* Couleur d'accent personnalisée (bleu médical) */
    -color-accent-emphasis: #0066CC;
    
    /* Couleur de succès personnalisée */
    -color-success-emphasis: #28A745;
    
    /* Couleur de danger personnalisée */
    -color-danger-emphasis: #DC3545;
}
```

## 📋 Exemple complet : Dashboard Tile

```xml
<VBox styleClass="dashboard-tile" spacing="10" alignment="CENTER" onMouseClicked="#handleTileClick">
    <Label text="125" styleClass="stat-value"/>
    <Label text="Patients aujourd'hui" styleClass="stat-label"/>
</VBox>
```

Avec le CSS correspondant déjà dans `atlantafx-custom.css` :

```css
.dashboard-tile {
    -fx-background-color: -color-bg-default;
    -fx-border-color: -color-border-default;
    -fx-border-radius: 8;
    -fx-background-radius: 8;
    -fx-padding: 20;
    -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 8, 0, 0, 2);
}

.dashboard-tile:hover {
    -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 12, 0, 0, 4);
    -fx-cursor: hand;
}
```

## 🚀 Tester l'application

```bash
# Compiler
mvn clean compile

# Lancer l'application
mvn javafx:run

# Créer un JAR
mvn clean package
```

## 📚 Documentation complète

Consultez `MIGRATION_JAVAFX21.md` pour tous les détails de la migration.

### Ressources AtlantaFX

- **Site officiel :** https://mkpaz.github.io/atlantafx/
- **Sampler (exemples) :** https://github.com/mkpaz/atlantafx
- **Composants disponibles :** https://mkpaz.github.io/atlantafx/sampler/

## 💡 Conseils

1. **Commencez simple :** Appliquez juste le thème, testez
2. **Utilisez le Sampler :** Téléchargez-le pour voir tous les composants
3. **Variables CSS :** Profitez des variables CSS pour une cohérence visuelle
4. **Mode sombre :** Testez votre app en mode sombre pour l'accessibilité

## ⚡ Changement de thème dynamique

Pour permettre à l'utilisateur de changer de thème :

```java
public void switchTheme(boolean isDark) {
    if (isDark) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    } else {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }
}
```

Vous pouvez lier cela à un Toggle Switch dans vos paramètres !

---

**Bon développement ! 🎨**
