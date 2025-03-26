package Model;

public class Filtro {
    private String nome;
    private String ruolo;
    private Piede piede;
    private Integer minimoGoalSegnati;
    private Integer massimoGoalSegnati;
    private Integer minimoGoalSubiti;
    private Integer massimoGoalSubiti;
    private Integer minimoEta;
    private Integer massimoEta;
    private Squadra squadra;

    private Filtro() {}

    public String getNome() {
        return nome;
    }

    public String getRuolo() {
        return ruolo;
    }

    public Piede getPiede() {
        return piede;
    }

    public Integer getMinimoGoalSegnati() {
        return minimoGoalSegnati;
    }

    public Integer getMassimoGoalSegnati() {
        return massimoGoalSegnati;
    }

    public Integer getMinimoGoalSubiti() {
        return minimoGoalSubiti;
    }

    public Integer getMassimoGoalSubiti() {
        return massimoGoalSubiti;
    }

    public Integer getMinimoEta() {
        return minimoEta;
    }

    public Integer getMassimoEta() {
        return massimoEta;
    }

    public Squadra getSquadra() {
        return squadra;
    }

    public static class Builder {
        private Filtro filtro;

        public Builder() {
            filtro = new Filtro();
        }

        public void buildNome(String nome) {
            filtro.nome = nome;
        }

        public void buildRuolo(String ruolo) {
            filtro.ruolo = ruolo;
        }

        public void buildPiede(Piede piede) {
            filtro.piede = piede;
        }

        public void buildMinimoGoalSegnati(Integer minimoGoalSegnati) {
            filtro.minimoGoalSegnati = minimoGoalSegnati;
        }

        public void buildMassimoGoalSegnati(Integer massimoGoalSegnati) {
            filtro.massimoGoalSegnati = massimoGoalSegnati;
        }

        public void buildMinimoGoalSubiti(Integer minimoGoalSubiti) {
            filtro.minimoGoalSubiti = minimoGoalSubiti;
        }

        public void buildMassimoGoalSubiti(Integer massimoGoalSubiti) {
            filtro.massimoGoalSubiti = massimoGoalSubiti;
        }

        public void buildMinimoEta(Integer minimoEta) {
            filtro.minimoEta = minimoEta;
        }

        public void buildMassimoEta(Integer massimoEta) {
            filtro.massimoEta = massimoEta;
        }

        public void buildSquadra(Squadra squadra) {
            filtro.squadra = squadra;
        }

        public Filtro getResult() {
            Filtro filtro = this.filtro;
            this.filtro = new Filtro();
            return filtro;
        }
    }
}
