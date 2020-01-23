package Categorie;

import java.util.ArrayList;

public class CategoriaSensore extends CategoriaDispositivo{
    private ArrayList<Rilevazione> informazioni = new ArrayList<>();

    public CategoriaSensore(String nome) {
        super(nome);
    }

    public void setInformazioni(Rilevazione informazioni) {
        this.informazioni.add(informazioni);
    }

    public String getValore(){
        return informazioni.get(0).getValore();
    }
}
