package bibliemprunt.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CompteClient {
    public final String numeroCompte;
    public final String NIP;
    public final String nom;
    private List<Emprunt> historiqueEmprunts;
    private boolean compteBloque;
    private long tempsBloque;

    public CompteClient(String numeroCompte, String NIP, String nom) {
        this.numeroCompte = numeroCompte;
        this.NIP = NIP;
        this.nom = nom;
        this.historiqueEmprunts = new ArrayList<>();
        this.compteBloque = false;
        this.tempsBloque = 0;
    }

    public List<Emprunt> getHistoriqueEmprunts() {
        return historiqueEmprunts;
    }

    public List<Emprunt> avoirEmpruntsActifs() {
        Date maintenant = new Date();
        return historiqueEmprunts.stream()
                .filter(emprunt -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(emprunt.dateEmprunt);
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

    public long getTempsBloque() {
        return tempsBloque;
    }
}
