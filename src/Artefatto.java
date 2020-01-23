import java.util.ArrayList;

public class Artefatto {
    private String nome;
    private ArrayList<Attuatore> attuatori;
    private ArrayList<Sensore> sensori;
    //non può esistere più di un sensore per categoria associato a ciascuna stanza o
    //artefatto;
    private ArrayList<String> categoriaSensoriPresenti;
    private ArrayList<String> categoriaAttuatoriPresenti;

    public Artefatto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
