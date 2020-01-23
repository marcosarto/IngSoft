package Utility;

import java.util.Scanner;

public class Interazione {
    private static Scanner in = new Scanner(System.in);

    public static int interrogazione(String domanda,String[] entrate){
        int menuItem;

        for (int i = 1; i <= entrate.length; i++)
            System.out.println(i + ". " + entrate[i-1]);

        do {
            System.out.print(domanda);
            menuItem = Integer.valueOf(in.nextLine());
        } while (!(menuItem>=0 && menuItem<=entrate.length));

        return menuItem-1;
    }

    public static String domanda(String domanda){
        System.out.println(domanda);
        return in.nextLine();
    }
}
