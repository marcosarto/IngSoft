package Contenitori;

import java.util.ArrayList;

public abstract class Contenitore {
    protected String nome;
    protected ArrayList<Sensore> sensori=new ArrayList<>();
    protected ArrayList<Attuatore> attuatori = new ArrayList<>();
    //non può esistere più di un sensore per categoria associato a ciascuna stanza o
    //artefatto;
    protected ArrayList<String> categoriaSensoriPresenti = new ArrayList<>();
    protected ArrayList<String> categoriaAttuatoriPresenti = new ArrayList<>();

    public Contenitore(String nome){
        this.nome = nome;
    }

    public boolean aggiungiSensore(Sensore s){
        if(categoriaSensoriPresenti.contains(s.getCategoria().getNome()))
            return false;
        sensori.add(s);
        categoriaSensoriPresenti.add(s.categoria.getNome());
        return true;
    }

    public boolean aggiungiAttuatore(Attuatore a){
        if(categoriaAttuatoriPresenti.contains(a.getCategoria().getNome()))
            return false;
        attuatori.add(a);
        categoriaAttuatoriPresenti.add(a.getCategoria().getNome());
        return true;
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
