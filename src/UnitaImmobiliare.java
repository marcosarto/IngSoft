import java.util.ArrayList;

public class UnitaImmobiliare {
    private String nomeUnitaImmobiliare,tipoUnitaImmobiliare;
    private ArrayList<Stanza> stanze = new ArrayList<>();
    private ArrayList<Artefatto> artefattiEsterni = new ArrayList<>();

    public UnitaImmobiliare() {
        String nome = Interazione.domanda("Nome dell'unita immobiliare?");
        this.nomeUnitaImmobiliare=nome;
        String tipo = Interazione.domanda("Tipologia unita immobiliare?");
        this.tipoUnitaImmobiliare = tipo;
        int risposta = Interazione.interrogazione
                ("Bisogna aggiungere almeno una stanza o un artefatto," +
                        " (nel seguito se ne potranno aggiungere altri)",
                        new String[]{"Artefatto","Stanza"});

        switch (risposta){
            case 0:
                aggiungiArtefatto();
                break;
            case 1:
                aggiungiStanza();
                break;
        }
    }

    public void aggiungiStanza(){
        do {
            String stanza = Interazione.domanda("Inserisci nome stanza(0 per uscire) : ");
            if(stanza.equals("0")) break;


            boolean esiste=false;

            //Se c'e` gia almeno una stanza controlla che il nome non sia duplicato
            if (stanze.size() != 0) {
                for (Stanza s :
                        stanze) {
                    if (s.getNome().equals(stanza))
                        esiste = true;
                }
                if (esiste){
                    System.out.println("Nome stanza gia esistente, non aggiunta");
                    continue;
                }
            }

            if(!esiste) stanze.add(new Stanza(stanza));

        }while(true);
    }

    public void aggiungiArtefatto(){
        do {
            String artefatto = Interazione.domanda("Inserisci nome artefatto(0 per uscire) : ");
            if (artefatto.equals("0")) break;

            int stanzaDaAbbinare = Interazione.interrogazione
                    ("In quale stanza vuoi aggiungere l'artefatto " +
                                    "(se non si seleziona nessuna stanza verra` abbinato all'esterno)",
                            stanze.toArray(new String[0])
                            );
            switch (stanzaDaAbbinare){
                case -1:
                    artefattiEsterni.add(new Artefatto(artefatto));
                    break;
                default:
                    boolean successo = stanze.get(stanzaDaAbbinare).aggiungiArtefatto(artefatto);
                    if(!successo)
                        System.out.println("Il nome dell'artefatto esiste gia` in questa stanza");
            }

        }while (true);

    }

    public void flussoFruitore(){
        int risposta = Interazione.interrogazione("Cosa vuoi fare?",
                new String[]{"Visualizza piantina unita' immobiliare","Visualizza valori di sensori specifici"});
        switch(risposta){
            case 0:
                stampaAlberoUnitaImmobiliare();
                break;
            case 1:
                //proceduraLetturaSensori();
                break;
        }
    }

    private void stampaAlberoUnitaImmobiliare(){
        StringBuilder tree = new StringBuilder();
        tree.append(nomeUnitaImmobiliare);
        for (Stanza s :
                stanze) {
            tree.append("/t"+s.getNome());
            for (String sensore :
                    s.getCategoriaSensoriPresenti()) {
                tree.append("/t/t"+sensore);
            }
            for (String attuatore :
                    s.getCategoriaAttuatoriPresenti()) {
                tree.append("/t/t"+attuatore);
            }
        }
        System.out.println(tree.toString());
    }
}
