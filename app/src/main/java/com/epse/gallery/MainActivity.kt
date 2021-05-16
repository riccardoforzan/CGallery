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
import com.epse.gallery.screen.*

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    companion object{
        var isPortrait by mutableStateOf(true)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            isPortrait = (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
            SetNavigation()
        }
    }

    @Composable
    fun SetNavigation(){

        /**
         * Init nav controller
         */
        val navController= rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screens.SetupScreen_AskPermissions
        ) {

            /**
             * Calls in SetupScreen the function to ask for read permission in storage
             */
            composable(route = Screens.SetupScreen_AskPermissions){
                SetupScreen(this@MainActivity,navController).AskPermissions()
            }

            /**
             * Calls in SetupScreen the function to ask for read permission in storage
             */
            composable(route = Screens.SetupScreen_AskForReadStorage){
                SetupScreen(this@MainActivity,navController).AskForReadStorage()
            }

            /**
             * Calls in SetupScreen the function to show error when permission to read the storage
             * has not been granted by the user (or has been revoke)
             */
            composable(route = Screens.SetupScreen_ReadStorageDenied){
                SetupScreen(this@MainActivity,navController).ReadStorageDenied()
            }

            //


            /**
             * Calls ImagesGrid
             */
            composable(route = Screens.ImagesGrid_ShowGrid){
                ImagesGrid(this@MainActivity,navController).ShowGrid()
            }

            /**
             * This is how we are supposed to pass custom objects
             * but this way does not work right now because
             * 'Parcelables don't support default values'
             * even if android.net.Uri is a Parcelable type
             * https://developer.android.com/reference/android/net/Uri
            composable(
                route = Screens.FullImageShowFullImage + "/{imageURI}",
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

            composable(
                route = Screens.FullImage_ShowFullImage + "/{imageURI}",
                arguments = listOf(navArgument("imageURI"){
                    type = NavType.StringType
                })
            ){ backStackEntry ->
                val imageURI = backStackEntry.arguments?.getString("imageURI")
                //Log.d("DEB Passed URI:",imageURI.toString())
                FullImage(this@MainActivity,navController).ShowFullImage(imageURI = Uri.parse(imageURI))
            }

            composable(
                route = Screens.ImageDetails_ShowDetail + "/{imageURI}",
                arguments = listOf(navArgument("imageURI"){
                    type = NavType.StringType
                })
            ){ backStackEntry ->
                val imageURI = backStackEntry.arguments?.getString("imageURI")
                //Log.d("DEB Passed URI:",imageURI.toString())
                ImageDetails(this@MainActivity,navController).ShowDetail(imageURI = Uri.parse(imageURI))
            }

        }
    }

}