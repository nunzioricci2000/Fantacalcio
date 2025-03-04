package Control;

import Model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Controller implements Utente, Amministratore {
    private boolean isAmministratore = false;
    private final ArrayList<Calciatore> calciatori = new ArrayList<>();
    private Filtro filtro = null;

    public Controller() {}

    public boolean isAmministratore() {
        return isAmministratore;
    }

    public ArrayList<Calciatore> getCalciatori() {
        if (filtro == null) {
            return calciatori;
        }
        ArrayList<Calciatore> listaCalciatori = new ArrayList<>(calciatori);
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            listaCalciatori = listaCalciatori.stream()
                    .filter(c -> (c.getNome() + " " + c.getCognome()).toLowerCase().contains(filtro.getNome().toLowerCase()))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
        return listaCalciatori;
    }

    @Override
    public void inserisciCalciatore(String nome, String cognome, Date dataDiNascita, Date dataDiRitiro, List<Skill> skills, List<Ruolo> ruoli, Piede piede, List<Militanza> carriera) {
        if (!isAmministratore) {
            showError("Amministratore non autenticato! Azione non consentita!");
            return;
        }
        if (!isValidInfoCalciatore(nome, cognome, dataDiNascita, dataDiRitiro, skills, ruoli, piede, carriera)) {
            return;
        }
        // TODO
        Calciatore calciatore = new Calciatore(calciatori.stream().map(Calciatore::getId).max(Comparator.naturalOrder()).orElse(0), nome, cognome, dataDiNascita, dataDiRitiro, piede, ruoli, skills, new ArrayList<>(), carriera);
        calciatori.add(calciatore);
        viewCalciatori();
    }

    @Override
    public void modificaCalciatore(Calciatore calciatore, String nome, String cognome, Date dataDiNascita, Date dataDiRitiro, List<Skill> skills, List<Ruolo> ruoli, Piede piede, List<Militanza> carriera) {
        if (!isAmministratore) {
            showError("Amministratore non autenticato! Azione non consentita!");
            return;
        }
        if (!isValidInfoCalciatore(nome, cognome, dataDiNascita, dataDiRitiro, skills, ruoli, piede, carriera)) {
            return;
        }
        // TODO
        calciatore.setNome(nome);
        calciatore.setCognome(cognome);
        calciatore.setDataDiNascita(dataDiNascita);
        calciatore.setDataDiRitiro(dataDiRitiro);
        calciatore.setSkills(skills);
        calciatore.setRuoli(ruoli);
        calciatore.setPiede(piede);
        calciatore.setCarriera(carriera);
        viewCalciatori();
    }

    @Override
    public void eliminaCalciatore(Calciatore calciatore) {
        if (!isAmministratore) {
            showError("Amministratore non autenticato! Azione non consentita!");
            return;
        }
        // TODO
        calciatori.remove(calciatore);
        viewCalciatori();
    }

    @Override
    public void logout() {
        if (!isAmministratore) {
            showError("Amministratore non autenticato! Azione non consentita!");
            isAmministratore = false;
        }
        viewLogin();
    }

    @Override
    public void accediComeAmministratore(String password) {
        if (password.equals("admin")) {
            switchToAmministratore();
        } else {
            showError("Password errata!");
        }
    }

    @Override
    public void vediElencoCalciatori() {
        viewCalciatori();
    }

    @Override
    public void vediCaratteristicheCalciatore(Calciatore calciatore) {
        viewCalciatore(calciatore);
    }

    @Override
    public void filtraPer(Filtro filtro) {
        this.filtro = filtro;
    }

    private void showError(String message) {
        System.err.println(message);
    }

    private boolean isValidInfoCalciatore(String nome, String cognome, Date dataDiNascita, Date dataDiRitiro, List<Skill> skills, List<Ruolo> ruoli, Piede piede, List<Militanza> carriera) {
        if (nome == null || nome.isEmpty()) { showError("Nome non valido!"); return false; }
        if (cognome == null || cognome.isEmpty()) { showError("Cognome non valido!"); return false; }
        if (dataDiNascita == null || dataDiNascita.getTime() > new Date().getTime()) { showError("Data di nascita non valida!"); return false; }
        if (skills == null) { showError("Skills non valido!"); return false; }
        if (ruoli == null || ruoli.isEmpty()) { showError("Ruolo non valido!"); return false; }
        if (piede == null) { showError("Piede non valido!"); return false; }
        if (carriera == null) { showError("Carriera non valido!"); return false; }
        return true;
    }

    private void viewCalciatori() {
        System.out.println("Calciatori");
    }

    private void viewLogin() {
        System.out.println("Login");
    }

    private void switchToAmministratore() {
        isAmministratore = true;
        System.out.println("Sei amministratore");
    }

    private void viewCalciatore(Calciatore calciatore) {
        System.out.println("Calciatore: " + calciatore.getNome() + " " + calciatore.getCognome());
        System.out.println("Data di nascita: " + calciatore.getDataDiNascita());
    }
}
