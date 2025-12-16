package bibliemprunt.données;

import bibliemprunt.models.CompteClient;
import bibliemprunt.models.Livre;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BanqueLivres {

    public static void initialiser() {
    }

    public static Livre avoirLivre(int RFID) {
        ResultSet rs = SQLInterface.avoirLivre(RFID);
        try {
            if (!rs.next()) {
                return null;
            }

            Livre livre = new Livre(rs.getInt("RFID"), rs.getString("titre"), rs.getString("auteur"),
                    rs.getString("edition"), rs.getInt("annee_parution"), rs.getInt("nb_pages"));
            return livre;
        } catch (SQLException e) {
            System.err.println(
                    "[BanqueLivre.avoirLivre]: Impossible de convertir le résultat en Livre : \n\t" + e.getMessage());
            return null;
        }
    }

    public static void enregistrerLivre(Livre livre) {
        SQLInterface.enregistrerLivre(livre);
    }

    public static void retirerLivre(int RFID) {
        SQLInterface.retirerLivre(RFID);
    }

    public static Livre[] avoirListeLivres() {
        ResultSet rs = SQLInterface.avoirListeLivres();
        Livre[] livres = new Livre[SQLInterface.avoirNbLivres()];
        int i = 0;
        try {
            while (rs.next()) {
                livres[i] = new Livre(rs.getInt("RFID"), rs.getString("titre"), rs.getString("auteur"),
                        rs.getString("edition"), rs.getInt("annee_parution"), rs.getInt("nb_pages"));
                i++;
            }
        } catch (SQLException e) {
            System.err.println(
                    "[BanqueLivre.avoirListeLivre]: Impossible de convertir le résultat en Livres : \n\t"
                            + e.getMessage());
            return null;
        }

        return livres;
    }
}
