-- Creazione dei tipi enumerati
CREATE TYPE T_PIEDE AS ENUM ('DESTRO', 'SINISTRO', 'AMBIDESTRO');
CREATE TYPE T_RUOLO AS ENUM ('PORTIERE', 'DIFENSORE', 'CENTROCAMPISTA', 'ATTACCANTE');
CREATE TYPE T_SKILL AS ENUM (
    'COLPO_DI_TESTA',
    'TACKLE',
    'ROVESCIATA',
    'DRIBBLING',
    'PASSAGGIO',
    'TIRO',
    'CONTROLLO_PALLA',
    'FINTA',
    'CROSS',
    'VELOCITA',
    'AGILITA',
    'RESISTENZA',
    'VISIONE_DI_GIOCO',
    'POSIZIONAMENTO',
    'LEADERSHIP'
);

----------------------------------------------------------------
-- Tabella Calciatore
----------------------------------------------------------------
CREATE TABLE Calciatore (
    ID SERIAL PRIMARY KEY,
    NOME VARCHAR(255) NOT NULL,
    COGNOME VARCHAR(255) NOT NULL,
    DATA_DI_NASCITA DATE NOT NULL,
    DATA_DI_RITIRO DATE,
    PIEDE T_PIEDE NOT NULL
);

----------------------------------------------------------------
-- Tabella Squadra
----------------------------------------------------------------
CREATE TABLE Squadra (
    NOME VARCHAR(255) NOT NULL,
    NAZIONALITÀ VARCHAR(255) NOT NULL,
    PRIMARY KEY (NOME, NAZIONALITÀ)
);

----------------------------------------------------------------
-- Tabella Trofeo
----------------------------------------------------------------
CREATE TABLE Trofeo (
    NOME VARCHAR(255) NOT NULL,
    DATA DATE NOT NULL,
    ID_CALCIATORE INTEGER,
    NOME_SQUADRA VARCHAR(255),
    NAZIONALITÀ_SQUADRA VARCHAR(255),
    PRIMARY KEY (NOME, DATA),
    CONSTRAINT chk_trofeo_exclusive CHECK (
            (ID_CALCIATORE IS NOT NULL AND NOME_SQUADRA IS NULL AND NAZIONALITÀ_SQUADRA IS NULL) OR
            (ID_CALCIATORE IS NULL AND NOME_SQUADRA IS NOT NULL AND NAZIONALITÀ_SQUADRA IS NOT NULL)
        ),
    CONSTRAINT fk_trofeo_calciatore FOREIGN KEY (ID_CALCIATORE) REFERENCES Calciatore(ID),
    CONSTRAINT fk_trofeo_squadra FOREIGN KEY (NOME_SQUADRA, NAZIONALITÀ_SQUADRA) REFERENCES Squadra(NOME, NAZIONALITÀ)
);

----------------------------------------------------------------
-- Tabella Militanza
----------------------------------------------------------------
CREATE TABLE Militanza (
    ID_CALCIATORE INTEGER NOT NULL,
    NOME_SQUADRA VARCHAR(255) NOT NULL,
    NAZIONALITÀ_SQUADRA VARCHAR(255) NOT NULL,
    PARTITE_GIOCATE INTEGER NOT NULL,
    GOAL_SEGNATI INTEGER NOT NULL,
    GOAL_SUBITI INTEGER,
    PRIMARY KEY (ID_CALCIATORE, NOME_SQUADRA, NAZIONALITÀ_SQUADRA),
    CONSTRAINT fk_militanza_calciatore FOREIGN KEY (ID_CALCIATORE) REFERENCES Calciatore(ID),
    CONSTRAINT fk_militanza_squadra FOREIGN KEY (NOME_SQUADRA, NAZIONALITÀ_SQUADRA) REFERENCES Squadra(NOME, NAZIONALITÀ)
);

-- Funzione trigger per il controllo di GOAL_SUBITI
CREATE OR REPLACE FUNCTION check_goal_subiti_trigger()
RETURNS trigger AS $$
DECLARE is_portiere BOOLEAN;
BEGIN
    SELECT EXISTS(
        SELECT 1 FROM Ruoli
        WHERE ID_CALCIATORE = NEW.ID_CALCIATORE AND RUOLO = 'PORTIERE'
    ) INTO is_portiere;
    IF NOT is_portiere THEN
        IF NEW.GOAL_SUBITI IS NOT NULL THEN
            RAISE EXCEPTION 'GOAL_SUBITI deve essere NULL per un calciatore NON-portiere';
        END IF;
    ELSE
        IF NEW.GOAL_SUBITI IS NULL THEN
            RAISE EXCEPTION 'GOAL_SUBITI non può essere NULL per un portiere';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_goal_subiti
    BEFORE INSERT OR UPDATE ON Militanza
        FOR EACH ROW EXECUTE FUNCTION check_goal_subiti_trigger();

----------------------------------------------------------------
-- Tabella Periodo
----------------------------------------------------------------
CREATE TABLE Periodo (
    ID_CALCIATORE INTEGER NOT NULL,
    NOME_SQUADRA VARCHAR(255) NOT NULL,
    NAZIONALITÀ_SQUADRA VARCHAR(255) NOT NULL,
    DATA_INIZIO DATE NOT NULL,
    DATA_FINE DATE NOT NULL,
    PRIMARY KEY (ID_CALCIATORE, NOME_SQUADRA, NAZIONALITÀ_SQUADRA, DATA_INIZIO, DATA_FINE),
    CONSTRAINT fk_periodo_militanza FOREIGN KEY (ID_CALCIATORE, NOME_SQUADRA, NAZIONALITÀ_SQUADRA)
    REFERENCES Militanza(ID_CALCIATORE, NOME_SQUADRA, NAZIONALITÀ_SQUADRA),
    CONSTRAINT chk_periodo_dates CHECK (DATA_INIZIO < DATA_FINE)
);

-- Funzione trigger per evitare periodi sovrapposti per lo stesso calciatore
CREATE OR REPLACE FUNCTION check_overlapping_periods_trigger()
RETURNS trigger AS $$
DECLARE overlapping RECORD;
BEGIN
    SELECT 1 INTO overlapping
    FROM Periodo
    WHERE ID_CALCIATORE = NEW.ID_CALCIATORE
        AND (NEW.DATA_INIZIO, NEW.DATA_FINE) OVERLAPS (DATA_INIZIO, DATA_FINE)
        AND NOT (DATA_INIZIO = NEW.DATA_INIZIO
            AND DATA_FINE = NEW.DATA_FINE
            AND NOME_SQUADRA = NEW.NOME_SQUADRA
            AND NAZIONALITÀ_SQUADRA = NEW.NAZIONALITÀ_SQUADRA)
            LIMIT 1;

    IF FOUND THEN
        RAISE EXCEPTION 'Periodo sovrapposto per il calciatore %', NEW.ID_CALCIATORE;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_overlapping_periods
    BEFORE INSERT OR UPDATE ON Periodo
        FOR EACH ROW EXECUTE FUNCTION check_overlapping_periods_trigger();

----------------------------------------------------------------
-- Tabella Skills
----------------------------------------------------------------
CREATE TABLE Skills (
    ID_CALCIATORE INTEGER NOT NULL,
    SKILL T_SKILL NOT NULL,
    PRIMARY KEY (ID_CALCIATORE, SKILL),
    CONSTRAINT fk_skills_calciatore FOREIGN KEY (ID_CALCIATORE)
        REFERENCES Calciatore(ID)
);

----------------------------------------------------------------
-- Tabella Ruoli
----------------------------------------------------------------
CREATE TABLE Ruoli (
    ID_CALCIATORE INTEGER NOT NULL,
    RUOLO T_RUOLO NOT NULL,
    PRIMARY KEY (ID_CALCIATORE, RUOLO),
    CONSTRAINT fk_ruoli_calciatore FOREIGN KEY (ID_CALCIATORE)
        REFERENCES Calciatore(ID)
);


----------------------------------------------------------------
-- View TrofeiCalciatore
----------------------------------------------------------------

CREATE VIEW TrofeiCalciatore AS
SELECT
    C.ID AS ID_CALCIATORE,
    T.NOME AS NOME_TROFEO,
    T.DATA AS DATA_TROFEO,
    T.NOME_SQUADRA AS NOME_SQUADRA,
    T.NAZIONALITÀ_SQUADRA AS NAZIONALITÀ_SQUADRA
FROM
    Calciatore C
        JOIN
    Trofeo T ON C.ID = T.ID_CALCIATORE
UNION ALL
SELECT
    C.ID AS ID_CALCIATORE,
    T.NOME AS NOME_TROFEO,
    T.DATA AS DATA_TROFEO,
    T.NOME_SQUADRA,
    T.NAZIONALITÀ_SQUADRA
FROM
    Calciatore C
        JOIN
    Periodo P ON C.ID = P.ID_CALCIATORE
        JOIN
    Trofeo T ON P.NOME_SQUADRA = T.NOME_SQUADRA AND P.NAZIONALITÀ_SQUADRA = T.NAZIONALITÀ_SQUADRA
WHERE
    T.ID_CALCIATORE IS NULL
  AND T.DATA BETWEEN P.DATA_INIZIO AND P.DATA_FINE;
