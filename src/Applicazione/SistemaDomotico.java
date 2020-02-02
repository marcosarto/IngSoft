package Applicazione;

import Categorie.*;
import Utility.Interazione;

import java.io.*;
import java.util.ArrayList;

public class SistemaDomotico implements Serializable {

    private ArrayList<UnitaImmobiliare> unitaImmobiliari = new ArrayList();
    private ArrayList<CategoriaDispositivo> categorieSensori = new ArrayList();
    private ArrayList<CategoriaDispositivo> categorieAttuatori = new ArrayList();

    public SistemaDomotico() {
        init();
    }

    public void init() {
        String uscita = null;
        do {
            int risposta = Interazione.interrogazione("Seleziona la modalita` operativa",
                    new String[]{"Utente", "Manutentore"}, true);

            switch (risposta) {
                case 0:
                    flussoFruitore();
                    break;
                case 1:
                    flussoManutentore();
                    break;
            }
            uscita = Interazione.domanda("Vuoi uscire (y/any key) : ");
        } while (!uscita.equals("y"));
    }

    public void terminaThreads() {
        for (UnitaImmobiliare u : unitaImmobiliari) {
            u.terminaThread();
        }
    }

    public void aggiungiUnitaImmobiliare() {
        unitaImmobiliari.add(new UnitaImmobiliare());
    }

    private int visualizzaElencoUnitaImmobiliari() {
        //riempie entrateUnitaImmobiliare con i nomi delle unita immobiliari
        String[] entrateUnitaImmobiliare = new String[unitaImmobiliari.size()];
        if (entrateUnitaImmobiliare.length == 0) {
            System.out.println("Nessuna unita immobiliare ancora aggiunta");
            return -1;
        }
        for (int i = 0; i < entrateUnitaImmobiliare.length; i++) {
            entrateUnitaImmobiliare[i] = unitaImmobiliari.get(i).getNomeUnitaImmobiliare();
        }

        int risposta = Interazione.interrogazione("Seleziona l'unita' immobiliare che vuoi ispezionare :",
                entrateUnitaImmobiliare, true);
        return risposta;
    }

    private CategoriaSensore creaCatSensore(String nome) {
        if (controlloNomiCategoria(nome, categorieSensori)) {
            return null;
        } else
            return new CategoriaSensore(nome);
    }

    private boolean aggiungiDescrizioneCatSensore(CategoriaSensore categoriaSensore, String descr) {
        if (descr.length() > 180) {
            return false;
        }
        categoriaSensore.setDescrizione(descr);
        categorieSensori.add(categoriaSensore);
        return true;
    }

    private Rilevazione creaRilevazioneNumerica(String nome, int min, int max, String unitaDiMisura) {
        Rilevazione rilevazione = new Rilevazione();
        rilevazione.setNome(nome);
        rilevazione.setMinimo(min);
        rilevazione.setMassimo(max);
        rilevazione.setUnitaDiMisura(unitaDiMisura);
        return rilevazione;
    }

    private Rilevazione creaRilevazioneStati(String nome, String stato) {
        Rilevazione rilevazione = new Rilevazione();
        rilevazione.setNome(nome);
        rilevazione.aggiungiStato(stato);
        return rilevazione;
    }

    private void aggiungiRilevazioneStato(Rilevazione rilevazione, String stato) {
        rilevazione.aggiungiStato(stato);
    }

    private void aggiungiCategorieSensori() {
        String risposta = Interazione.domanda("Nome della categoria : ");
        CategoriaSensore categoriaSensore = creaCatSensore(risposta);
        if (categoriaSensore == null) {
            System.out.print("Gia` presente, ritorno alla schermata precedente");
            return;
        }

        risposta = Interazione.domanda("Testo libero (massimo 180 caratteri) " +
                "(unita` di misura dei sensori numerici divisi da ,): ");
        if (!aggiungiDescrizioneCatSensore(categoriaSensore, risposta)) {
            System.out.println("Il testo supera la lunghezza massima");
            return;
        }

        int counterRilevazioniPerUnitaMisura = 0;
        String isUscita = null;
        do {
            String nome;
            Rilevazione rilevazione = null;
            nome = Interazione.domanda("Nome della rilevazione del sensore : ");
            int scelta = Interazione.interrogazione("Misura un valore numerico o un dominio discreto? ",
                    new String[]{"Valore numerico", "Dominio discreto"}, false);
            if (scelta == 0) {
                try {
                    int min, max;
                    risposta = Interazione.domanda("Minimo rilevabile dal sensore : ");
                    min = Integer.valueOf(risposta);
                    risposta = Interazione.domanda("Massimo rilevabile dal sensore : ");
                    max = Integer.valueOf(risposta);
                    String unitaDiMisura = categoriaSensore.getUnitaMisura(counterRilevazioniPerUnitaMisura);
                    rilevazione = creaRilevazioneNumerica(nome, min, max, unitaDiMisura);
                } catch (Exception e) {
                    System.out.println("Devi inserire un numero!");
                    continue;
                }
            } else if (scelta == 1) {
                risposta = Interazione.domanda("Inserisci primo elemento del dominio");
                rilevazione = creaRilevazioneStati(nome, risposta);
                do {
                    risposta = Interazione.domanda("Inserisci un elemento del dominio (0 per uscire)");
                    if (!risposta.equals("0")) aggiungiRilevazioneStato(rilevazione, risposta);
                } while (!risposta.equals("0"));
            }
            categoriaSensore.setInformazioni(rilevazione);
            counterRilevazioniPerUnitaMisura++;
            isUscita = Interazione.domanda("Vuoi inserire un'altra rilevazione? (y/any key)");
        } while (isUscita.equals("y"));
    }

    private CategoriaAttuatore creaCategoriaAttuatore(String nome) {
        if (!controlloNomiCategoria(nome, categorieAttuatori))
            return new CategoriaAttuatore(nome);
        else
            return null;
    }

    private boolean aggiungiDescrizioneCatAttuatore(CategoriaAttuatore categoriaAttuatore, String descr) {
        if (descr.length() > 180)
            return false;
        else {
            categorieAttuatori.add(categoriaAttuatore);
            categoriaAttuatore.setDescrizione(descr);
            return true;
        }
    }

    private ModalitaOperativa creaModOp(String nome) {
        ModalitaOperativa op = new ModalitaOperativa();
        op.setNome(nome);
        return op;
    }

    private void aggiungiStato(ModalitaOperativa op, String stato) {
        op.aggiungiStato(stato);
    }

    private void aggiungiParametro(ModalitaOperativa op, String nome) {
        op.addParametro(nome, creaParametro(nome));
    }

    private void aggiungiCategoriaAttuatori() {
        String risposta = Interazione.domanda("Nome della categoria : ");
        CategoriaAttuatore categoriaAttuatore = creaCategoriaAttuatore(risposta);
        if (categoriaAttuatore == null) {
            System.out.print("Gia` presente, ritorno alla schermata precedente");
            return;
        }
        risposta = Interazione.domanda("Testo libero (massimo 180 caratteri) : ");
        if (!aggiungiDescrizioneCatAttuatore(categoriaAttuatore, risposta)) {
            System.out.println("Il testo supera la lunghezza massima, ritorno schemata precedente");
            return;
        }

        String isEsci;
        do {
            risposta = Interazione.domanda("Nome della modalita` opertiva");
            ModalitaOperativa modalitaOperativa = creaModOp(risposta);
            int scelta = Interazione.interrogazione("Modalita` a stati o parametrica?",
                    new String[]{"A stati", "Parametrica"}, false);
            if (scelta == 0) {
                risposta = Interazione.domanda("Inserisci stati possibili");
                while (!risposta.equals("0")) {
                    aggiungiStato(modalitaOperativa, risposta);
                    risposta = Interazione.domanda("Inserisci stati possibili (0 per uscire)");
                }
            } else {
                risposta = Interazione.domanda("Inserisci nome parametro settabile : ");
                while (!risposta.equals("0")) {
                    aggiungiParametro(modalitaOperativa, risposta);
                    risposta = Interazione.domanda("Inserisci nome parametro settabile (0 per uscire) : ");
                }
            }
            categoriaAttuatore.addModalita(modalitaOperativa);
            isEsci = Interazione.domanda("Vuoi aggiungere una nuova modalita` operativa all'attuatore? (y/any key)");
        } while (isEsci.equals("y"));
    }

    private Parametro creaParametro(String nome) {
        Parametro p = new Parametro(nome);
        return p;
    }

    private boolean controlloNomiCategoria(String nome, ArrayList<CategoriaDispositivo> lista) {
        for (CategoriaDispositivo c :
                lista) {
            if (c.getNome().equals(nome))
                return true;
        }
        return false;
    }

    private void flussoFruitore() {
        if (unitaImmobiliari.size() == 0) {
            System.out.println("Non e` ancora stata creata nessuna unita` immobiliare, contatta il manutentore");
        } else {
            int numeroUnitaImmobiliare = visualizzaElencoUnitaImmobiliari();
            if (numeroUnitaImmobiliare == -1)
                return;
            unitaImmobiliari.get(numeroUnitaImmobiliare).flussoFruitore(this);
        }
    }

    private void flussoManutentore() {
        boolean esci = false;
        do {
            int risposta = Interazione.interrogazione("Cosa vuoi fare(qualsiasi altro tasto per uscire) :",
                    new String[]{"Aggiungi categorie sensori",
                            "Aggiungi categoria attuatori",
                            "Creare e descrivere l'unita` immobiliare",
                            "Selezionare un'unita` immobiliare per lavorarci",
                            "Importa categorie sensori",
                            "Importa categoria attuatori",
                            "Importa unita immobiliari"
                    }, true);
            switch (risposta) {
                case 0:
                    aggiungiCategorieSensori();
                    break;
                case 1:
                    aggiungiCategoriaAttuatori();
                    break;
                case 2:
                    aggiungiUnitaImmobiliare();
                    break;
                case 3:
                    int numeroUnitaImm = visualizzaElencoUnitaImmobiliari();
                    if (numeroUnitaImm != -1)
                        unitaImmobiliari.get(numeroUnitaImm).flussoManutentore(this);
                    break;
                case 4:
                    importaCategorieDaFile("importaCategorieSensori.txt", true);
                    break;
                case 5:
                    importaCategorieDaFile("importaCategorieAttuatori.txt", false);
                    break;
                case 6:
                    importaUnitaImmobiliari();
                    break;
                default:
                    esci = true;
            }
        } while (!esci);
    }

    private void aggiungiCatSensoreDaFile(String riga) {
        String[] campi = riga.split("-");
        //0 nome 1 descrizione 2 nome rilevazione 3 (1 se numerica 2 se a stati)
        //caso 3 sia 1 allora 4 min 5 max, da li riinizio come se leggessi il 3
        //caso 3 sia 2 allora da 4 in poi sono stati del sensore fino a 0, da li riinizio come se leggessi il 3
        CategoriaSensore categoriaSensore = creaCatSensore(campi[0]);
        if (categoriaSensore == null) return;

        if (!aggiungiDescrizioneCatSensore(categoriaSensore, campi[1])) return;

        int counterRilevazioniPerUnitaMisura = 0;
        int pos = 3;
        boolean esci = false;
        do {
            Rilevazione rilevazione = null;
            if (campi[pos].equals("1")) {
                try {
                    String unitaDiMisura = categoriaSensore.getUnitaMisura(counterRilevazioniPerUnitaMisura);
                    int min = Integer.parseInt(campi[pos + 1]);
                    int max = Integer.parseInt(campi[pos + 2]);
                    rilevazione = creaRilevazioneNumerica(campi[pos - 1], min, max, unitaDiMisura);
                    pos = pos + 4;
                } catch (Exception e) {
                    System.out.println("Errore nella conversione del numero");
                }
            } else {
                String elemento = campi[pos + 1];
                rilevazione = creaRilevazioneStati(campi[pos - 1], elemento);
                do {
                    pos++;
                    elemento = campi[pos + 1];
                    if (!elemento.equals("0")) aggiungiRilevazioneStato(rilevazione, elemento);
                } while (!elemento.equals("0"));
                pos = pos + 3;
            }
            categoriaSensore.setInformazioni(rilevazione);
            counterRilevazioniPerUnitaMisura++;
            if (pos > campi.length) esci = true;
        } while (!esci);

    }

    private void aggiungiCatAttuatoreDaFile(String riga) {
        String[] campi = riga.split("-");

        CategoriaAttuatore categoriaAttuatore = creaCategoriaAttuatore(campi[0]);
        if (categoriaAttuatore == null) return;

        if (!aggiungiDescrizioneCatAttuatore(categoriaAttuatore, campi[1])) return;

        int pos = 3;
        boolean esci = false;
        do {
            ModalitaOperativa modalitaOperativa = creaModOp(campi[pos - 1]);
            if (campi[pos].equals("1")) {
                do {
                    pos++;
                    aggiungiStato(modalitaOperativa, campi[pos]);
                } while (!campi[pos + 1].equals("0"));
                pos = pos + 2;
            } else {
                do {
                    pos++;
                    aggiungiParametro(modalitaOperativa, campi[pos]);
                } while (!campi[pos + 1].equals("0"));
                pos = pos + 2;
            }
            categoriaAttuatore.addModalita(modalitaOperativa);
            if (!(pos < campi.length)) esci = true;
        } while (!esci);
    }

    private void importaCategorieDaFile(String file, boolean sensori) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String daButtare = reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                if (sensori)
                    aggiungiCatSensoreDaFile(line);
                else
                    aggiungiCatAttuatoreDaFile(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importaUnitaImmobiliariRiga(String riga) {
        String[] campi = riga.split("-");
        UnitaImmobiliare unitaImmobiliare = new UnitaImmobiliare(campi[0], campi[1]);
        int pos = 2;
        if (campi[pos].equals("")) {
            System.out.println("Devi inserire almeno una stanza riga " + riga + " NON INSERITA");
            return;
        }
        do {
            String stanzaCorrente = campi[pos];
            unitaImmobiliare.aggiungiStanzaCode(stanzaCorrente);
            pos++;
            if (!campi[pos].equals("")) {
                do {
                    //Aggiungo solo le categorie che sono contenute nel sistema domotico ma non lancio errori in caso contrario
                    //nel caso basterebbe mettere un messaggio nell'else
                    if (isCategoriaSensorePresente(campi[pos]))
                        unitaImmobiliare.aggiungiCategoriaSensoreAStanza(stanzaCorrente, campi[pos]);
                    pos++;
                } while (!campi[pos].equals("0"));
            }
            pos++;
            if (!campi[pos].equals("")) {
                do {
                    if (isCategoriaAttuatorePresente(campi[pos]))
                        unitaImmobiliare.aggiungiCategoriaAttuatoreAStanza(stanzaCorrente, campi[pos]);
                    pos++;
                } while (!campi[pos].equals("0"));
            }
            pos++;
            String artefatto="";
            if (!campi[pos].equals("")) {
                artefatto = campi[pos];
                unitaImmobiliare.aggiungiArtefattoCode(stanzaCorrente, artefatto);
            }
            pos++;
            if (!campi[pos].equals("")) {
                if(artefatto.equals(""))
                    System.out.println("Non puoi aggiungere categorie di sensori senza un artefatto riga "+riga+" ERRORE");
                do {
                    if (isCategoriaSensorePresente(campi[pos]))
                        unitaImmobiliare.aggiungiCategoriaSensoreAArtefatto(stanzaCorrente, artefatto, campi[pos]);
                    pos++;
                } while (!campi[pos].equals("0"));
            }
            pos++;
            if (!campi[pos].equals("")) {
                if(artefatto.equals(""))
                    System.out.println("Non puoi aggiungere categorie di attuatori senza un artefatto riga "+riga+" ERRORE");
                do {
                    if (isCategoriaAttuatorePresente(campi[pos]))
                        unitaImmobiliare.aggiungiCategoriaAttuatoreAArtefatto(stanzaCorrente,artefatto, campi[pos]);
                    pos++;
                } while (!campi[pos].equals("0"));
            }
            pos++;
        }while(pos<campi.length);
        unitaImmobiliari.add(unitaImmobiliare);
    }

    private void importaUnitaImmobiliari() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("importaUnitaImmobiliari.txt"));
            String daButtare = reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                importaUnitaImmobiliariRiga(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<CategoriaDispositivo> getCategorieSensori() {
        return categorieSensori;
    }

    ArrayList<CategoriaDispositivo> getCategorieAttuatori() {
        return categorieAttuatori;
    }

    private boolean isCategoriaSensorePresente(String cat){
        for(CategoriaDispositivo c : categorieSensori){
            if(c.getNome().equals(cat))
                return true;
        }
        return false;
    }

    private boolean isCategoriaAttuatorePresente(String cat){
        for(CategoriaDispositivo c : categorieAttuatori){
            if(c.getNome().equals(cat))
                return true;
        }
        return false;
    }
}
