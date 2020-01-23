import Categorie.CategoriaAttuatore;
import Categorie.CategoriaSensore;
import Contenitori.Attuatore;
import Contenitori.Sensore;
import Contenitori.Stanza;
import Utility.Interazione;

import java.util.ArrayList;

public class UnitaImmobiliare {
    private String nomeUnitaImmobiliare, tipoUnitaImmobiliare;
    private ArrayList<Stanza> stanze = new ArrayList<>();
    private Stanza esterno = new Stanza("Esterno");

    public UnitaImmobiliare() {
        String nome = Interazione.domanda("Nome dell'unita immobiliare?");
        this.nomeUnitaImmobiliare = nome;
        String tipo = Interazione.domanda("Tipologia unita immobiliare?");
        this.tipoUnitaImmobiliare = tipo;
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
        do {
            String stanza = Interazione.domanda("Inserisci nome stanza(0 per uscire) : ");
            if (stanza.equals("0")) break;


            boolean esiste = false;

            //Se c'e` gia almeno una stanza controlla che il nome non sia duplicato
            if (stanze.size() != 0) {
                for (Stanza s :
                        stanze) {
                    if (s.getNome().equals(stanza))
                        esiste = true;
                }
                if (esiste) {
                    System.out.println("Nome stanza gia esistente, non aggiunta");
                    continue;
                }
            }

            if (!esiste) stanze.add(new Stanza(stanza));

        } while (true);
    }

    public void aggiungiArtefatto() {
        do {
            String artefatto = Interazione.domanda("Inserisci nome artefatto(0 per uscire) : ");
            if (artefatto.equals("0")) break;

            int stanzaDaAbbinare = Interazione.interrogazione
                    ("In quale stanza vuoi aggiungere l'artefatto " +
                                    "(se non si seleziona nessuna stanza verra` abbinato all'esterno)",
                            stanze.toArray(new String[0])
                    );
            switch (stanzaDaAbbinare) {
                case -1:
                    if (!esterno.aggiungiArtefatto(artefatto))
                        System.out.println("Artefatto con questo nome gia` esistente nell'esterno");
                    break;
                default:
                    boolean successo = stanze.get(stanzaDaAbbinare).aggiungiArtefatto(artefatto);
                    if (!successo)
                        System.out.println("Il nome dell'artefatto esiste gia` in questa stanza");
            }

        } while (true);

    }

    public void flussoFruitore() {
        int risposta = Interazione.interrogazione("Cosa vuoi fare?",
                new String[]{"Visualizza piantina unita' immobiliare", "Visualizza valori di sensori specifici"});
        switch (risposta) {
            case 0:
                stampaAlberoUnitaImmobiliare();
                break;
            case 1:
                //proceduraLetturaSensori();
                break;
        }
    }

    public void flussoManutentore() {
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
            risposta = Interazione.interrogazione("Su quale artefatto? " +
                            "(Siamo nell` esterno della abitazione se non e` questo il luogo che " +
                            "contiene l'artefatto voluto passare alla prossima con 0)",
                    esterno.visualizzaArtefatti());
            if (risposta != -1) {
                esterno.aggiungiSensoreSuArtefatto(risposta, creaSensore());
            }
            for (Stanza s :
                    stanze) {
                risposta = Interazione.interrogazione("Su quale artefatto? " +
                                "(Siamo nella stanza " + s.getNome() + " se non e` questa la stanza che " +
                                "contiene l'artefatto voluto passare alla prossima con 0)",
                        s.visualizzaArtefatti());
                if (risposta != -1) {
                    s.aggiungiSensoreSuArtefatto(risposta, creaSensore());
                }
            }
        } else {
            String[] nomiStanze = new String[stanze.size()];
            for (int i = 0; i < stanze.size(); i++) {
                nomiStanze[i] = stanze.get(i).getNome();
            }
            risposta = Interazione.interrogazione("In quale stanza? ", nomiStanze);
            if (risposta != -1)
                stanze.get(risposta).aggiungiSensore(creaSensore());
        }
    }

    private void aggingiAttuatore(){
        int risposta = Interazione.interrogazione("Dove vuoi posizionare l'attuatore?",
                new String[]{"Artefatto", "Stanza"});
        if (risposta == 0) {
            risposta = Interazione.interrogazione("Su quale artefatto? " +
                            "(Siamo nell` esterno della abitazione se non e` questo il luogo che " +
                            "contiene l'artefatto voluto passare alla prossima con 0)",
                    esterno.visualizzaArtefatti());
            if (risposta != -1) {
                esterno.aggiungiAttuatoreSuArtefatto(risposta, creaAttuatore());
            }
            for (Stanza s :
                    stanze) {
                risposta = Interazione.interrogazione("Su quale artefatto? " +
                                "(Siamo nella stanza " + s.getNome() + " se non e` questa la stanza che " +
                                "contiene l'artefatto voluto passare alla prossima con 0)",
                        s.visualizzaArtefatti());
                if (risposta != -1) {
                    s.aggiungiAttuatoreSuArtefatto(risposta, creaAttuatore());
                }
            }
        } else {
            String[] nomiStanze = new String[stanze.size()];
            for (int i = 0; i < stanze.size(); i++) {
                nomiStanze[i] = stanze.get(i).getNome();
            }
            risposta = Interazione.interrogazione("In quale stanza? ", nomiStanze);
            if (risposta != -1)
                stanze.get(risposta).aggiungiAttuatore(creaAttuatore());
        }
    }

    private Sensore creaSensore() {
        String nome = Interazione.domanda("Nome fantasia sensore");
        String[] nomiCat = new String[SistemaDomotico.categorieSensori.size()];
        for (int i = 0; i < nomiCat.length; i++) {
            nomiCat[i] = SistemaDomotico.categorieSensori.get(i).getNome();
        }
        int cat = Interazione.interrogazione("Scegli la categoria del sensore", nomiCat);
        return new Sensore((CategoriaSensore) SistemaDomotico.categorieSensori.get(cat),nome);
    }

    private Attuatore creaAttuatore(){
        String nome = Interazione.domanda("Nome fantasia attuatore");
        String[] nomiCat = new String[SistemaDomotico.categorieAttuatori.size()];
        for (int i = 0; i < nomiCat.length; i++) {
            nomiCat[i] = SistemaDomotico.categorieAttuatori.get(i).getNome();
        }
        int cat = Interazione.interrogazione("Scegli la categoria dell'attuatore", nomiCat);
        return new Attuatore((CategoriaAttuatore) SistemaDomotico.categorieAttuatori.get(cat),nome);
    }

    private void stampaAlberoUnitaImmobiliare() {
        StringBuilder tree = new StringBuilder();
        tree.append(nomeUnitaImmobiliare+"\n");
        for (Stanza s :
                stanze) {
            tree.append("\t" + s.getNome()+"\n");
            for (String sensore :
                    s.getCategoriaSensoriPresenti()) {
                tree.append("\t\t" + sensore+"\n");
            }
            for (String attuatore :
                    s.getCategoriaAttuatoriPresenti()) {
                tree.append("\t\t" + attuatore+"\n");
            }
        }
        System.out.println(tree.toString());
    }
}
