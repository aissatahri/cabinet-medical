# Configuration de la Base de Données

## Vue d'ensemble

L'application **CabinetMedical** permet désormais de configurer facilement les paramètres de connexion à la base de données MySQL sans modifier le code source. Cette fonctionnalité est particulièrement utile lorsque :

- L'adresse IP du serveur change
- Vous passez d'une connexion locale à une connexion distante
- Vous souhaitez utiliser différents serveurs MySQL

## Fichiers de Configuration

### 1. Fichier `database.properties`

Le fichier de configuration est automatiquement créé dans le dossier utilisateur pour éviter les problèmes de permissions :

- **Emplacement Windows** : `C:\Users\[VotreNom]\AppData\Roaming\CabinetMedical\database.properties`
- **Dans les ressources** : `src/main/resources/database.properties` (modèle par défaut)

#### Paramètres disponibles :

```properties
# Configuration de la base de données
db.host=localhost          # Adresse IP ou nom d'hôte du serveur MySQL
db.port=3306              # Port du serveur MySQL (3306 par défaut)
db.name=CabinetMedical    # Nom de la base de données
db.username=root          # Nom d'utilisateur MySQL
db.password=              # Mot de passe MySQL
```

## Utilisation de l'Interface Graphique

### Méthode 1 : Depuis l'écran de connexion

1. **Lancez l'application**
2. Si la connexion à la base de données échoue, une boîte de dialogue apparaîtra
3. Cliquez sur le bouton **"⚙ Configurer"**
4. La fenêtre de configuration s'ouvrira automatiquement

### Méthode 2 : Depuis les Paramètres

1. **Connectez-vous à l'application**
2. Accédez au menu **Paramètres**
3. Dans l'onglet **"Général"**, cliquez sur **"⚙ Configurer la Base de Données"**

### Fenêtre de Configuration

La fenêtre de configuration vous permet de :

#### Champs à remplir :
- **Adresse IP / Hôte** : 
  - `localhost` pour une connexion locale
  - Adresse IP du serveur (ex: `192.168.1.100`) pour une connexion distante
  
- **Port** : 
  - `3306` (port MySQL par défaut)
  - Modifiez si votre serveur utilise un port différent
  
- **Nom de la base** : 
  - `CabinetMedical` (par défaut)
  - La base sera créée automatiquement si elle n'existe pas
  
- **Nom d'utilisateur** : 
  - Votre nom d'utilisateur MySQL (ex: `root`)
  
- **Mot de passe** : 
  - Votre mot de passe MySQL

#### Actions disponibles :
- **🔌 Tester la connexion** : Vérifie que les paramètres sont corrects
- **💾 Sauvegarder** : Enregistre les paramètres
- **❌ Annuler** : Ferme la fenêtre sans sauvegarder

## Exemples de Configuration

### Configuration Locale (XAMPP, WAMP, MAMP)

```properties
db.host=localhost
db.port=3306
db.name=CabinetMedical
db.username=root
db.password=
```

### Configuration Serveur Distant

```properties
db.host=192.168.1.100
db.port=3306
db.name=CabinetMedical
db.username=admin
db.password=votre_mot_de_passe
```

### Configuration Serveur Cloud (ex: Infomaniak)

```properties
db.host=pcdb-mau7krg.c02.dbaas.infomaniak.cloud
db.port=24158
db.name=Cabinetmedical
db.username=admin
db.password=votre_mot_de_passe_sécurisé
```

## Configuration Manuelle

Si vous préférez modifier le fichier directement :

1. Ouvrez l'Explorateur Windows et tapez dans la barre d'adresse : `%APPDATA%\CabinetMedical`
2. Ouvrez le fichier `database.properties` avec le Bloc-notes
3. Modifiez les valeurs selon vos besoins
4. Sauvegardez le fichier
5. Redémarrez l'application

**Note** : Le fichier est stocké dans le dossier AppData de l'utilisateur pour éviter les problèmes de permissions Windows.

## Dépannage

### Problème : "Échec de la connexion à la base de données"

**Solutions :**
1. Vérifiez que MySQL est démarré
2. Vérifiez l'adresse IP et le port
3. Vérifiez le nom d'utilisateur et le mot de passe
4. Vérifiez que le pare-feu autorise la connexion
5. Pour une connexion distante, vérifiez que MySQL autorise les connexions externes

### Problème : "Impossible de sauvegarder la configuration"

**Solutions :**
1. Vérifiez que vous avez les droits en écriture dans le répertoire
2. Fermez tous les éditeurs de texte qui pourraient avoir ouvert le fichier
3. Essayez de lancer l'application en tant qu'administrateur

### Vérifier les Permissions MySQL pour Connexion Distante

Si vous vous connectez à un serveur distant :

```sql
-- Depuis MySQL, autorisez les connexions distantes :
GRANT ALL PRIVILEGES ON CabinetMedical.* TO 'admin'@'%' IDENTIFIED BY 'votre_mot_de_passe';
FLUSH PRIVILEGES;
```

## Sécurité

⚠️ **Important** :
- Ne partagez jamais votre fichier `database.properties` contenant des mots de passe
- Utilisez des mots de passe forts pour MySQL
- Pour les déploiements en production, utilisez des utilisateurs MySQL avec des privilèges limités

## Support Technique

Pour toute question ou problème :
- Consultez la documentation MySQL
- Vérifiez les logs de l'application
- Contactez votre administrateur système
