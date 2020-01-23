package Categorie;
import java.util.Random;

public class Rilevazione {
    private String nome,unitaDiMisura;
    private int valore,minimo,massimo;
    private Random rand = new Random();

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
        return rand.nextInt(50);
    }
}
