package bibliemprunt.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CompteClient {
    public final String numeroCompte;
    public final int NIP;
    public final String nom;
    private List<Emprunt> historiqueEmprunts;
    private boolean compteBloque;
    private long tempsBloque;
    private byte nbTentativesAuthentification;

    private final static long JOURS_EN_MILLIS = 86_400_000;

    public CompteClient(String numeroCompte, int NIP, String nom) {
        this.numeroCompte = numeroCompte;
        this.NIP = NIP;
        this.nom = nom;
        this.historiqueEmprunts = new ArrayList<>();
        this.compteBloque = false;
        this.tempsBloque = 0;
        this.nbTentativesAuthentification = 0;
    }

    public boolean authentifier(int NIP) {
        if (this.NIP != NIP) {
            this.nbTentativesAuthentification++;

            if (this.nbTentativesAuthentification >= 3) {
                bloquerCompte();
            }

            return false;
        }

        this.nbTentativesAuthentification = 0;
        return true;
    }

    public List<Emprunt> getHistoriqueEmprunts() {
        return historiqueEmprunts;
    }

    public List<Emprunt> avoirEmpruntsActifs() {
        Date dateRetour = new Date();
        Calendar cal = Calendar.getInstance();
        return historiqueEmprunts.stream()
                .filter(emprunt -> {
                    dateRetour.setTime(emprunt.dateEmprunt.getTime() + emprunt.getDureeEmprunt() * JOURS_EN_MILLIS);
                    return dateRetour.after(cal.getTime()) || dateRetour.equals(cal.getTime());
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

    public long getTempsBloque() {
        return tempsBloque;
    }
}
