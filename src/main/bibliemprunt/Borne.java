package bibliemprunt;

import java.util.ArrayList;

import bibliemprunt.gui.GUI;
import bibliemprunt.models.CompteClient;
import bibliemprunt.models.Emprunt;

public class Borne {
    private CompteClient clientSession;
    private ArrayList<Emprunt> empruntsEnCours;
    private GUI gui;

    public Borne() {
        // TODO implémenter constructeur
        throw new UnsupportedOperationException();
    }

    public void démarrerSession(int numeroCompte, int NIP) {
        // TODO implémenter constructeur
        throw new UnsupportedOperationException();
    }

    public Emprunt emprunterLivre(int RIFD) {
        // TODO implémenter constructeur
        throw new UnsupportedOperationException();
    }

    public void annulerEmprunt(Emprunt emprunt) {
        // TODO implémenter constructeur
        throw new UnsupportedOperationException();
    }

    public void confirmerEmprunts() {
        // TODO implémenter constructeur
        throw new UnsupportedOperationException();
    }

    public void fermerSession() {
        // TODO implémenter constructeur
        throw new UnsupportedOperationException();
    }

    public boolean clientExiste(int numéroCompte) {
        // TODO implémenter constructeur
        throw new UnsupportedOperationException();
    }

    public int duréeCompteBloqué(CompteClient compte) {
        // TODO implémenter constructeur
        throw new UnsupportedOperationException();
    }

    public void annulerTransaction() {
        // TODO implémenter constructeur
        throw new UnsupportedOperationException();
    }
}
