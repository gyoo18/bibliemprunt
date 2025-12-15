package bibliemprunt;

public class Paramètre {
    public static int nbMaxEmprunts;
    public static int duréeEmpruntDéfaut;
    public static int duréeCompteBlocage;
    public static int authentificationEssaisMax;

    public static void initialiser() {
        nbMaxEmprunts = 5;
        duréeEmpruntDéfaut = 14; // 14 jours
        duréeCompteBlocage = 300; // 5 minutes en secondes
        authentificationEssaisMax = 3;
    }
}
