package Contenitori;

import Categorie.CategoriaAttuatore;
import Categorie.ModalitaOperativa;

import java.io.Serializable;
import java.util.HashMap;

public class Attuatore extends Dispositivo implements Serializable {

    private HashMap<String,DatiAttuatore> datiAttuali = new HashMap<>();

    public Attuatore(CategoriaAttuatore categoria, String nome) {
        super(categoria, nome);
        riempiDatiAttuali();
    }

    private void riempiDatiAttuali(){
        HashMap<String, ModalitaOperativa> mod = ((CategoriaAttuatore)categoria).getModalitaOperativa();
        for(ModalitaOperativa m : mod.values()){
            DatiAttuatore d = new DatiAttuatore();
            d.setaStati(m.isAStati());
            if(!m.isAStati())
                d.setLenghtParametri(m.getLenghtParametri());
            datiAttuali.put(m.getNome(),d);
        }
    }

    public Integer[] ritornaValoreParametri(String mod){
        return datiAttuali.get(mod).ritornaValoreParametri();
    }

    public String getStatoCorrente(String mod) {
        return datiAttuali.get(mod).getStatoCorrente();
    }

    public void setValoriParametri(String mod,int pos,int val) {
        datiAttuali.get(mod).setValoriParametri(pos,val);
    }

    public void setStatoCorrente(String mod,String statoCorrente) {
        datiAttuali.get(mod).setStatoCorrente(statoCorrente);
    }

}
