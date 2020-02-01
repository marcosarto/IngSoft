package DipendenteDalTempo;

import Applicazione.UnitaImmobiliare;
import Categorie.*;
import Contenitori.Attuatore;
import Contenitori.DatiAttuatore;
import Contenitori.Sensore;

import java.util.ArrayList;

public class Regola {
    private Antecedente condizione;
    private Sensore[] sensoreCondizione=new Sensore[2];
    private ArrayList<Conseguente> azione = new ArrayList<>();
    private String expr;
    private Orario orario;


    public Regola(UnitaImmobiliare immobile, String expr) throws IllegalArgumentException {
        this.expr = expr;
        boolean isPrimoNum = false, isSecondoNum = false;
        boolean isPrimaStr = false, isSecondaStr = false;
        int const1 = 0, const2 = 0, const3 = 0;
        String str1 = null, str2 = null, str3 = null;
        Rilevazione r1 = null, r2 = null;
        String operatore = "";
        ModalitaOperativa modOp = null;
        Parametro parametro = null;


        String[] anticedenti;

        String inizio = expr.substring(0, 2);
        if (!inizio.equals("if") || !expr.contains("then")) {
            throw new IllegalArgumentException("La condizione non e` scritta correttamente");
        }

        String antecedente = expr.substring(3, expr.indexOf("then") - 1);
        anticedenti = antecedente.split("(<=|>=|=|>|<)");

        if (antecedente.contains("<="))
            operatore = "<=";
        else if (antecedente.contains(">="))
            operatore = ">=";
        else if (antecedente.contains("="))
            operatore = "=";
        else if (antecedente.contains("<"))
            operatore = "<";
        else if (antecedente.contains(">"))
            operatore = ">";
        else
            throw new IllegalArgumentException("Operatore non consentito");


        anticedenti[0] = anticedenti[0].trim();
        anticedenti[1] = anticedenti[1].trim();

        if(anticedenti[0].equals("time")){
            if (operatore.contains(">"))
                operatore = operatore.replace(">", "<");
            else if (operatore.contains("<"))
                operatore = operatore.replace("<", ">");
            int ore = Integer.parseInt(anticedenti[1].split("[.]")[0]);
            int minuti = Integer.parseInt(anticedenti[1].split("[.]")[1]);
            condizione = new AntecedenteNumeroTime(ore,minuti,operatore);
        }

        else if(anticedenti[1].equals("time")){
            int ore = Integer.parseInt(anticedenti[0].split("[.]")[0]);
            int minuti = Integer.parseInt(anticedenti[0].split("[.]")[1]);
            condizione = new AntecedenteNumeroTime(ore,minuti,operatore);
        }

        else {

            if (!isNumeric(anticedenti[0])) {
                Sensore s = immobile.cercaSensoreRestituisciSensore(anticedenti[0].split("[.]")[0]);
                if (s == null) {
                    //Potrebbe essere un valore stringa
                    if (!operatore.equals("="))
                        throw new IllegalArgumentException("Con sensori a dominio discreto serve l' '=' ");
                    str1 = anticedenti[0];
                    isPrimaStr = true;
                } else {
                    sensoreCondizione[0] = s;
                    r1 = ((CategoriaSensore) s.getCategoria()).getRilevazione(anticedenti[0].split("[.]")[1]);
                    if (r1 == null) {
                        throw new IllegalArgumentException("Rilevazione " + anticedenti[0].split("[.]")[1] + " non trovato");
                    }
                }
            } else {
                isPrimoNum = true;
                const1 = Integer.parseInt(anticedenti[0]);
            }

            if (!isNumeric(anticedenti[1])) {
                Sensore s = immobile.cercaSensoreRestituisciSensore(anticedenti[1].split("[.]")[0]);
                if (s == null) {
                    if (!operatore.equals("="))
                        throw new IllegalArgumentException("Con sensori a dominio discreto serve l' '=' ");
                    str2 = anticedenti[1];
                    isSecondaStr = true;
                } else {
                    sensoreCondizione[1] = s;
                    r2 = ((CategoriaSensore) s.getCategoria()).getRilevazione(anticedenti[1].split("[.]")[1]);
                    if (r2 == null) {
                        throw new IllegalArgumentException("Rilevazione " + anticedenti[1].split("[.]")[1] + " non trovato");
                    }
                }

            } else {
                isSecondoNum = true;
                const2 = Integer.parseInt(anticedenti[1]);
            }

            if (!operatore.equals("=")) {
                if (r1 != null && !r1.isMassimoMinimo()) {
                    throw new IllegalArgumentException("Tutte le rilevazioni dovrebbero essere numeriche e non a stati, errore");
                }
                if (r2 != null && !r2.isMassimoMinimo()) {
                    throw new IllegalArgumentException("Tutte le rilevazioni dovrebbero essere numeriche e non a stati, errore");
                }
            }

            if (isPrimoNum && !isSecondoNum)
                condizione = new AntecedenteNumeroSensore(const1, r2, operatore);
            else if (!isPrimoNum && isSecondoNum) {
                if (operatore.contains(">"))
                    operatore = operatore.replace(">", "<");
                else if (operatore.contains("<"))
                    operatore = operatore.replace("<", ">");
                condizione = new AntecedenteNumeroSensore(const2, r1, operatore);
            } else if (isPrimaStr && !isSecondaStr)
                condizione = new AntecedenteStringaSensore(str1, r2, operatore);
            else if (!isPrimaStr && isSecondaStr)
                condizione = new AntecedenteStringaSensore(str2, r1, operatore);
            else if (isPrimoNum && isSecondoNum)
                condizione = new AntecedenteNumeroNumero(const1, const2, operatore);
            else
                condizione = new AntecedenteSensoreSensore(r1, r2, operatore);
        }

        String[] conseguenti;

        String conseguenteMultiplo = expr.substring(expr.indexOf("then") + 5);
        for(String conseguente : conseguenteMultiplo.split(",")) {
            conseguenti = conseguente.split(":=");
            conseguenti[0] = conseguenti[0].trim();
            conseguenti[1] = conseguenti[1].trim();

            if (conseguenti[0].equals("start")) {
                int ora = Integer.parseInt(conseguenti[1].split("[.]")[0]);
                int minuto = Integer.parseInt(conseguenti[1].split("[.]")[1]);
                orario = new Orario(ora, minuto);
            } else {

                Attuatore a = immobile.cercaAttuatoreRestituisciAttuatore(conseguenti[0].split("[.]")[0]);
                if (a == null) {
                    throw new IllegalArgumentException("Attuatore con nome " + conseguenti[0].split("[.]")[0] + " non trovato");
                }
                if (((CategoriaAttuatore) a.getCategoria()).getModalitaOperativa().containsKey(conseguenti[0].split("[.]")[1]))
                    modOp = ((CategoriaAttuatore) a.getCategoria()).getModalitaOperativa().get(conseguenti[0].split("[.]")[1]);
                else
                    throw new IllegalArgumentException("Modalita operativa con nome " + conseguenti[0].split("[.]")[1] + " non trovata");

                if (!modOp.isAStati()) {
                    if (modOp.getParametri().containsKey(conseguenti[0].split("[.]")[2]))
                        parametro = modOp.getParametri().get(conseguenti[0].split("[.]")[2]);
                    else
                        throw new IllegalArgumentException("Parametro " + conseguenti[0].split("[.]")[2] + " non esistente");
                }

                DatiAttuatore datiAttuatore = a.getDatiAttuali().get(conseguenti[0].split("[.]")[1]);

                if (modOp.isAStati()) {
                    str3 = conseguenti[1];
                    if (!modOp.getStati().contains(str3))
                        throw new IllegalArgumentException("La modalita` operativa non contiene lo stato " + str3);
                    azione.add(new Conseguente(datiAttuatore, str3, a, modOp.getNome()));
                } else {
                    if (isNumeric(conseguenti[1]))
                        const3 = Integer.parseInt(conseguenti[1]);
                    else
                        throw new IllegalArgumentException("La modalita` operativa accetta solo numeri");
                    azione.add(new Conseguente(datiAttuatore, parametro.getNome(), const3, a, modOp.getNome()));
                }
            }
        }

    }

    public void valuta() {
        if (antecedente())
            conseguente();
    }

    public void stampa(){
        System.out.println(expr);
    }

    private void conseguente() {
        if(orario!=null){
            for(Conseguente conseguente: azione)
                Orologio.registrati(conseguente,orario);
        }
        else {
            for (Conseguente conseguente : azione) {
                conseguente.aziona();
            }
        }
    }

    private boolean antecedente() {
        if((sensoreCondizione[0]!=null&&!sensoreCondizione[0].isAttivo())||
                (sensoreCondizione[1]!=null&&!sensoreCondizione[1].isAttivo()))
            return false;
        else
            return condizione.valuta();
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
