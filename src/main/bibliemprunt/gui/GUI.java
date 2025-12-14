package bibliemprunt.gui;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import bibliemprunt.Borne;
import bibliemprunt.Paramètre;
import bibliemprunt.données.BanqueEmprunts;
import bibliemprunt.données.BanqueLivres;
import bibliemprunt.models.CompteClient;
import bibliemprunt.models.Emprunt;
import bibliemprunt.models.Livre;

enum ÉtatGUI {
    ATTENTE_AUTENTIFICATION,
    AUTHENTIFICATION,
    COMPTE_INCONNU,
    COMPTE_BLOQUÉ,
    EMPRUNTS,
    ANNULER_EMPRUNT,
    ANNULER_TRANSACTION,
    TRANSACTION_ANNULÉE,
    MAX_EMPRUNTS_ATTEINT,
    EMPRUNT_INVALIDE,
    LIVRE_EMPRUNTÉ,
    DEMANDER_REÇUS,
    AFFICHER_REÇUS,
    QUITTER
}

public class GUI {
    // private Page[] pages;
    // private Page pageActuelle;

    private Borne borne;

    private String nomUtilisateur;
    private int nip;

    private Scanner scanner;

    private ÉtatGUI étatGUI;
    // Le comportement d'un état nécessite toujours une initialisation, puis une
    // exécution. Cette variable garde une trace du fait que l'état a été initialisé
    // ou non.
    private boolean estÉtatInitialisation;

    private final String ANSI_CLAIR = "\033[0m";
    private final String ANSI_ROUGE = "\033[31m";
    private final String ANSI_VERT = "\033[32m";
    private final String ANSI_JAUNE = "\033[33m";
    private final String ANSI_BLEU = "\033[34m";
    private final String ANSI_CYAN = "\033[36m";

    private final static long JOURS_EN_MILLIS = 86_400_000;
    private final static String os = System.getProperty("os.name");

    public GUI(Borne borne) {
        System.out.println("Initialisation de l'interface utilisateur...");

        this.borne = borne;
        this.scanner = new Scanner(System.in);
        this.étatGUI = ÉtatGUI.ATTENTE_AUTENTIFICATION;
        this.estÉtatInitialisation = true;
    }

    public void miseÀJour() {
        switch (this.étatGUI) {
            case ATTENTE_AUTENTIFICATION:
                étatAttenteAuthentification();
                break;
            case AUTHENTIFICATION:
                étatAuthentification();
                break;
            case COMPTE_INCONNU:
                étatCompteInconnu();
                break;
            case COMPTE_BLOQUÉ:
                étatCompteBloqué();
                break;
            case EMPRUNTS:
                étatEmprunt();
                break;
            case ANNULER_EMPRUNT:
                étatAnnulerEmprunt();
                break;
            case ANNULER_TRANSACTION:
                étatAnnulerTransaction();
                break;
            case TRANSACTION_ANNULÉE:
                étatTransactionAnnulée();
                break;
            case MAX_EMPRUNTS_ATTEINT:
                étatMaxEmpruntsAtteint();
                break;
            case EMPRUNT_INVALIDE:
                étatEmpruntInvalide();
                break;
            case LIVRE_EMPRUNTÉ:
                étatLivreEmprunté();
                break;
            case DEMANDER_REÇUS:
                étatDemanderReçus();
                break;
            case AFFICHER_REÇUS:
                étatAfficherReçus();
                break;
            case QUITTER:
                étatQuitter();
                break;
        }
        /*
         * boolean continuer = true;
         * 
         * while (continuer) {
         * try {
         * // Authentification
         * System.out.println("\n=== Authentification ===");
         * System.out.print("Nom d'utilisateur (ou 'quit' pour quitter): ");
         * String username = scanner.nextLine().trim();
         * 
         * if (username.equalsIgnoreCase("quit")) {
         * continuer = false;
         * break;
         * }
         * 
         * System.out.print("NIP: ");
         * String nip = scanner.nextLine().trim();
         * 
         * // Démarrer session
         * borne.démarrerSession(username, nip);
         * System.out.println("✓ Authentification réussie! Bienvenue " +
         * borne.getClientSession().nom);
         * 
         * // Boucle d'emprunt
         * boolean sessionActive = true;
         * while (sessionActive) {
         * System.out.println("\n=== Menu ===");
         * System.out.println("1. Emprunter un livre (scanner RFID)");
         * System.out.println("2. Voir emprunts en cours");
         * System.out.println("3. Confirmer et finaliser");
         * System.out.println("4. Annuler et fermer session");
         * System.out.print("Choix: ");
         * 
         * String choix = scanner.nextLine().trim();
         * 
         * switch (choix) {
         * case "1":
         * System.out.print("Entrez le RFID du livre: ");
         * try {
         * int rfid = Integer.parseInt(scanner.nextLine().trim());
         * Emprunt emprunt = borne.emprunterLivre(rfid);
         * System.out.println(
         * "✓ Livre ajouté: " + emprunt.livre.titre + " par " + emprunt.livre.auteur);
         * } catch (NumberFormatException e) {
         * System.out.println("✗ RFID invalide");
         * } catch (IllegalArgumentException | IllegalStateException e) {
         * System.out.println("✗ " + e.getMessage());
         * }
         * break;
         * 
         * case "2":
         * System.out.println("\n=== Emprunts en cours ===");
         * 
         * // Debug
         * Emprunt[] tousEmprunts = BanqueEmprunts.listeEmpruntsGlobal();
         * System.out.println("[DEBUG] Total emprunts dans BanqueEmprunts: " +
         * tousEmprunts.length);
         * 
         * // Afficher les emprunts déjà confirmés
         * Emprunt[] empruntsConfirmes =
         * BanqueEmprunts.listeEmpruntsActifs(borne.getClientSession());
         * System.out.println("[DEBUG] Emprunts pour ce client: " +
         * empruntsConfirmes.length);
         * 
         * if (empruntsConfirmes.length > 0) {
         * System.out.println("Emprunts confirmés:");
         * for (Emprunt e : empruntsConfirmes) {
         * System.out.println("- " + e.livre.titre + " (" + e.livre.auteur + ")");
         * }
         * }
         * 
         * // Afficher les emprunts de la session actuelle (non confirmés)
         * if (!borne.getEmpruntsEnCours().isEmpty()) {
         * System.out.println("\nEmprunts en attente de confirmation:");
         * for (Emprunt e : borne.getEmpruntsEnCours()) {
         * System.out.println("- " + e.livre.titre + " (" + e.livre.auteur + ")");
         * }
         * }
         * 
         * if (empruntsConfirmes.length == 0 && borne.getEmpruntsEnCours().isEmpty()) {
         * System.out.println("Aucun emprunt");
         * }
         * break;
         * 
         * case "3":
         * if (borne.getEmpruntsEnCours().isEmpty()) {
         * System.out.println("Aucun emprunt à confirmer");
         * } else {
         * int nbEmpruntsAConfirmer = borne.getEmpruntsEnCours().size();
         * borne.confirmerEmprunts();
         * System.out.println(
         * "✓ " + nbEmpruntsAConfirmer + " emprunt(s) confirmé(s) et enregistré(s)!");
         * System.out.println("[DEBUG] Total emprunts après confirmation: "
         * + BanqueEmprunts.listeEmpruntsGlobal().length);
         * System.out.println("Merci et bonne lecture!");
         * }
         * borne.fermerSession();
         * sessionActive = false;
         * break;
         * 
         * case "4":
         * borne.annulerTransaction();
         * borne.fermerSession();
         * System.out.println("Session fermée");
         * sessionActive = false;
         * break;
         * 
         * default:
         * System.out.println("Choix invalide");
         * }
         * }
         * 
         * } catch (IllegalArgumentException e) {
         * System.out.println("✗ Authentification échouée: identifiants incorrects");
         * } catch (IllegalStateException e) {
         * System.out.println("✗ " + e.getMessage());
         * } catch (Exception e) {
         * System.out.println("✗ Erreur: " + e.getMessage());
         * e.printStackTrace();
         * }
         * }
         */
    }

    public void destruction() {
        System.out.println("Fermeture de l'interface utilisateur...");
        scanner.close();
    }

    public void sigkillQuitter() {
        this.étatGUI = ÉtatGUI.QUITTER;
        this.estÉtatInitialisation = true;
    }

    private void étatAttenteAuthentification() {
        if (this.estÉtatInitialisation) {
            effacerÉcran();
            System.out.println(
                    "=== " + ANSI_CYAN + "BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES" + ANSI_CLAIR + " ===\n" +

                            "Bienvenue au système d'emprunt de livres bibliemprunt\n" +
                            "--- Authentification ---\n" +
                            "\n" +
                            "Veuillez vous authentifier.\n");
            this.estÉtatInitialisation = false;
        }

        System.out.print("Votre nom d'utilisateur :\t");
        this.nomUtilisateur = scanner.nextLine();

        if (this.nomUtilisateur.toLowerCase().compareTo("q") == 0
                || this.nomUtilisateur.toLowerCase().compareTo("quit") == 0) {
            this.étatGUI = ÉtatGUI.QUITTER;
            this.estÉtatInitialisation = true;
            return;
        }

        System.out.print("Votre NIP :\t");
        String nipString = scanner.nextLine();

        if (nipString.length() != 4) {
            System.out.println("[" + ANSI_ROUGE + "ERREUR" + ANSI_CLAIR + "] Veuillez entrer un NIP valide.");
            return;
        }

        try {
            this.nip = Integer.parseInt(nipString);
        } catch (Exception e) {
            System.out.println("[" + ANSI_ROUGE + "ERREUR" + ANSI_CLAIR + "] Veuillez entrer un NIP valide.");
            return;
        }

        this.étatGUI = ÉtatGUI.AUTHENTIFICATION;
        this.estÉtatInitialisation = true;
    }

    private void étatAuthentification() {
        System.out.println("\n\nAuthentification en cours...");

        if (!this.borne.clientExiste(this.nomUtilisateur)) {
            System.out.println(
                    "[" + ANSI_ROUGE + "ERREUR" + ANSI_CLAIR + "] Le nom d'utilisateur ou mot de passe est invalide.");
            this.étatGUI = ÉtatGUI.ATTENTE_AUTENTIFICATION;
            this.estÉtatInitialisation = true;
            scanner.nextLine();
            return;
        }

        int réponse = this.borne.démarrerSession(this.nomUtilisateur, this.nip);

        switch (réponse) {
            case 0:
                System.out.println("[" + ANSI_VERT + "AUTHENTIFICATION RÉUSSIE" + ANSI_CLAIR + "]");
                this.étatGUI = ÉtatGUI.EMPRUNTS;
                scanner.nextLine();
                break;
            case 1:
            case 2:
                this.étatGUI = ÉtatGUI.COMPTE_INCONNU;
                break;
            case 3:
                this.étatGUI = ÉtatGUI.COMPTE_BLOQUÉ;
                break;
            default:
                throw new UnsupportedOperationException(
                        ANSI_ROUGE + "[GUI.étatAuthentification]: code de réponse invalide de borne.démarrerSession()"
                                + ANSI_CLAIR);
        }
        this.estÉtatInitialisation = true;
    }

    private void étatCompteInconnu() {
        System.out.println("[" + ANSI_ROUGE + "ERREUR" + ANSI_CLAIR
                + "] Le nom d'utilisateur ou le mot de passe est invalide.");

        scanner.nextLine();
        this.étatGUI = ÉtatGUI.ATTENTE_AUTENTIFICATION;
        this.estÉtatInitialisation = true;
    }

    private void étatCompteBloqué() {
        if (this.estÉtatInitialisation) {
            System.out.println(
                    "=== " + ANSI_ROUGE + "COMPTE BLOQUÉ" + ANSI_CLAIR + " ===\n");
            this.estÉtatInitialisation = false;
        }

        if (scanner.hasNextLine()) {
            this.étatGUI = ÉtatGUI.ATTENTE_AUTENTIFICATION;
            this.estÉtatInitialisation = true;
        }

        effacerLigne();
        Date tempsBloque = new Date(System.currentTimeMillis() - borne.getClientSession().getTempsBloque());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        System.out.println("Temps restant : " + sdf.format(tempsBloque));

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void étatEmprunt() {
        if (this.estÉtatInitialisation) {
            effacerÉcran();
            System.out.print(
                    "=== " + ANSI_CYAN + "BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES" + ANSI_CLAIR + " ===\n" +
                            "\n" +
                            "--- Emprunts ---\n" +
                            "\n" +
                            "1. Emprunter un livre\n" +
                            "2. Annuler un emprunt\n" +
                            "3. Annuler la transaction\n" +
                            "4. Terminer la transaction\n\n");
            this.estÉtatInitialisation = false;
        }

        System.out.print("> ");
        String réponseString = scanner.nextLine();
        if (réponseString.length() != 1 || "1234".indexOf(réponseString.charAt(0)) == -1) {
            System.out.println("[" + ANSI_ROUGE + "ERREUR" + ANSI_CLAIR + "] Veuillez spécifier une option [1-4]");
            return;
        }

        int réponse = Integer.parseInt(réponseString);

        if (réponse == 1) {
            System.out.print("RFID :\t");
            réponseString = scanner.nextLine();

            int rfid;
            try {
                rfid = Integer.parseInt(réponseString);
            } catch (Exception e) {
                System.out.println("[" + ANSI_ROUGE + "ERREUR" + ANSI_CLAIR + "] Veuillez préciser un RFID valide.");
                return;
            }

            int code = this.borne.emprunterLivre(rfid);
            switch (code) {
                case 0: // Emprunt succès
                    Livre livre = BanqueLivres.avoirLivre(rfid);
                    System.out.println("[" + ANSI_VERT + "EMPRUNT RÉUSSI" + ANSI_CLAIR + "] Vous avez emprunté : "
                            + livre.titre + " de " + livre.auteur);
                    return;
                case 1: // Nombre maximal d'emprunts atteints.
                    this.étatGUI = ÉtatGUI.MAX_EMPRUNTS_ATTEINT;
                    this.estÉtatInitialisation = true;
                    return;
                case 2: // Le livre n'existe pas.
                    this.étatGUI = ÉtatGUI.EMPRUNT_INVALIDE;
                    this.estÉtatInitialisation = true;
                    return;
                case 3: // Le livre est déjà emprunté.
                    this.étatGUI = ÉtatGUI.LIVRE_EMPRUNTÉ;
                    this.estÉtatInitialisation = true;
                    return;
                default:
                    throw new UnsupportedOperationException(ANSI_ROUGE
                            + "[GUI.étatEmprunt]: code de réponse invalide pour borne.emprunterLivre()" + ANSI_CLAIR);
            }
        }

        switch (réponse) {
            case 2:
                this.étatGUI = ÉtatGUI.ANNULER_EMPRUNT;
                break;
            case 3:
                this.étatGUI = ÉtatGUI.ANNULER_TRANSACTION;
                break;
            case 4:
                this.étatGUI = ÉtatGUI.DEMANDER_REÇUS;
                break;
        }
        this.estÉtatInitialisation = true;
    }

    private void étatAnnulerEmprunt() {
        if (this.estÉtatInitialisation) {
            System.out.print(
                    "=== " + ANSI_CYAN + "BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES" + ANSI_CLAIR + " ===\n" +
                            "\n" +
                            "--- Annuler un emprunt ---\n" +
                            "\n" +
                            "Vos emprunts en cours :\n");
            effacerÉcran();
            this.estÉtatInitialisation = false;
        }

        ArrayList<Emprunt> emprunts = this.borne.getEmpruntsEnCours();
        for (int i = 0; i < emprunts.size(); i++) {
            Emprunt emprunt = emprunts.get(i);
            Livre livre = emprunt.livre;
            System.out.println(i + ": " + livre.RFID + "\t" + livre.titre + " (" + livre.auteur + ") "
                    + livre.anneeParution + "\t" + livre.nbPages + "pgs");
        }

        System.out.print("\nSélectionnez un ou plusieurs emprunts à annuler > ");
        String[] réponsesString = scanner.nextLine().split(" |,");

        Emprunt[] retirer = new Emprunt[réponsesString.length];
        for (int i = 0; i < réponsesString.length; i++) {
            int n;
            try {
                n = Integer.parseInt(réponsesString[i]);
            } catch (Exception e) {
                System.out.println("[" + ANSI_ROUGE + "ERREUR" + ANSI_CLAIR + "] La " + i + "ième option ("
                        + réponsesString[i] + ") n'est pas un nombre.");
                continue;
            }

            if (n < 0 || n >= emprunts.size()) {
                System.out.println("[" + ANSI_ROUGE + "ERREUR" + ANSI_CLAIR + "] La " + i + "ième option ("
                        + réponsesString[i] + ") n'est pas dans la liste.");
                continue;
            }

            retirer[i] = emprunts.get(n);
        }

        for (int i = 0; i < retirer.length; i++) {
            if (retirer[i] == null) {
                continue;
            }

            emprunts.remove(retirer[i]);
        }

        System.out.println("[" + ANSI_VERT + "LIVRES RETIRÉS" + ANSI_CLAIR + "]");
        System.out.print("Appuyez sur q pour revenir aux emprunts : ");
        String réponseString = scanner.nextLine();
        if (réponseString.toLowerCase().compareTo("q") == 0) {
            this.étatGUI = ÉtatGUI.EMPRUNTS;
            this.estÉtatInitialisation = true;
        }
    }

    private void étatAnnulerTransaction() {
        effacerÉcran();

        System.out.print(
                "=== " + ANSI_CYAN + "BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES" + ANSI_CLAIR + " ===\n" +
                        "\n" +
                        ANSI_ROUGE + "--- ANNULER TRANSACTION ---" + ANSI_CLAIR + "\n");
        System.out.print("Êtes-vous sûr de vouloir annuler toute la transaction? [o|n] : ");
        String réponse = scanner.nextLine();
        if (réponse.toLowerCase().compareTo("o") != 0 && réponse.toLowerCase().compareTo("oui") != 0) {
            this.étatGUI = ÉtatGUI.EMPRUNTS;
            this.estÉtatInitialisation = true;
            return;
        }

        this.borne.annulerTransaction();
        this.étatGUI = ÉtatGUI.TRANSACTION_ANNULÉE;
        this.estÉtatInitialisation = true;
    }

    private void étatTransactionAnnulée() {
        effacerÉcran();
        System.out.print(
                "=== " + ANSI_CYAN + "BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES" + ANSI_CLAIR + " ===\n" +
                        "\n" +
                        ANSI_ROUGE + "--- TRANSACTION ANNULÉE ---" + ANSI_CLAIR + "\n");

        System.out.print("La transaction est annulée. Souhaitez-vous vous déconnecter? [o|n] : ");
        String réponse = scanner.nextLine();
        if (réponse.toLowerCase().compareTo("o") == 0 || réponse.toLowerCase().compareTo("oui") == 0) {
            this.étatGUI = ÉtatGUI.ATTENTE_AUTENTIFICATION;
            this.borne.fermerSession();
        } else {
            this.étatGUI = ÉtatGUI.EMPRUNTS;
        }
        this.estÉtatInitialisation = true;
    }

    private void étatMaxEmpruntsAtteint() {
        effacerÉcran();
        System.out.print(
                "=== " + ANSI_CYAN + "BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES" + ANSI_CLAIR + " ===\n" +
                        "\n" +
                        ANSI_ROUGE + "--- NOMBRE MAXIMAL D'EMPRUNTS ATTEINTS ---" + ANSI_CLAIR + "\n" +
                        "\n" +
                        "Vous avez "
                        + (this.borne.getClientSession().nbEmpruntsActifs() + this.borne.getEmpruntsEnCours().size())
                        + " emprunts actifs.\n");
        scanner.nextLine();
        this.étatGUI = ÉtatGUI.EMPRUNTS;
        this.estÉtatInitialisation = true;
    }

    private void étatEmpruntInvalide() {
        effacerÉcran();
        System.out.print(
                "=== " + ANSI_CYAN + "BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES" + ANSI_CLAIR + " ===\n" +
                        "\n" +
                        ANSI_ROUGE + "--- EMPRUNT INVALIDE ---" + ANSI_CLAIR + "\n" +
                        "\n" +
                        "Le livre que vous tentez d'emprunter n'existe pas.\n");
        scanner.nextLine();
        this.étatGUI = ÉtatGUI.EMPRUNTS;
        this.estÉtatInitialisation = true;
    }

    private void étatLivreEmprunté() {
        effacerÉcran();
        System.out.print(
                "=== " + ANSI_CYAN + "BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES" + ANSI_CLAIR + " ===\n" +
                        "\n" +
                        ANSI_ROUGE + "--- EMPRUNT INVALIDE ---" + ANSI_CLAIR + "\n" +
                        "\n" +
                        "Le livre que vous tentez d'emprunter est déjà emprunté.\n");
        scanner.nextLine();
        this.étatGUI = ÉtatGUI.EMPRUNTS;
        this.estÉtatInitialisation = true;
    }

    private void étatDemanderReçus() {
        effacerÉcran();
        System.out.print(
                "=== " + ANSI_CYAN + "BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES" + ANSI_CLAIR + " ===\n" +
                        "\n" +
                        "--- TRANSACTION CONFIRMÉE ---\n");

        System.out.print("Souhaitez-vous imprimer un reçus? [o|n] : ");
        String réponse = scanner.nextLine();
        if (réponse.toLowerCase().compareTo("n") == 0 || réponse.toLowerCase().compareTo("non") == 0) {
            this.étatGUI = ÉtatGUI.ATTENTE_AUTENTIFICATION;
            // Le reçus a besoin des emprunts en cours pour être imprimé et cette
            // fonction vide la liste d'emprunts en cours.
            this.borne.confirmerEmprunts();
            this.borne.fermerSession();
        } else {
            this.étatGUI = ÉtatGUI.AFFICHER_REÇUS;
        }

        this.estÉtatInitialisation = true;
    }

    private void étatAfficherReçus() {
        ArrayList<Emprunt> emprunts = this.borne.getEmpruntsEnCours();
        StringBuilder reçus = new StringBuilder();
        reçus.append("\n=== BIBLIEMPRUNT | REÇUS ===\n\n");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
        for (int i = 0; i < emprunts.size(); i++) {
            Emprunt emprunt = emprunts.get(i);
            Livre livre = emprunts.get(i).livre;
            reçus.append(i + ". " + livre.titre + "\t" + livre.RFID + "\t Date de retour : "
                    + sdf.format(new Date(
                            emprunt.getDureeEmprunt() + (long) emprunt.dateEmprunt.getTime() * JOURS_EN_MILLIS)));
        }

        reçus.append(
                "\n------------\n" +
                        "Total : " + emprunts.size() + " livre\n" +
                        "Client : " + this.borne.getClientSession().nom + "\n" +
                        "Date : "
                        + new SimpleDateFormat("yyyy MMM dd - HH:mm").format(Calendar.getInstance().getTime()) + "\n");
        System.out.println(reçus.toString());

        // Le reçus a besoin des emprunts en cours pour être imprimé et cette
        // fonction vide la liste d'emprunts en cours.
        this.borne.confirmerEmprunts();

        System.out.print("Souhaitez-vous vous déconnecter? [o|n] : ");
        String réponse = scanner.nextLine();
        if (réponse.toLowerCase().compareTo("n") == 0 || réponse.toLowerCase().compareTo("non") == 0) {
            this.étatGUI = ÉtatGUI.EMPRUNTS;
        } else {
            this.étatGUI = ÉtatGUI.ATTENTE_AUTENTIFICATION;
            this.borne.fermerSession();
        }

        this.estÉtatInitialisation = true;
        return;
    }

    private void étatQuitter() {
        effacerÉcran();
        System.out.println(
                "=== " + ANSI_CYAN + "BIBLIEMPRUNT | SYSTÈME D'EMPRUNT DE LIVRES" + ANSI_CLAIR + " ===\n" +
                        "\n" +
                        "--- Quitter ---\n" +
                        "\n" +
                        "Merci d'avoir utilisé Bibliemprunt.\n" +
                        "Bonne lecture :)");
        System.out.flush();
        scanner.nextLine();

        this.borne.quitter();
        return;
    }

    private void effacerÉcran() {
        ProcessBuilder proc = new ProcessBuilder();
        if (os.contains("Windows")) {
            proc.command(new String[] { "cmd", "/c", "cls" });
        } else {
            proc.command(new String[] { "clear" });
        }

        try {
            proc.inheritIO().start();
            Thread.sleep(100); // Attendre que la commande soit exécutée
        } catch (Exception e) {
            System.err.println("[GUI.effacerÉcran()] : Impossible d'effacer l'écran : " + e.getMessage());
        }
    }

    private void effacerLigne() {
        System.out.print("\033[1G\033[0K"); // Déplace le curseur au début de la ligne et efface depuis le curseur
                                            // jusqu'à la fin de la ligne
    }
}
