package Model;

public record Squadra(String nome, String nazionalita) {
    public Squadra {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome Squadra nullo");
        }
        if (nazionalita == null || nazionalita.isEmpty()) {
            throw new IllegalArgumentException("Nazionalita Squadra nullo");
        }
    }
}
