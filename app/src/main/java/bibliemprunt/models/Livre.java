package bibliemprunt.models;

public class Livre {
    public final int RFID;
    public final String titre;
    public final String auteur;
    public final String edition;
    public final int anneeParution;
    public final int nbPages;

    public Livre(int RFID, String titre, String auteur, String edition, int anneeParution, int nbPages) {
        this.RFID = RFID;
        this.titre = titre;
        this.auteur = auteur;
        this.edition = edition;
        this.anneeParution = anneeParution;
        this.nbPages = nbPages;
    }
}
