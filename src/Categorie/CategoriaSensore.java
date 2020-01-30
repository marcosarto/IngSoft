package Categorie;

import java.io.Serializable;
import java.util.HashMap;

public class CategoriaSensore extends CategoriaDispositivo implements Serializable {
    private HashMap<String,Rilevazione> informazioni = new HashMap<>();

    public CategoriaSensore(String nome) {
        super(nome);
    }

    public void setInformazioni(Rilevazione informazioni) {
        this.informazioni.put(informazioni.getNome(),informazioni);
    }

    public String getValore(){
        StringBuilder str = new StringBuilder();
        for(Rilevazione r : informazioni.values()) {
            str.append("Rilevazione " + r.getNome() +" con valore : "+r.getValore()+"\n");
        }
        return str.toString();
    }

    public Rilevazione getRilevazione(String nome){
        if(informazioni.containsKey(nome))
            return informazioni.get(nome);
        else
            return null;
    }
}
