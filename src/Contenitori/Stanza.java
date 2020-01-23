package Contenitori;

import java.util.ArrayList;

public class Stanza extends Contenitore{
    private ArrayList<Artefatto> artefatti = new ArrayList<>();

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

    public boolean aggiungiSensoreSuArtefatto(int posArtefatto,Sensore sensore){
        return artefatti.get(posArtefatto).aggiungiSensore(sensore);
    }

    public boolean aggiungiAttuatoreSuArtefatto(int posArtefatto,Attuatore attuatore){
        return artefatti.get(posArtefatto).aggiungiAttuatore(attuatore);
    }
    
    public String[] visualizzaArtefatti(){
        String[] array = new String[artefatti.size()];
        for(int i=0;i<artefatti.size();i++){
            array[i]=artefatti.get(i).getNome();
        }
        return array;
    }

}
