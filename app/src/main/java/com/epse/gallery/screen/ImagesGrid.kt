package com.epse.gallery.screen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.epse.gallery.R
import com.epse.gallery.StorageUtils
import com.google.accompanist.coil.rememberCoilPainter

/**
 * @param ctx: context of the calling activity
 */
@ExperimentalFoundationApi
class ImagesGrid(private val ctx: Context, private val navController: NavHostController) {

    /**
     * This is an interchange function:
     * checks if the context given has the permission to read the storage.
     * It it has, then fetches the images from the storage,
     * otherwise shows an error
     */
    @Composable
    fun ShowGrid() {

        /**
         * This is redundant but if the app is suspended and permissions are revoked
         * this call shows an error
         */
        if(StorageUtils.hasReadStoragePermission(ctx)){
            val photos = StorageUtils.getImageURIs(ctx)
            CreateGrid(photos,180)
        }else{
            navController.navigate("setup-RSD")
        }

    }

    /**
     * TODO: Implement calculation
     * This function calculates dimensions to
     * Google guide https://developer.android.com/training/multiscreen/screensizes#TaskUseSWQuali
     */
    private fun calculateBoxSize(column:Int){
        val screenDensity = ctx.resources.displayMetrics.densityDpi
        val screenWidth = ctx.resources.displayMetrics.xdpi
        val screenHeight = ctx.resources.displayMetrics.ydpi

        /*
        Log.d("DENSITY:", screenDensity.toString())
        Log.d("WIDTH:", screenWidth.toString())
        Log.d("HEIGHT:", screenHeight.toString())
        */
    }

    /**
     * This functions prints on screen a grid with photos and columns number passed as a parameter
     * @param photos ArrayList containing URIs
     * @param boxSize Size in dp of every box element in the grid
     */
    @Composable
    private fun CreateGrid(photos: ArrayList<Uri>, boxSize:Int){
        LazyVerticalGrid(
            cells = GridCells.Fixed(3)
        ) {
            items(photos.size) { index ->
                Box(
                    Modifier
                        .size(boxSize.dp)
                ) {
                    Image(
                        painter = rememberCoilPainter(
                            request = photos[index]
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(1.dp)
                            .clickable {
                                navController.navigate(
                                    route = Screens.FullImage_ShowFullImage + "/${photos[index]}")
                            }
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

}

