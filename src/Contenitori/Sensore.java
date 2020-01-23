package Contenitori;

import Categorie.CategoriaSensore;

public class Sensore extends Dispositivo{
    public Sensore(CategoriaSensore categoria, String nome) {
        super(categoria, nome);
    }

    public String getValore(){
        return ((CategoriaSensore)categoria).getValore();
    }
}
