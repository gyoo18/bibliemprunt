package bibliemprunt.données;

import bibliemprunt.models.CompteClient;
import bibliemprunt.models.Emprunt;
import bibliemprunt.models.Livre;

import java.util.ArrayList;

public class BanqueEmprunts {
    private static ArrayList<Emprunt> emprunts = new ArrayList<>();

    public static void initialiser() {
        // Aucune initialisation nécessaire, la liste commence vide
    }

    public static Emprunt avoirEmprunt(Livre livre) {
        for (Emprunt emprunt : emprunts) {
            if (emprunt.livre.RFID == livre.RFID) {
                return emprunt;
            }
        }
        return null;
    }

    public static Emprunt[] listeEmpruntsActifs(CompteClient compteClient) {
        return emprunts.stream()
                .filter(e -> e.client.numeroCompte.equals(compteClient.numeroCompte))
                .toArray(Emprunt[]::new);
    }

    public static void enregistrerEmprunt(Emprunt emprunt) {
        emprunts.add(emprunt);
    }

    public static Emprunt[] listeEmpruntsGlobal() {
        return emprunts.toArray(new Emprunt[0]);
    }

    public static Emprunt[] listeHistorique() {
        return emprunts.toArray(new Emprunt[0]);
    }

    public static Emprunt avoirEmprunt(int empruntID) {
        if (empruntID >= 0 && empruntID < emprunts.size()) {
            return emprunts.get(empruntID);
        }
        return null;
    }
}
