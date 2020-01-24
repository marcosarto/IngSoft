package Contenitori;

import Categorie.CategoriaSensore;

import java.io.Serializable;

public class Sensore extends Dispositivo implements Serializable {
    public Sensore(CategoriaSensore categoria, String nome) {
        super(categoria, nome);
    }

    public String getValore(){
        return ((CategoriaSensore)categoria).getValore();
    }
}
