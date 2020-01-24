package Contenitori;

import Categorie.CategoriaAttuatore;

import java.io.Serializable;

public class Attuatore extends Dispositivo implements Serializable {
    public Attuatore(CategoriaAttuatore categoria, String nome) {
        super(categoria, nome);
    }
}
