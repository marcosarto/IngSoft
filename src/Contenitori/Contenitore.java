package Contenitori;

import java.util.ArrayList;

public abstract class Contenitore {
    protected String nome;
    protected ArrayList<Sensore> sensori;
    protected ArrayList<Attuatore> attuatori;

    //non può esistere più di un sensore per categoria associato a ciascuna stanza o
    //artefatto;
    protected ArrayList<String> categoriaSensoriPresenti;
    protected ArrayList<String> categoriaAttuatoriPresenti;

    public Contenitore(String nome){
        this.nome = nome;
    }

    public ArrayList<String> getCategoriaSensoriPresenti() {
        return categoriaSensoriPresenti;
    }

    public ArrayList<String> getCategoriaAttuatoriPresenti() {
        return categoriaAttuatoriPresenti;
    }

    public String getNome() {
        return nome;
    }
}
