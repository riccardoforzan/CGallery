package com.epse.gallery.screen

/**
 * This class contains information about all the available path in the application.
 */
sealed class Screens {

    /**
     * This companion object stores all the possible routes in the application
     */
    companion object{
        const val SetupScreen_AskPermissions = "SetupScreen.AskPermissions"
        const val SetupScreen_AskForReadStorage = "SetupScreen.AskForReadStorage"
        const val SetupScreen_ReadStorageDenied = "SetupScreen.ReadStorageDenied"
        const val ImagesGrid_ShowGrid = "ImagesGrid.ShowGrid"
        const val FullImage_ShowFullImage = "FullImage.ShowFullImage"
        const val ImageDetails_ShowDetail = "ImageDetails.ShowDetail"
    }

}