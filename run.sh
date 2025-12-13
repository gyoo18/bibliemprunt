#!/bin/bash
# Script de lancement de la borne d'emprunt

cd "$(dirname "$0")"

# Lancer directement l'application sans le bruit de Gradle
./src/build/install/src/bin/src
