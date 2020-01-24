package Categorie;

import java.io.Serializable;
import java.util.ArrayList;

public class ModalitaOperativa implements Serializable {
    private String nome;
    private ArrayList<String> stati = new ArrayList<>();
    //private int parametro; // non prima versione

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void aggiungiStato(String stato){
        stati.add(stato);
    }
}
