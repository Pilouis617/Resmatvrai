## Organisation du code

Le code Java est réparti par responsabilités : `model` (entités JPA), `repository` (accès aux données), `service` (règles métier), `controller` (routes HTTP), `dto` (formulaires), `security` (authentification) et `config` (initialisation). Les fichiers `.hbs` sont des templates Mustache, et le CSS se trouve dans `static/css`.

Les classes ont été remises en forme pour faciliter la lecture dans Eclipse. Les noms des tables et les champs JPA existants ont été conservés pour protéger la compatibilité avec la base actuelle. La version n’a pas été validée par un démarrage complet avec Docker dans cet environnement.

# ResMat — projet Spring Boot / BTS SIO SLAM

Projet Maven Java 21 avec Spring Boot, Mustache `.hbs`, MariaDB, JPA, BCrypt, Spring Security et protection CSRF.

## Installation dans Eclipse
1. Installer Java 21, Docker Desktop et Eclipse (support Maven).
2. Dans ce dossier : `docker compose up -d`.
3. Eclipse → **File → Import → Maven → Existing Maven Projects** → sélectionner ce dossier.
4. Attendre le téléchargement Maven puis exécuter `ResMatApplication.java` avec **Run As → Java Application**.
5. Ouvrir http://localhost:8080

Comptes créés **uniquement si la table Utilisateur est vide** : `etudiant` / `Demo123!` et `gestionnaire` / `Demo123!`. Remplacer les mots de passe pour tout usage hors démonstration.

## Architecture
- `model/` : `Categorie`, `TypeMateriel`, `Materiel`, `Utilisateur`, `Reservation`, `Emprunt`.
- `repository/` : un repository JPA par entité.
- `dto/` : validation des formulaires.
- `service/` : gestion des types, demandes et règles de conflits.
- `controller/` : pages serveur avec Mustache.
- `security/` : authentification, rôles USER et GESTIONNAIRE, CSRF.

## Important
Cette livraison est une **base pédagogique fonctionnelle à compléter**, pas un projet entièrement validé. Le script `resmat-data-original.sql` est fourni tel quel, **ne pas l'exécuter automatiquement** : il commence par des `TRUNCATE` (effacement de tables). Il dépend aussi du schéma exact du professeur et de la prise en charge des UUID par MariaDB. Les fichiers `.loo` originaux sont conservés, mais pas interprétés ici.

À finir et vérifier : masquage de l’action de remise une fois la remise réalisée ; sélection de catégorie correcte lors de modification de type ; consultation et filtres par catégorie/disponibilité ; tests de réservation concurrente et gestion de conflits en transaction ; formulaires avec conservation des saisies ; documentation de migration SQL ; création/modification/désactivation des matériels physiques ; sécurisation et validation métier exhaustive. Les identifiants restent configurables par variables d'environnement.

## Sécurité
Les URL `/gestion/**` sont réservées à `ROLE_GESTIONNAIRE`, les autres à un utilisateur connecté. Les POST utilisent le jeton CSRF ; ne jamais désactiver CSRF pour contourner un problème.

## Correctif affichage CSRF / erreur 500
Ce correctif ajoute `CsrfModelAdvice.java` et expose `csrfToken` et `csrfParameterName` à tous les templates `.hbs`, sans désactiver la protection CSRF. La page `/login?error` affiche aussi le message de connexion incorrecte.
