package Categorie;

public abstract class CategoriaDispositivo {
    protected String nome,descrizione;
    protected boolean stato;

    public String getPrimoCampo(){
        // Primo elemento della descizione e` unita di misura per sensore e default modalita operativa
        return descrizione.split(",")[0];
    }
}
