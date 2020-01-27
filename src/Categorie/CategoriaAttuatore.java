package Categorie;

import java.io.Serializable;
import java.util.HashMap;

public class CategoriaAttuatore extends CategoriaDispositivo implements Serializable {
    private HashMap<String,ModalitaOperativa> modalita = new HashMap<>();

    public CategoriaAttuatore(String nome) {
        super(nome);
    }

    public void addModalita(ModalitaOperativa modalita) {
        this.modalita.put(modalita.getNome(),modalita);
    }

    public HashMap<String, ModalitaOperativa> getModalitaOperativa(){
        return modalita;
    }
}
