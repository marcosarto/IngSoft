package Categorie;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoriaSensore extends CategoriaDispositivo implements Serializable {
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
