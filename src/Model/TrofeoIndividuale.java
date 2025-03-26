package Model;

import java.util.Date;

public class TrofeoIndividuale extends Trofeo {
    private final int idCalciatore;

    public TrofeoIndividuale(String nome, Date data, int idCalciatore) {
        super(nome, data);
        this.idCalciatore = idCalciatore;
    }

    public int getIdCalciatore() {
        return idCalciatore;
    }
}
