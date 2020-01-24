package Categorie;

import java.io.Serializable;

public abstract class CategoriaDispositivo implements Serializable {
    protected String nome,descrizione;
    protected boolean stato;

    public CategoriaDispositivo(String nome) {
        this.nome = nome;
        stato = true; //prima versione sempre accesi
    }

    public String getPrimoCampo(){
        // Primo elemento della descizione e` unita di misura per sensore e default modalita operativa
        return descrizione.split(",")[0];
    }

    public String getNome() {
        return nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
