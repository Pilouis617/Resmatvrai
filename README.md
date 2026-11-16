# ResMat — Réservation de matériel

Application web de réservation et de prêt de matériel (informatique, audiovisuel, etc.) pour un établissement scolaire.

## Description

Cette application permet aux étudiants de consulter le catalogue de matériel, effectuer des demandes de réservation et suivre l’état de leurs emprunts. Les gestionnaires peuvent ensuite valider ou refuser les demandes, attribuer le matériel disponible et gérer les retours.

## Fonctionnalités

### Côté étudiant
- Consultation du catalogue par catégorie
- Demande de réservation sur une période donnée
- Suivi du statut des demandes (en attente, acceptée, refusée, annulée)
- Historique des emprunts
- Annulation d’une demande avant traitement

### Côté gestionnaire
- Gestion de la file des demandes en attente
- Validation ou refus avec motif
- Attribution automatique d’un exemplaire disponible
- Gestion des retours et des dégradations
- Suivi des emprunts en retard
- Création et modification des types de matériel

## Règles métier
- La date de début doit être dans le futur.
- La date de fin doit être postérieure à la date de début.
- La durée demandée ne doit pas dépasser la durée maximale d’emprunt.
- Il est impossible de réserver un matériel retiré du catalogue ou sans exemplaire disponible.
- Une demande est bloquée en cas de conflit avec les réservations déjà existantes.
- Une réservation ne peut générer qu’un emprunt au maximum.

## Projet

Projet réalisé dans le cadre du BTS SIO.
