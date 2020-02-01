package Contenitori;

import Categorie.CategoriaDispositivo;

import java.io.Serializable;

public abstract class Dispositivo implements Serializable {
    protected CategoriaDispositivo categoria;
    protected String nome;
    protected boolean attivo;

    public Dispositivo(CategoriaDispositivo categoria, String nome) {
        this.categoria = categoria;
        this.nome = nome;
        attivo = true;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    public String getNome() {
        return nome;
    }

    public CategoriaDispositivo getCategoria() {
        return categoria;
    }
}
