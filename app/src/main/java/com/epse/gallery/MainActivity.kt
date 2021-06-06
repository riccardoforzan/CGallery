package com.epse.gallery

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.epse.gallery.screen.*
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    companion object{
        lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intentSenderLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if(it.resultCode == RESULT_OK) {
                //Addressing API 29 (Android 10) tricky behaviour
                if(Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                    val name = SPStrings.preferences
                    val sp = getSharedPreferences(name, Context.MODE_PRIVATE)
                    val spKey = SPStrings.API29_delete
                    val deletedImage = sp.getString(spKey,null)
                    if(deletedImage!=null) {
                        val deletedImageUri = Uri.parse(deletedImage)
                        lifecycleScope.launch {
                            StorageUtils.delete(this@MainActivity, deletedImageUri ?: return@launch)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //Check in shared preferences if is the first launch
        val name = SPStrings.preferences
        val sp = this.getSharedPreferences(name, Context.MODE_PRIVATE)
        val firstTime:Boolean = !(sp.contains(SPStrings.first_time))

        if(firstTime){
            setContent{
                val ctx = LocalContext.current
                ctx.startActivity(Intent(ctx,FirstTimeActivity::class.java))
            }
        } else {
            //Check if permissions has changed while the app was in background
            val actualPermission = StorageUtils.hasReadStoragePermission(this)
            if (actualPermission) {
                setContent {
                    //Start reading the image and cache them
                    //Getting preferred order
                    val order = sp.getString(SPStrings.default_order,"DESC")!!
                    StorageUtils.setQueryOrder(order,this)
                    SetNavigation()
                }
            } else {
                setContent {
                    val ctx = LocalContext.current
                    ctx.startActivity(Intent(ctx, PermissionActivity::class.java))
                }
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
             * Navigation to the settings screen
             */
            composable(route = Screens.Settings_ShowSettings){
                Settings(this@MainActivity,navController).ShowSettings()
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