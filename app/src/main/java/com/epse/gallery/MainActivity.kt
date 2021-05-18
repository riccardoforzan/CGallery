package com.epse.gallery

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.epse.gallery.screen.*
import com.google.accompanist.coil.rememberCoilPainter

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    private val storagePermissionCode = 1

    companion object{
        var isPortrait by mutableStateOf(true)
    }

    override fun onStart() {
        super.onStart()
        setContent {
            isPortrait = (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
            ManagePermissions()
        }
    }

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
    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>,
                                             grantResults: IntArray) {

        if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            setContent{
                SetNavigation()
            }
        } else {
            setContent{
                ReadStorageDenied()
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
                    RationateUI()
                } else {
                    ReadStorageDenied()
                }
            } else {
                ReadStorageDenied()
            }
        }
    }

    @Composable
    fun RationateUI(){
        MaterialTheme {
            val typography = MaterialTheme.typography
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    /**
                     * TODO:
                     * Insert application logo as request parameter
                     */
                    painter = rememberCoilPainter(request = null),
                    contentDescription = null,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(16.dp))

                Text(
                    text=this@MainActivity.getString(R.string.permission_read_external_storage_description),
                    style = typography.h6
                )

                Button(
                    modifier = Modifier.padding(30.dp),
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
                            this@MainActivity.requestPermissions(arrayOf(permission),storagePermissionCode)
                        }
                    }
                ){
                    Text(
                        text = this@MainActivity.getString(R.string.btn_manage_permissions),
                    )
                }

            }
        }

    }

    /**
     * Screen to show when read on external storage permission has not been granted
     */
    @Composable
    fun ReadStorageDenied(){
        MaterialTheme{
            val typography = MaterialTheme.typography
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text=this@MainActivity.getString(R.string.permission_read_external_storage_not_granted),
                    style = typography.h6
                )
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