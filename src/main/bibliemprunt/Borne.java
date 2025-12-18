package bibliemprunt;

import java.util.ArrayList;
import java.util.Date;

import bibliemprunt.données.BanqueClient;
import bibliemprunt.données.BanqueEmprunts;
import bibliemprunt.données.BanqueLivres;
import bibliemprunt.gui.GUI;
import bibliemprunt.models.CompteClient;
import bibliemprunt.models.Emprunt;
import bibliemprunt.models.Livre;

public class Borne {

    public boolean estActive;

    private CompteClient clientSession;
    private ArrayList<Emprunt> empruntsEnCours;
    private GUI gui;

    public Borne() {
        System.out.println("Initialisation de la borne d'emprunt...");

        this.clientSession = null;
        this.empruntsEnCours = new ArrayList<>();
        this.gui = new GUI(this);

        this.estActive = true;
    }

    public void miseÀJour() {
        this.gui.miseÀJour();
    }

    public void destruction() {
        System.out.println("Fermeture de la borne d'emprunt...");

        this.gui.destruction();
    }

    /**
     * Cette fonction démarre une session utilisateur et l'authentifie à l'aide d'un
     * nom d'utilisateur et un NIP.
     * De plus, elle renvoie un code d'erreur que le GUI pourra gérer.
     * 
     * # Codes d'erreurs :
     * 0. L'authentification a réussi.
     * 1. Le compte n'existe pas.
     * 2. Le compte est bloqué.
     * 3. Le NIP est mauvais.
     * 
     * @param numeroCompte
     * @param NIP
     * @return Code d'erreur
     */
    public int démarrerSession(String numeroCompte, int NIP) {

        if (!BanqueClient.clientExiste(numeroCompte)) {
            return 1; // Le compte n'existe pas
        }

        if (BanqueClient.avoirClient(numeroCompte).estcompteBloque()) {
            return 2; // Le compte est bloqué
        }

        CompteClient client = BanqueClient.authentifierClient(numeroCompte, NIP);
        if (client == null) {
            return 3; // Le NIP est mauvais
        }
        this.clientSession = client;
        this.empruntsEnCours.clear();
        return 0;
    }

    public CompteClient avoirClient(String nomCompte) {
        return BanqueClient.avoirClient(nomCompte);
    }

    public int avoirEmpruntsActifs() {
        return BanqueEmprunts.avoirNbEmprunts(clientSession) + empruntsEnCours.size();
    }

    /**
     * Cette fonction tente d'emprunter un livre à partir de son RFID.
     * Elle renvoie un code d'erreur que le GUI pourra gérer.
     * 
     * # Codes d'erreurs :
     * 0. L'emprunt est un succès.
     * 1. Le nombre maximal d'emprunts est atteint
     * 2. Le livre n'existe pas.
     * 3. Le livre est déjà emprunté.
     * 
     * @param RFID
     * @return Code d'erreur.
     */
    public int emprunterLivre(int RFID) {
        if (clientSession == null) {
            throw new IllegalStateException("Aucune session active");
        }

        // Vérifier que le client n'a pas atteint le max d'emprunts
        int nbEmpruntsTotal = BanqueEmprunts.avoirNbEmprunts(clientSession) + empruntsEnCours.size();
        if (nbEmpruntsTotal >= Paramètre.nbMaxEmprunts) {
            return 1; // Nombre maximal d'emprunts atteint.
        }

        Livre livre = BanqueLivres.avoirLivre(RFID);
        if (livre == null) {
            return 2; // Le livre n'existe pas.
        }

        if (BanqueEmprunts.estLivremprunté(livre)) {
            return 3; // Le livre est déjà emprunté
        }
        for (Emprunt emprunt : empruntsEnCours) {
            if (livre.RFID == emprunt.livre.RFID) {
                return 3; // Le livre est déjà emprunté
            }
        }

        Emprunt emprunt = new Emprunt(livre, clientSession, new Date(), Paramètre.duréeEmpruntDéfaut);
        empruntsEnCours.add(emprunt);
        return 0;
    }

    public Livre[] avoirLivresDisponibles() {
        Livre[] livres = BanqueLivres.avoirListeLivres();
        int nLivresDispo = 0;
        for (int i = 0; i < livres.length; i++) {
            if (BanqueEmprunts.estLivremprunté(livres[i])) {
                livres[i] = null;
                continue;
            }
            nLivresDispo++;
        }

        Livre[] livresDispo = new Livre[nLivresDispo];
        int i = 0;
        for (Livre livre : livres) {
            if (livre == null) {
                continue;
            }

            livresDispo[i] = livre;
            i++;
        }

        return livresDispo;
    }

    public void annulerEmprunt(Emprunt emprunt) {
        empruntsEnCours.remove(emprunt);
    }

    public void confirmerEmprunts() {
        for (Emprunt emprunt : empruntsEnCours) {
            BanqueEmprunts.enregistrerEmprunt(emprunt);
        }
        empruntsEnCours.clear();
    }

    public void fermerSession() {
        BanqueClient.mettreÀJourClient(clientSession);
        this.clientSession = null;
        this.empruntsEnCours.clear();
    }

    public boolean clientExiste(String numéroCompte) {
        return BanqueClient.clientExiste(numéroCompte);
    }

    public int duréeCompteBloqué() {
        if (!this.clientSession.estcompteBloque()) {
            return 0;
        }
        long tempsEcoule = (System.currentTimeMillis() - this.clientSession.getTempsBloque()) / 1000;
        int dureeRestante = Paramètre.duréeCompteBlocage - (int) tempsEcoule;
        return Math.max(0, dureeRestante);
    }

    public void annulerTransaction() {
        empruntsEnCours.clear();
    }

    public CompteClient getClientSession() {
        return clientSession;
    }

    public ArrayList<Emprunt> getEmpruntsEnCours() {
        return empruntsEnCours;
    }

    public void quitter() {
        this.estActive = false;
    }

    public void sigkillQuitter() {
        this.estActive = false;
        gui.sigkillQuitter();
    }
}
