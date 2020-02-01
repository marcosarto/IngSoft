package DipendenteDalTempo;

public class Orario {
    private int ora,minuto;

    public Orario(int ora, int minuto) {
        this.ora = ora;
        this.minuto = minuto;
    }

    public int getOra() {
        return ora;
    }

    public int getMinuto() {
        return minuto;
    }

    public boolean isOra(int ora, int minuto){
        return ora==this.ora&&minuto==this.minuto;
    }
}
