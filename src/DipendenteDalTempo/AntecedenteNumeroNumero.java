package DipendenteDalTempo;


public class AntecedenteNumeroNumero implements Antecedente {
    private int n1,n2;
    private String operatore;

    public AntecedenteNumeroNumero(int n1, int n2, String operatore) {
        this.n1 = n1;
        this.n2 = n2;
        this.operatore = operatore;
    }

    @Override
    public boolean valuta() {
        switch (operatore){
            case ">=":
                return n1>=n2;
            case "<=":
                return n1<=n2;
            case "=":
                return n1==n2;
            case "<":
                return n1<n2;
            case ">":
                return n1>n2;
        }
        return false;
    }
}
