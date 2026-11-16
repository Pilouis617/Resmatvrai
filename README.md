ResMat — Réservation de matériel

Application web de réservation et de prêt de matériel (informatique, audiovisuel…) pour un établissement scolaire. Les étudiants consultent le catalogue et demandent une réservation ; les gestionnaires valident les demandes, remettent le matériel et enregistrent son retour.

Projet réalisé dans le cadre du BTS SIO.

Fonctionnalités
Côté étudiant
Catalogue des types de matériel disponibles, classés par catégorie
Demande de réservation sur une période (date et heure de début et de fin)
Mes réservations : suivi du statut (En attente, Acceptée, Refusée, Annulée) et historique des emprunts
Annulation d'une demande tant qu'elle n'a pas été traitée
Côté gestionnaire (/gestion)
File des demandes en attente : acceptation ou refus avec motif
Remise du matériel : un exemplaire libre est attribué automatiquement
Retours : date de retour réelle, signalement des dégradations, remarque
Repérage des emprunts en retard
Gestion des types de matériel : création, modification, durée maximale d'emprunt, retrait du catalogue
Règles métier
La date de début doit être dans le futur et la date de fin après la date de début
La durée demandée ne peut pas dépasser la durée maximale d'emprunt du type (14 jours par défaut, de 1 à 365)
Impossible de réserver un matériel retiré du catalogue ou sans exemplaire disponible
À l'acceptation, l'application détecte les conflits : la demande est bloquée si toutes les unités sont déjà réservées sur la période
Une réservation donne lieu à un emprunt au maximum
