package com.epse.gallery

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import android.util.Log
import com.google.accompanist.coil.rememberCoilPainter

/**
 * @param ctx: context of the calling activity
 */
class ImagesGrid(private val ctx: Context, private val navController: NavController) {

    /**
     * Shows a grid layout with random images fetched from https://picsum.photos/300/300
     * This functions requires the internet access (see AndroidManifest.xml).
     * This function uses the coil library to fetch images.
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ShowGridAllImages(navController: NavController) {

        val photos = ImagesFetcher().getImageURIs(ctx)
        Log.d("DEB: # of photo: ", photos.size.toString())

        var index = 0

        LazyVerticalGrid(
            cells = GridCells.Fixed(4)
        ) {
            items(photos.size) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = rememberCoilPainter(
                            request = photos.get(index++)
                        ),
                            contentDescription = null,
                            modifier = Modifier
                                .height(180.dp)
                                .clickable { navController.navigate("displayImage") }
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )

                }
            }
        }
    }

    /**
     * Shows a grid layout with random images fetched from https://picsum.photos/300/300
     * This functions requires the internet access (see AndroidManifest.xml).
     * This function uses the coil library to fetch images.
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ShowGridRandomImages(navController: NavController){

        val photos = ImagesFetcher().generateDummyArray(20)

        LazyVerticalGrid(
            cells = GridCells.Fixed(4)
        ) {
            items(photos.size) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = rememberCoilPainter(
                            request = "https://picsum.photos/300/300"
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .height(180.dp)
                            .clickable { navController.navigate("displayImage") }
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

    /**
     * The preview function does not work because the ImagesGrid class does not have
     * a default constructor
     *
    @Preview(showBackground = true)
    @Composable
    fun ShowGridPreview() {
        ShowGridAllImages()
    }
    */

}
