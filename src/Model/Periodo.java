package Model;

import java.util.Date;

public record Periodo(Date dataInizio, Date dataFine) {
    public Periodo {
        if (dataInizio == null || dataFine == null) {
            throw new IllegalArgumentException("dataInizio o dataFine sono null");
        }
        if (dataInizio.getTime() > dataFine.getTime()) {
            throw new IllegalArgumentException("dataInizio deve essere precedente a dataFine");
        }
    }
}
