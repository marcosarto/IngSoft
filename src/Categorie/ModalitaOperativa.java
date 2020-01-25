package Categorie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ModalitaOperativa implements Serializable {
    private String nome;
    private ArrayList<String> stati = new ArrayList<>();
    private HashMap<String,Parametro> parametri = new HashMap<>();
    private boolean aStati;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void aggiungiStato(String stato){
        stati.add(stato);
        aStati = true;
    }

    public void addParametro(String nome ,Parametro p){
        parametri.put(nome,p);
        aStati=false;
    }
}
