package Model;

import java.util.List;

public class MilitanzaPortiere extends Militanza {
    private int goalSubiti;

    public MilitanzaPortiere(Squadra squadra, int partiteGiocate, int goalSegnati, int goalSubiti, List<Periodo> periodi) {
        super(squadra, partiteGiocate, goalSegnati, periodi);
        if (goalSubiti < 0) {
            throw new IllegalArgumentException("Goal subiti negativi!");
        }
        this.goalSubiti = goalSubiti;
    }

    public int getGoalSubiti() {
        return goalSubiti;
    }

    public void setGoalSubiti(int goalSubiti) {
        this.goalSubiti = goalSubiti;
    }
}
