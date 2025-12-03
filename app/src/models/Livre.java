package models;

public class Livre {
    private int RFID;
    private String titre;
    private String auteur;
    private String edition;
    private int anneeParution;
    private int nbPages;

    public Livre(int RFID, String titre, String auteur, String edition, int anneeParution, int nbPages) {
        this.RFID = RFID;
        this.titre = titre;
        this.auteur = auteur;
        this.edition = edition;
        this.anneeParution = anneeParution;
        this.nbPages = nbPages;
    }

    public int getRFID() {
        return RFID;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getEdition() {
        return edition;
    }

    public int getAnneeParution() {
        return anneeParution;
    }

    public int getNbPages() {
        return nbPages;
    }
}
