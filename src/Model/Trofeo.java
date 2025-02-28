package Model;

import java.util.Date;

public abstract class Trofeo {
    private final String nome;
    private final Date data;

    public Trofeo(String nome, Date data) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome del Trofeo non valido");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data del Trofeo non valida");
        }
        this.nome = nome;
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public Date getData() {
        return data;
    }
}
