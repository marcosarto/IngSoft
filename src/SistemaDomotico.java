import java.util.ArrayList;

public class SistemaDomotico {

    ArrayList<UnitaImmobiliare> unitaImmobiliari = new ArrayList();
    ArrayList<CategoriaSensore> categorieSensori = new ArrayList();
    ArrayList<CategoriaAttuatore> categorieAttuatori = new ArrayList();

    public static void main(String[] args) {new SistemaDomotico();}

    public SistemaDomotico() {
        aggiungiUnitaImmobiliare();

        int risposta = Interazione.interrogazione("Seleziona la modalita` operativa",
                new String[]{"Utente", "Manutentore"});

        switch (risposta) {
            case 0:
                flussoFruitore();
                break;
            case 1:
                //flussoManutentore();
                break;
        }
    }

    public void aggiungiUnitaImmobiliare(){
        if(unitaImmobiliari.size()<1) {
            unitaImmobiliari.add(new UnitaImmobiliare());
        }
        else
            System.out.println("Esiste gia un'unita immobiliare, nella prima versione massimo una");

    }

    private int visualizzaElencoUnitaImmobiliari(){
        //riempie entrateUnitaImmobiliare con i nomi delle unita immobiliari
        String[] entrateUnitaImmobiliare = new String[unitaImmobiliari.size()];
        for(int i=0;i<entrateUnitaImmobiliare.length;i++){
            entrateUnitaImmobiliare[i]=unitaImmobiliari.get(i).toString();
        }

        int risposta = Interazione.interrogazione("Seleziona l'unita' immobiliare che vuoi ispezionare :",
                entrateUnitaImmobiliare);
        return risposta;
    }

    private boolean aggiungiCategorieSensori(){
        String risposta = Interazione.domanda("Nome della categoria : ");
        if (categorieSensori.contieneNome(risposta)) {
            System.out.print("Gia` presente, ritorno alla schermata precedente");
            return false;
        }
        CategoriaSensore categoriaSensore = new CategoriaSensore();
        risposta = Interazione.domanda("Testo libero (massimo 180 caratteri) : ");
        if()
        
    }

    private void flussoFruitore(){
        int numeroUnitaImmobiliare = visualizzaElencoUnitaImmobiliari();
        unitaImmobiliari.get(numeroUnitaImmobiliare).flussoFruitore();
    }

    private void flussoManutentore(){
        int risposta = Interazione.interrogazione("Cosa vuoi fare :",
                new String[]{"Aggiungi categorie sensori",
                        "Aggiungi categoria attuatori",
                        "Creare e descrivere l'unita` immobiliare",
                        "Selezionare un'unita` immobiliare per lavorarci"});
        switch (risposta){
            case 2:
                aggiungiUnitaImmobiliare();
                break;


        }
    }
    
    private boolean contieneNome(String nome){
        for (:
             ) {
            
        }
    }


}
