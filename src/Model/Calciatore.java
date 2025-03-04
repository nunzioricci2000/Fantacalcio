package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calciatore {
    private final int id;
    private String nome;
    private String cognome;
    private Date dataDiNascita;
    private Date dataDiRitiro;
    private Piede piede;
    private final ArrayList<Ruolo> ruoli = new ArrayList<>();
    private final ArrayList<Skill> skills = new ArrayList<>();
    private final ArrayList<Trofeo> trofei = new ArrayList<>();
    private final ArrayList<Militanza> carriera = new ArrayList<>();

    public Calciatore(
            int id,
            String nome,
            String cognome,
            Date dataDiNascita,
            Date dataDiRitiro,
            Piede piede,
            List<Ruolo> ruoli,
            List<Skill> skills,
            List<Trofeo> trofei,
            List<Militanza> carriera
    ) {
        if (nome == null || cognome == null || dataDiNascita == null || piede == null || skills == null || ruoli == null || trofei == null || carriera == null) {
            throw new IllegalArgumentException("found null parameter");
        }
        if (ruoli.isEmpty()) {
            throw new IllegalArgumentException("found empty ruoli");
        }
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.dataDiRitiro = dataDiRitiro;
        this.piede = piede;
        this.ruoli.addAll(ruoli);
        this.skills.addAll(skills);
        this.trofei.addAll(trofei);
        this.carriera.addAll(carriera);
    }


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public Date getDataDiRitiro() {
        return dataDiRitiro;
    }

    public void setDataDiRitiro(Date dataDiRitiro) {
        this.dataDiRitiro = dataDiRitiro;
    }

    public Piede getPiede() {
        return piede;
    }

    public void setPiede(Piede piede) {
        this.piede = piede;
    }

    public ArrayList<Ruolo> getRuoli() {
        return new ArrayList<>(this.ruoli);
    }

    public void setRuoli(List<Ruolo> ruoli) {
        this.ruoli.removeIf((_)->true);
        this.ruoli.addAll(ruoli);
    }

    public ArrayList<Skill> getSkills() {
        return new ArrayList<>(this.skills);
    }

    public void setSkills(List<Skill> skills) {
        this.skills.removeIf((_)->true);
        this.skills.addAll(skills);
    }

    public ArrayList<Trofeo> getTrofei() {
        return new ArrayList<>(this.trofei);
    }

    public void setTrofei(List<Trofeo> trofei) {
        this.trofei.removeIf((_)->true);
        this.trofei.addAll(trofei);
    }

    public ArrayList<Militanza> getCarriera() {
        return new ArrayList<>(this.carriera);
    }

    public void setCarriera(List<Militanza> carriera) {
        this.carriera.removeIf((_)->true);
        this.carriera.addAll(carriera);
    }
}
