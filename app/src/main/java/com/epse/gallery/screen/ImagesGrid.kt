package com.epse.gallery.screen

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.epse.gallery.StorageUtils
import com.google.accompanist.coil.rememberCoilPainter

/**
 * @param ctx: context of the calling activity
 */
@ExperimentalFoundationApi
class ImagesGrid(private val ctx: Context, private val navController: NavHostController) {

    /**
     * This function shows the Grid.
     * It uses storage permissions.
     */
    @Composable
    fun ShowGrid() {

        //Minimum size of each image
        val size = 120.dp

        val photos = StorageUtils.getImageURIs()

        CreateGrid(photos,size)
    }

    /**
     * This functions prints on screen a grid with photos and columns number passed as a parameter
     * @param photos ArrayList containing URIs
     * @param size Number of DP for each grid element
     * https://developer.android.com/jetpack/compose/lists
     */
    @Composable
    private fun CreateGrid(photos: ArrayList<Uri>,size:Dp){
        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = size)
        ) {
            items(photos.size) { index ->
                Box(
                    modifier = Modifier.size(size)
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

