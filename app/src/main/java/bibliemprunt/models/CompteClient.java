package bibliemprunt.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CompteClient {
    private int numeroCompte;
    private int NIP;
    private String nom;
    private List<Emprunt> historiqueEmprunts;
    private boolean compteBloque;
    private long tempsBloque;

    public CompteClient(int numeroCompte, int NIP, String nom) {
        this.numeroCompte = numeroCompte;
        this.NIP = NIP;
        this.nom = nom;
        this.historiqueEmprunts = new ArrayList<>();
        this.compteBloque = false;
        this.tempsBloque = 0;
    }

    public int getNumeroCompte() {
        return numeroCompte;
    }

    public int getNIP() {
        return NIP;
    }

    public String getNom() {
        return nom;
    }

    public List<Emprunt> getHistoriqueEmprunts() {
        return historiqueEmprunts;
    }

    public List<Emprunt> avoirEmpruntsActifs() {
        Date maintenant = new Date();
        return historiqueEmprunts.stream()
                .filter(emprunt -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(emprunt.getDateEmprunt());
                    cal.add(Calendar.DATE, emprunt.getDureeEmprunt());
                    Date dateRetour = cal.getTime();
                    return dateRetour.after(maintenant) || dateRetour.equals(maintenant);
                })
                .collect(Collectors.toList());
    }

    public void enregistrerEmprunt(Emprunt emprunt) {
        this.historiqueEmprunts.add(emprunt);
    }

    public void bloquerCompte() {
        this.compteBloque = true;
        this.tempsBloque = System.currentTimeMillis();
    }

    public boolean estcompteBloque() {
        return compteBloque;
    }

    public int nbEmpruntsActifs() {
        return avoirEmpruntsActifs().size();
    }

    public boolean getcompteBloque() {
        return compteBloque;
    }

    public long getTempsBloque() {
        return tempsBloque;
    }
}
