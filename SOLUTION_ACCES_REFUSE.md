# Solution au Problème "Accès Refusé" lors de la Configuration

## Problème Résolu

Le problème **"Impossible de sauvegarder la configuration database.properties - Accès refusé"** a été corrigé.

## Cause du Problème

Lorsque l'application est installée dans `C:\Program Files\`, Windows empêche les modifications de fichiers sans privilèges administrateur pour des raisons de sécurité.

## Solution Implémentée

Le fichier de configuration est maintenant sauvegardé dans le dossier utilisateur :

```
C:\Users\[VotreNom]\AppData\Roaming\CabinetMedical\database.properties
```

### Avantages

✅ **Pas de problème de permissions** - Chaque utilisateur peut modifier sa configuration
✅ **Configuration par utilisateur** - Plusieurs utilisateurs peuvent avoir des configurations différentes sur le même PC
✅ **Sécurité** - Le mot de passe de la base de données est protégé dans le profil utilisateur
✅ **Pas besoin d'administrateur** - L'application fonctionne sans élévation de privilèges

## Comment Accéder au Fichier de Configuration

### Méthode 1 : Via l'Explorateur Windows
1. Appuyez sur `Windows + R`
2. Tapez : `%APPDATA%\CabinetMedical`
3. Appuyez sur Entrée
4. Vous verrez le fichier `database.properties`

### Méthode 2 : Via l'Application
1. Ouvrez l'application
2. Allez dans **Paramètres** → **Général**
3. Cliquez sur **"⚙ Configurer la Base de Données"**
4. Le chemin complet du fichier est affiché après la sauvegarde

## Migration depuis l'Ancienne Version

Si vous aviez une ancienne version avec le fichier dans le dossier d'installation :

1. Ouvrez l'ancien emplacement (dossier d'installation)
2. Copiez le fichier `database.properties`
3. Naviguez vers `%APPDATA%\CabinetMedical`
4. Collez le fichier
5. Redémarrez l'application

## Ordre de Chargement de la Configuration

L'application charge la configuration dans cet ordre :

1. **AppData** (`C:\Users\[VotreNom]\AppData\Roaming\CabinetMedical\database.properties`) - **Priorité 1**
2. **Répertoire courant** (`database.properties`) - **Priorité 2**
3. **Ressources intégrées** (configuration par défaut) - **Priorité 3**

## Pour les Administrateurs

Si vous souhaitez déployer une configuration par défaut :

1. Créez le dossier : `C:\Users\Public\Documents\CabinetMedical\`
2. Placez-y un fichier `database.properties.template`
3. Les utilisateurs pourront le copier dans leur `%APPDATA%\CabinetMedical\`

## Besoin d'Aide ?

Si vous rencontrez toujours des problèmes :

1. Vérifiez que le dossier `%APPDATA%\CabinetMedical` existe
2. Vérifiez que vous avez les droits d'écriture (normalement oui dans AppData)
3. Vérifiez que votre antivirus ne bloque pas l'application
4. Exécutez l'application et consultez les logs pour voir le chemin exact utilisé
