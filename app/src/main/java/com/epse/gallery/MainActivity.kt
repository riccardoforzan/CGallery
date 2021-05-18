package com.epse.gallery

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
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
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.epse.gallery.screen.*

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    companion object{
        val storagePermissionCode = 1
        var isPortrait by mutableStateOf(true)
    }

    override fun onStart() {
        super.onStart()
        setContent {
            isPortrait = (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
            ManagePermissions()
        }
    }

    /**
     * TODO: Improve, when app is in background and permission changes from denied to granted
     */
    override fun onResume() {
        if(StorageUtils.hasReadStoragePermission(this)){
            super.onResume()
        } else {
            super.onStart()
        }
    }

    /**
     * Managed permission following guidelines
     * https://developer.android.com/training/permissions/requesting
     */
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                             grantResults: IntArray) {

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

    @Composable
    fun ManagePermissions(){
        if(StorageUtils.hasReadStoragePermission(this)){
            Log.d("DEB PERMISSION","Permission granted")
            SetNavigation()
        } else {
            Log.d("DEB PERMISSION","Permission denied")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
                val shouldShowRationaleUI = this.shouldShowRequestPermissionRationale(permission)
                Log.d("DEB SHOW RATIONALE", shouldShowRationaleUI.toString())
                if(shouldShowRationaleUI){
                    PermissionScreen(this).RationateUI()
                } else {
                    PermissionScreen(this).ReadStorageDenied()
                }
            } else {
                PermissionScreen(this).ReadStorageDenied()
            }
        }
    }

    /**
     * Composable function used to init the nav controller
     */
    @Composable
    fun SetNavigation(){

        /**
         * Init nav controller
         */
        val navController= rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screens.ImagesGrid_ShowGrid
        ) {


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