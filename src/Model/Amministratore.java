package Model;

import java.util.Date;
import java.util.List;

public interface Amministratore extends Utente {
    void inserisciCalciatore(String nome, String cognome, Date dataDiNascita, Date dataDiRitiro, List<Skill> skills, List<Ruolo> ruoli, Piede piede, List<Militanza> carriera);
    void modificaCalciatore(Calciatore calciatore, String nome, String cognome, Date dataDiNascita, Date dataDiRitiro, List<Skill> skills, List<Ruolo> ruoli, Piede piede, List<Militanza> carriera);
    void eliminaCalciatore(Calciatore calciatore);
    void logout();
}
