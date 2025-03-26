package Model;

import java.util.ArrayList;
import java.util.List;

public class Militanza {
    private final Squadra squadra;
    private int partiteGiocate;
    private int goalSegnati;
    private final ArrayList<Periodo> periodi = new ArrayList<>();

    public Militanza(Squadra squadra, int partiteGiocate, int goalSegnati, List<Periodo> periodi) {
        if (partiteGiocate < 0 || goalSegnati < 0) {
            throw new IllegalArgumentException("Invalid negative parameters");
        }
        if (periodi == null) {
            throw new IllegalArgumentException("periodi is null");
        }
        if (periodi.isEmpty()) {
            throw new IllegalArgumentException("periodi is empty");
        }
        this.squadra = squadra;
        this.partiteGiocate = partiteGiocate;
        this.goalSegnati = goalSegnati;
        this.periodi.addAll(periodi);
    }

    public Squadra getSquadra() {
        return squadra;
    }

    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    public void setPartiteGiocate(int partiteGiocate) {
        this.partiteGiocate = partiteGiocate;
    }

    public int getGoalSegnati() {
        return goalSegnati;
    }

    public void setGoalSegnati(int goalSegnati) {
        this.goalSegnati = goalSegnati;
    }

    public List<Periodo> getPeriodi() {
        return new ArrayList<>(this.periodi);
    }

    public void setPeriodi(List<Periodo> periodi) {
        this.periodi.removeIf(_ -> true);
        this.periodi.addAll(periodi);
    }
}
