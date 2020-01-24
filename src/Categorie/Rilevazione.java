package Categorie;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class Rilevazione implements Serializable {
    private String nome,unitaDiMisura;
    private int minimo,massimo;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUnitaDiMisura(String unitaDiMisura) {
        this.unitaDiMisura = unitaDiMisura;
    }

    public void setMinimo(int minimo) {
        this.minimo = minimo;
    }

    public void setMassimo(int massimo) {
        this.massimo = massimo;
    }

    public String getValore() {
        return generaValore()+unitaDiMisura;
    }

    private int generaValore(){
        return ThreadLocalRandom.current().nextInt(minimo, massimo + 1);
    }
}
