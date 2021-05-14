package com.epse.gallery.screen

/**
 * This class contains information about all the available path in the application.
 */
sealed class Screens {

    /**
     * This companion object stores all the possible routes in the application
     */
    companion object{
        const val SetupAskForStorage = "SetupScreen.AskForStorage"
        const val SetupReadStorageDenied = "SetupScreen.ReadStorageDenied"
        const val ImagesGridShowGrid = "ImagesGrid.ShowGrid"
        const val FullImageShowFullImage = "FullImage.ShowFullImage"
        const val ImageDetailsShowDetail = "ImageDetails.ShowDetail"
    }

}