package Categorie;

public class CategoriaAttuatore extends CategoriaDispositivo{
    private ModalitaOperativa modalita;

    public CategoriaAttuatore(String nome) {
        super(nome);
    }

    public void setModalita(ModalitaOperativa modalita) {
        this.modalita = modalita;
    }
}
