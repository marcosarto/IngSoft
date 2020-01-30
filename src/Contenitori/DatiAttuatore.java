package Contenitori;

import java.io.Serializable;
import java.util.HashMap;

public class DatiAttuatore implements Serializable {
    private HashMap<String,Integer> valoriParametri = new HashMap<>();
    private String statoCorrente;
    private boolean aStati;

    public Integer ritornaValoreParametri(String par){
        if(valoriParametri.containsKey(par))
            return valoriParametri.get(par);
        else
            return -1;
    }

    public String getStatoCorrente() {
        return statoCorrente;
    }

    public void setValoriParametri(String nome,int val) {
        valoriParametri.put(nome,val);
    }

    public void setStatoCorrente(String statoCorrente) {
        this.statoCorrente = statoCorrente;
    }

    public void setaStati(boolean aStati) {
        this.aStati = aStati;
    }
}
