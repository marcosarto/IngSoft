package Categorie;

public abstract class CategoriaDispositivo {
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
