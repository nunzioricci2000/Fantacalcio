# Documentazione Database Fantacalcio

Questa cartella contiene la documentazione relativa al sistema di gestione del database dei calciatori.

## Schema del Database

Lo schema del database è rappresentato in due diagrammi UML:

- `database.puml`: Modello originale del database
- `database-ristrutturato.puml`: Modello ristrutturato del database

Il modello ristrutturato semplifica le relazioni tra le entità mantenendo tutte le funzionalità.

## Struttura del Database

### Entità Principali

- **Calciatore**: Memorizza le informazioni sui giocatori (nome, data di nascita, data di ritiro, piede preferito)
- **Squadra**: Rappresenta le squadre con nome e nazionalità
- **Militanza**: Registra le relazioni calciatore-squadra con statistiche (partite giocate, gol segnati/subiti)
- **Periodo**: Tiene traccia dei periodi in cui i calciatori hanno fatto parte di specifiche squadre
- **Trofeo**: Contiene informazioni sui trofei vinti da calciatori o squadre
- **Ruoli**: Definisce le posizioni dei giocatori (portiere, difensore, centrocampista, attaccante)
- **Skills**: Registra le abilità speciali dei calciatori

### Caratteristiche Speciali

Il database implementa diverse funzionalità avanzate:

- **Prevenzione di Periodi Sovrapposti**: I trigger impediscono la registrazione di periodi sovrapposti per un calciatore
- **Vincoli Basati sul Ruolo**: Le statistiche sui gol subiti vengono tracciate solo per i portieri
- **Viste**: `TrofeiCalciatore` e `StatisticheCalciatore` forniscono statistiche aggregate sui calciatori

## Implementazione

I file di implementazione si trovano nella cartella `/database`:

- `create_tables.sql`: Definizione dello schema con tabelle, vincoli, trigger e viste
- `fill_tables.sql`: Dati di esempio per i test

## Utilizzo

Importa lo schema e i dati nel seguente ordine:

1. Esegui `create_tables.sql` per creare la struttura del database
2. Esegui `fill_tables.sql` per popolare il database con dati di esempio

## Tipi di Entità

Tipi Enumerati:

- `T_PIEDE`: DESTRO, SINISTRO, AMBIDESTRO
- `T_RUOLO`: PORTIERE, DIFENSORE, CENTROCAMPISTA, ATTACCANTE
- `T_SKILL`: Varie abilità dei calciatori (COLPO_DI_TESTA, TACKLE, ecc.)
