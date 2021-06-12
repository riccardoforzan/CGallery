# CGallery

<p align="center">
  <img src="https://github.com/riccardoforzan/ProgettoSE/blob/main/app/src/main/res/mipmap-xxxhdpi/app_icon.png"/>
</p>

**[Relazione su Jetpack Compose](https://docs.google.com/document/d/12CNiPrigRfxPjgjuLXrUTwFWbnnxqSGiqUS9mBNqstQ/edit?usp=sharing)**

Galleria per la visualizzazione di foto sviluppata utlizzando JetPack Compose

## Tecnologie utilizzate:
* [Android Jetpack - Compose](https://developer.android.com/jetpack/compose)
* [Coil library](https://github.com/google/accompanist/tree/main/coil)
* [Accompanist - Coil](https://google.github.io/accompanist/coil/)

____

## Funzionalità
* Supporto alla darkmode (Attraverso i temi)
* Supporto lingua Italiano e Inglese
* Implementazione della navigazione tramite Navigation
* Possibilità di utilizzo delle gesture di swipe e zoom in FullImage
* Possibilità di modificare le preferenze della galleria:
* * Nome
* * Modalità di ordinamento per data
* * Dimensione delle immagini
* Possibilità di condividere foto
* Possibilità di aprire la fotocamera dall'app 

## Overview delle principali classi

### FistTimeActivity.kt
* [x] Creazione di una UI di primo avvio dell'app
* [x] Intent per lanciare la gestione dei permessi

### PermissionActivity.kt
* [x] Implementazione della richiesta dei premessi secondo linee guida
* [x] Creazione UI per la gestione dei premessi
* [x] Creazione della UI nel caso in cui i permessi vengano negati 

### MainActivity.kt
Questa classe è l'activity principale della nostra applicazione, nonchè quella che viene lanciata all'avvio.
La sua funzione è di disambiguazione, può:
* Mostrare FirstTimeActivity se è il primo lancio dell'applicazione
* Mostrare PermissionActivity per la gestione dei permessi
* Impostare la navigazione e lanciare /screen/ImagesGrid.kt

### SPStrings.kt
Questa classe contiene tutte le stringhe che abbiamo utilizzato come chiavi per salvare valori sulle shared preferences

### StorageUtils.kt
Lo scopo di questa classe è quello di fornire un livello di astrazione rispetto alla gestione della memoria di Android.
* [x] Reperire gli URI da MediaStore
* [x] Reperire i metadati EXIF delle foto
* [x] Metodo per la cancellazione delle foto

### ../ui/theme/..
Dentro questo package vengono definiti colori e temi presenti per la nostra applicazione.
Viene definito il **GalleryTheme** che poi abbiamo applicato alle nostre UI.
L'utilizzo dei temi ha permesso di avere una coerenza grafica e l'implementazione della Dark Mode.

### ../screen/
Dentro questo package sono state inserite tutte le UI che vengono usate dalla Navigation.

#### screen/ImagesGrid.kt
* [x] Creazione UI (responsive) griglia principale
* * [x] Creazione di TopBar e Bottombar
* * [x] Rendere responsive alle modifiche eseguite in screen/Settings.kt
* [x] Rendere modificabile la dimensione dei box
* * [x] Aggiunta FloatingActionButton con intent a forocamera 
* [x] Creazione di una UI da mostrare se non ci sono foto
* * [x] Aggiungre intent per aprire la fotocamera se non ci sono foto

#### screen/ImageDetails.kt
* [x] Creazione UI (responsive) per immagine con metadati EXIF
* * [x] Visualizzazione in 3 schede dei metadati EXIF 
* [x] Aggiunta del bottone per la condivisione dell'immagine (Intent)
* [x] Il tocco sull'immagine mostrata deve riportare a FullImage

#### screen/FullImage.kt
* [x] Creazione UI (responsive) per immagine a schermo intero
* [x] Implementazione delle gesture
* * [x] Implementazione della gesture per lo zoom
* * [x] Implementazione delle gesture di swipe per la navigazione tra le immagini
* [x] Implementazione FloatingActionButton con ulteriori opzioni 
* * [x] Implementazione della cancellazione di immagini
* * [x] Implementazione SnackBar per conferma cancellazione
* * [x] Implementazione Toast per cancellazione non consentita 

#### screen/Settings.kt
* [x] Creazione UI (responsive) per la gestione delle impostazioni
* * [x] Possibilità di cambiare il nome della galleria 
* * [x] Possibilità di cambiare la dimensione mediante uno scroller
* * [x] Ordinamento per data
