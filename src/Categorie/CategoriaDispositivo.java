package Categorie;

import java.io.Serializable;

public abstract class CategoriaDispositivo implements Serializable {
    protected String nome,descrizione;
    protected boolean stato;

    public CategoriaDispositivo(String nome) {
        this.nome = nome;
        stato = true; //prima versione sempre accesi
    }

    public String getUnitaMisura(int pos){
        if(pos>=descrizione.split(",").length)
            System.out.println("Non e` stata inserita nessuna unita` di misura di default");
        return descrizione.split(",")[pos];
    }

    public String getNome() {
        return nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
