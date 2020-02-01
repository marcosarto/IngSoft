package DipendenteDalTempo;

import java.util.*;

public class Orologio implements Runnable{
    private static Calendar calendar;
    private Thread t = new Thread(this);
    private static HashMap<Orario,Conseguente> conseguenti = new HashMap<>();

    void startT(){
        t.start();
    }

    void terminaThread(){
        System.exit(0);
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(30000);
                calendar = Calendar.getInstance();
                int ora = calendar.get(Calendar.HOUR_OF_DAY);
                int minuto = calendar.get(Calendar.MINUTE);
                for(Orario o : conseguenti.keySet()){
                    if(o.isOra(ora,minuto)) {
                        conseguenti.get(o).aziona();
                        conseguenti.remove(o);
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }while(true);
    }

    public static void registrati(Conseguente c,Orario orario){
        Orario o = orario;
        conseguenti.put(o,c);
    }

    static Orario getOra(){
        calendar = Calendar.getInstance();
        return new Orario(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
    }
}
