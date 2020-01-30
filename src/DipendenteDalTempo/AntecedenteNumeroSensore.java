package DipendenteDalTempo;

import Categorie.Rilevazione;

public class AntecedenteNumeroSensore implements Antecedente {
    private int n1;
    private Rilevazione r1;
    private String operatore;

    public AntecedenteNumeroSensore(int n1, Rilevazione r1, String operatore) {
        this.n1 = n1;
        this.r1 = r1;
        this.operatore = operatore;
    }

    @Override
    public boolean valuta() {
        switch (operatore){
            case ">=":
                return n1>=Integer.parseInt(r1.getValoreSenzaUnitaMisura());
            case "<=":
                return n1<=Integer.parseInt(r1.getValoreSenzaUnitaMisura());
            case "=":
                return n1==Integer.parseInt(r1.getValoreSenzaUnitaMisura());
            case "<":
                return n1<Integer.parseInt(r1.getValoreSenzaUnitaMisura());
            case ">":
                return n1>Integer.parseInt(r1.getValoreSenzaUnitaMisura());
        }
        return false;
    }
}
