# Migration vers JavaFX 21 LTS et AtlantaFX

## 📋 Résumé des changements

Ce document décrit la migration du projet CabinetMedical de JavaFX 13 vers JavaFX 21 LTS, et le remplacement de JFoenix par AtlantaFX.

## ✅ Changements effectués

### 1. **Mise à jour du POM.xml**

#### Versions JavaFX
- **Avant :** JavaFX 13 (versions mixtes 13/22-ea)
- **Après :** JavaFX 21.0.5 (LTS)

```xml
<properties>
    <javafx.version>21.0.5</javafx.version>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>
```

#### Dépendances mises à jour

| Bibliothèque | Ancienne version | Nouvelle version | Notes |
|--------------|------------------|------------------|-------|
| JavaFX Controls | 13 | 21.0.5 | LTS |
| JavaFX FXML | 13 | 21.0.5 | LTS |
| JavaFX Web | 22-ea+11 | 21.0.5 | LTS |
| JavaFX Swing | - | 21.0.5 | Ajouté |
| ControlsFX | 11.1.2 | 11.2.1 | Compatible JavaFX 21 |
| ValidatorFX | 0.4.0 | 0.5.0 | Dernière version |
| TilesFX | 11.48 | 21.0.9 | Compatible JavaFX 21 |
| FXGL | 17.3 | 21.1 | Compatible JavaFX 21 |
| FontAwesomeFX | 8.2 | 8.9 | Dernière version |
| Log4j2 | 2.20.0 | 2.23.1 | Sécurité |
| JFXtras Controls | 8.0-r6 | 17-r1 | Compatible JavaFX 17+ |
| **JFoenix** | ~~9.0.10~~ | **SUPPRIMÉ** | ❌ Plus maintenu |
| **AtlantaFX** | - | **2.0.1** | ✅ Remplace JFoenix |

#### Plugins Maven
```xml
<!-- Compiler mis à jour -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
    <configuration>
        <release>21</release>
    </configuration>
</plugin>

<!-- JavaFX Maven Plugin -->
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
</plugin>
```

### 2. **Mise à jour du module-info.java**

```java
// AVANT
requires com.jfoenix;

// APRÈS
requires atlantafx.base;
```

### 3. **Remplacement des composants JFoenix**

#### JFXButton → Button standard
**Fichier :** `ListeAttenteController.java`

```java
// AVANT
import com.jfoenix.controls.JFXButton;
private final JFXButton btn = new JFXButton();

// APRÈS
import javafx.scene.control.Button;
private final Button btn = new Button();
btn.getStyleClass().add("button");
```

#### JFXHamburger → Menu hamburger personnalisé
**Fichier :** `dashboardMedecin-view.fxml`

```xml
<!-- AVANT -->
<?import com.jfoenix.controls.JFXHamburger?>
<JFXHamburger />

<!-- APRÈS -->
<?import javafx.scene.shape.Line?>
<Button fx:id="btnHamburger" styleClass="hamburger-button">
    <graphic>
        <VBox alignment="CENTER" spacing="3">
            <Line endX="20" startX="0" strokeWidth="2"/>
            <Line endX="20" startX="0" strokeWidth="2"/>
            <Line endX="20" startX="0" strokeWidth="2"/>
        </VBox>
    </graphic>
</Button>
```

### 4. **Correction de l'API FontAwesomeFX**

L'API a changé entre les versions 8.2 et 8.9.

#### Modification des fichiers
- `LignePrescription.java`
- `DashboardAssistanteController.java`
- `PatientController.java`

```java
// AVANT (version 8.2)
FontAwesomeIcon icon = new FontAwesomeIcon();
icon.setGlyphName("TRASH");
icon.setSize("1.5em");
icon.setFill(Color.RED);

// APRÈS (version 8.9)
de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView icon = 
    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TRASH
    );
icon.setSize("1.5em");
icon.setFill(Color.RED);
```

## 🎨 Utilisation d'AtlantaFX

AtlantaFX est une bibliothèque de thèmes moderne pour JavaFX, activement maintenue.

### Application d'un thème

Dans votre classe principale `App.java`, ajoutez :

```java
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.PrimerDark;

@Override
public void start(Stage stage) throws IOException {
    // Appliquer le thème AtlantaFX
    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    // Ou pour le thème sombre : new PrimerDark()
    
    // Votre code existant...
    scene = new Scene(loadFXML("view/login-view"), 640, 520);
    stage.setScene(scene);
    stage.show();
}
```

### Thèmes disponibles

AtlantaFX propose plusieurs thèmes :

```java
// Thèmes clairs
new PrimerLight()
new NordLight()
new CupertinoLight()

// Thèmes sombres
new PrimerDark()
new NordDark()
new CupertinoDark()
new Dracula()
```

### Composants AtlantaFX

AtlantaFX fournit des composants améliorés :

```java
import atlantafx.base.controls.*;

// Cards
Card card = new Card();

// ToggleSwitch (remplace JFXToggleButton)
ToggleSwitch toggle = new ToggleSwitch();

// Message (remplace JFXSnackbar)
Message message = new Message();

// PasswordTextField
PasswordTextField password = new PasswordTextField();
```

### Pseudo-classes CSS

AtlantaFX utilise des pseudo-classes CSS modernes :

```css
.button {
    -fx-background-color: -color-accent-emphasis;
}

.button:hover {
    -fx-background-color: -color-accent-muted;
}

/* Classes de style supplémentaires */
.button.accent { }
.button.success { }
.button.danger { }
```

## 🔧 Compilation et exécution

### Compiler le projet
```bash
mvn clean compile
```

### Exécuter le projet
```bash
mvn javafx:run
```

### Créer un JAR exécutable
```bash
mvn clean package
```

## ⚠️ Avertissements résolus

Les avertissements suivants sont normaux et ne posent pas de problème :

```
[WARNING] * Required filename-based automodules detected: 
  [mysql-connector-j-8.0.32.jar, fontawesomefx-8.9.jar, 
   kernel-8.0.5.jar, layout-8.0.5.jar]
```

Ces bibliothèques ne sont pas encore modulaires (Java 9+). C'est attendu et fonctionnel.

## 📚 Ressources

- **JavaFX 21 Documentation :** https://openjfx.io/
- **AtlantaFX Documentation :** https://mkpaz.github.io/atlantafx/
- **AtlantaFX Sampler (démos) :** https://github.com/mkpaz/atlantafx
- **ControlsFX :** https://controlsfx.github.io/
- **FontAwesomeFX :** https://bitbucket.org/Jerady/fontawesomefx

## 🚀 Prochaines étapes recommandées

1. **Appliquer AtlantaFX :** Ajouter les thèmes dans `App.java`
2. **Réviser les CSS :** Adapter les fichiers CSS pour profiter des variables AtlantaFX
3. **Tests :** Tester toutes les fonctionnalités de l'application
4. **Sécurité :** Implémenter le hashing des mots de passe (BCrypt)
5. **Tests unitaires :** Ajouter des tests avec JUnit 5
6. **Documentation :** Documenter les API publiques avec Javadoc

## 📝 Notes importantes

- ✅ **Compilation réussie** avec JavaFX 21
- ✅ **Toutes les dépendances** sont compatibles
- ✅ **JFoenix complètement supprimé** du projet
- ✅ **API FontAwesome** mise à jour
- ⚠️ **AtlantaFX disponible** mais non encore appliqué (optionnel)

---

**Date de migration :** 3 décembre 2025  
**JavaFX version :** 21.0.5 (LTS)  
**Java version :** 21
