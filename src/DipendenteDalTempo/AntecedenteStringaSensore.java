package DipendenteDalTempo;

import Categorie.Rilevazione;


public class AntecedenteStringaSensore implements Antecedente {

    private String str1;
    private Rilevazione r1;
    private String operatore;

    public AntecedenteStringaSensore(String str1, Rilevazione r1, String operatore) {
        this.str1 = str1;
        this.r1 = r1;
        this.operatore = operatore;
    }

    @Override
    public boolean valuta() {
        return str1.equals(r1.getValoreSenzaUnitaMisura());
    }
}
