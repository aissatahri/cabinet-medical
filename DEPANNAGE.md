# Guide de Dépannage - L'Application Ne Démarre Pas

## 🔴 Problème : L'application ne démarre pas après configuration

### Solution Rapide

#### Étape 1 : Utiliser la Version Debug
1. Utilisez `CabinetMedical-Universal-Debug.exe` (version avec console)
2. Lisez les messages d'erreur qui s'affichent
3. Notez les erreurs pour les diagnostiquer

#### Étape 2 : Vérifier le Fichier de Configuration
1. Appuyez sur `Windows + R`
2. Tapez : `%APPDATA%\CabinetMedical`
3. Ouvrez `database.properties`
4. Vérifiez qu'il contient :
```properties
db.host=192.168.1.X (ou localhost)
db.port=3306
db.name=CabinetMedicalbis
db.username=root
db.password=votre_mot_de_passe
```

#### Étape 3 : Vérifier que les valeurs sont correctes
- **Pas d'espaces** avant ou après les valeurs
- **Pas de guillemets** autour des valeurs
- **Port en chiffres** (3306)
- **Nom de base correct** (CabinetMedicalbis)

### Exemple CORRECT :
```properties
db.host=localhost
db.port=3306
db.name=CabinetMedicalbis
db.username=root
db.password=
```

### Exemple INCORRECT ❌ :
```properties
db.host = "localhost"    # Pas de guillemets ni espaces
db.port = 3306           # Pas d'espaces
db.name=                 # Nom vide
```

## 🔧 Solutions Détaillées

### Solution 1 : Réinitialiser la Configuration

1. Fermez l'application
2. Supprimez le fichier : `%APPDATA%\CabinetMedical\database.properties`
3. Relancez l'application
4. Reconfigurez depuis zéro

### Solution 2 : Vérifier Java

1. Ouvrez une invite de commandes
2. Tapez : `java -version`
3. Vous devez voir Java 21 ou supérieur
4. Si non, installez Java 21 : https://adoptium.net/

### Solution 3 : Vérifier la Connexion MySQL

1. Assurez-vous que MySQL est démarré sur le serveur
2. Testez la connexion avec :
```batch
ping 192.168.1.X
```
3. Vérifiez que le port 3306 est ouvert
4. Vérifiez les identifiants MySQL

### Solution 4 : Utiliser le Script de Diagnostic

1. Dans le dossier d'installation, double-cliquez sur `diagnostic.bat`
2. Lisez les résultats
3. Corrigez les erreurs affichées

## 📝 Messages d'Erreur Courants

### "Impossible de se connecter à la base de données"
**Cause** : Mauvaise configuration réseau ou serveur MySQL arrêté
**Solution** :
- Vérifiez que MySQL est démarré
- Vérifiez l'adresse IP dans database.properties
- Testez : `ping IP_DU_SERVEUR`

### "Access denied for user"
**Cause** : Mauvais nom d'utilisateur ou mot de passe
**Solution** :
- Vérifiez le nom d'utilisateur dans database.properties
- Vérifiez le mot de passe
- Assurez-vous que l'utilisateur a les droits sur la base

### "Unknown database 'CabinetMedicalbis'"
**Cause** : La base de données n'existe pas
**Solution** :
- L'application devrait la créer automatiquement
- Vérifiez que l'utilisateur MySQL a les droits CREATE DATABASE
- Ou créez la base manuellement dans MySQL

### L'application se ferme immédiatement
**Cause** : Erreur de configuration ou erreur Java
**Solution** :
- Utilisez la version Debug pour voir les erreurs
- Vérifiez les logs dans la console
- Vérifiez que Java 21+ est installé

## 🆘 Besoin d'Aide Supplémentaire ?

### Générer un Rapport de Diagnostic

1. Exécutez `diagnostic.bat`
2. Le fichier `app-diagnostic.log` sera créé
3. Envoyez ce fichier au support avec :
   - Version de Windows
   - Version de Java
   - Message d'erreur exact
   - Contenu de database.properties (sans le mot de passe)

### Logs de l'Application

Les logs sont affichés dans :
- La console (version Debug)
- Le fichier log4j (si configuré)

### Contacter le Support

Fournissez :
1. Le fichier `app-diagnostic.log`
2. Capture d'écran de l'erreur
3. Contenu de `%APPDATA%\CabinetMedical\database.properties`
4. Résultat de `java -version`

## 🎯 Checklist Finale

Avant de contacter le support, vérifiez :

- [ ] Java 21 ou supérieur est installé
- [ ] MySQL est démarré sur le serveur
- [ ] Le fichier database.properties existe dans `%APPDATA%\CabinetMedical`
- [ ] Les valeurs dans database.properties sont correctes (pas d'espaces, pas de guillemets)
- [ ] Le serveur MySQL est accessible (test ping)
- [ ] L'utilisateur MySQL a les bons droits
- [ ] Le pare-feu autorise la connexion au port 3306
- [ ] Vous avez testé avec la version Debug
- [ ] Vous avez lu les logs dans la console
