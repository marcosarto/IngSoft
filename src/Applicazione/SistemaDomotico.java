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

    private boolean aggiungiCategoriaAttuatori() {
        String risposta = Interazione.domanda("Nome della categoria : ");
        if (controlloNomiCategoria(risposta, categorieAttuatori)) {
            System.out.print("Gia` presente, ritorno alla schermata precedente");
            return false;
        }
        CategoriaAttuatore categoriaAttuatore = new CategoriaAttuatore(risposta);
        risposta = Interazione.domanda("Testo libero (massimo 180 caratteri) : ");
        if (risposta.length() > 180) {
            System.out.println("Il testo supera la lunghezza massima, ritorno schemata precedente");
            return false;
        }
        categorieAttuatori.add(categoriaAttuatore);
        categoriaAttuatore.setDescrizione(risposta);

        String isEsci = null;
        do {
            risposta = Interazione.domanda("Nome della modalita` opertiva");
            ModalitaOperativa modalitaOperativa = new ModalitaOperativa();
            modalitaOperativa.setNome(risposta);
            int scelta = Interazione.interrogazione("Modalita` a stati o parametrica?",
                    new String[]{"A stati", "Parametrica"}, false);
            if (scelta == 0) {
                do {
                    risposta = Interazione.domanda("Inserisci stati possibili");
                    modalitaOperativa.aggiungiStato(risposta);
                    risposta = Interazione.domanda("Vuoi aggiungere un nuovo stato possibile? (y/any key)");
                } while (risposta.equals("y"));
            } else {
                do {
                    risposta = Interazione.domanda("Inserisci nome parametro settabile : ");
                    modalitaOperativa.addParametro(risposta, creaParametro(risposta));
                    risposta = Interazione.domanda("Vuoi inserire un altro parametro nell'unita` (y/any key)");
                } while (risposta.equals("y"));
            }
            categoriaAttuatore.addModalita(modalitaOperativa);
            isEsci = Interazione.domanda("Vuoi aggiungere una nuova modalita` operativa all'attuatore? (y/any key)");
        } while (isEsci.equals("y"));
        return true;
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
                            "Importa categoria attuatori"
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
                    importaCategorieSensori();
                    break;
//                case 5:
//                    importaCategorieAttuatori();
//                    break;
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
        boolean esci=false;
        do {
            Rilevazione rilevazione=null;
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
            }
            else{
                String elemento = campi[pos+1];
                rilevazione = creaRilevazioneStati(campi[pos-1], elemento);
                do {
                    pos++;
                    elemento = campi[pos+1];
                    if (!elemento.equals("0")) aggiungiRilevazioneStato(rilevazione, elemento);
                } while (!elemento.equals("0"));
                pos = pos+3;
            }
            categoriaSensore.setInformazioni(rilevazione);
            counterRilevazioniPerUnitaMisura++;
            if(pos>campi.length) esci = true;
        } while (!esci);

    }

    public void importaCategorieSensori() {
        String file = "importaCategorieSensori.txt";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String daButtare = reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                aggiungiCatSensoreDaFile(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CategoriaDispositivo> getCategorieSensori() {
        return categorieSensori;
    }

    public ArrayList<CategoriaDispositivo> getCategorieAttuatori() {
        return categorieAttuatori;
    }
}
