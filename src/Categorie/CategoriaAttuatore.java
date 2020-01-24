package Categorie;

import java.io.Serializable;

public class CategoriaAttuatore extends CategoriaDispositivo implements Serializable {
    private ModalitaOperativa modalita;

    public CategoriaAttuatore(String nome) {
        super(nome);
    }

    public void setModalita(ModalitaOperativa modalita) {
        this.modalita = modalita;
    }
}
