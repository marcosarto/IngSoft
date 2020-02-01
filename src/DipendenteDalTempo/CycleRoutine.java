package DipendenteDalTempo;

import Applicazione.UnitaImmobiliare;
import Utility.Interazione;

import java.util.HashMap;


public class CycleRoutine implements Runnable {
    private Thread t = new Thread(this);
    private UnitaImmobiliare immobile;
    private HashMap<String,Regola> regole = new HashMap<>();
    private HashMap<String,Regola> regoleDisattivate = new HashMap<>();
    private Orologio orologio;

    public CycleRoutine(UnitaImmobiliare immobile) {
        this.immobile = immobile;
        t.start();
        this.orologio = new Orologio();
        orologio.startT();
    }

    public void stampaRegole(){
        for(String s : regole.keySet()){
            System.out.println("ATTIVA - "+s);
        }
        for(String s: regoleDisattivate.keySet()){
            System.out.println("DISATTIVA - "+s);
        }
    }

    public void disattivaRegola(){
        String[] regoleExpr = regole.keySet().toArray(new String[0]);
        int risposta = Interazione.interrogazione("Quale regola vuoi disattivare? : ",
                regoleExpr,true);
        regoleDisattivate.put(regoleExpr[risposta],regole.get(regoleExpr[risposta]));
        regole.remove(regoleExpr[risposta]);
    }

    public void attivaRegola(){
        String[] regoleExpr = regoleDisattivate.keySet().toArray(new String[0]);
        int risposta = Interazione.interrogazione("Quale regola vuoi attivare? : ",
                regoleExpr,true);
        regole.put(regoleExpr[risposta],regoleDisattivate.get(regoleExpr[risposta]));
        regoleDisattivate.remove(regoleExpr[risposta]);
    }

    public void aggiungiRegola(String input){
        try {
            regole.put(input,new Regola(immobile, input));
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public void terminaThread(){
        orologio.terminaThread();
        System.exit(0);
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(5000);
                for (Regola r : regole.values())
                    r.valuta();
            } catch (Exception e) {
                System.out.println("Errore nella routine periodica controllo sensori");
            }
        }while(true);
    }
}
