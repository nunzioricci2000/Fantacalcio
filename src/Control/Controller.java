package Control;

import DAO.*;
import Model.*;
import UI.UserInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controller implements Utente, Amministratore {
    private final UserInterface ui;
    private final CalciatoreDAO calciatoreDAO;
    private final SkillsDAO skillsDAO;
    private final RuoliDAO ruoliDAO;
    private final SquadraDAO squadraDAO;
    private final MilitanzaDAO militanzaDAO;
    private final TrofeoDAO trofeoDAO;
    
    private boolean isAdmin = false;
    private static final String ADMIN_PASSWORD = "admin";
    
    public Controller(UserInterface ui, CalciatoreDAO calciatoreDAO, SkillsDAO skillsDAO, 
                     RuoliDAO ruoliDAO, SquadraDAO squadraDAO, MilitanzaDAO militanzaDAO,
                     TrofeoDAO trofeoDAO) {
        this.calciatoreDAO = calciatoreDAO;
        this.skillsDAO = skillsDAO;
        this.ruoliDAO = ruoliDAO;
        this.squadraDAO = squadraDAO;
        this.militanzaDAO = militanzaDAO;
        this.trofeoDAO = trofeoDAO;
        this.ui = ui;
        ui.setController(this);
    }
    
    public void startApplication() {
        ui.initialize();
        ui.showLoginView();
    }
    
    // Implementazione dei metodi dell'interfaccia Utente
    @Override
    public void accediComeAmministratore(String password) {
        if (ADMIN_PASSWORD.equals(password)) {
            isAdmin = true;
            ui.notifyAdminMode(true);
            ui.showMessage("Accesso come amministratore effettuato");
        } else {
            ui.showError("Password errata");
        }
    }

    @Override
    public void vediElencoCalciatori() {
        try {
            List<Calciatore> calciatori = calciatoreDAO.readCalciatori(null);
            ui.displayCalciatori(calciatori);
        } catch (SQLException e) {
            ui.showError("Errore durante il recupero dei calciatori: " + e.getMessage());
        }
    }

    @Override
    public void vediCaratteristicheCalciatore(int id) {
        try {
            Calciatore dettaglio = calciatoreDAO.read(id);
            ui.displayDettaglioCalciatore(dettaglio);
        } catch (SQLException e) {
            ui.showError("Errore durante il recupero dei dettagli del calciatore: " + e.getMessage());
        }
    }

    @Override
    public void filtraPer(Filtro filtro) {
        try {
            List<Calciatore> calciatoriFiltrati = calciatoreDAO.readCalciatori(filtro);
            ui.displayCalciatori(calciatoriFiltrati);
        } catch (SQLException e) {
            ui.showError("Errore durante il filtraggio dei calciatori: " + e.getMessage());
        }
    }
    
    @Override
    public void inserisciCalciatore(String nome, String cognome, Date dataDiNascita, 
                                  Date dataDiRitiro, List<Skill> skills, List<Ruolo> ruoli, 
                                  Piede piede, List<Militanza> carriera) {
        if (!isAdmin) {
            ui.showError("Devi essere amministratore per inserire un calciatore");
            return;
        }
        
        try {
            Calciatore nuovoCalciatore = new Calciatore(
                0, nome, cognome, dataDiNascita, dataDiRitiro, 
                piede, ruoli, skills, new ArrayList<>(), carriera
            );
            
            calciatoreDAO.create(nuovoCalciatore);
            ui.showMessage("Calciatore inserito con successo");
            vediElencoCalciatori(); // Aggiorna la lista
        } catch (SQLException e) {
            ui.showError("Errore durante l'inserimento del calciatore: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            ui.showError("Dati non validi: " + e.getMessage());
        }
    }

    @Override
    public void modificaCalciatore(Calciatore calciatore, String nome, String cognome, 
                                 Date dataDiNascita, Date dataDiRitiro, List<Skill> skills, 
                                 List<Ruolo> ruoli, Piede piede, List<Militanza> carriera) {
        if (!isAdmin) {
            ui.showError("Devi essere amministratore per modificare un calciatore");
            return;
        }
        
        try {
            calciatore.setNome(nome);
            calciatore.setCognome(cognome);
            calciatore.setDataDiNascita(dataDiNascita);
            calciatore.setDataDiRitiro(dataDiRitiro);
            calciatore.setPiede(piede);
            calciatore.setRuoli(ruoli);
            calciatore.setSkills(skills);
            calciatore.setCarriera(carriera);
            
            calciatoreDAO.update(calciatore);
            ui.showMessage("Calciatore modificato con successo");
            vediElencoCalciatori(); // Aggiorna la lista
        } catch (SQLException e) {
            ui.showError("Errore durante la modifica del calciatore: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            ui.showError("Dati non validi: " + e.getMessage());
        }
    }

    @Override
    public void eliminaCalciatore(Calciatore calciatore) {
        if (!isAdmin) {
            ui.showError("Devi essere amministratore per eliminare un calciatore");
            return;
        }
        
        try {
            calciatoreDAO.delete(calciatore);
            ui.showMessage("Calciatore eliminato con successo");
            vediElencoCalciatori(); // Aggiorna la lista
        } catch (SQLException e) {
            ui.showError("Errore durante l'eliminazione del calciatore: " + e.getMessage());
        }
    }

    @Override
    public void logout() {
        isAdmin = false;
        ui.notifyAdminMode(false);
        ui.showLoginView();
        ui.showMessage("Logout effettuato con successo");
    }
    
    public List<Squadra> getSquadre() {
        try {
            return squadraDAO.readSquadre();
        } catch (SQLException e) {
            ui.showError("Errore durante il recupero delle squadre: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Calciatore> getCalciatori() {
        try {
            return calciatoreDAO.readCalciatori(null);
        } catch (SQLException e) {
            ui.showError("Errore durante il recupero dei calciatori: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Calciatore getCalciatore(int id) {
        try {
            return calciatoreDAO.read(id);
        } catch (SQLException e) {
            ui.showError("Errore durante il recupero del calciatore: " + e.getMessage());
            return null;
        }
    }

    public void addMilitanza(int idCalciatore, Militanza militanza) {
        if (!isAdmin) {
            ui.showError("Devi essere amministratore per aggiungere una militanza");
            return;
        }
        
        try {
            // Usa il DAO della militanza per creare la nuova militanza (include già la gestione del periodo)
            militanzaDAO.create(idCalciatore, militanza);
            
            // Aggiorna la vista del calciatore corrente
            Calciatore calciatore = calciatoreDAO.read(idCalciatore);
            if (calciatore != null) {
                ui.displayDettaglioCalciatore(calciatore);
            }
            
            ui.showMessage("Militanza aggiunta con successo");
        } catch (SQLException e) {
            ui.showError("Errore durante l'aggiunta della militanza: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            ui.showError("Dati non validi: " + e.getMessage());
        }
    }
    
    public void aggiornaSkills(int idCalciatore, List<Skill> skills) {
        if (!isAdmin) {
            ui.showError("Devi essere amministratore per modificare un calciatore");
            return;
        }
        
        try {
            skillsDAO.deleteSkillsCalciatore(idCalciatore);
            for (Skill skill : skills) {
                skillsDAO.addSkillCalciatore(idCalciatore, skill);
            }
            
            ui.showMessage("Skills aggiornate con successo");
            ui.displayDettaglioCalciatore(calciatoreDAO.read(idCalciatore));
        } catch (SQLException e) {
            ui.showError("Errore durante l'aggiornamento delle skills: " + e.getMessage());
        }
    }
    
    public void aggiornaRuoli(int idCalciatore, List<Ruolo> ruoli) {
        if (!isAdmin) {
            ui.showError("Devi essere amministratore per modificare un calciatore");
            return;
        }
        
        if (ruoli.isEmpty()) {
            ui.showError("Un calciatore deve avere almeno un ruolo");
            return;
        }
        
        try {
            // Aggiorna direttamente i ruoli usando il DAO specifico
            ruoliDAO.deleteRuoliCalciatore(idCalciatore);
            for (Ruolo ruolo : ruoli) {
                ruoliDAO.addRuoloCalciatore(idCalciatore, ruolo);
            }
            
            // Aggiorna la vista
            ui.showMessage("Ruoli aggiornati con successo");
            ui.displayDettaglioCalciatore(calciatoreDAO.read(idCalciatore));
        } catch (SQLException e) {
            ui.showError("Errore durante l'aggiornamento dei ruoli: " + e.getMessage());
        }
    }
}