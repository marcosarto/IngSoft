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
                    new String[]{"Utente", "Manutentore"});

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
                entrateUnitaImmobiliare);
        return risposta;
    }

    private boolean aggiungiCategorieSensori() {
        String risposta = Interazione.domanda("Nome della categoria : ");
        if (controlloNomiCategoria(risposta, categorieSensori)) {
            System.out.print("Gia` presente, ritorno alla schermata precedente");
            return false;
        }
        CategoriaSensore categoriaSensore = new CategoriaSensore(risposta);
        risposta = Interazione.domanda("Testo libero (massimo 180 caratteri) (unita` di misura dei sensori numerici divisi da ,): ");
        if (risposta.length() > 180) {
            System.out.println("Il testo supera la lunghezza massima, ritorno schemata precedente");
            return false;
        }
        categoriaSensore.setDescrizione(risposta);
        categorieSensori.add(categoriaSensore);

        int counterRilevazioniPerUnitaMisura = 0;
        String isUscita = null;
        do {
            risposta = Interazione.domanda("Nome della rilevazione del sensore : ");
            Rilevazione rilevazione = new Rilevazione();
            rilevazione.setNome(risposta);
            int scelta = Interazione.interrogazione("Misura un valore numerico o un dominio discreto? ",
                    new String[]{"Valore numerico", "Dominio discreto"});
            if (scelta == 0) {
                try {
                    risposta = Interazione.domanda("Minimo rilevabile dal sensore : ");
                    rilevazione.setMinimo(Integer.valueOf(risposta));
                    risposta = Interazione.domanda("Massimo rilevabile dal sensore : ");
                    rilevazione.setMassimo(Integer.valueOf(risposta));
                    rilevazione.setUnitaDiMisura(categoriaSensore.getUnitaMisura(counterRilevazioniPerUnitaMisura));
                }catch(Exception e){
                    System.out.println("Devi inserire un numero!");
                    continue;
                }
            } else if (scelta == 1) {
                do {
                    risposta = Interazione.domanda("Inserisci un elemento del dominio (0 per uscire)");
                    if (risposta.equals("0"))
                        break;
                    rilevazione.aggiungiStato(risposta);
                } while (!risposta.equals("0"));
            }
            categoriaSensore.setInformazioni(rilevazione);
            counterRilevazioniPerUnitaMisura++;
            isUscita = Interazione.domanda("Vuoi inserire un'altra rilevazione? (y/any key)");
        } while (isUscita.equals("y"));

        return true;
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

        String isEsci=null;
        do {
            risposta = Interazione.domanda("Nome della modalita` opertiva");
            ModalitaOperativa modalitaOperativa = new ModalitaOperativa();
            modalitaOperativa.setNome(risposta);
            int scelta = Interazione.interrogazione("Modalita` a stati o parametrica?",
                    new String[]{"A stati", "Parametrica"});
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
                            "Selezionare un'unita` immobiliare per lavorarci"});
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
                default:
                    esci = true;
            }
        } while (!esci);
    }

    public ArrayList<CategoriaDispositivo> getCategorieSensori() {
        return categorieSensori;
    }

    public ArrayList<CategoriaDispositivo> getCategorieAttuatori() {
        return categorieAttuatori;
    }
}
