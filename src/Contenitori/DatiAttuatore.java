package Contenitori;

import java.io.Serializable;

public class DatiAttuatore implements Serializable {
    private Integer[] valoriParametri;
    private String statoCorrente;
    private boolean aStati;

    public Integer[] ritornaValoreParametri(){
        return valoriParametri;
    }

    public String getStatoCorrente() {
        return statoCorrente;
    }

    public void setValoriParametri(int pos,int val) {
        valoriParametri[pos]=val;
    }

    public void setLenghtParametri(int l){
        valoriParametri = new Integer[l];
    }

    public void setStatoCorrente(String statoCorrente) {
        this.statoCorrente = statoCorrente;
    }

    public void setaStati(boolean aStati) {
        this.aStati = aStati;
    }
}
