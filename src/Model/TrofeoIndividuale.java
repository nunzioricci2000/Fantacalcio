package Model;

import java.util.Date;

public class TrofeoIndividuale extends Trofeo {
    private final Calciatore calciatore;

    public TrofeoIndividuale(String nome, Date data, Calciatore calciatore) {
        super(nome, data);
        if (calciatore == null) {
            throw new IllegalArgumentException("calciatore is null");
        }
        this.calciatore = calciatore;
    }

    public Calciatore getCalciatore() {
        return calciatore;
    }
}
