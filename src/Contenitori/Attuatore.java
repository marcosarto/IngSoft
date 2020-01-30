package Contenitori;

import Categorie.CategoriaAttuatore;
import Categorie.ModalitaOperativa;
import Categorie.Parametro;

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
            if(!m.isAStati()){
                for(Parametro p : m.getParametri().values()){
                    d.setValoriParametri(p.getNome(),p.getValoreDesiderato());
                }
            }
            datiAttuali.put(m.getNome(),d);
        }
    }


    public String getStatoCorrente(String mod) {
        return datiAttuali.get(mod).getStatoCorrente();
    }

    public void setStatoCorrente(String mod,String statoCorrente) {
        datiAttuali.get(mod).setStatoCorrente(statoCorrente);
    }

    public HashMap<String, DatiAttuatore> getDatiAttuali() {
        return datiAttuali;
    }
}
