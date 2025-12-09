package bibliemprunt.models;

import java.util.Date;

public class Emprunt {
    private Livre livre;
    private CompteClient client;
    private Date dateEmprunt;
    private int dureeEmprunt;

    public Emprunt(Livre livre, CompteClient client, Date dateEmprunt, int dureeEmprunt) {
        this.livre = livre;
        this.client = client;
        this.dateEmprunt = dateEmprunt;
        this.dureeEmprunt = dureeEmprunt;
    }

    public Livre getLivre() {
        return livre;
    }

    public CompteClient getClient() {
        return client;
    }

    public Date getDateEmprunt() {
        return dateEmprunt;
    }

    public int getDureeEmprunt() {
        return dureeEmprunt;
    }

    public void setDureeEmprunt(int duree) {
        this.dureeEmprunt = duree;
    }
}
