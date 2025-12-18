let inputNumCompte;
let inputNip;
let erreurNumCompte;
let erreurNip;

// Popup
let popup;
let fermerPopup;
let popupTemps

function avoirTempsBloqué() { 
    return 300;
}

function authentifier(numCompte, NIP) { 
    return 0; //TODO implémenter authentifier
}

function étatAttenteAuthentification() {
    if (isNaN(parseFloat(inputNip.value)) || inputNip.value.length != 4) {
        inputNip.style.setProperty("border", "2px solid red");
        erreurNip.style.setProperty("display", "block");
        erreurNip.innerText = "veuillez inclure un NIP valide";
        return;
    } else {
        inputNumCompte.style.setProperty("border", "0");
        erreurNumCompte.style.setProperty("display", "none");
        inputNip.style.setProperty("border", "0");
        erreurNip.style.setProperty("display","none");
    }

    let codeRetour = authentifier(inputNumCompte.value, inputNip.value);

    switch (codeRetour) {
        case 0: window.location.href = "../html/enregistrer-livre.html"; break;
        case 1: // Compte inconnu
        case 2: // Mauvais NIP
            inputNumCompte.style.setProperty("border", "2px solid red");
            erreurNumCompte.style.setProperty("display", "block");
            erreurNumCompte.innerText = "Le nom d'utilisateur ou le mot de passe est invalide";
            inputNip.style.setProperty("border", "2px solid red");
            break;
        case 3: // Compte bloqué
            popup.style.setProperty("display", "block");
            break;
    }
}

document.addEventListener("DOMContentLoaded", () => {
    inputNumCompte = document.getElementById("num-compte");
    inputNip = document.getElementById("NIP");
    erreurNumCompte = document.getElementById("erreur-num-compte");
    erreurNip = document.getElementById("erreur-NIP");
    popup = document.getElementById("popup");
    fermerPopup = document.getElementById("popup-fermer");
    popupTemps = document.getElementById("popup-temps");
    
    fermerPopup.addEventListener("click", () => {
        popup.style.setProperty("display", "none");
    });

    document.getElementById("authentifier").addEventListener("submit", (e) => {
        e.preventDefault();
        étatAttenteAuthentification();
    });

    setInterval(() => {
        if (popup.style.getPropertyValue("display") !== "none") {
            return;
        }

        let temps = avoirTempsBloqué();
        let temps_str = "" + Math.floor(temps / 60) + "min " + (temps % 60) + "sec";
        popupTemps.innerText = temps_str;
    }, 1000);
});