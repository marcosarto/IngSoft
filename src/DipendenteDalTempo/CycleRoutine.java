package DipendenteDalTempo;

import Applicazione.UnitaImmobiliare;

import java.util.ArrayList;


public class CycleRoutine implements Runnable {
    private Thread t = new Thread(this);
    private UnitaImmobiliare immobile;
    private ArrayList<Regola> regole = new ArrayList<>();

    public CycleRoutine(UnitaImmobiliare immobile) {
        this.immobile = immobile;
        t.start();
    }

    public void aggiungiRegola(String input){
        try {
            regole.add(new Regola(immobile, input));
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(5000);
                for (Regola r : regole)
                    r.valuta();
            } catch (Exception e) {
                System.out.println("Errore nella routine periodica controllo sensori");
            }
        }while(true);
    }
}
