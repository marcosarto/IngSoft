import Categorie.CategoriaAttuatore;
import Categorie.CategoriaSensore;
import Contenitori.Artefatto;
import Contenitori.Attuatore;
import Contenitori.Sensore;
import Contenitori.Stanza;
import Utility.Interazione;
import java.util.HashMap;

public class UnitaImmobiliare implements java.io.Serializable{
    private String nomeUnitaImmobiliare, tipoUnitaImmobiliare;
    private HashMap<String, Stanza> stanze = new HashMap<>();
    private HashMap<String, Stanza> artefatti = new HashMap<>();//chiave nome artefatto valore stanza associata
    private SistemaDomotico sistemaDomotico;

    public UnitaImmobiliare() {
        String nome = Interazione.domanda("Nome dell'unita immobiliare?");
        this.nomeUnitaImmobiliare = nome;
        String tipo = Interazione.domanda("Tipologia unita immobiliare?");
        this.tipoUnitaImmobiliare = tipo;
        stanze.put("Esterno", new Stanza("Esterno"));
        int risposta = Interazione.interrogazione
                ("Bisogna aggiungere almeno una stanza o un artefatto," +
                                " (nel seguito se ne potranno aggiungere altri)",
                        new String[]{"Artefatto", "Stanza"});

        switch (risposta) {
            case 0:
                aggiungiArtefatto();
                break;
            case 1:
                aggiungiStanza();
                break;
        }
    }

    public void aggiungiStanza() {
        String risposta = null;
        do {
            String stanza = Interazione.domanda("Inserisci nome stanza : ");

            //Se c'e` gia almeno una stanza controlla che il nome non sia duplicato
            if (stanze.containsKey(stanza)) {
                System.out.println("Nome stanza gia esistente, non aggiunta");
                continue;
            }

            stanze.put(stanza, new Stanza(stanza));

            risposta = Interazione.domanda("Vuoi uscire? (y/any key) : ");

        } while (!risposta.equals("y"));
    }

    public void aggiungiArtefatto() {
        String risposta = null;
        do {

            String artefatto = Interazione.domanda("Inserisci nome artefatto : ");

            String[] elencoStanze = stanze.keySet().toArray(new String[0]);

            int stanzaDaAbbinare = Interazione.interrogazione
                    ("In quale stanza vuoi aggiungere l'artefatto ", elencoStanze);

            if (!stanze.get(elencoStanze[stanzaDaAbbinare]).aggiungiArtefatto(artefatto))
                System.out.println("Artefatto gia esistente in questa stanza");
            else
                artefatti.put(artefatto, stanze.get(elencoStanze[stanzaDaAbbinare]));

            risposta = Interazione.domanda("Vuoi uscire? (y/any key) : ");

        } while (!risposta.equals("y"));

    }

    public void flussoFruitore(SistemaDomotico sistemaDomotico) {
        this.sistemaDomotico = sistemaDomotico;
        int risposta = Interazione.interrogazione("Cosa vuoi fare?",
                new String[]{"Visualizza piantina unita' immobiliare", "Visualizza valori di sensori specifici"});
        switch (risposta) {
            case 0:
                stampaAlberoUnitaImmobiliare();
                break;
            case 1:
                proceduraLetturaSensori();
                break;
        }
    }

    public void flussoManutentore(SistemaDomotico sistemaDomotico) {
        this.sistemaDomotico = sistemaDomotico;
        boolean esci = false;
        do {
            int risposta = Interazione.interrogazione("Cosa vuoi fare (qualsiasi altro tasto per uscire) :",
                    new String[]{"Aggiungi stanze",
                            "Aggiungi artefatti",
                            "Aggiungi sensore",
                            "Aggiungi attuatore",
                            "Visualizza albero associazioni"});
            switch (risposta) {
                case 0:
                    aggiungiStanza();
                    break;
                case 1:
                    aggiungiArtefatto();
                    break;
                case 2:
                    aggiungiSensore();
                    break;
                case 3:
                    aggingiAttuatore();
                    break;
                case 4:
                    stampaAlberoUnitaImmobiliare();
                    break;
                default:
                    esci = true;
                    break;
            }
        } while (!esci);
    }

    private void aggiungiSensore() {
        int risposta = Interazione.interrogazione("Dove vuoi posizionare il sensore?",
                new String[]{"Artefatto", "Stanza"});
        if (risposta == 0) {
            StringBuilder st = new StringBuilder();
            st.append("Scegli il l'artefatto (inserisci il nome) : "+"\n");
            for (String s : artefatti.keySet()) {
                st.append(s + "\n");
            }
            String artefattoScelto = Interazione.domanda(st.toString());
            if(!artefatti.containsKey(artefattoScelto))
                System.out.println("Nome non corretto o non esistenete");
            else{
                if(!artefatti.get(artefattoScelto).aggiungiSensore(creaSensore()))
                    System.out.println("C'e` gia un sensore di questa categoria!");
            }

        } else {
            String[] elencoStanze = stanze.keySet().toArray(new String[0]);
            risposta = Interazione.interrogazione("In quale stanza? ", elencoStanze);
            if(elencoStanze[risposta].equals("Esterno"))
                System.out.println("Non si puo` aggiungere un sensore all'esterno senza un artefatto");
            else if(!stanze.get(elencoStanze[risposta]).aggiungiSensore(creaSensore()))
                System.out.println("C'e` gia un sensore di questa categoria!");
        }

    }

    private void aggingiAttuatore() {
        int risposta = Interazione.interrogazione("Dove vuoi posizionare l'attuatore?",
                new String[]{"Artefatto", "Stanza"});
        if (risposta == 0) {
            StringBuilder st = new StringBuilder();
            st.append("Scegli il l'artefatto (inserisci il nome) : "+"\n");
            for (String s : artefatti.keySet()) {
                st.append(s + "\n");
            }
            String artefattoScelto = Interazione.domanda(st.toString());
            if(!artefatti.containsKey(artefattoScelto))
                System.out.println("Nome non corretto o non esistenete");
            else{
                if(!artefatti.get(artefattoScelto).aggiungiAttuatore(creaAttuatore()))
                    System.out.println("C'e` gia un'attuatore di questa categoria!");
            }

        } else {
            String[] elencoStanze = stanze.keySet().toArray(new String[0]);
            risposta = Interazione.interrogazione("In quale stanza? ", elencoStanze);
            if(elencoStanze[risposta].equals("Esterno"))
                System.out.println("Non si puo` aggiungere un attuatore all'esterno senza un artefatto");
            else if(!stanze.get(elencoStanze[risposta]).aggiungiAttuatore(creaAttuatore()))
                System.out.println("C'e` gia un attuatore di questa categoria!");
        }
    }

    private Sensore creaSensore() {
        String nome = Interazione.domanda("Nome fantasia sensore");
        String[] nomiCat = new String[sistemaDomotico.getCategorieSensori().size()];
        for (int i = 0; i < nomiCat.length; i++) {
            nomiCat[i] = sistemaDomotico.getCategorieSensori().get(i).getNome();
        }
        int cat = Interazione.interrogazione("Scegli la categoria del sensore", nomiCat);
        return new Sensore((CategoriaSensore) sistemaDomotico.getCategorieSensori().get(cat), nome);
    }

    private Attuatore creaAttuatore() {
        String nome = Interazione.domanda("Nome fantasia attuatore");
        String[] nomiCat = new String[sistemaDomotico.getCategorieAttuatori().size()];
        for (int i = 0; i < sistemaDomotico.getCategorieAttuatori().size(); i++) {
            nomiCat[i] = sistemaDomotico.getCategorieAttuatori().get(i).getNome();
        }
        int cat = Interazione.interrogazione("Scegli la categoria dell'attuatore", nomiCat);
        return new Attuatore((CategoriaAttuatore) sistemaDomotico.getCategorieAttuatori().get(cat), nome);
    }

    private void proceduraLetturaSensori() {
        for (Stanza s : stanze.values()) {
            System.out.println(s.getSensori());
        }
    }

    private void stampaAlberoUnitaImmobiliare() {

        StringBuilder tree = new StringBuilder();
        tree.append("Nome unita immobiliare :" + nomeUnitaImmobiliare + "\n\n");
        for (Stanza s : stanze.values()) {
            tree.append("\tCategorie sensori nella stanza " + s.getNome() + "\n");
            for (String sensore : s.getCategoriaSensoriPresenti()) {
                tree.append("\t\t" + sensore + "\n");
            }
            tree.append("\n");
            tree.append("\tCategorie attuatori nella stanza " + s.getNome() + "\n");
            for (String attuatore : s.getCategoriaAttuatoriPresenti()) {
                tree.append("\t\t" + attuatore + "\n");
            }
            tree.append("\n");
            for (Artefatto a : s.getArtefatti()) {
                tree.append("\t\tCategorie sensori dell'artefatto " + a.getNome() + " nella stanza\n");
                for (String sensore : s.getCategoriaSensoriPresenti()) {
                    tree.append("\t\t\t" + sensore + "\n");
                }
                tree.append("\n");
                tree.append("\t\tCategorie attuatori dell'artefatto " + a.getNome() + " nella stanza\n");
                for (String attuatore : s.getCategoriaAttuatoriPresenti()) {
                    tree.append("\t\t\t" + attuatore + "\n");
                }
            }
        }
        System.out.println(tree.toString());
    }

    public String getNomeUnitaImmobiliare() {
        return nomeUnitaImmobiliare;
    }
}

