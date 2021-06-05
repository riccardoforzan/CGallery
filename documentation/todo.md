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

## TODO
* [ ] Creazione di una pagina di presentazione dell'applicazione
* [ ] Aggiungere un banner di avviso se l'eliminazione di una foto non è consentita perchè manca il permesso in lettura
* [ ] Possibilità di cambiare l'ordinamento delle foto
* [ ] Portare tutte le stringhe in in res/strings.xml
* [ ] Aggiunta di una pagina di impostazioni (Dai un nome alla galleria, seleziona la grandezza delle immagini e l'ordinamento di default)
* [X] Aggiunta del tasto per apertura della fotocamera
* [ ] Adattamento dinamico delle dimensioni
* [ ] Aggiunta del tasto per la condivisione in ImagesDetails [Documentazione](https://developer.android.com/training/sharing/send)
* [ ] Aggiunta della descrizione come meta ad una foto [Documentazione](https://developer.android.com/reference/android/media/ExifInterface.html)

## TEST
* [ ] Debug sugli stati dell'applicazione

## BUG NOTI
* [ ] Cambiando i permessi (da non concessi a concessi) in background e premendo riptetutamente il tasto indietro mostra lo stato precedente

