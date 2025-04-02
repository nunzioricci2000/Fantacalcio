
----------------------------------------------------------------
-- Inserimento dei dati relativi alla Serie A (stagione 2023-2024)
----------------------------------------------------------------

-- 1. Squadre
INSERT INTO Squadra (NOME, NAZIONALITÀ) VALUES ('Juventus', 'Italia');
INSERT INTO Squadra (NOME, NAZIONALITÀ) VALUES ('Inter', 'Italia');
INSERT INTO Squadra (NOME, NAZIONALITÀ) VALUES ('Napoli', 'Italia');
INSERT INTO Squadra (NOME, NAZIONALITÀ) VALUES ('Roma', 'Italia');

-- 2. Calciatori
-- Gli ID sono impostati esplicitamente per poterli poi utilizzare nelle tabelle correlate
INSERT INTO Calciatore (ID, NOME, COGNOME, DATA_DI_NASCITA, DATA_DI_RITIRO, PIEDE) VALUES
                                                                                       (1, 'Wojciech', 'Szczęsny', '1990-04-18', NULL, 'DESTRO'),
                                                                                       (2, 'Leonardo', 'Bonucci', '1987-05-01', NULL, 'DESTRO'),
                                                                                       (3, 'Dusan', 'Vlahovic', '2000-01-15', NULL, 'DESTRO'),
                                                                                       (4, 'Milan', 'Skriniar', '1995-10-27', NULL, 'DESTRO'),
                                                                                       (5, 'Victor', 'Osimhen', '1998-12-29', NULL, 'DESTRO'),
                                                                                       (6, 'Lorenzo', 'Pellegrini', '1996-01-14', NULL, 'AMBIDESTRO');

-- 3. Ruoli
-- Importante: i ruoli devono essere inseriti prima della Militanza (trigger sul GOAL_SUBITI)
INSERT INTO Ruoli (ID_CALCIATORE, RUOLO) VALUES
                                             (1, 'PORTIERE'),
                                             (2, 'DIFENSORE'),
                                             (3, 'ATTACCANTE'),
                                             (4, 'DIFENSORE'),
                                             (5, 'ATTACCANTE'),
                                             (6, 'CENTROCAMPISTA');

-- 4. Militanza
-- Per i portieri, GOAL_SUBITI NON deve essere NULL; per gli altri deve essere NULL.
INSERT INTO Militanza (ID_CALCIATORE, NOME_SQUADRA, NAZIONALITÀ_SQUADRA, PARTITE_GIOCATE, GOAL_SEGNATI, GOAL_SUBITI) VALUES
                                                                                                                         (1, 'Juventus', 'Italia', 30, 0, 30),       -- Szczęsny (portiere)
                                                                                                                         (2, 'Juventus', 'Italia', 30, 1, NULL),       -- Bonucci (difensore)
                                                                                                                         (3, 'Juventus', 'Italia', 28, 15, NULL),      -- Vlahovic (attaccante)
                                                                                                                         (4, 'Inter',    'Italia', 32, 2, NULL),       -- Skriniar (difensore)
                                                                                                                         (5, 'Napoli',   'Italia', 30, 18, NULL),      -- Osimhen (attaccante)
                                                                                                                         (6, 'Roma',     'Italia', 33, 6, NULL);       -- Pellegrini (centrocampista)

-- 5. Periodi di militanza
-- Per ogni calciatore viene registrato il periodo (stagione 2023-2024)
INSERT INTO Periodo (ID_CALCIATORE, NOME_SQUADRA, NAZIONALITÀ_SQUADRA, DATA_INIZIO, DATA_FINE) VALUES
                                                                                           (1, 'Juventus', 'Italia', '2023-08-01', '2024-06-30'),
                                                                                           (2, 'Juventus', 'Italia', '2023-08-01', '2024-06-30'),
                                                                                           (3, 'Juventus', 'Italia', '2023-08-01', '2024-06-30'),
                                                                                           (4, 'Inter',    'Italia', '2023-08-01', '2024-06-30'),
                                                                                           (5, 'Napoli',   'Italia', '2023-08-01', '2024-06-30'),
                                                                                           (6, 'Roma',     'Italia', '2023-08-01', '2024-06-30');

-- 6. Skills
-- Assegno alcune skills rappresentative per ogni calciatore
INSERT INTO Skills (ID_CALCIATORE, SKILL) VALUES
                                              (1, 'CONTROLLO_PALLA'),           -- Szczęsny
                                              (2, 'TACKLE'),
                                              (2, 'POSIZIONAMENTO'),            -- Bonucci
                                              (3, 'TIRO'),
                                              (3, 'VELOCITA'),                  -- Vlahovic
                                              (4, 'TACKLE'),
                                              (4, 'RESISTENZA'),                -- Skriniar
                                              (5, 'TIRO'),
                                              (5, 'DRIBBLING'),                 -- Osimhen
                                              (6, 'PASSAGGIO'),
                                              (6, 'VISIONE_DI_GIOCO');          -- Pellegrini

-- 7. Trofei
-- Esempio di trofeo individuale: 'Capocannoniere' assegnato a Vlahovic (ID 3)
INSERT INTO Trofeo (NOME, DATA, ID_CALCIATORE, NOME_SQUADRA, NAZIONALITÀ_SQUADRA) VALUES
    ('Capocannoniere', '2023-05-28', 3, NULL, NULL);

-- Esempio di trofeo di squadra: 'Scudetto' assegnato alla squadra Inter
INSERT INTO Trofeo (NOME, DATA, ID_CALCIATORE, NOME_SQUADRA, NAZIONALITÀ_SQUADRA) VALUES
    ('Scudetto', '2023-05-28', NULL, 'Inter', 'Italia');

SELECT setval('calciatore_id_seq', (SELECT MAX(id) FROM Calciatore));