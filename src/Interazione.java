import java.util.ArrayList;
import java.util.Scanner;

public class Interazione {
    private static Scanner in = new Scanner(System.in);

    public static int interrogazione(String domanda,String[] entrate){
        int menuItem;

        for (int i = 1; i <= entrate.length; i++)
            System.out.println(i + ". " + entrate[i-1] + i);
        System.out.println("0. Esci");

        do {
            System.out.print("Seleziona entrata : ");
            menuItem = in.nextInt();
        } while (!(menuItem>=0 && menuItem<=entrate.length));

        return menuItem-1;
    }

    public static String domanda(String domanda){
        System.out.println(domanda);
        return in.next();
    }

//    public static String[] richiestaRipetuta(String domanda){
//        ArrayList<String> risposte = new ArrayList<>();
//        System.out.println(domanda);
//        risposte.add(in.next());
//        String nuovaRisposta;
//        do{
//            System.out.println("Vuoi aggiungere una nuova entrata? (0 per uscire)");
//            nuovaRisposta = in.next();
//            risposte.add(nuovaRisposta);
//        }while(!nuovaRisposta.equals("0"));
//        risposte.remove(risposte.size()-1);
//        return risposte.toArray(new String[0]);
//    }
}
