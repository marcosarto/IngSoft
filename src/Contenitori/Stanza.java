package Contenitori;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class Stanza extends Contenitore implements Serializable {
    private HashMap<String,Artefatto> artefatti = new HashMap<>();

    public Stanza(String nome) {
        super(nome);
    }

    public boolean aggiungiArtefatto(String arteS){
        if(artefatti.containsKey(arteS))
            return false;
        artefatti.put(arteS,new Artefatto(arteS));
        return true;
    }

    public void aggiungiCategoriaSensoriPresentiSuArtifatto(String artefatto,String catSensore){
        if(artefatti.containsKey(artefatto))
            artefatti.get(artefatto).aggiungiCategoriaSensoriPresenti(catSensore);
    }

    public void aggiungiCategoriaAttuatoriPresentiSuArtifatto(String artefatto,String catAttuatore){
        if(artefatti.containsKey(artefatto))
            artefatti.get(artefatto).aggiungiCategoriaAttuatoriPresenti(catAttuatore);
    }

    public boolean aggiungiSensoreSuArtefatto(String arteS,Sensore s){
        return artefatti.get(arteS).aggiungiSensore(s);
    }

    public boolean aggiungiAttuatoreSuArtefatto(String arteS,Attuatore a){
        return artefatti.get(arteS).aggiungiAttuatore(a);
    }

    public Collection<Artefatto> getArtefatti(){
        return artefatti.values();
    }


}
