-- Ajouter le champ ETT dans la table consultations
ALTER TABLE consultations ADD COLUMN ett TEXT AFTER examenClinique;
