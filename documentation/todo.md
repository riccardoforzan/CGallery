# TODO

Bisogna cercare di scorporare il più possibile la parte dell'applicazione che lavora con i dati
da quella che lavora con la UI Compose.

Creare quindi una classe kotlin che fornisca:
* ArrayList di tutte le immagini della galleria (forse meglio con un oggetto custom che contenga bitmap + eventuali metadati)
* Immagini di prova prese da internet a caso

## Next
 * [ ] Scorporare la parte che lavora con i dati da quella che lavora con la UI
 * [ ] Caricare immagini dall'URI interno del sistema
 * [ ] Richiedere accesso in lettura alle foto del sistema
 * [ ] Progettare view per la creazione di tag
 * [ ] Progettare view per l'assegnazione di tag ad una foto
 * [ ] Scrittura dei metadati su una foto [Documentazione](https://developer.android.com/reference/android/media/ExifInterface.html)
 * [ ] Rierca tra i metadata delle foto quelle taggate con i tag creati

## Extra
* Leggere dai metadata delle foto la posizione in cui sono state scattate e
  mostrare una mappa con dei segnaposti fissi sul luogo dove la foto è stata scattata
* Implementare la ricerca per luogo tra le  foto
