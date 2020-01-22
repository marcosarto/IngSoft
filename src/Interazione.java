import java.util.Scanner;

public class Interazione {
    private static Scanner in = new Scanner(System.in);

    public static int interrogaUtente(String[] entrate) {
        boolean quit = false;
        int menuItem;

        for (int i = 1; i <= entrate.length; i++)
            System.out.println(i + ". " + entrate[i-1] + i);
        System.out.println("0. Esci");

        do {
            System.out.print("Seleziona entrata : ");
            menuItem = in.nextInt();
            if (menuItem>=0 && menuItem<=entrate.length)
                return menuItem;


        } while (!quit);
        return -1;
    }
}
