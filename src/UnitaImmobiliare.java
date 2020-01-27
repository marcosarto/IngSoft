import Categorie.CategoriaAttuatore;
import Categorie.CategoriaSensore;
import Categorie.ModalitaOperativa;
import Categorie.Parametro;
import Contenitori.Artefatto;
import Contenitori.Attuatore;
import Contenitori.Sensore;
import Contenitori.Stanza;
import Utility.Interazione;

import java.util.HashMap;

public class UnitaImmobiliare implements java.io.Serializable {
    private String nomeUnitaImmobiliare, tipoUnitaImmobiliare;
    private HashMap<String, Stanza> stanze = new HashMap<>();
    private HashMap<String, Stanza> artefatti = new HashMap<>();//chiave nome artefatto valore stanza associata
    private SistemaDomotico sistemaDomotico;
    private boolean almenoUnSensore = false;

    public UnitaImmobiliare() {
        String nome = Interazione.domanda("Nome dell'unita immobiliare?");
        this.nomeUnitaImmobiliare = nome;
        String tipo = Interazione.domanda("Tipologia unita immobiliare?");
        this.tipoUnitaImmobiliare = tipo;
        stanze.put("Esterno", new Stanza("Esterno"));
        int risposta = Interazione.interrogazione
                ("Bisogna aggiungere almeno una stanza o un artefatto," +
                                " (nel seguito se ne potranno aggiungere altri)",
                        new String[]{"Artefatto", "Stanza"}, false);

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
                    ("In quale stanza vuoi aggiungere l'artefatto ", elencoStanze, false);

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
                new String[]{"Visualizza piantina unita' immobiliare",
                        "Visualizza valori di sensori specifici",
                        "Agisci sugli attuatori",
                }, true);
        switch (risposta) {
            case 0:
                stampaAlberoUnitaImmobiliare();
                break;
            case 1:
                proceduraLetturaSensori();
                break;
            case 2:
                proceduraAttuatori();
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
                            "Visualizza albero associazioni"}, true);
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
        Sensore sensoreCorrente = creaSensore();
        if (sensoreCorrente == null)
            return;
        int risposta = Interazione.interrogazione("Dove vuoi posizionare il sensore?",
                new String[]{"Artefatto", "Stanza"}, false);
        if (risposta == 0) {
            if (artefatti.keySet().size() == 0) {
                System.out.println("Non hai artefatti presenti al momento, ritorno al menu");
                return;
            }
            boolean nuovoArtefatto = false;
            do {
                StringBuilder st = new StringBuilder();
                st.append("Scegli il l'artefatto (inserisci il nome) : " + "\n");
                for (String s : artefatti.keySet()) {
                    st.append(s + "\n");
                }
                String artefattoScelto = Interazione.domanda(st.toString());
                if (!artefatti.containsKey(artefattoScelto))
                    System.out.println("Nome non corretto o non esistenete");
                else {
                    if (!artefatti.get(artefattoScelto).aggiungiSensoreSuArtefatto(artefattoScelto, sensoreCorrente))
                        System.out.println("C'e` gia un sensore di questa categoria!");
                    else
                        almenoUnSensore = true;
                }
                if (Interazione.domanda("Vuoi collegarlo ad un altro artefatto? (y/any key)").equals("y"))
                    nuovoArtefatto = true;
                else
                    nuovoArtefatto = false;

            } while (nuovoArtefatto);

        } else {
            if (stanze.keySet().size() == 0) {
                System.out.println("Non hai stanze presenti al momento, ritorno al menu");
                return;
            }
            boolean nuovaStanza = false;
            do {
                String[] elencoStanze = stanze.keySet().toArray(new String[0]);
                risposta = Interazione.interrogazione("In quale stanza? ", elencoStanze, false);
                if (elencoStanze[risposta].equals("Esterno"))
                    System.out.println("Non si puo` aggiungere un sensore all'esterno senza un artefatto");
                else if (!stanze.get(elencoStanze[risposta]).aggiungiSensore(sensoreCorrente))
                    System.out.println("C'e` gia un sensore di questa categoria!");
                else
                    almenoUnSensore = true;

                if (Interazione.domanda("Vuoi collegarlo ad un altra stanza? (y/any key)").equals("y"))
                    nuovaStanza = true;
                else
                    nuovaStanza = false;
            } while (nuovaStanza);
        }

    }

    private void aggingiAttuatore() {
        Attuatore attuatoreCorrente = creaAttuatore();
        if (attuatoreCorrente == null)
            return;
        int risposta = Interazione.interrogazione("Dove vuoi posizionare l'attuatore?",
                new String[]{"Artefatto", "Stanza"}, false);
        if (risposta == 0) {
            if (artefatti.keySet().size() == 0) {
                System.out.println("Non hai artefatti presenti al momento, ritorno al menu");
                return;
            }
            boolean nuovoArtefatto = false;
            do {
                StringBuilder st = new StringBuilder();
                st.append("Scegli il l'artefatto (inserisci il nome) : " + "\n");
                for (String s : artefatti.keySet()) {
                    st.append(s + "\n");
                }
                String artefattoScelto = Interazione.domanda(st.toString());
                if (!artefatti.containsKey(artefattoScelto))
                    System.out.println("Nome non corretto o non esistenete");
                else {
                    if (!artefatti.get(artefattoScelto).aggiungiAttuatoreSuArtefatto(artefattoScelto, attuatoreCorrente))
                        System.out.println("C'e` gia un'attuatore di questa categoria!");
                }
                if (Interazione.domanda("Vuoi collegarlo ad un altro artefatto? (y/any key)").equals("y"))
                    nuovoArtefatto = true;
                else
                    nuovoArtefatto = false;

            } while (nuovoArtefatto);

        } else {
            if (stanze.keySet().size() == 0) {
                System.out.println("Non hai stanze presenti al momento, ritorno al menu");
                return;
            }
            boolean nuovaStanza = false;
            do {
                String[] elencoStanze = stanze.keySet().toArray(new String[0]);
                risposta = Interazione.interrogazione("In quale stanza? ", elencoStanze, false);
                if (elencoStanze[risposta].equals("Esterno"))
                    System.out.println("Non si puo` aggiungere un attuatore all'esterno senza un artefatto");
                else if (!stanze.get(elencoStanze[risposta]).aggiungiAttuatore(attuatoreCorrente))
                    System.out.println("C'e` gia un attuatore di questa categoria!");

                if (Interazione.domanda("Vuoi collegarlo ad un altra stanza? (y/any key)").equals("y"))
                    nuovaStanza = true;
                else
                    nuovaStanza = false;
            } while (nuovaStanza);
        }
    }

    private Sensore creaSensore() {
        String nome = Interazione.domanda("Nome fantasia sensore");
        String[] nomiCat = new String[sistemaDomotico.getCategorieSensori().size()];
        for (int i = 0; i < nomiCat.length; i++) {
            nomiCat[i] = sistemaDomotico.getCategorieSensori().get(i).getNome();
        }
        if (nomiCat.length == 0) {
            System.out.println("Al momento non hai categorie sensori tra cui scegliere, ritorno al menu");
            return null;
        }
        int cat = Interazione.interrogazione("Scegli la categoria del sensore", nomiCat, false);
        return new Sensore((CategoriaSensore) sistemaDomotico.getCategorieSensori().get(cat), nome);
    }

    private Attuatore creaAttuatore() {
        String nome = Interazione.domanda("Nome fantasia attuatore");
        String[] nomiCat = new String[sistemaDomotico.getCategorieAttuatori().size()];
        for (int i = 0; i < sistemaDomotico.getCategorieAttuatori().size(); i++) {
            nomiCat[i] = sistemaDomotico.getCategorieAttuatori().get(i).getNome();
        }
        if (nomiCat.length == 0) {
            System.out.println("Al momento non hai categorie attuatori tra cui scegliere, ritorno al menu");
            return null;
        }
        int cat = Interazione.interrogazione("Scegli la categoria dell'attuatore", nomiCat, false);

        return new Attuatore((CategoriaAttuatore) sistemaDomotico.getCategorieAttuatori().get(cat), nome);
    }

    private void proceduraLetturaSensori() {
        do {
            for (Stanza s : stanze.values()) {
                System.out.println("Sensori nella stanza " + s.getNome());
                System.out.println(s.getSensori());
                System.out.println("Sensori sugli artifatti della stanza " + s.getNome());
                for (Artefatto a : s.getArtefatti()) {
                    System.out.println(a.getSensori());
                }
                System.out.println(Interazione.DELIMITATORE);
            }
            String risposta = Interazione.domanda("Inserisci nome sensore che vuoi ispezionare : ");
            for (Stanza s : stanze.values()) {
                String val = s.ritornaValoreSensore(risposta);
                if (!val.isEmpty()) {
                    System.out.println(val);
                    val = Interazione.domanda("Vuoi controllare un altro sensore? (y/any key)");
                    if (val.equals("y"))
                        continue;
                    else
                        return;
                }
                for (Artefatto a : s.getArtefatti()) {
                    String valA = a.ritornaValoreSensore(risposta);
                    if (!valA.isEmpty()) {
                        System.out.println("Il valore del sensore e` : " + valA);
                        valA = Interazione.domanda("Vuoi controllare un altro sensore? (y/any key)");
                        if (valA.equals("y"))
                            continue;
                        else
                            return;
                    }
                }
            }
        } while (true);
    }

    private void proceduraAttuatori() {
        do {
            for (Stanza s : stanze.values()) {
                System.out.println("Attuatori nella stanza " + s.getNome());
                System.out.println(s.getAttuatori());
                System.out.println("Attuatori sugli artifatti della stanza " + s.getNome());
                for (Artefatto a : s.getArtefatti()) {
                    System.out.println(a.getAttuatori());
                }
                System.out.println(Interazione.DELIMITATORE);
            }
            String risposta = Interazione.domanda("Inserisci nome attuatore che vuoi comandare : ");
            Attuatore val = null;
            for (Stanza s : stanze.values()) {
                if (s.ritornaRiferimentoAttuatore(risposta) != null) {
                    val = s.ritornaRiferimentoAttuatore(risposta);
                }
                for (Artefatto a : s.getArtefatti()) {
                    if (a.ritornaRiferimentoAttuatore(risposta) != null) {
                        val = a.ritornaRiferimentoAttuatore(risposta);
                    }
                }
            }
            if (val != null) {
                CategoriaAttuatore cat = (CategoriaAttuatore) val.getCategoria();
                System.out.println("Attuatore di categoria " + cat.getNome());
                int risp = Interazione.interrogazione("Quale modalita operativa vuoi comandare : ",
                        cat.getModalitaOperativa().keySet().toArray(new String[0]), false);
                String modS = cat.getModalitaOperativa().keySet().toArray(new String[0])[risp];
                ModalitaOperativa mod = cat.getModalitaOperativa().get(modS);
                if (mod.isAStati()) {
                    if (val.getStatoCorrente(modS) == null)
                        System.out.println("Al momento questo attuatore non ha uno stato settato");
                    else
                        System.out.println("Stato corrente " + val.getStatoCorrente(modS));
                    risp = Interazione.interrogazione("Quale stato vuoi applicare? ",
                            mod.getStati().toArray(new String[0]), false);
                    String nuovoStato = mod.getStati().toArray(new String[0])[risp];
                    val.setStatoCorrente(modS, nuovoStato);
                } else {
                    for (Parametro p : mod.getParametri().values()) {
                        System.out.println("Il parametro " + p.getNome() + " ha un valore desiderato di " + p.getValoreDesiderato());
                    }
                    String parametroDaModificare = Interazione.domanda("Quale parametro vuoi cambiare? : ");
                    if (!mod.getParametri().containsKey(parametroDaModificare))
                        System.out.println("Non esiste un parametro con quel nome");
                    boolean fineTransizione = false;
                    do {
                        try {
                            String valoreDesiderato = Interazione.domanda("Che valore vuoi impostare? : ");
                            mod.getParametri().get(parametroDaModificare).setValoreDesiderato(Integer.parseInt(valoreDesiderato));
                            fineTransizione = true;
                        } catch (Exception e) {
                            System.out.println("Devi inserire un valore numerico");
                        }
                    } while (!fineTransizione);
                }
            } else {
                System.out.println("Attuatore non trovato ");
            }
            String valA = Interazione.domanda("Vuoi controllare un altro attuatore? (y/any key)");
            if (valA.equals("y"))
                continue;
            else
                return;


        } while (true);
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
                for (String sensore : a.getCategoriaSensoriPresenti()) {
                    tree.append("\t\t\t" + sensore + "\n");
                }
                tree.append("\n");
                tree.append("\t\tCategorie attuatori dell'artefatto " + a.getNome() + " nella stanza\n");
                for (String attuatore : a.getCategoriaAttuatoriPresenti()) {
                    tree.append("\t\t\t" + attuatore + "\n");
                }
                tree.append("\n");
            }
        }
        System.out.println(tree.toString());
    }

    public String getNomeUnitaImmobiliare() {
        return nomeUnitaImmobiliare;
    }
}

