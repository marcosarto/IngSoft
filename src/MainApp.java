import Applicazione.SistemaDomotico;
import DipendenteDalTempo.Regola;

import java.io.*;

public class MainApp {
    public static void main(String[] args) {

        SistemaDomotico presenteOggetto;
        String filename = "file.ser";
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            presenteOggetto = (SistemaDomotico)in.readObject();

            in.close();
            file.close();

            System.out.println("Ripristino precedente salvataggio se esistente");
            presenteOggetto.init();
        }

        catch(IOException ex)
        {
            presenteOggetto = new SistemaDomotico();
        }

        catch(ClassNotFoundException ex)
        {
            presenteOggetto = new SistemaDomotico();
        }

        filename = "file.ser";
        try
        {
            //Salvo in un file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Metodo per la serializzazione
            out.writeObject(presenteOggetto);

            out.close();
            file.close();

            System.out.println("Stato salvato");
            presenteOggetto.terminaThreads();

        }

        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
