package com.epse.gallery

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController

var isPortrait by mutableStateOf(true)
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    /**
     * TODO:
     * Create a setup screen to ask for permissions at the start of the app if permission are
     * not granted
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            isPortrait = (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
            SetNavigation()
        }
    }

    @Composable
    fun SetNavigation(){

        val navController= rememberNavController()

        NavHost(navController = navController, startDestination = "imagesGrid" ) {

            /**
             * Debug route, creates a ImagesGrid with images from the web
             composable("imagesGrid"){ImagesGrid(this@MainActivity, navController).ShowGridRandomImages(navController)}
             */

            composable("imagesGrid"){ImagesGrid(this@MainActivity, navController).ShowGridAllImages()}

            /**
             * This is how we are supposed to pass custom objects
             * but this way does not work right now because
             * 'Parcelables don't support default values'
             * even if android.net.Uri is a Parcelable type
             * https://developer.android.com/reference/android/net/Uri
            composable("displayImage/{imageURI}",
                arguments = listOf(navArgument("imageURI"){
                    type = NavType.ParcelableType(Uri::class.java)
                })
            ){ backStackEntry ->
                val imageURI = backStackEntry.arguments?.getParcelable<Uri>("imageURI")
                Log.d("DEB Passed URI:",imageURI.toString())
                DisplayImage().MovingImage(imageURI = imageURI!!)
            }
            */

            /**
             * Workaround: Passing URI as a string and then reconstruct it
             */
            composable("fullImage/{imageURI}",
                arguments = listOf(navArgument("imageURI"){
                    type = NavType.StringType
                })
            ){ backStackEntry ->
                val imageURI = backStackEntry.arguments?.getString("imageURI")
                Log.d("DEB Passed URI:",imageURI.toString())
                FullImage(navController).ShowFullImage(imageURI = Uri.parse(imageURI))
            }

            composable("displayImage/{imageURI}",
                arguments = listOf(navArgument("imageURI"){
                    type = NavType.StringType
                })
            ){ backStackEntry ->
                val imageURI = backStackEntry.arguments?.getString("imageURI")
                Log.d("DEB Passed URI:",imageURI.toString())
                DisplayImage().MovingImage(imageURI = Uri.parse(imageURI))
            }

        }
    }

}