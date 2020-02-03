package Contenitori;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Contenitore implements Serializable {
    protected String nome;
    protected HashMap<String,Sensore> sensori=new HashMap<>();
    protected HashMap<String,Attuatore> attuatori = new HashMap<>();
    //non può esistere più di un sensore per categoria associato a ciascuna stanza o
    //artefatto;
    protected ArrayList<String> categoriaSensoriPresenti = new ArrayList<>();
    protected ArrayList<String> categoriaAttuatoriPresenti = new ArrayList<>();

    public Contenitore(String nome){
        this.nome = nome;
    }

    public boolean aggiungiSensore(Sensore s){
        if(categoriaSensoriPresenti.contains(s.getCategoria().getNome()))
            return false;
        sensori.put(s.getNome()+"_"+s.getCategoria().getNome().trim(),s);
        categoriaSensoriPresenti.add(s.categoria.getNome());
        return true;
    }

    public boolean aggiungiAttuatore(Attuatore a){
        if(categoriaAttuatoriPresenti.contains(a.getCategoria().getNome()))
            return false;
        attuatori.put(a.getNome()+"_"+a.getCategoria().getNome().trim(),a);
        categoriaAttuatoriPresenti.add(a.getCategoria().getNome());
        return true;
    }

    public ArrayList<String> getCategoriaSensoriPresenti() {
        return categoriaSensoriPresenti;
    }

    public ArrayList<String> getCategoriaAttuatoriPresenti() {
        return categoriaAttuatoriPresenti;
    }

    public void aggiungiCategoriaSensoriPresenti(String catSensore){
        if(!categoriaSensoriPresenti.contains(catSensore))
            categoriaSensoriPresenti.add(catSensore);
    }

    public void aggiungiCategoriaAttuatoriPresenti(String catAttuatore){
        if(!categoriaAttuatoriPresenti.contains(catAttuatore))
            categoriaAttuatoriPresenti.add(catAttuatore);
    }

    public String getNome() {
        return nome;
    }

    public String getSensori(){
        StringBuilder st = new StringBuilder();
        for (String s : sensori.keySet()) {
            if (sensori.get(s).isAttivo())
                st.append("ATTIVO - "+s + "\n");
            else
                st.append("DISATTIVO - "+s + "\n");
        }
        return st.toString();
    }

    public String getAttuatori(){
        StringBuilder st = new StringBuilder();
        for (String s : attuatori.keySet()) {
            if (attuatori.get(s).isAttivo())
                st.append("ATTIVO - "+s + "\n");
            else
                st.append("DISATTIVO - "+s+"\n");
        }
        return st.toString();
    }

    public String ritornaValoreSensore(String key){
        if(sensori.containsKey(key))
            if(sensori.get(key).isAttivo())
                return sensori.get(key).getValore();
            else{
                System.out.println("Il sensore e` disattivo, non produce output");
                return "";
            }
        else return "";
    }

    public Sensore ritornaRiferimentoSensore(String nome){
        if(sensori.containsKey(nome))
            return sensori.get(nome);
        else
            return null;
    }

    public Attuatore ritornaRiferimentoAttuatore(String nome){
        if(attuatori.containsKey(nome))
            return attuatori.get(nome);
        else
            return null;
    }

    public boolean almenoUnAttuatore(boolean attivo){
        for(Attuatore a: attuatori.values()){
            if(attivo){
                if(a.isAttivo())
                    return true;
            }
            else
            if(!a.isAttivo())
                return true;
        }
        return false;
    }

    public boolean almenoUnSensore(boolean attivo){
        for(Sensore a: sensori.values()){
            if(attivo){
                if(a.isAttivo())
                    return true;
            }
            else
            if(!a.isAttivo())
                return true;
        }
        return false;
    }
}
