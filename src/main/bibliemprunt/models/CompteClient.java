package bibliemprunt.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import bibliemprunt.Paramètre;

public class CompteClient {
    public final String nomCompte;
    public final int NIP;
    public final String nom;
    private boolean compteBloque;
    private long tempsBloque;
    private byte nbTentativesAuthentification;

    private final static long JOURS_EN_MILLIS = 86_400_000;

    public CompteClient(String numeroCompte, int NIP, String nom) {
        this.nomCompte = numeroCompte;
        this.NIP = NIP;
        this.nom = nom;
        this.compteBloque = false;
        this.tempsBloque = 0;
        this.nbTentativesAuthentification = 0;
    }

    public CompteClient(String numeroCompte, int NIP, String nom, boolean compteBloque, long tempsBloque,
            byte nbTentativesAuthentification) {
        this.nomCompte = numeroCompte;
        this.NIP = NIP;
        this.nom = nom;
        this.compteBloque = compteBloque;
        this.tempsBloque = tempsBloque;
        this.nbTentativesAuthentification = nbTentativesAuthentification;
    }

    public boolean authentifier(int NIP) {
        if (this.NIP != NIP) {
            this.nbTentativesAuthentification++;

            if (this.nbTentativesAuthentification >= Paramètre.authentificationEssaisMax) {
                bloquerCompte();
            }

            return false;
        }

        if (compteBloque && System.currentTimeMillis() > tempsBloque + Paramètre.duréeCompteBlocage) {
            nbTentativesAuthentification = 0;
            compteBloque = false;
        } else {
            bloquerCompte();
            return false;
        }

        this.nbTentativesAuthentification = 0;
        return true;
    }

    public void bloquerCompte() {
        this.compteBloque = true;
        this.tempsBloque = System.currentTimeMillis();
    }

    public boolean estcompteBloque() {
        if (compteBloque && System.currentTimeMillis() > tempsBloque + Paramètre.duréeCompteBlocage) {
            nbTentativesAuthentification = 0;
            compteBloque = false;
        }
        return compteBloque;
    }

    public long getTempsBloque() {
        return tempsBloque;
    }

    public byte avoirNbTentativesAuthentification() {
        return nbTentativesAuthentification;
    }

    public void donnerNbTentativesAuthentification(byte n) {
        this.nbTentativesAuthentification = n;
    }
}
