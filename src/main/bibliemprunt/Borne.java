package bibliemprunt;

import java.util.ArrayList;
import java.util.Date;

import bibliemprunt.données.BanqueClient;
import bibliemprunt.données.BanqueEmprunts;
import bibliemprunt.données.BanqueLivres;
import bibliemprunt.models.CompteClient;
import bibliemprunt.models.Emprunt;
import bibliemprunt.models.Livre;

public class Borne {
    private CompteClient clientSession;
    private ArrayList<Emprunt> empruntsEnCours;

    public Borne() {
        this.clientSession = null;
        this.empruntsEnCours = new ArrayList<>();
    }

    public void démarrerSession(String numeroCompte, String NIP) {
        CompteClient client = BanqueClient.authentifierClient(numeroCompte, NIP);
        if (client == null) {
            throw new IllegalArgumentException("Authentification échouée");
        }
        if (client.estcompteBloque()) {
            throw new IllegalStateException("Compte bloqué");
        }
        this.clientSession = client;
        this.empruntsEnCours.clear();
    }

    public Emprunt emprunterLivre(int RFID) {
        if (clientSession == null) {
            throw new IllegalStateException("Aucune session active");
        }

        // Vérifier que le client n'a pas atteint le max d'emprunts
        int nbEmpruntsTotal = clientSession.nbEmpruntsActifs() + empruntsEnCours.size();
        if (nbEmpruntsTotal >= Paramètre.nbMaxEmprunts) {
            throw new IllegalStateException("Nombre maximum d'emprunts atteint");
        }

        Livre livre = BanqueLivres.avoirLivre(RFID);
        if (livre == null) {
            throw new IllegalArgumentException("Livre non trouvé");
        }

        Emprunt emprunt = new Emprunt(livre, clientSession, new Date(), Paramètre.duréeEmpruntDéfaut);
        empruntsEnCours.add(emprunt);
        return emprunt;
    }

    public void annulerEmprunt(Emprunt emprunt) {
        empruntsEnCours.remove(emprunt);
    }

    public void confirmerEmprunts() {
        for (Emprunt emprunt : empruntsEnCours) {
            BanqueEmprunts.enregistrerEmprunt(emprunt);
            clientSession.enregistrerEmprunt(emprunt);
            // Retirer le livre du stock
            BanqueLivres.retirerLivre(emprunt.livre.RFID);
        }
        empruntsEnCours.clear();
    }

    public void fermerSession() {
        this.clientSession = null;
        this.empruntsEnCours.clear();
    }

    public boolean clientExiste(String numéroCompte) {
        return BanqueClient.authentifierClient(numéroCompte, "") != null;
    }

    public int duréeCompteBloqué(CompteClient compte) {
        if (!compte.estcompteBloque()) {
            return 0;
        }
        long tempsEcoule = (System.currentTimeMillis() - compte.getTempsBloque()) / 1000;
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
}
