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

        public Builder() {}

        void buildNome(String nome) {
            filtro.nome = nome;
        }

        void buildRuolo(String ruolo) {
            filtro.ruolo = ruolo;
        }

        void buildPiede(Piede piede) {
            filtro.piede = piede;
        }

        void buildMinimoGoalSegnati(Integer minimoGoalSegnati) {
            filtro.minimoGoalSegnati = minimoGoalSegnati;
        }

        void buildMinimoGoalSubiti(Integer minimoGoalSubiti) {
            filtro.minimoGoalSubiti = minimoGoalSubiti;
        }

        void buildMassimoGoalSubiti(Integer massimoGoalSubiti) {
            filtro.massimoGoalSubiti = massimoGoalSubiti;
        }

        void buildMinimoEta(Integer minimoEta) {
            filtro.minimoEta = minimoEta;
        }

        void buildMassimoEta(Integer massimoEta) {
            filtro.massimoEta = massimoEta;
        }

        void buildSquadra(Squadra squadra) {
            filtro.squadra = squadra;
        }

        Filtro getResult() {
            Filtro filtro = this.filtro;
            this.filtro = new Filtro();
            return filtro;
        }
    }
}
