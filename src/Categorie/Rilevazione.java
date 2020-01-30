package Categorie;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Rilevazione implements Serializable {
    private String nome,unitaDiMisura;
    private int minimo,massimo;
    private ArrayList<String> stati = new ArrayList<>();
    boolean massimoMinimo;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUnitaDiMisura(String unitaDiMisura) {
        this.unitaDiMisura = unitaDiMisura;
    }

    public void setMinimo(int minimo) {
        this.minimo = minimo;
        massimoMinimo = true;
    }

    public void setMassimo(int massimo) {
        this.massimo = massimo;
    }

    public boolean isMassimoMinimo() {
        return massimoMinimo;
    }

    public void aggiungiStato(String stato){
        stati.add(stato);
        massimoMinimo = false;
    }

    public String getValore() {
        if(massimoMinimo)
            return generaValore()+unitaDiMisura;
        else
            return generaStato();
    }

    public String getValoreSenzaUnitaMisura(){
        if(massimoMinimo)
            return ""+generaValore();
        else
            return generaStato();
    }

    public String getNome() {
        return nome;
    }

    private int generaValore(){
        return ThreadLocalRandom.current().nextInt(minimo, massimo + 1);
    }

    private String generaStato(){
        int numrandom = ThreadLocalRandom.current().nextInt(0, stati.size());
        return stati.get(numrandom);
    }
}
