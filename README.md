# CGallery

<p align="center">
  <img src="https://github.com/riccardoforzan/ProgettoSE/blob/main/app/src/main/res/mipmap-xxxhdpi/app_icon.png"/>
</p>

Galleria per la visualizzazione di sole foto sviluppata utlizzando JetPack Compose

## Tecnologie utilizzate:
* [Android Jetpack - Compose](https://developer.android.com/jetpack/compose)
* [Coil library](https://github.com/google/accompanist/tree/main/coil)
* [Coil documentation](https://google.github.io/accompanist/coil/)

____
# TODO

## DONE
* [x] Scorporare la parte che lavora con i dati da quella che lavora con la UI
* [x] Caricare immagini dall'URI interno del sistema
* [x] Passaggio argomenti via navController alle funzioni composable
* [x] Progettare composable Immagine a schermo intero (con floating button)
* [x] SnackBar per la conferma di cancellazione
* [ ] ImagesDetails con modello grafico [Riferimento](https://developer.android.com/jetpack/compose/tutorial)
* [x] Rendere responsive ImagesGrid
* [x] Implementare i metodi su StroageUtils (cancellazione e recupero ulteriori files)
* [x] Schermata di installazione per richiedere accesso in lettura alle foto del sistema
* [x] Supporto per la dark mode
* [x] Supporto per le gesture

## TODO (Updated 5/6/2021)
* [X] Creazione di una pagina di presentazione dell'applicazione
* [X] Aggiungere un banner di avviso se l'eliminazione di una foto non è consentita perchè manca il permesso in lettura (Aggiunto un toast, da provare)
* [X] Possibilità di cambiare l'ordinamento delle foto
* [X] Portare tutte le stringhe in in res/strings.xml
* [X] Aggiunta di una pagina di impostazioni (Nome galleria, ordinamento e dimensione immagini)
* [X] Aggiunta del tasto per apertura della fotocamera
* [X] Click sull'immagine da ImageDetails che riporti a FullImage
* [ ] Adattamento dinamico delle dimensioni dei banner
* [ ] ImagesGrid aggiungere padding alla fine della schermata in modo che la barra di navigazione non copra le ultime foto
* [X] Aggiunta del tasto per la condivisione in ImagesDetails [Documentazione](https://developer.android.com/training/sharing/send)
* [ ] Aggiunta della descrizione come meta ad una foto [Documentazione](https://developer.android.com/reference/android/media/ExifInterface.html)

## TEST
* [ ] Debug sugli stati dell'applicazione

## BUG NOTI
* [ ] Cambiando i permessi (da non concessi a concessi) in background e premendo riptetutamente il tasto indietro mostra lo stato precedente
