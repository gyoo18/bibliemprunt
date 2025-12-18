package bibliemprunt.données;

import bibliemprunt.models.CompteClient;
import bibliemprunt.models.Emprunt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class BanqueClient {

    public static void initialiser() {
    }

    /**
     * Tente d'authentifier le client avec le nom d'utilisateur et le NIP fournit et
     * renvoie le compte client correspondant. Si l'authentification échoue, renvoie
     * null.
     * 
     * @param numéroCompte
     * @param NIP
     * @return CompteClient ou null
     */
    public static CompteClient authentifierClient(String numéroCompte, int NIP) {
        ResultSet rs = SQLInterface.avoirClient(numéroCompte);
        CompteClient client;
        try {
            if (!rs.next()) {
                return null;
            }
            client = new CompteClient(rs.getString("nom_compte"), rs.getInt("NIP"), rs.getString("nom_client"),
                    rs.getBoolean("est_bloque"), rs.getLong("temps_bloque") * 1000,
                    rs.getByte("nb_tentatives_authentifications"));
        } catch (SQLException e) {
            System.out.println(
                    "[BanqueClient.authentifierClient] Impossible de convertir le résultat en CompteClient:\n\t"
                            + e.getMessage());
            return null;
        }

        boolean estAuthentifié = client.authentifier(NIP);
        mettreÀJourClient(client);
        if (estAuthentifié) {
            return client;
        } else {
            return null;
        }
    }

    public static CompteClient avoirClient(String numéroCompte) {
        ResultSet rs = SQLInterface.avoirClient(numéroCompte);
        CompteClient client;
        try {
            if (!rs.next()) {
                return null;
            }
            client = new CompteClient(rs.getString("nom_compte"), rs.getInt("NIP"), rs.getString("nom_client"),
                    rs.getBoolean("est_bloque"), rs.getLong("temps_bloque") * 1000,
                    rs.getByte("nb_tentatives_authentifications"));
        } catch (SQLException e) {
            System.out.println(
                    "[BanqueClient.authentifierClient] Impossible de convertir le résultat en CompteClient:\n\t"
                            + e.getMessage());
            return null;
        }

        return client;
    }

    public static void enregistrerClient(CompteClient client) {
        SQLInterface.enregistrerClient(client);
    }

    public static void retirerClient(CompteClient client) {
        SQLInterface.retirerClient(client);
    }

    public static boolean clientExiste(String nomUtilisateur) {
        ResultSet rs = SQLInterface.avoirClient(nomUtilisateur);
        try {
            return rs.next();
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.clientExiste]: Impossible de déterminer si le client existe :\n\t" + e.getMessage());
            return false;
        }
    }

    public static void mettreÀJourClient(CompteClient client) {
        SQLInterface.mettreÀJourClient(client);
    }
}
