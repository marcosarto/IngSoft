package DipendenteDalTempo;

import Contenitori.DatiAttuatore;

public class Conseguente{
    DatiAttuatore dati;
    String statoVoluto,nomeParametro;
    int valParametro;
    boolean aStati = false;

    public Conseguente(DatiAttuatore dati,String statoVoluto){
        aStati = true;
        this.dati = dati;
        this.statoVoluto = statoVoluto;
    }

    public Conseguente(DatiAttuatore dati,String nomeParametro,int valParametro){
        this.nomeParametro = nomeParametro;
        this.valParametro = valParametro;
        this.dati = dati;
        aStati = false;
    }

    public void aziona(){
        if(aStati)
            dati.setStatoCorrente(statoVoluto);
        else{
            dati.setValoriParametri(nomeParametro,valParametro);
        }
    }

}
