package Applicazione;

import Categorie.CategoriaAttuatore;
import Categorie.CategoriaSensore;
import Categorie.ModalitaOperativa;
import Categorie.Parametro;
import Contenitori.Artefatto;
import Contenitori.Attuatore;
import Contenitori.Sensore;
import Contenitori.Stanza;
import DipendenteDalTempo.CycleRoutine;
import Utility.Interazione;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class UnitaImmobiliare implements java.io.Serializable {
    private String nomeUnitaImmobiliare, tipoUnitaImmobiliare;
    private HashMap<String, Stanza> stanze = new HashMap<>();
    private HashMap<String, Stanza> artefatti = new HashMap<>();//chiave nome artefatto valore stanza associata
    private SistemaDomotico sistemaDomotico;
    private boolean almenoUnSensore = false;
    private transient CycleRoutine cycle;

    public UnitaImmobiliare() {
        cycle = new CycleRoutine(this);
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

    public UnitaImmobiliare(String nome, String tipo) {
        cycle = new CycleRoutine(this);
        nomeUnitaImmobiliare = nome;
        tipoUnitaImmobiliare = tipo;
    }

    public void terminaThread() {
        if (cycle != null)
            cycle.terminaThread();
    }

    public boolean aggiungiStanzaCode(String stanza) {
        if (stanze.containsKey(stanza))
            return false;
        else {
            stanze.put(stanza, new Stanza(stanza));
            return true;
        }
    }

    public void aggiungiStanza() {
        String risposta = null;
        do {
            String stanza = Interazione.domanda("Inserisci nome stanza : ");

            //Se c'e` gia almeno una stanza controlla che il nome non sia duplicato
            if (!aggiungiStanzaCode(stanza)) {
                System.out.println("Nome stanza gia esistente, non aggiunta");
            }

            risposta = Interazione.domanda("Vuoi uscire? (y/any key) : ");

        } while (!risposta.equals("y"));
    }

    public void aggiungiCategoriaSensoreAStanza(String stanza, String catSensore) {
        stanze.get(stanza).aggiungiCategoriaSensoriPresenti(catSensore);
    }

    public void aggiungiCategoriaAttuatoreAStanza(String stanza, String catAttuatore) {
        stanze.get(stanza).aggiungiCategoriaAttuatoriPresenti(catAttuatore);
    }

    public void aggiungiCategoriaSensoreAArtefatto(String stanza, String artefatto, String catSensore) {
        if (stanza.contains(stanza))
            stanze.get(stanza).aggiungiCategoriaSensoriPresentiSuArtifatto(artefatto, catSensore);
    }

    public void aggiungiCategoriaAttuatoreAArtefatto(String stanza, String artefatto, String catAttuatore) {
        if (stanza.contains(stanza))
            stanze.get(stanza).aggiungiCategoriaAttuatoriPresentiSuArtifatto(artefatto, catAttuatore);
    }

    public boolean aggiungiArtefattoCode(String nomeStanza, String nomeArtefatto) {
        if (!stanze.get(nomeStanza).aggiungiArtefatto(nomeArtefatto))
            return false;
        else {
            artefatti.put(nomeArtefatto, stanze.get(nomeStanza));
            return true;
        }
    }

    public void aggiungiArtefatto() {
        String risposta = null;
        do {

            String artefatto = Interazione.domanda("Inserisci nome artefatto : ");

            String[] elencoStanze = stanze.keySet().toArray(new String[0]);

            int stanzaDaAbbinare = Interazione.interrogazione
                    ("In quale stanza vuoi aggiungere l'artefatto ", elencoStanze, false);
            String nomeStanzaDaAbbinare = elencoStanze[stanzaDaAbbinare];

            if (!aggiungiArtefattoCode(nomeStanzaDaAbbinare, artefatto))
                System.out.println("Artefatto gia esistente in questa stanza");

            risposta = Interazione.domanda("Vuoi uscire? (y/any key) : ");

        } while (!risposta.equals("y"));

    }

    public void flussoFruitore(SistemaDomotico sistemaDomotico) {
        int risposta;
        do {
            this.sistemaDomotico = sistemaDomotico;
            risposta = Interazione.interrogazione("Cosa vuoi fare?",
                    new String[]{"Visualizza piantina unita' immobiliare",
                            "Visualizza valori di sensori specifici",
                            "Agisci sugli attuatori",
                            "Inserisci regola",
                            "Stampa regole gia` inserite",
                            "Disattiva regola",
                            "Attiva regola",
                            "Disattiva sensore",
                            "Disattiva attuatore",
                            "Attiva sensore",
                            "Attiva attuatore"
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
                case 3:
                    inserisciRegola();
                    break;
                case 4:
                    stampaRegole();
                    break;
                case 5:
                    disattivaRegola();
                    break;
                case 6:
                    attivaRegola();
                    break;
                case 7:
                    attivaDisattivaSensore(false);
                    break;
                case 8:
                    attivaDisattivaAttuatore(false);
                    break;
                case 9:
                    attivaDisattivaSensore(true);
                    break;
                case 10:
                    attivaDisattivaAttuatore(true);
                    break;
            }
        } while (risposta != -1);
    }

    private void attivaDisattivaSensore(boolean attiva) {
        if(!almenoUnSensore(!attiva)){
            System.out.println("Non ci sono sensori da commutare");
            return;
        }
        String risp;
        do {
            elencaSensoriUnitaImmobiliare();
            String risposta = Interazione.domanda("Inserisci nome sensore che vuoi mutare : ");
            Sensore val = cercaSensoreRestituisciSensore(risposta);
            if (val != null) {
                val.setAttivo(attiva);
            } else
                System.out.println("Nome sensore non corretto");
            risp = Interazione.domanda("Vuoi mutare un altro sensore? (y/any key)");
        } while (risp.equals("y"));
    }

    private void attivaDisattivaAttuatore(boolean attiva) {
        if(!almenoUnAttuatore(!attiva)){
            System.out.println("Non ci sono attuatori da commutare");
            return;
        }
        String valA;
        do {
            elencaAttuatoriUnitaImmobiliare();
            String risposta = Interazione.domanda("Inserisci nome attuatore che vuoi comandare : ");
            Attuatore val = cercaAttuatoreRestituisciAttuatore(risposta);

            if (val != null) {
                val.setAttivo(attiva);
            } else
                System.out.println("Il nome attuatore non e` corretto");
            valA = Interazione.domanda("Vuoi controllare un altro attuatore? (y/any key)");
        } while (valA.equals("y"));
    }

    private void disattivaRegola() {
        if (cycle != null)
            cycle.disattivaRegola();
        else
            System.out.println("Ancora nessuna regola inserita");
    }

    private void attivaRegola() {
        if (cycle != null)
            cycle.attivaRegola();
        else
            System.out.println("Ancora nessuna regola inserita");
    }

    private void stampaRegole() {
        if (cycle != null)
            cycle.stampaRegole();
        else
            System.out.println("Al momento non son state inserite regole");
    }

    private void inserisciRegola() {
        String regola = Interazione.domanda("Inserisci regola 'if antecedente then conseguente' : ");
        try {
            inserisciRegolaCode(regola);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void inserisciRegolaCode(String regola) throws IllegalArgumentException {
        if (cycle == null)
            cycle = new CycleRoutine(this);
        cycle.aggiungiRegola(regola);
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
                            "Visualizza albero associazioni",
                            "Importa regole da file"
                    }, true);
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
                case 5:
                    importaRegoleDaFile();
                    break;
                default:
                    esci = true;
                    break;
            }
        } while (!esci);
    }

    private void importaRegoleDaFile() {
        String filename = Interazione.domanda("Nome del file contenente le regole? : ");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                try{
                    inserisciRegolaCode(line);
                }catch(IllegalArgumentException e){
                    System.out.println("La riga "+line+" ha generato il seguente errore");
                    System.out.println(e.getMessage());
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Il file non esiste, controlla il nome inserito");
        }
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
        if(cercaSensoreRestituisciSensore(nome+"_"+nomiCat[cat])!=null) {
            System.out.println("Esiste gia un sensore di questa categoria con questo nome");
            return null;
        }
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
        if(cercaAttuatoreRestituisciAttuatore(nome+"_"+nomiCat[cat])!=null) {
            System.out.println("Esiste gia un attuatore di questa categoria con questo nome");
            return null;
        }
        return new Attuatore((CategoriaAttuatore) sistemaDomotico.getCategorieAttuatori().get(cat), nome);
    }

    private void elencaSensoriUnitaImmobiliare() {
        for (Stanza s : stanze.values()) {
            System.out.println("Sensori nella stanza " + s.getNome());
            System.out.println(s.getSensori());
            System.out.println("Sensori sugli artifatti della stanza " + s.getNome());
            for (Artefatto a : s.getArtefatti()) {
                System.out.println(a.getSensori());
            }
            System.out.println(Interazione.DELIMITATORE);
        }
    }

    private void proceduraLetturaSensori() {
        do {
            elencaSensoriUnitaImmobiliare();
            String risposta = Interazione.domanda("Inserisci nome sensore che vuoi ispezionare : ");
            String val = cercaSensoreRestituisciValore(risposta);
            if (!val.isEmpty()) {
                System.out.println("Il valore del sensore e` : " + val);
            }
            val = Interazione.domanda("Vuoi controllare un altro sensore? (y/any key)");
            if (!val.equals("y"))
                return;
        } while (true);
    }

    public String cercaSensoreRestituisciValore(String risposta) {
        String val = "";
        for (Stanza s : stanze.values()) {
            if (val.isEmpty())
                val = s.ritornaValoreSensore(risposta);
            for (Artefatto a : s.getArtefatti()) {
                if (val.isEmpty())
                    val = a.ritornaValoreSensore(risposta);
            }
        }
        return val;
    }

    public Sensore cercaSensoreRestituisciSensore(String risposta) {
        Sensore val = null;
        for (Stanza s : stanze.values()) {
            if (val == null)
                val = s.ritornaRiferimentoSensore(risposta);
            for (Artefatto a : s.getArtefatti()) {
                if (val == null)
                    val = a.ritornaRiferimentoSensore(risposta);
            }
        }
        return val;
    }

    public void elencaAttuatoriUnitaImmobiliare() {
        for (Stanza s : stanze.values()) {
            System.out.println("Attuatori nella stanza " + s.getNome());
            System.out.println(s.getAttuatori());
            System.out.println("Attuatori sugli artifatti della stanza " + s.getNome());
            for (Artefatto a : s.getArtefatti()) {
                System.out.println(a.getAttuatori());
            }
            System.out.println(Interazione.DELIMITATORE);
        }
    }

    private void proceduraAttuatori() {
        do {
            elencaAttuatoriUnitaImmobiliare();
            String risposta = Interazione.domanda("Inserisci nome attuatore che vuoi comandare : ");
            Attuatore val = cercaAttuatoreRestituisciAttuatore(risposta);

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
            if (!valA.equals("y"))
                return;

        } while (true);
    }

    public Attuatore cercaAttuatoreRestituisciAttuatore(String risposta) {
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
        return val;
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

    private boolean almenoUnAttuatore(boolean attivo){
        for(Stanza s : stanze.values()){
            if(s.almenoUnAttuatore(attivo))
                return true;
            for(Artefatto a : s.getArtefatti()){
                if(a.almenoUnAttuatore(attivo))
                    return true;
            }
        }
        return false;
    }

    private boolean almenoUnSensore(boolean attivo){
        for(Stanza s : stanze.values()){
            if(s.almenoUnSensore(attivo))
                return true;
            for(Artefatto a : s.getArtefatti()){
                if(a.almenoUnSensore(attivo))
                    return true;
            }
        }
        return false;
    }
}

