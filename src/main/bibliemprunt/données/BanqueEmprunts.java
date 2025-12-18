package bibliemprunt.données;

import bibliemprunt.models.CompteClient;
import bibliemprunt.models.Emprunt;
import bibliemprunt.models.Livre;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class BanqueEmprunts {
    private final static long JOURS_EN_MILLIS = 86_400_000;

    public static void initialiser() {
        // Aucune initialisation nécessaire, la liste commence vide
    }

    public static Emprunt[] avoirListeEmprunts(Livre livre) {
        ResultSet rs = SQLInterface.avoirListeEmprunts(livre);
        Emprunt[] emprunts = new Emprunt[SQLInterface.avoirNbEmprunts(livre)];
        try {
            int i = 0;
            while (rs.next()) {
                // Aller chercher le client correspondant
                ResultSet rsc = SQLInterface.avoirClient(rs.getString("client"));
                CompteClient client = null;
                if (rsc.next()) {
                    client = new CompteClient(rsc.getString("nom_compte"), rsc.getInt("nip"),
                            rsc.getString("nom_client"),
                            rsc.getBoolean("est_bloque"), rsc.getLong("temps_bloque"),
                            rsc.getByte("nb_tentatives_authentification"));
                }

                emprunts[i] = new Emprunt(livre, client, rs.getDate("date_emprunt"), rs.getInt("duree_emprunt"));
            }
            return emprunts;
        } catch (SQLException e) {
            System.out.println(
                    "[BanqueClient.avoirListeEmprunts] Impossible de convertir le résultat en Emprunt[]:\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static boolean estLivremprunté(Livre livre) {
        try {
            return SQLInterface.avoirEmpruntActif(livre).next();
        } catch (SQLException e) {
            System.out.println(
                    "[BanqueClient.estLivreEmprunté] Impossible déterminer si le livre est emprunté:\n\t"
                            + e.getMessage());
            return false;
        }
    }

    public static Emprunt[] listeEmpruntsActifs(CompteClient compteClient) {
        ResultSet rs = SQLInterface.avoirListeEmpruntsActifs(compteClient);
        Emprunt[] emprunts = new Emprunt[SQLInterface.avoirNbEmpruntsActifs(compteClient)];
        try {
            int i = 0;
            while (rs.next()) {
                // Aller chercher le livre correspondant
                ResultSet rsl = SQLInterface.avoirLivre(rs.getInt("livre"));
                Livre livre = null;
                if (rsl.next()) {
                    livre = new Livre(rsl.getInt("RFID"), rsl.getString("titre"), rsl.getString("auteur"),
                            rsl.getString("edition"), rsl.getInt("annee_parution"), rsl.getInt("nb_pages"));
                }

                emprunts[i] = new Emprunt(livre, compteClient, rs.getDate("date_emprunt"), rs.getInt("duree_emprunt"));
                i++;
            }
            return emprunts;
        } catch (SQLException e) {
            System.out.println(
                    "[BanqueClient.avoirEmpruntsActifs] Impossible de convertir le résultat en Emprunt[]:\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static int avoirNbEmprunts(CompteClient client) {
        return SQLInterface.avoirNbEmprunts(client);
    }

    public static Emprunt[] listeEmprunts(CompteClient compteClient) {
        ResultSet rs = SQLInterface.avoirListeEmprunts(compteClient);
        Emprunt[] emprunts = new Emprunt[SQLInterface.avoirNbEmprunts(compteClient)];
        try {
            int i = 0;
            while (rs.next()) {
                // Aller chercher le client correspondant
                ResultSet rsl = SQLInterface.avoirLivre(rs.getInt("livre"));
                Livre livre = null;
                if (rsl.next()) {
                    livre = new Livre(rsl.getInt("RFID"), rsl.getString("titre"), rsl.getString("auteur"),
                            rsl.getString("edition"), rsl.getInt("annee_parution"), rsl.getInt("nb_pages"));
                }

                emprunts[i] = new Emprunt(livre, compteClient, rs.getDate("date_emprunt"), rs.getInt("duree_emprunt"));
            }
            return emprunts;
        } catch (SQLException e) {
            System.out.println(
                    "[BanqueClient.avoirEmprunts] Impossible de convertir le résultat en Emprunt[]:\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static void enregistrerEmprunt(Emprunt emprunt) {
        SQLInterface.enregistrerEmprunt(emprunt);
    }

    public static Emprunt[] listeEmpruntsGlobal() {
        ResultSet rs = SQLInterface.avoirListeEmpruntsGlobal();
        Emprunt[] emprunts = new Emprunt[SQLInterface.avoirNbEmpruntsGlobal()];
        try {
            int i = 0;
            while (rs.next()) {
                // Aller chercher le client correspondant
                ResultSet rsc = SQLInterface.avoirClient(rs.getString("client"));
                CompteClient client = null;
                if (rsc.next()) {
                    client = new CompteClient(rsc.getString("nom_compte"), rsc.getInt("nip"),
                            rsc.getString("nom_client"),
                            rsc.getBoolean("est_bloque"), rsc.getLong("temps_bloque"),
                            rsc.getByte("nb_tentatives_authentification"));
                }

                // Aller chercher le livre correspondant
                ResultSet rsl = SQLInterface.avoirLivre(rs.getInt("livre"));
                Livre livre = null;
                if (rsl.next()) {
                    livre = new Livre(rsl.getInt("RFID"), rsl.getString("titre"), rsl.getString("auteur"),
                            rsl.getString("edition"), rsl.getInt("annee_parution"), rsl.getInt("nb_pages"));
                }

                emprunts[i] = new Emprunt(livre, client, rs.getDate("date_emprunt"), rs.getInt("duree_emprunt"));
            }
            return emprunts;
        } catch (SQLException e) {
            System.out.println(
                    "[BanqueClient.listeEmpruntsGlobal] Impossible de convertir le résultat en Emprunt[]:\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static Emprunt[] listeHistorique() {
        ResultSet rs = SQLInterface.avoirListeEmpruntsHistorique();
        Emprunt[] emprunts = new Emprunt[SQLInterface.avoirNbEmpruntsHistorique()];
        try {
            int i = 0;
            while (rs.next()) {
                // Aller chercher le client correspondant
                ResultSet rsc = SQLInterface.avoirClient(rs.getString("client"));
                CompteClient client = null;
                if (rsc.next()) {
                    client = new CompteClient(rsc.getString("nom_compte"), rsc.getInt("nip"),
                            rsc.getString("nom_client"),
                            rsc.getBoolean("est_bloque"), rsc.getLong("temps_bloque"),
                            rsc.getByte("nb_tentatives_authentification"));
                }

                // Aller chercher le livre correspondant
                ResultSet rsl = SQLInterface.avoirLivre(rs.getInt("livre"));
                Livre livre = null;
                if (rsl.next()) {
                    livre = new Livre(rsl.getInt("RFID"), rsl.getString("titre"), rsl.getString("auteur"),
                            rsl.getString("edition"), rsl.getInt("annee_parution"), rsl.getInt("nb_pages"));
                }

                emprunts[i] = new Emprunt(livre, client, rs.getDate("date_emprunt"), rs.getInt("duree_emprunt"));
            }
            return emprunts;
        } catch (SQLException e) {
            System.out.println(
                    "[BanqueClient.listeEmpruntsHistorique] Impossible de convertir le résultat en Emprunt[]:\n\t"
                            + e.getMessage());
            return null;
        }
    }

    public static Emprunt avoirEmprunt(int empruntID) {
        try {
            ResultSet rs = SQLInterface.avoirEmprunt(empruntID);
            if (!rs.next()) {
                return null;
            }

            // Aller chercher le client correspondant
            ResultSet rsc = SQLInterface.avoirClient(rs.getString("client"));
            CompteClient client = null;
            if (rsc.next()) {
                client = new CompteClient(rsc.getString("nom_compte"), rsc.getInt("nip"), rsc.getString("nom_client"),
                        rsc.getBoolean("est_bloque"), rsc.getLong("temps_bloque"),
                        rsc.getByte("nb_tentatives_authentification"));
            }

            // Aller chercher le livre correspondant
            ResultSet rsl = SQLInterface.avoirLivre(rs.getInt("livre"));
            Livre livre = null;
            if (rsl.next()) {
                livre = new Livre(rsl.getInt("RFID"), rsl.getString("titre"), rsl.getString("auteur"),
                        rsl.getString("edition"), rsl.getInt("annee_parution"), rsl.getInt("nb_pages"));
            }
            return new Emprunt(livre, client, rs.getDate("date_emprunt"), rs.getInt("duree_emprunt"));
        } catch (SQLException e) {
            System.out.println(
                    "[BanqueClient.avoirEmprunt] Impossible de convertir le résultat en Emprunt:\n\t"
                            + e.getMessage());
            return null;
        }
    }
}
