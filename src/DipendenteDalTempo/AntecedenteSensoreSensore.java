package DipendenteDalTempo;

import Categorie.Rilevazione;


public class AntecedenteSensoreSensore implements Antecedente {
    private Rilevazione r1,r2;
    private String operatore;

    public AntecedenteSensoreSensore(Rilevazione r1, Rilevazione r2, String operatore) {
        this.r1 = r1;
        this.r2 = r2;
        this.operatore = operatore;
    }

    @Override
    public boolean valuta() {
        switch (operatore){
            case ">=":
                return Integer.parseInt(r1.getValoreSenzaUnitaMisura())>=Integer.parseInt(r2.getValoreSenzaUnitaMisura());
            case "<=":
                return Integer.parseInt(r1.getValoreSenzaUnitaMisura())<=Integer.parseInt(r2.getValoreSenzaUnitaMisura());
            case "=":
                if(r1.isMassimoMinimo()&&r2.isMassimoMinimo())
                    return Integer.parseInt(r1.getValoreSenzaUnitaMisura())==Integer.parseInt(r2.getValoreSenzaUnitaMisura());
                else if(!r1.isMassimoMinimo()&&!r2.isMassimoMinimo())
                    return r1.getValoreSenzaUnitaMisura().equals(r2.getValoreSenzaUnitaMisura());
                else
                    new IllegalArgumentException("Comparazione tra sensori uno a stati uno parametrico illegale");
            case "<":
                return Integer.parseInt(r1.getValoreSenzaUnitaMisura())<Integer.parseInt(r2.getValoreSenzaUnitaMisura());
            case ">":
                return Integer.parseInt(r1.getValoreSenzaUnitaMisura())>Integer.parseInt(r2.getValoreSenzaUnitaMisura());
        }
        return false;
    }
}
