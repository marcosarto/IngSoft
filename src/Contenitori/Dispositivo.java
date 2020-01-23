package Contenitori;

import Categorie.CategoriaDispositivo;

public abstract class Dispositivo {
    protected CategoriaDispositivo categoria;
    protected String nome;

    public Dispositivo(CategoriaDispositivo categoria, String nome) {
        this.categoria = categoria;
        this.nome = nome;
    }

    public CategoriaDispositivo getCategoria() {
        return categoria;
    }
}
