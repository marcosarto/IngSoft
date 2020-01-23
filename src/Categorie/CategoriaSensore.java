package Categorie;

import java.util.ArrayList;

public class CategoriaSensore extends CategoriaDispositivo{
    private ArrayList<Rilevazione> informazioni;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setInformazioni(ArrayList<Rilevazione> informazioni) {
        this.informazioni = informazioni;
    }
}
