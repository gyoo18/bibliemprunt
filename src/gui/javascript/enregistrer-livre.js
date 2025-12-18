let RFID;
let erreurRFID;
let emprunts;

let popupAnnulerTransaction;
let popupTerminerTransaction;
let popupDéconnecter;
let popupDemanderReçus;
let popupAfficherReçus;
let reçusTexte;

let empruntsEnCoursReçus = [];

function avoirEmpruntsEnCours() {
    return []; // TODO implémenter
}

function emprunterLivre(rfid) {
    return 0; // TODO implémenter
}

function annulerEmprunt(rfid) {
    // TODO implémenter
}

function annulerTransaction() { 
    //TODO implémenter
}

function confirmerTransaction() {
    // TODO implémenter
}

function avoirEmpruntEnCours(rfid) {
    return {} // TODO implémenter
}

function fermerSession() {
    // TODO implémenter
}

function étatDéconnecter() { 
    fermerSession();
    window.location.href = "../html/authentification.html"
}

function étatAfficherReçus() {
    reçusTexte.innerHTML = "";
    for (e of empruntsEnCoursReçus) {
        // TODO implémenter date de retour
        reçusTexte.innerHTML += '<div class="emprunt"><div class="info"><p>' + e.titre + '</p><p>(' + e.auteur + ')</p><p>' + e.annee + '</p><p>date de retour</p></div></div>';
    }

    popupDemanderReçus.style.setProperty("display", "none");
    popupAfficherReçus.style.setProperty("display","block")
}

function étatTerminerTransaction() { 
    empruntsEnCoursReçus = avoirEmpruntsEnCours();
    confirmerTransaction();

    for (const child of emprunts.children) {
        child.remove();
    }

    popupTerminerTransaction.style.setProperty("display", "none");
    popupDemanderReçus.style.setProperty("display", "block");
}

function étatAnnulerTransaction() {
    annulerTransaction();

    for (const child of emprunts.children) {
        child.remove();
    }

    popupAnnulerTransaction.style.setProperty("display", "none");
    popupDéconnecter.style.setProperty("display", "block");
}

function étatEmprunter() {

    function erreur(message) {
        RFID.style.setProperty("border", "2px solid red");
        erreurRFID.style.setProperty("display", "block");
        erreurRFID.innerText = message;
    }

    RFID.style.setProperty("border", "0");
    erreurRFID.style.setProperty("display", "none");

    if (isNaN(parseInt(RFID.value)) || RFID.value.length != 6) {
        erreur("RFID invalide");
        return;
    }

    let codeRetour = emprunterLivre(parseInt(RFID.value));
    switch (codeRetour) {
        case 0: // Succès
            ajouterEmprunt(avoirEmpruntEnCours(RFID.value)); break;
        case 1: // Max emprunts atteint
            erreur("Nombre maximal d'emprunts atteint");break;
        case 2: // Livre innexistant
            erreur("Ce livre n'existe pas");break;
        case 3: // Livre déjà emprunté
            erreur("Ce livre est déjà emprunté"); break;
    }
}

function ajouterEmprunt(emprunt) {
    emprunts.innerHTML += (
        // TODO implémenter date de retour
        '<div class="emprunt" id="emprunt-' + emprunt.livre.rfid + '"><div class="info"><p>' + emprunt.titre + '</p><p>(' + emprunt.auteur + ')</p><p>' + emprunt.annee + '</p><p>date de retour</p></div><button id="annuler-emprunt-' + emprunt.rfid + '">x</button></div>'
    );
    document.getElementById("annuler-emprunt-" + emprunt.livre.rfid).addEventListener("click", () => {
        annulerEmprunt(emprunt.livre.rfid);
        document.getElementById("emprunt-" + emprunt.livre.rfid).remove();
    });
}

document.addEventListener("DOMContentLoaded", () => {
    RFID = document.getElementById("RFID");
    erreurRFID = document.getElementById("erreur-rfid");
    emprunts = document.getElementById("empruntsEnCours");
    popupAnnulerTransaction = document.getElementById("popup-annuler-transaction");
    popupTerminerTransaction = document.getElementById("popup-terminer-transaction");
    popupDéconnecter = document.getElementById("popup-déconnecter");
    popupDemanderReçus = document.getElementById("popup-demander-reçus");
    popupAfficherReçus = document.getElementById("popup-afficher-reçus");
    reçusTexte = document.getElementById("reçus-texte");
    
    let empruntsEncoursListe = avoirEmpruntsEnCours();
    for (let i = 0; i < empruntsEncoursListe.length; i++){
        ajouterEmprunt(empruntsEncoursListe[i]);
    }

    // Emprunts actifs
    document.getElementById("empruntsActifs").addEventListener("click", () => {
        window.location.href = "../html/emprunts-actifs.html";
    });

    // Livres disponibles
    document.getElementById("livresDisponibles").addEventListener("click", () => {
        window.location.href = "../html/livres-disponibles.html";
    });

    // Annuler transaction popup
    document.getElementById("annulerTransaction").addEventListener("click", () => {
        popupAnnulerTransaction.style.setProperty("display", "block");
    });

    // Terminer transaction popup
    document.getElementById("terminerTransaction").addEventListener("click", () => {
        popupTerminerTransaction.style.setProperty("display", "block");
    });

    // Emprunter RFID
    document.getElementById("emprunter").addEventListener("submit", (e) => { 
        e.preventDefault();
        étatEmprunter();
    });

    // Fermer popup Annuler Transaction
    document.getElementById("fermer-popup-annuler-transaction").addEventListener("click", () => {
        popupAnnulerTransaction.style.setProperty("display", "none");
    });

    // Fermer popup Terminer Transaction
    document.getElementById("fermer-terminer-transaction").addEventListener("click", () => {
        popupTerminerTransaction.style.setProperty("display", "none");
    })

    // Fermer popup Déconnecter
    document.getElementById("fermer-popup-déconnecter").addEventListener("click", () => {
        popupDéconnecter.style.setProperty("display", "none");
    });

    // Fermer popup Demander Reçus
    document.getElementById("fermer-demander-reçus").addEventListener("click", () => {
        popupDemanderReçus.style.setProperty("display", "none");
    });

    // Fermer popup Afficher Reçus
    document.getElementById("fermer-afficher-reçus").addEventListener("click", () => {
        popupAfficherReçus.style.setProperty("display", "none");
    });

    // Annuler Transaction
    document.getElementById("annuler-transaction").addEventListener("click", étatAnnulerTransaction);

    // Annuler Transaction retour
    document.getElementById("annuler-transaction-retour").addEventListener("click", () => {
        popupAnnulerTransaction.style.setProperty("display", "none");
    });

    // Terminer Transaction
    document.getElementById("terminer-transaction").addEventListener("click", étatTerminerTransaction);

    // Terminer Transaction retour
    document.getElementById("terminer-transaction-retour").addEventListener("click", () => {
        popupTerminerTransaction.style.setProperty("display", "none");
    });

    // Afficher Reçus
    document.getElementById("demander-reçus").addEventListener("click", étatAfficherReçus);

    // Refuser Reçus
    document.getElementById("demander-reçus-retour").addEventListener("click", () => { 
        popupDemanderReçus.style.setProperty("display", "none");
        popupDéconnecter.style.setProperty("display", "block");
    })

    // Terminé reçus
    document.getElementById("afficher-reçus").addEventListener("click", () => {
        popupAfficherReçus.style.setProperty("display", "none");
        popupDéconnecter.style.setProperty("display", "block");
    });

    // Déconnecter
    document.getElementById("déconnecter").addEventListener("click", étatDéconnecter);

    // Déconnecter retour
    document.getElementById("déconnecter-retour").addEventListener("click", () => {
        popupDéconnecter.style.setProperty("display", "none");
    });
});