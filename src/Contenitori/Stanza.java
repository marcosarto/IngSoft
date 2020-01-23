package Contenitori;

import java.util.ArrayList;

public class Stanza extends Contenitore{
    private ArrayList<Artefatto> artefatti;

    public Stanza(String nome) {
        super(nome);
    }

    public boolean aggiungiArtefatto(String arteS){
        for (Artefatto arteO :
                artefatti) {
            if(arteO.getNome().equals(arteS))
                return false;
        }
        artefatti.add(new Artefatto(arteS));
        return true;
    }

}
