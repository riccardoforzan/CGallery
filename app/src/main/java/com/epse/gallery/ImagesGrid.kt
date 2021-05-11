package com.epse.gallery

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
    fun ShowGridAllImages() {

        if(StorageUtils.hasReadStoragePermission(ctx)){
            var column = 4;
            val photos = StorageUtils.getImageURIs(ctx)
            createGrid(photos = photos,column = column)
        }else{
            readStorageDenied()
        }
        
    }

    /**
     * This functions prints on screen a grid with photos and columns number passed as a parameter
     * @param photos ArrayList containing URIs
     * @param columns number of column to display
     */
    @Composable
    private fun createGrid(photos: ArrayList<Uri>, column:Int){
        LazyVerticalGrid(
            cells = GridCells.Fixed(column)
        ) {
            items(photos.size) { index ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = rememberCoilPainter(
                            request = photos[index]
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .height(180.dp)
                            .clickable { navController.navigate("displayImage/${photos[index]}") }
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )

                }
            }
        }
    }

    /**
     * Screen to show when read on external storage permission has not been granted
     */
    @Composable
    fun readStorageDenied(){
        MaterialTheme{
            val typography = MaterialTheme.typography
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    ctx.getString(R.string.permission_read_external_storage_not_granted),
                    style = typography.h6
                )
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
