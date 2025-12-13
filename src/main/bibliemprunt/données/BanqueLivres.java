package bibliemprunt.données;

import bibliemprunt.models.Livre;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.util.ArrayList;

public class BanqueLivres {
    private static ArrayList<Livre> livres = new ArrayList<>();

    public static void initialiser() {
        try (var stream = BanqueLivres.class.getClassLoader().getResourceAsStream("livres.json")) {
            if (stream == null) {
                throw new RuntimeException("Fichier livres.json non trouvé dans les ressources");
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new java.io.InputStreamReader(stream), JsonObject.class);
            JsonArray livresArray = jsonObject.getAsJsonArray("livres");

            for (int i = 0; i < livresArray.size(); i++) {
                JsonObject livreJson = livresArray.get(i).getAsJsonObject();
                int rfid = Integer.parseInt(livreJson.get("rfid").getAsString());
                String titre = livreJson.get("titre").getAsString();
                String auteur = livreJson.get("auteur").getAsString();
                String edition = livreJson.get("edition").getAsString();
                int anneeParution = livreJson.get("dateParution").getAsInt();
                int nbPages = livreJson.get("nombrePages").getAsInt();

                Livre livre = new Livre(rfid, titre, auteur, edition, anneeParution, nbPages);
                livres.add(livre);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des livres: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Livre avoirLivre(int RFID) {
        for (Livre livre : livres) {
            if (livre.RFID == RFID) {
                return livre;
            }
        }
        return null;
    }

    public static void enregistrerLivre(Livre livre) {
        if (!livres.contains(livre)) {
            livres.add(livre);
        }
    }

    public static void retirerLivre(int RFID) {
        livres.removeIf(livre -> livre.RFID == RFID);
    }
}
