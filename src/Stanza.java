import java.util.ArrayList;

public class Stanza {
    private String nome;
    private ArrayList<Artefatto> artefatti;
    private ArrayList<Sensore> sensori;
    private ArrayList<Attuatore> attuatori;
    //non può esistere più di un sensore per categoria associato a ciascuna stanza o
    //artefatto;
    private ArrayList<String> categoriaSensoriPresenti;
    private ArrayList<String> categoriaAttuatoriPresenti;

    public boolean aggiungiArtefatto(String arteS){
        for (Artefatto arteO :
                artefatti) {
            if(arteO.getNome().equals(arteS))
                return false;
        }
        artefatti.add(new Artefatto(arteS));
        return true;
    }

    public Stanza(String nome) {
        this.nome = nome;
    }

    public ArrayList<String> getCategoriaSensoriPresenti() {
        return categoriaSensoriPresenti;
    }

    public ArrayList<String> getCategoriaAttuatoriPresenti() {
        return categoriaAttuatoriPresenti;
    }

    public String getNome() {
        return nome;
    }
}
