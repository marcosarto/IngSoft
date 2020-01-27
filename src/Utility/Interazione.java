package Utility;

import java.util.Scanner;

public class Interazione {
    private static Scanner in = new Scanner(System.in);
    public static final String DELIMITATORE = "---------------------------------------------------------------------------------------";
    public static final String NEW_LINE = "";


    public static int interrogazione(String domanda,String[] entrate,boolean uscitaPossibile){
        int menuItem=-1;
        boolean condizioneUscita;
        if(uscitaPossibile)
            condizioneUscita = !(menuItem>=0&&menuItem<=entrate.length);
        else
            condizioneUscita = !(menuItem>0&&menuItem<=entrate.length);
        System.out.println(DELIMITATORE);
        if(uscitaPossibile)
            System.out.println("0. Uscita");
        for (int i = 1; i <= entrate.length; i++)
            System.out.println(i + ". " + entrate[i-1]);

        do {
            System.out.print(domanda+" ");
            try {
                String risposta;
                do {
                    risposta = in.nextLine();
                }while (risposta.equals(NEW_LINE));
                menuItem = Integer.parseInt(risposta);
            }catch(Exception e){
                System.out.println("Inserisci un numero nel range possibile grazie");
            }
            System.out.println(DELIMITATORE);

        } while (!(menuItem>=0 && menuItem<=entrate.length));

        return menuItem-1;
    }

    public static String domanda(String domanda){
        System.out.println(DELIMITATORE);
        System.out.println(domanda+" ");
        String risposta;
        do {
            risposta = in.nextLine();
        }while (risposta.equals(NEW_LINE));
        System.out.println(DELIMITATORE);
        return risposta;
    }
}
