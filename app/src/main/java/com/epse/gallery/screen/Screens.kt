package com.epse.gallery.screen

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavHostController

/**
 * This class contains information about all the available path in the application.
 */
@ExperimentalFoundationApi
class Screens(private val ctx: Activity, private val navController: NavHostController) {

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

    val setup = SetupScreen(ctx,navController)
    val imagesGrid = ImagesGrid(ctx,navController)
    val imageDetails = ImageDetails(ctx,navController)
    val fullImage = FullImage(ctx,navController)
    val displayImage = DisplayImage(ctx,navController)

}