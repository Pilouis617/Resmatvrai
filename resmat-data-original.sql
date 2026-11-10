USE resmat;

-- =====================================================
-- Mot de passe : hash BCrypt (les $ sont encodés avec CHAR(36))
-- =====================================================
SET @pwd = CONCAT(CHAR(36), '2a', CHAR(36), '12', CHAR(36),
                  '/VLsdQ4t2Ple.UP9Hc79Je2hRASvm71qlQg617VxViTGGjPDlDB9O');

-- =====================================================
-- Nettoyage (supprime ce bloc pour conserver tes données)
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Emprunt;
TRUNCATE TABLE Reservation;
TRUNCATE TABLE Materiel;
TRUNCATE TABLE TypeMateriel;
TRUNCATE TABLE Categorie;
TRUNCATE TABLE Utilisateur;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- Table de nombres 1..500 (temporaire)
-- =====================================================
DROP TEMPORARY TABLE IF EXISTS seq;
CREATE TEMPORARY TABLE seq (n INT PRIMARY KEY);
INSERT INTO seq (n)
SELECT a.i + b.i * 10 + c.i * 100 + 1
FROM (SELECT 0 i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4
      UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a,
     (SELECT 0 i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4
      UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b,
     (SELECT 0 i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) c;

-- =====================================================
-- CATEGORIES (8)
-- =====================================================
INSERT INTO Categorie (id, nom) VALUES
(CAST('ca000000-0000-0000-0000-000000000001' AS UUID), 'Informatique'),
(CAST('ca000000-0000-0000-0000-000000000002' AS UUID), 'Audiovisuel'),
(CAST('ca000000-0000-0000-0000-000000000003' AS UUID), 'Photographie'),
(CAST('ca000000-0000-0000-0000-000000000004' AS UUID), 'Électronique'),
(CAST('ca000000-0000-0000-0000-000000000005' AS UUID), 'Mesure et laboratoire'),
(CAST('ca000000-0000-0000-0000-000000000006' AS UUID), 'Réseau'),
(CAST('ca000000-0000-0000-0000-000000000007' AS UUID), 'Réalité virtuelle'),
(CAST('ca000000-0000-0000-0000-000000000008' AS UUID), 'Accessoires');

-- =====================================================
-- TYPES DE MATERIEL (30)
-- (id, categorie, nom, description, duree max en jours, au catalogue)
-- =====================================================
INSERT INTO TypeMateriel (id, auCatalogue, description, dureeMaxEmprunt, nom, categorie_id) VALUES
(CAST('7e000000-0000-0000-0000-000000000001' AS UUID), b'1', 'Ordinateur portable 15 pouces, 16 Go de RAM',            14, 'PC portable standard',          CAST('ca000000-0000-0000-0000-000000000001' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000002' AS UUID), b'1', 'Station de travail portable pour la 3D et la vidéo',    7,  'PC portable haute performance', CAST('ca000000-0000-0000-0000-000000000001' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000003' AS UUID), b'1', 'Tablette 10 pouces avec stylet',                        14, 'Tablette graphique',            CAST('ca000000-0000-0000-0000-000000000001' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000004' AS UUID), b'1', 'Chromebook pour le travail en salle',                   21, 'Chromebook',                    CAST('ca000000-0000-0000-0000-000000000001' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000005' AS UUID), b'1', 'Écran externe 24 pouces Full HD',                       14, 'Écran 24 pouces',               CAST('ca000000-0000-0000-0000-000000000001' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000006' AS UUID), b'1', 'Vidéoprojecteur HDMI, 3500 lumens',                     3,  'Vidéoprojecteur',               CAST('ca000000-0000-0000-0000-000000000002' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000007' AS UUID), b'1', 'Caméscope Full HD avec trépied',                        7,  'Caméra vidéo',                  CAST('ca000000-0000-0000-0000-000000000002' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000008' AS UUID), b'1', 'Micro-cravate sans fil',                               5,  'Micro sans fil',                CAST('ca000000-0000-0000-0000-000000000002' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000009' AS UUID), b'1', 'Enceinte portable avec entrée jack et Bluetooth',        3,  'Enceinte de sonorisation',      CAST('ca000000-0000-0000-0000-000000000002' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000000a' AS UUID), b'1', 'Casque audio fermé pour le montage',                   7,  'Casque audio studio',           CAST('ca000000-0000-0000-0000-000000000002' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000000b' AS UUID), b'1', 'Enregistreur audio portable 4 pistes',                 7,  'Enregistreur audio',            CAST('ca000000-0000-0000-0000-000000000002' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000000c' AS UUID), b'1', 'Appareil photo reflex avec objectif 18-55 mm',         7,  'Appareil photo reflex',         CAST('ca000000-0000-0000-0000-000000000003' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000000d' AS UUID), b'1', 'Trépied aluminium 160 cm',                             14, 'Trépied photo',                 CAST('ca000000-0000-0000-0000-000000000003' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000000e' AS UUID), b'1', 'Kit de 2 torches LED avec diffuseurs',                 7,  'Kit d''éclairage LED',          CAST('ca000000-0000-0000-0000-000000000003' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000000f' AS UUID), b'1', 'Stabilisateur 3 axes pour smartphone',                 7,  'Stabilisateur (gimbal)',        CAST('ca000000-0000-0000-0000-000000000003' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000010' AS UUID), b'1', 'Carte Arduino Uno avec câble USB',                     30, 'Carte Arduino',                 CAST('ca000000-0000-0000-0000-000000000004' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000011' AS UUID), b'1', 'Micro-ordinateur Raspberry Pi 4 avec alimentation',    30, 'Raspberry Pi',                  CAST('ca000000-0000-0000-0000-000000000004' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000012' AS UUID), b'1', 'Kit de capteurs et composants de prototypage',          30, 'Kit capteurs',                  CAST('ca000000-0000-0000-0000-000000000004' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000013' AS UUID), b'1', 'Fer à souder avec station et accessoires',             7,  'Station de soudure',            CAST('ca000000-0000-0000-0000-000000000004' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000014' AS UUID), b'1', 'Imprimante 3D de bureau (accès supervisé)',            7,  'Imprimante 3D',                 CAST('ca000000-0000-0000-0000-000000000004' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000015' AS UUID), b'1', 'Oscilloscope numérique 2 voies 100 MHz',               5,  'Oscilloscope',                  CAST('ca000000-0000-0000-0000-000000000005' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000016' AS UUID), b'1', 'Multimètre numérique avec cordons',                    14, 'Multimètre',                    CAST('ca000000-0000-0000-0000-000000000005' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000017' AS UUID), b'1', 'Alimentation de laboratoire réglable 0-30 V',          7,  'Alimentation stabilisée',       CAST('ca000000-0000-0000-0000-000000000005' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000018' AS UUID), b'1', 'Générateur de fonctions 20 MHz',                      5,  'Générateur de signaux',         CAST('ca000000-0000-0000-0000-000000000005' AS UUID)),
(CAST('7e000000-0000-0000-0000-000000000019' AS UUID), b'1', 'Routeur Wi-Fi pour travaux pratiques réseau',          14, 'Routeur Wi-Fi',                 CAST('ca000000-0000-0000-0000-000000000006' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000001a' AS UUID), b'1', 'Switch 8 ports manageable',                           14, 'Switch manageable',             CAST('ca000000-0000-0000-0000-000000000006' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000001b' AS UUID), b'1', 'Casque de réalité virtuelle autonome',                7,  'Casque VR',                     CAST('ca000000-0000-0000-0000-000000000007' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000001c' AS UUID), b'1', 'Adaptateur USB-C vers HDMI et USB',                   30, 'Adaptateur USB-C',              CAST('ca000000-0000-0000-0000-000000000008' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000001d' AS UUID), b'1', 'Rallonge multiprise 5 prises avec parafoudre',        30, 'Multiprise',                    CAST('ca000000-0000-0000-0000-000000000008' AS UUID)),
(CAST('7e000000-0000-0000-0000-00000000001e' AS UUID), b'0', 'Ancien modèle retiré du catalogue',                    7,  'Projecteur ancienne génération', CAST('ca000000-0000-0000-0000-000000000002' AS UUID));

-- =====================================================
-- UTILISATEURS : 6 gestionnaires + 120 étudiants
-- =====================================================
INSERT INTO Utilisateur (id, email, gestionnaire, login, motDePasse, nom, prenom) VALUES
(CAST('0e000000-0000-0000-0000-000000000001' AS UUID), 'marie.lefevre@caensup.fr',     b'1', 'mlefevre',   @pwd, 'Lefèvre',   'Marie'),
(CAST('0e000000-0000-0000-0000-000000000002' AS UUID), 'thomas.moreau@caensup.fr',     b'1', 'tmoreau',    @pwd, 'Moreau',    'Thomas'),
(CAST('0e000000-0000-0000-0000-000000000003' AS UUID), 'sophie.bernard@caensup.fr',    b'1', 'sbernard',   @pwd, 'Bernard',   'Sophie'),
(CAST('0e000000-0000-0000-0000-000000000004' AS UUID), 'julien.petit@caensup.fr',      b'1', 'jpetit',     @pwd, 'Petit',     'Julien'),
(CAST('0e000000-0000-0000-0000-000000000005' AS UUID), 'camille.durand@caensup.fr',    b'1', 'cdurand',    @pwd, 'Durand',    'Camille'),
(CAST('0e000000-0000-0000-0000-000000000006' AS UUID), 'nicolas.leroy@caensup.fr',     b'1', 'nleroy',     @pwd, 'Leroy',     'Nicolas');

INSERT INTO Utilisateur (id, email, gestionnaire, login, motDePasse, nom, prenom)
SELECT CAST(CONCAT('0e000000-0000-0000-0000-', LPAD(HEX(100 + s.n), 12, '0')) AS UUID),
       CONCAT(LOWER(p.prenom), '.', LOWER(REPLACE(REPLACE(REPLACE(n.nom, 'é', 'e'), 'è', 'e'), ' ', '')), s.n, '@etu.caensup.fr'),
       b'0',
       CONCAT(LOWER(LEFT(p.prenom, 1)), LOWER(REPLACE(REPLACE(REPLACE(n.nom, 'é', 'e'), 'è', 'e'), ' ', '')), s.n),
       @pwd,
       n.nom,
       p.prenom
FROM seq s
JOIN (SELECT 1 AS k, 'Martin' AS nom UNION SELECT 2, 'Garcia' UNION SELECT 3, 'Roux' UNION SELECT 4, 'Fournier'
      UNION SELECT 5, 'Girard' UNION SELECT 6, 'Bonnet' UNION SELECT 7, 'Dupont' UNION SELECT 8, 'Lambert'
      UNION SELECT 9, 'Fontaine' UNION SELECT 10, 'Rousseau' UNION SELECT 11, 'Vincent' UNION SELECT 12, 'Muller'
      UNION SELECT 13, 'Lopez' UNION SELECT 14, 'Faure' UNION SELECT 15, 'Mercier' UNION SELECT 16, 'Blanc'
      UNION SELECT 17, 'Guerin' UNION SELECT 18, 'Boyer' UNION SELECT 19, 'Garnier' UNION SELECT 20, 'Chevalier') n
  ON n.k = 1 + (s.n MOD 20)
JOIN (SELECT 1 AS k, 'Léa' AS prenom UNION SELECT 2, 'Lucas' UNION SELECT 3, 'Emma' UNION SELECT 4, 'Hugo'
      UNION SELECT 5, 'Chloé' UNION SELECT 6, 'Louis' UNION SELECT 7, 'Manon' UNION SELECT 8, 'Gabriel'
      UNION SELECT 9, 'Inès' UNION SELECT 10, 'Arthur' UNION SELECT 11, 'Jade' UNION SELECT 12, 'Raphaël'
      UNION SELECT 13, 'Sarah' UNION SELECT 14, 'Adam' UNION SELECT 15, 'Lola' UNION SELECT 16, 'Théo') p
  ON p.k = 1 + ((s.n * 7) MOD 16)
WHERE s.n <= 120;

-- Normalisation des prénoms accentués dans l'email et le login
UPDATE Utilisateur
SET email = REPLACE(REPLACE(REPLACE(REPLACE(email, 'é', 'e'), 'è', 'e'), 'ë', 'e'), 'ï', 'i'),
    login = REPLACE(REPLACE(REPLACE(REPLACE(login, 'é', 'e'), 'è', 'e'), 'ë', 'e'), 'ï', 'i')
WHERE gestionnaire = b'0';

-- =====================================================
-- MATERIEL : 150 exemplaires répartis sur les 30 types
-- =====================================================
INSERT INTO Materiel (id, description, disponible, nom, type_id)
SELECT CAST(CONCAT('ae000000-0000-0000-0000-', LPAD(HEX(s.n), 12, '0')) AS UUID),
       CONCAT('Exemplaire n° ', LPAD(s.n, 4, '0'), ' - inventaire INV-', LPAD(s.n, 5, '0')),
       IF(s.n MOD 11 = 0, b'0', b'1'),
       CONCAT(t.nom, ' #', LPAD(s.n, 3, '0')),
       t.id
FROM seq s
JOIN (SELECT id, nom, ROW_NUMBER() OVER (ORDER BY id) AS rk FROM TypeMateriel) t
  ON t.rk = 1 + ((s.n - 1) MOD 30)
WHERE s.n <= 150;

-- =====================================================
-- RESERVATIONS : 400 réservations étalées sur ~14 mois
-- Passées, en cours et futures par rapport à NOW()
-- avis = 1 : acceptée ; avis = 0 : refusée
-- =====================================================
INSERT INTO Reservation (id, avis, commentaire, dateDebut, dateFin, dateResa, motivationAvis,
                         emprunteur_id, gestionnaire_id, typeMateriel_id)
SELECT CAST(CONCAT('ee000000-0000-0000-0000-', LPAD(HEX(s.n), 12, '0')) AS UUID),
       IF(s.n MOD 8 = 0, b'0', b'1'),
       ELT(1 + (s.n MOD 6), 'Pour un projet de groupe', 'Pour un TP', 'Pour un stage', 'Pour un mémoire',
           'Pour une présentation', NULL),
       dd.debut,
       DATE_ADD(dd.debut, INTERVAL 1 + (s.n MOD 7) DAY),
       DATE_SUB(dd.debut, INTERVAL 2 + (s.n MOD 10) DAY),
       IF(s.n MOD 8 = 0, 'Aucun exemplaire disponible sur la période demandée', 'Demande conforme au règlement'),
       CAST(CONCAT('0e000000-0000-0000-0000-', LPAD(HEX(101 + (s.n MOD 120)), 12, '0')) AS UUID),
       CAST(CONCAT('0e000000-0000-0000-0000-', LPAD(HEX(1 + (s.n MOD 6)), 12, '0')) AS UUID),
       CAST(CONCAT('7e000000-0000-0000-0000-', LPAD(HEX(1 + (s.n MOD 30)), 12, '0')) AS UUID)
FROM (SELECT n FROM seq WHERE n <= 400) s
JOIN (SELECT n AS k,
             DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 365 DAY), INTERVAL (n * 1.1) DAY) + INTERVAL (8 + n MOD 6) HOUR AS debut
      FROM seq WHERE n <= 400) dd
  ON dd.k = s.n;

-- =====================================================
-- EMPRUNTS : un emprunt par réservation acceptée et déjà démarrée
-- (reservation_id est UNIQUE)
-- =====================================================
INSERT INTO Emprunt (id, commentaire, dateRemise, dateRetourEffective, dateRetourPrevue,
                     degradations, reservation_id, materiel_id)
SELECT CAST(CONCAT('ef000000-0000-0000-0000-', LPAD(HEX(m.k), 12, '0')) AS UUID),
       IF(m.k MOD 17 = 0, 'Rayure constatée au retour', NULL),
       m.dateDebut,
       IF(m.dateFin < NOW() - INTERVAL 1 DAY AND m.k MOD 13 <> 0,
          DATE_ADD(m.dateFin, INTERVAL (m.k MOD 3) DAY), NULL),
       m.dateFin,
       IF(m.k MOD 17 = 0, b'1', b'0'),
       m.id,
       CAST(CONCAT('ae000000-0000-0000-0000-', LPAD(HEX(1 + ((m.k - 1) MOD 150)), 12, '0')) AS UUID)
FROM (
  SELECT ROW_NUMBER() OVER (ORDER BY r.dateDebut) AS k, r.id, r.dateDebut, r.dateFin
  FROM Reservation r
  WHERE r.avis = b'1' AND r.dateDebut <= NOW()
) m;

DROP TEMPORARY TABLE IF EXISTS seq;

-- =====================================================
-- CONTROLES
-- =====================================================
SELECT 'Categorie' AS table_name, COUNT(*) AS nb FROM Categorie
UNION ALL SELECT 'TypeMateriel', COUNT(*) FROM TypeMateriel
UNION ALL SELECT 'Materiel',     COUNT(*) FROM Materiel
UNION ALL SELECT 'Utilisateur',  COUNT(*) FROM Utilisateur
UNION ALL SELECT 'Reservation',  COUNT(*) FROM Reservation
UNION ALL SELECT 'Emprunt',      COUNT(*) FROM Emprunt;

SELECT COUNT(*) AS emprunts_en_cours  FROM Emprunt WHERE dateRetourEffective IS NULL;
SELECT COUNT(*) AS emprunts_en_retard FROM Emprunt
WHERE dateRetourEffective IS NULL AND dateRetourPrevue < NOW();