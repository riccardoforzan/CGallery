package com.epse.gallery

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import android.provider.MediaStore
import android.util.Log
import android.net.Uri
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.coil.rememberCoilPainter

/**
 * @param ctx: context of the calling activity
 */
class ImagesGrid(private val ctx: Context, private val navController: NavController) {

    /**
     * Generates an array of Dummy Elements with number elements
     * @param number: number of dummy items
     * @return ArrayList of dummy items
     */
    private fun generateDummyArray(number: Int):ArrayList<Uri>{
        val examplePhoto: Uri = Uri.parse("android.resource://com.epse.gallery/"
                + R.drawable.forest)
        Log.d("URI EXAMPLE",examplePhoto.toString())
        val imagesURIs: ArrayList<Uri> = ArrayList()
        for(index in 0..number){
            imagesURIs.add(examplePhoto)
        }
        return imagesURIs
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ShowGridAllImages(){

        val photos = generateDummyArray(20)

        LazyVerticalGrid(
            cells = GridCells.Fixed(4)
        ) {
            items(photos.size) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    photos.forEach { photo ->
                        val tmpBitmap:Bitmap = MediaStore.Images.Media.getBitmap(ctx.contentResolver,photo)
                        Image(
                            /**
                             * TODO:
                             * Find a method to replace the argument of the painter
                             * with the bitmap variable in tmpBitMap
                             */
                            painter = painterResource(R.drawable.forest),
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

    }


    /**
     * This function returns the images saved inside the storage of the smartphone.
     * Requires access to the media.
     * @param context: context of the application, used to query the internal storage
     * @return ArrayList containing image URIs
     */
    private fun getImageURIs(context:Context): ArrayList<String>{
        val imagesURIs: ArrayList<String> = ArrayList()
        val columns = arrayOf(MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media._ID)
        val imageCursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns,
            null,null,null)

        while(imageCursor!!.moveToNext()){
            val dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
            imagesURIs.add(imageCursor.getString(dataColumnIndex))
        }

        Log.d("Number of elements", imageCursor.count.toString())
        Log.d("ARRAY OF IMAGES",imagesURIs.toString())

        return imagesURIs
    }

    /**
     * Shows a grid layout with random images fetched from https://picsum.photos/300/300
     * This functions requires the internet access (see AndroidManifest.xml).
     * This function uses the coil library to fetch images.
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ShowGridRandomImages(navController: NavController){

        val photos = generateDummyArray(20)

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
     */
    @Preview(showBackground = true)
    @Composable
    fun ShowGridPreview() {
        ShowGridAllImages()
    }

}
