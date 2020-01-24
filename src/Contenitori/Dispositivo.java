package Contenitori;

import Categorie.CategoriaDispositivo;

import java.io.Serializable;

public abstract class Dispositivo implements Serializable {
    protected CategoriaDispositivo categoria;
    protected String nome;

    public Dispositivo(CategoriaDispositivo categoria, String nome) {
        this.categoria = categoria;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public CategoriaDispositivo getCategoria() {
        return categoria;
    }
}
