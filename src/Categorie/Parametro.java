package Categorie;

import java.io.Serializable;

public class Parametro implements Serializable {
    private String nome;
    private int valoreDesiderato;

    public Parametro(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getValoreDesiderato() {
        return valoreDesiderato;
    }

    public void setValoreDesiderato(int valoreDesiderato) {
        this.valoreDesiderato = valoreDesiderato;
    }
}
