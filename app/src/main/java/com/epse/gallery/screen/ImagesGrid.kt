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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.epse.gallery.R
import com.epse.gallery.StorageUtils
import com.epse.gallery.ui.theme.GalleryTheme
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
        val imagesList = StorageUtils.getImageURIs()
        if (imagesList.isNotEmpty()) {
            CreateGrid(imagesList)
        } else {
            NoPhotos()
        }
    }

    /**
     * This functions prints on screen a grid with photos and columns number passed as a parameter
     * @param photos ArrayList containing URIs
     * @param size Number of DP for each grid element
     * https://developer.android.com/jetpack/compose/lists
     */
    @Composable
    private fun CreateGrid(photos: List<Uri>,size:Dp = 120.dp) {
        GalleryTheme() {
            LazyVerticalGrid(
                cells = GridCells.Adaptive(minSize = size),
            ) {
                items(photos.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(size)
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
                                        route = Screens.FullImage_ShowFullImage + "/${photos[index]}"
                                    )
                                }
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }

    /**
     * Screen to show when read on external storage permission has not been granted
     */
    @Composable
    fun NoPhotos(){
        GalleryTheme(){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ctx.getString(R.string.no_images),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }

}

