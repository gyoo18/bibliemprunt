package bibliemprunt.models;

import java.util.Date;

public class Emprunt {
    public final Livre livre;
    public final CompteClient client;
    public final Date dateEmprunt;
    private int dureeEmprunt;

    public Emprunt(Livre livre, CompteClient client, Date dateEmprunt, int dureeEmprunt) {
        this.livre = livre;
        this.client = client;
        this.dateEmprunt = dateEmprunt;
        this.dureeEmprunt = dureeEmprunt;
    }

    public int getDureeEmprunt() {
        return dureeEmprunt;
    }

    public void setDureeEmprunt(int duree) {
        this.dureeEmprunt = duree;
    }
}
