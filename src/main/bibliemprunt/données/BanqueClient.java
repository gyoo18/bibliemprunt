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
                String nip = clientJson.get("nip").getAsString();
                String nom = clientJson.get("nom").getAsString();

                CompteClient client = new CompteClient(numeroCompte, nip, nom);
                clients.add(client);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des clients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static CompteClient authentifierClient(String numéroCompte, String NIP) {
        for (CompteClient client : clients) {
            if (client.numeroCompte.equals(numéroCompte) && client.NIP.equals(NIP)) {
                return client;
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
}
