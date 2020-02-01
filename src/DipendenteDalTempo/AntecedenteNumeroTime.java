package DipendenteDalTempo;


public class AntecedenteNumeroTime implements Antecedente {

    private int ora,minuto;
    private String operatore;

    public AntecedenteNumeroTime(int ora,int minuto, String operatore) {
        this.ora = ora;
        this.minuto = minuto;
        this.operatore = operatore;
    }

    @Override
    public boolean valuta() {
        switch (operatore){
            case ">=":
                if(ora<Orologio.getOra().getOra()||(ora==Orologio.getOra().getOra()&&minuto<Orologio.getOra().getMinuto()))
                    return false;
                else
                    return true;
            case "<=":
                if(ora>Orologio.getOra().getOra()||(ora==Orologio.getOra().getOra()&&minuto>Orologio.getOra().getMinuto()))
                    return false;
                else
                    return true;
            case "=":
                return ora==Orologio.getOra().getOra()&&minuto==Orologio.getOra().getMinuto();
            case "<":
                if(ora>Orologio.getOra().getOra()||(ora==Orologio.getOra().getOra()&&minuto>=Orologio.getOra().getMinuto()))
                    return false;
                else
                    return true;
            case ">":
                if(ora<Orologio.getOra().getOra()||(ora==Orologio.getOra().getOra()&&minuto<=Orologio.getOra().getMinuto()))
                    return false;
                else
                    return true;
        }
        return false;
    }
}
