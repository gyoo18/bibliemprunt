package bibliemprunt.données;

import bibliemprunt.models.CompteClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.util.ArrayList;

public class BanqueClient {
    private static ArrayList<CompteClient> clients = new ArrayList<>();

    public static void initialiser() {
        try (var stream = BanqueClient.class.getClassLoader().getResourceAsStream("clients.json")) {
            if (stream == null) {
                throw new RuntimeException("Fichier clients.json non trouvé dans les ressources");
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new java.io.InputStreamReader(stream), JsonObject.class);
            JsonArray clientsArray = jsonObject.getAsJsonArray("clients");

            for (int i = 0; i < clientsArray.size(); i++) {
                JsonObject clientJson = clientsArray.get(i).getAsJsonObject();
                String numeroCompte = clientJson.get("numeroCompte").getAsString();
                int nip = clientJson.get("nip").getAsInt();
                String nom = clientJson.get("prenom").getAsString() + " " + clientJson.get("nom").getAsString();

                CompteClient client = new CompteClient(numeroCompte, nip, nom);
                clients.add(client);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des clients: " + e.getMessage());
            e.printStackTrace();
        }
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
        for (CompteClient client : clients) {
            if (client.numeroCompte.equals(numéroCompte)) {
                if (client.authentifier(NIP)) {
                    return client;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public static void enregistrerClient(CompteClient client) {
        if (!clients.contains(client)) {
            clients.add(client);
        }
    }

    public static void retirerClient(CompteClient client) {
        clients.remove(client);
    }

    public static boolean clientExiste(String nomUtilisateur) {
        for (CompteClient client : clients) {
            if (client.numeroCompte.equals(nomUtilisateur)) {
                return true;
            }
        }

        return false;
    }
}
