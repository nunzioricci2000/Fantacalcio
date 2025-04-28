CREATE TYPE Piede as ENUM('destro', 'sinistro', 'ambidestro');
CREATE TYPE Ruolo as ENUM('attacco', 'difesa', 'centrocampo', 'porta');

CREATE TABLE Torneo (
    idTorneo int PRIMARY KEY,
    Nome varchar(255) NOT NULL,
    Federazione varchar(255),
    Anno int
);

CREATE TABLE Premio (
    idPremio int PRIMARY KEY,
    Nome varchar(255) NOT NULL,
    Data Date,
    idTorneo int,
    FOREIGN KEY (idTorneo) REFERENCES Torneo(idTorneo)
);


CREATE TABLE Player (

    ID int PRIMARY KEY,
    Nome varchar(255) NOT NULL,
    Cognome varchar(255) NOT NULL,
    DataNascita Date,
    Nazionalita varchar(255),
    Ruolo Ruolo,
    Piede Piede
);

CREATE TABLE Team (
    idTeam int PRIMARY KEY,
    Nome varchar(255) NOT NULL,
    Città varchar(255),
    Anno int,
    Nazione varchar(255)
);


CREATE TABLE Premio_Ind (
    idPremio int PRIMARY KEY,
    idPlayer int,
    FOREIGN KEY (idPremio) REFERENCES Premio(idPremio),
    FOREIGN KEY (idPlayer) REFERENCES Player(ID)
);


CREATE TABLE Premio_Team (
    idPremio int PRIMARY KEY,
    idTeam int,
    FOREIGN KEY (idPremio) REFERENCES Premio(idPremio),
    FOREIGN KEY (idTeam) REFERENCES Team(idTeam)
);


CREATE TABLE Militanza (
    DataInizio Date,
    idPlayer int,
    DataFine Date,
    idTeam int,
    PRIMARY KEY (idPlayer, DataInizio),
    FOREIGN KEY (idPlayer) REFERENCES Player(ID),
    FOREIGN KEY (idTeam) REFERENCES Team(idTeam)
);


CREATE TABLE Stats_Punteggi (
    idPlayer int,
    Stagione int,
    Goal int DEFAULT 0,
    Assist int DEFAULT 0,
    Partite int DEFAULT 0,
    Tornei int DEFAULT 0,
    PRIMARY KEY (idPlayer, Stagione),
    FOREIGN KEY (idPlayer) REFERENCES Player(ID)
);


CREATE TABLE Abilita (
    idPlayer int PRIMARY KEY,
    Tiro int CHECK (Tiro BETWEEN 0 AND 100),
    Rovesciata int CHECK (Rovesciata BETWEEN 0 AND 100),
    Parata int CHECK (Parata BETWEEN 0 AND 100),
    Difesa int CHECK (Difesa BETWEEN 0 AND 100),
    Velocita int CHECK (Velocita BETWEEN 0 AND 100),
    FOREIGN KEY (idPlayer) REFERENCES Player(ID)
);

CREATE TABLE Partecipazione (
    idTeam int,
    idTorneo int,
    Stagione int,
    PostoClassifica int,
    PRIMARY KEY (idTeam, idTorneo, Stagione),
    FOREIGN KEY (idTeam) REFERENCES Team(idTeam),
    FOREIGN KEY (idTorneo) REFERENCES Torneo(idTorneo)
);