-- Initial data for Campus Crawler API
-- Using INSERT IGNORE to avoid duplicates on restart

INSERT IGNORE INTO defensive_equipment (id, type, name, defense) VALUES
(1, 'Shield', 'Bouclier en bois', 3),
(2, 'Shield', 'Bouclier en acier', 6),
(3, 'Armor', 'Armure légère', 4),
(4, 'Armor', 'Armure lourde', 8),
(5, 'Helmet', 'Casque de cuir', 2),
(6, 'Helmet', 'Casque en acier', 4);

INSERT IGNORE INTO offensive_equipment (id, type, name, attack) VALUES
(1, 'Weapon', 'Marteau de fer', 8),
(2, 'Weapon', 'Épée d''acier', 12),
(3, 'Weapon', 'Hache de guerre', 16),
(4, 'Weapon', 'Épée légendaire', 20),
(5, 'Spell', 'Éclair', 8),
(6, 'Spell', 'Boule de feu', 12),
(7, 'Spell', 'Météorite', 18);

INSERT IGNORE INTO monster (id, type, name, max_health, base_attack) VALUES
(1, 'Dragon', 'Dragon', 25, 12),
(2, 'Goblin', 'Goblin', 12, 7),
(3, 'Orc', 'Orc', 18, 9),
(4, 'Witch', 'Witch', 20, 10);