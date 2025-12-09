-- Script pour ajouter la colonne 'ett' dans la table consultations
-- La colonne 'ecg' existe déjà, on ajoute 'ett' comme nouveau champ
-- Date: 2025-12-08

USE cabinetmedical;

-- Ajouter la colonne ett après ecg
ALTER TABLE consultations 
ADD COLUMN ett TEXT AFTER ecg;

-- Vérification
DESCRIBE consultations;
