package com.epse.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.content.edit
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.epse.gallery.screen.*

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    companion object{
        const val storagePermissionCode = 1
        var isPortrait by mutableStateOf(true)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        StorageUtils.acquireImageURIs(this)
    }

    override fun onResume() {
        super.onResume()
        //Check if permissions has changed while the app was in background
        val actualPermission = StorageUtils.hasReadStoragePermission(this)

        if(actualPermission){
            setContent{
                //Start reading the image and cache them
                isPortrait =
                    LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
                StorageUtils.acquireImageURIs(this)
                SetNavigation()
            }
        } else {
            /**
             * This code block is executed if the permission has been denied.
             * If the version of Android is > 6.0 then check if the application should show an UI
             * to ask for permissions.
             * If the version od Android is < 6.0 then permission must have been granted during
             * installation.
             */

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
                val shouldShowRationaleUI = this.shouldShowRequestPermissionRationale(permission)

                /**
                 * Check if is the first launch of the UI that asks for permissions
                 * If found in shared preferences firstTime = false
                 */
                val firstLaunch:Boolean = this.getPreferences(Context.MODE_PRIVATE)
                    .getBoolean("firstTime",true)

                if(firstLaunch){
                    setContent {
                        PermissionScreen(this).RationaleUI()
                    }
                } else {
                    val skip:Boolean = this.getPreferences(Context.MODE_PRIVATE)
                        .getBoolean("skipRationale",false)
                    when {
                        skip -> {
                            this.getPreferences(Context.MODE_PRIVATE).edit {
                                remove("skipRationale")
                            }
                            setContent {
                                PermissionScreen(this).ReadStorageDenied()
                            }
                        }
                        shouldShowRationaleUI -> {
                            setContent {
                                PermissionScreen(this).RationaleUI()
                            }
                        }
                        else -> {
                            setContent {
                                PermissionScreen(this).ReadStorageDenied()
                            }
                        }
                    }
                }

            } else {
                setContent {
                    PermissionScreen(this).ReadStorageDenied()
                }
            }
        }
    }

    /**
     * Managed permission following guidelines
     * https://developer.android.com/training/permissions/requesting
     */
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                             grantResults: IntArray) {

        /**
         * If user managed the permissions at least one time record it
         */

        val ft:Boolean = !(this.getPreferences(Context.MODE_PRIVATE).contains("firstTime"))
        if(ft) {
            this.getPreferences(Context.MODE_PRIVATE).edit()
                .putBoolean("firstTime", false)
                .putBoolean("skipRationale",true)
                .apply()
        }

        if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            setContent{
                SetNavigation()
            }
        } else {
            setContent{
                PermissionScreen(this).ReadStorageDenied()
            }
        }

    }

    /**
     * Composable function used to init the nav controller and it's path.
     */
    @Composable
    private fun SetNavigation(){

        /**
         * Init nav controller
         */
        val navController= rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screens.ImagesGrid_ShowGrid
        ) {


            /**
             * Calls the function to show the grid and refreshes the URIs found on the
             * storage
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
                FullImage(this@MainActivity,navController).ShowFullImage(
                    imageURI = Uri.parse(imageURI)
                )
            }

            composable(
                route = Screens.ImageDetails_ShowDetail + "/{imageURI}",
                arguments = listOf(navArgument("imageURI"){
                    type = NavType.StringType
                })
            ){ backStackEntry ->
                val imageURI = backStackEntry.arguments?.getString("imageURI")
                //Log.d("DEB Passed URI:",imageURI.toString())
                ImageDetails(this@MainActivity,navController,imageURI = Uri.parse(imageURI))
                    .ShowDetail(imageURI = Uri.parse(imageURI))
            }

        }
    }
}