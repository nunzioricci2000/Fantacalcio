package Model;

import java.util.Date;

public class TrofeoDiSquadra extends Trofeo {
    private final Squadra squadra;

    public TrofeoDiSquadra(String nome, Date data, Squadra squadra) {
        super(nome, data);
        if (squadra == null) {
            throw new IllegalArgumentException("squadra is null");
        }
        this.squadra = squadra;
    }

    public Squadra getSquadra() {
        return squadra;
    }
}
