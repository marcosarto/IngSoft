package Contenitori;

import Categorie.CategoriaAttuatore;

public class Attuatore {
    private CategoriaAttuatore categoria;
    private String nome;
    //Attuatori e sensori possono appartenere a 1..n stanze OPPURE 1..n artefatti
    private boolean appartieneStanza,appartieneArtefatto;
}
