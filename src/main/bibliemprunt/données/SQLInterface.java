package bibliemprunt.données;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import bibliemprunt.models.CompteClient;
import bibliemprunt.models.Emprunt;
import bibliemprunt.models.Livre;

public class SQLInterface {
    private static Connection connection;

    private final static String urlDB = "jdbc:sqlite:bibliempruntDB.db";

    public static void initialiser() {
        System.out.println("Initialisation de la base de donnée...");

        // Vérifier si la base de donnée existe
        File db = new File("bibliempruntDB.db");
        if (!db.exists() || db.isDirectory()) {
            créerBaseDeDonnée();
            return;
        }

        try {
            connection = DriverManager.getConnection(urlDB);
        } catch (SQLException e) {
            System.err.println("[SQLInterface.initialiser]: Impossible de se connecter à la base de donnée.");
            return;
        }
    }

    private static void créerBaseDeDonnée() {

        try {
            connection = DriverManager.getConnection(urlDB);
        } catch (SQLException e) {
            System.err
                    .println("[SQLInterface.initialiser]: Impossible de créer la base de donnée : \n" + e.getMessage());
            return;
        } catch (Exception e) {
            System.err
                    .println("[SQLInterface.initialiser]: Impossible de créer la base de donnée : \n" + e.getMessage());
            return;
        }

        // Création des tables
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE Livres(" +
                            "RFID INT PRIMARY KEY," +
                            "titre VARCHAR(1000)," +
                            "auteur VARCHAR(300)," +
                            "edition VARCHAR(300)," +
                            "annee_parution INT," +
                            "nb_pages INT)");

            statement.executeUpdate(
                    "CREATE TABLE ComptesClients(" +
                            "nom_compte VARCHAR(300) PRIMARY KEY," +
                            "NIP INT," +
                            "nom_client VARCHAR(300)," +
                            "est_bloque BOOLEAN DEFAULT FALSE," +
                            "temps_bloque TIMESTAMP," +
                            "nb_tentatives_authentifications INT DEFAULT 0)");

            statement.executeUpdate(
                    "CREATE TABLE Emprunts(" +
                            "emprunt_id INT PRIMARY KEY," +
                            "livre INT," +
                            "client VARCHAR(300)," +
                            "date_emprunt DATE," +
                            "duree_emprunt INT," +

                            "FOREIGN KEY (livre) REFERENCES Livres(RFID)," +
                            "FOREIGN KEY (client) REFERENCES ComptesClients(nomCompte))");

        } catch (SQLException e) {
            System.out
                    .println("[SQLInterface.créerBaseDeDonnée] Impossible de créer les tables :\n\t" + e.getMessage());
            return;
        }

        // Insérer les données des clients
        try (var stream = BanqueClient.class.getClassLoader().getResourceAsStream("clients.json")) {
            if (stream == null) {
                throw new RuntimeException("Fichier clients.json non trouvé dans les ressources");
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new java.io.InputStreamReader(stream), JsonObject.class);
            JsonArray clientsArray = jsonObject.getAsJsonArray("clients");

            StringBuilder commande = new StringBuilder();
            commande.append("INSERT INTO ComptesClients(nom_compte, NIP, nom_client) VALUES ");
            for (int i = 0; i < clientsArray.size(); i++) {
                JsonObject clientJson = clientsArray.get(i).getAsJsonObject();
                String numeroCompte = clientJson.get("numeroCompte").getAsString();
                int nip = clientJson.get("nip").getAsInt();
                String nom = clientJson.get("prenom").getAsString() + " " + clientJson.get("nom").getAsString();

                commande.append("('" + numeroCompte + "'," + nip + ",'" + nom + "')");
                if (i != clientsArray.size() - 1) {
                    commande.append(",");
                }
            }

            statement.executeUpdate(commande.toString());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des clients: " + e.getMessage());
            e.printStackTrace();
        }

        // Insérer les données des livres
        try (var stream = BanqueLivres.class.getClassLoader().getResourceAsStream("livres.json")) {
            if (stream == null) {
                throw new RuntimeException("Fichier livres.json non trouvé dans les ressources");
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new java.io.InputStreamReader(stream), JsonObject.class);
            JsonArray livresArray = jsonObject.getAsJsonArray("livres");

            StringBuilder commande = new StringBuilder();
            commande.append("INSERT INTO Livres(RFID, titre, auteur, edition, annee_parution, nb_pages) VALUES ");
            for (int i = 0; i < livresArray.size(); i++) {
                JsonObject livreJson = livresArray.get(i).getAsJsonObject();
                int rfid = Integer.parseInt(livreJson.get("rfid").getAsString());
                String titre = livreJson.get("titre").getAsString().replace("'", "''");
                String auteur = livreJson.get("auteur").getAsString().replace("'", "''");
                String edition = livreJson.get("edition").getAsString().replace("'", "''");
                int anneeParution = livreJson.get("dateParution").getAsInt();
                int nbPages = livreJson.get("nombrePages").getAsInt();

                commande.append("(" + rfid + ",'" + titre + "','" + auteur + "','" + edition + "'," + anneeParution
                        + "," + nbPages + ")");
                if (i != livresArray.size() - 1) {
                    commande.append(",");
                }
            }

            statement.executeUpdate(commande.toString());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des livres: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ResultSet avoirLivre(int RFID) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(
                    "SELECT * FROM Livres WHERE RFID=" + RFID);
        } catch (SQLException e) {
            System.err.println("[SQLInterface.avoirLivre]: Impossible d'obtenir le livre :\n\t" + e.getMessage());
            return null;
        }
    }

    public static void enregistrerLivre(Livre livre) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "INSERT INTO Livres(RFID, titre, auteur, edition, annee_parution, nb_pages) VALUES " +
                            "(" + livre.RFID + ",'" + livre.titre + "','" + livre.auteur + "','" + livre.edition + "',"
                            + livre.anneeParution + "," + livre.nbPages);
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.enregistrerLivre]: Impossible d'enregistrer le livre :\n\t" + e.getMessage());
        }
    }

    public static void retirerLivre(int RFID) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM Livres WHERE RFID=" + RFID);
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.retirerLivre]: Impossible de retirer le livre :\n\t" + e.getMessage());
        }
    }

    public static ResultSet avoirListeLivres() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(
                    "SELECT * FROM Livres");
        } catch (SQLException e) {
            System.err.println("[SQLInterface.avoirListeLivres]: Impossible la liste de livres :\n\t" + e.getMessage());
            return null;
        }
    }

    public static int avoirNbLivres() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT COUNT(*) FROM Livres").getInt(1);
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirNbLivres]: Impossible d'obtenir le nombre de livres :\n\t" + e.getMessage());
            return -1;
        }
    }

    public static ResultSet avoirClient(String nomCompte) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT * FROM ComptesClients WHERE nom_compte='" + nomCompte + "'");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirClient]: Impossible d'obtenir le client :\n\t" + e.getMessage());
            return null;
        }
    }

    public static void enregistrerClient(CompteClient client) {
        try {
            Statement statement = connection.createStatement();
            statement
                    .executeUpdate(
                            "INSERT INTO ComptesClients(nom_compte, NIP, nom_client, est_bloque, temps_bloque, nb_tentatives_authentification) VALUES "
                                    +
                                    "('" + client.nomCompte + "'," + client.NIP + ",'" + client.nom + ","
                                    + client.estcompteBloque() + "," + client.getTempsBloque() + ","
                                    + client.avoirNbTentativesAuthentification() + ")");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.enregistrerClient]: Impossible d'enregistrer le client :\n\t" + e.getMessage());
        }
    }

    public static void mettreÀJourClient(CompteClient client) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE ComptesClients SET " +
                    "est_bloque=" + client.estcompteBloque() + "," +
                    "temps_bloque=" + client.getTempsBloque() / 1000 + "," +
                    "nb_tentatives_authentifications=" + client.avoirNbTentativesAuthentification() +
                    " WHERE nom_compte='" + client.nomCompte + "'");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.mettreÀJourClient]: Impossible de mettre à jour le client :\n\t" + e.getMessage());
        }
    }

    public static void retirerClient(CompteClient client) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM ComptesClients WHERE nom_compte='" + client.nomCompte + "'");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.retirerClient]: Impossible de retirer le client :\n\t" + e.getMessage());
        }
    }

    public static ResultSet avoirListeEmprunts(CompteClient client) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT * FROM Emprunts WHERE client='" + client.nomCompte + "'");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirListeEmprunts]: Impossible de trouver la liste d'emprunts du client :\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static ResultSet avoirListeEmpruntsActifs(CompteClient client) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT * FROM Emprunts WHERE client='" + client.nomCompte
                    + "' AND JULIANDAY('now')-JULIANDAY(date_emprunt,'unixepoch') < duree_emprunt");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirListeEmprunts]: Impossible de trouver la liste d'emprunts du client :\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static int avoirNbEmprunts(CompteClient client) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT COUNT(*) FROM Emprunts WHERE client='" + client.nomCompte + "'")
                    .getInt(1);
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirNbEmprunts]: Impossible de trouver le nombre d'emprunts pour le client :\n\t"
                            + e.getMessage());
            return -1;
        }
    }

    public static int avoirNbEmpruntsActifs(CompteClient client) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT COUNT(*) FROM Emprunts WHERE client='" + client.nomCompte
                    + "' AND JULIANDAY('now')-JULIANDAY(date_emprunt,'unixepoch') < duree_emprunt")
                    .getInt(1);
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirNbEmprunts]: Impossible de trouver le nombre d'emprunts pour le client :\n\t"
                            + e.getMessage());
            return -1;
        }
    }

    public static ResultSet avoirListeEmprunts(Livre livre) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT * FROM Emprunts WHERE livre='" + livre.RFID + "'");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirListeEmprunts]: Impossible de trouver la liste d'emprunts du livre :\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static int avoirNbEmprunts(Livre livre) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT COUNT(*) FROM Emprunts WHERE livre='" + livre.RFID + "'").getInt(1);
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirNbEmprunts]: Impossible de trouver le nombre d'emprunts pour le livre :\n\t"
                            + e.getMessage());
            return -1;
        }
    }

    public static ResultSet avoirEmpruntActif(Livre livre) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(
                    "SELECT * FROM Emprunts WHERE livre='" + livre.RFID
                            + "' AND JULIANDAY('now') - JULIANDAY(date_emprunt,'unixepoch') < duree_emprunt");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirListeEmprunts]: Impossible de trouver la liste d'emprunts du livre :\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static void enregistrerEmprunt(Emprunt emprunt) {
        try {
            Statement statement = connection.createStatement();
            // SQLite auto-incrémente la clée primaire par défaut
            statement.executeUpdate(
                    "INSERT INTO Emprunts(livre, client, date_emprunt, duree_emprunt) VALUES " +
                            "(" + emprunt.livre.RFID + ",'" + emprunt.client.nomCompte + "',"
                            + (emprunt.dateEmprunt.getTime() / 1000) + "," + emprunt.getDureeEmprunt() + ")");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.enregistrerEmprunt]: Impossible d'enregistrer l'emprunt :\n\t"
                            + e.getMessage());
        }
    }

    public static ResultSet avoirListeEmpruntsGlobal() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(
                    "SELECT * FROM Emprunts WHERE JULIANDAY('now') - JULIANDAY(date_emprunt,'unixepoch') < duree_emprunt");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirListeEmpruntsGlobal]: Impossible de trouver la liste d'emprunts :\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static int avoirNbEmpruntsGlobal() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(
                    "SELECT COUNT(*) FROM Emprunts WHERE JULIANDAY('now') - JULIANDAY(date_emprunt,'unixepoch') < duree_emprunt")
                    .getInt(1);
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirNbEmpruntsGlobal]: Impossible de trouver le nombre d'emprunts:\n\t"
                            + e.getMessage());
            return -1;
        }
    }

    public static ResultSet avoirListeEmpruntsHistorique() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(
                    "SELECT * FROM Emprunts");
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirListeEmpruntsHistorique]: Impossible de trouver la liste d'emprunts :\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static int avoirNbEmpruntsHistorique() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(
                    "SELECT COUNT(*) FROM Emprunts")
                    .getInt(1);
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirNbEmpruntsHistorique]: Impossible de trouver le nombre d'emprunts :\n\t"
                            + e.getMessage());
            return -1;
        }
    }

    public static ResultSet avoirEmprunt(int empruntID) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(
                    "SELECT * FROM Emprunts WHERE emprunt_id=" + empruntID);
        } catch (SQLException e) {
            System.err.println(
                    "[SQLInterface.avoirEmprunt]: Impossible de trouver l'emprunts :\n\t"
                            + e.getMessage());
            return null;
        }
    }
}
