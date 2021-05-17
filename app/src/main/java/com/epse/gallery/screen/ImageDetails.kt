package com.epse.gallery.screen

import android.content.Context
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.epse.gallery.StorageUtils
import com.google.accompanist.coil.rememberCoilPainter
import java.io.File
import kotlin.math.round


class ImageDetails(private val ctx: Context, private val navController: NavHostController){

    @Composable
    fun ShowDetail(imageURI: Uri) {


        Log.d("DEB  URI toString:", imageURI.toString())
        Log.d("DEB URI path:", imageURI.path!!)

        //var imageFile = File(imageURI.path)
        val imageStream = ctx.contentResolver.openInputStream(imageURI)

        val imageEI = ExifInterface(imageStream!!)
        val paint = rememberCoilPainter(imageURI)
        var addedToFavorite by remember { mutableStateOf(false) }
        val description = imageEI.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
        val length = imageEI.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
        val width = imageEI.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
        val imageName = imageEI.getAttribute(ExifInterface.TAG_FILE_SOURCE)
        val date = imageEI.getAttribute(ExifInterface.TAG_DATETIME)
        val focal = imageEI.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
        val iso = imageEI.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)
        val expTime = round(imageEI.getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME,0.0)*1000)/1000
        val exp = imageEI.getAttribute(ExifInterface.TAG_EXPOSURE_INDEX)
        val GPSlatitude =  imageEI.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
        val GPSlongitude =  imageEI.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Image Name") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    },

                    actions = {
                        //IconButton that does nothing
                        IconButton(onClick = { addedToFavorite = !addedToFavorite }) {
                            if (addedToFavorite) Icon(
                                Icons.Filled.Favorite,
                                contentDescription = null
                            )
                            else Icon(Icons.Filled.FavoriteBorder, contentDescription = null)
                        }
                    }
                )
            }
        )

        {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    //.verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Image(
                    painter = paint,
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clickable(onClick = {}),
                    contentScale = ContentScale.Crop
                )


                Spacer(Modifier.height(16.dp))


                var state by remember { mutableStateOf(0) }
                TabRow(selectedTabIndex = state) {
                    Tab(
                        text = { Text("General") },
                        selected = state == 0,
                        onClick = { state = 0}
                    )

                    Tab(
                        text = { Text("Shooting") },
                        selected = state == 1,
                        onClick = { state = 1}
                    )

                    Tab(
                        text = { Text("Other") },
                        selected = state == 2,
                        onClick = { state = 2}
                    )

                }


                val fileAttributes:Map<String, String> = StorageUtils.getFileData(ctx,imageURI)

                /**
                 * Iterate oer the entries of the map
                 */

                Log.d("DEB keys: ",fileAttributes.keys.toString())
                Log.d("DEB values: ",fileAttributes.values.toString())

                Column (
                    modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(20.dp)
                ){
                    if (state == 0) {
                        Text(text = "NOME= $imageName", Modifier.height(50.dp))
                        Text(text = "Path:", Modifier.height(50.dp))
                        Text(text = "latitude= $GPSlatitude", Modifier.height(50.dp))
                        Text(text = "longitude= $GPSlongitude", Modifier.height(50.dp))
                        Text(text = "data= $date", Modifier.height(50.dp))
                    }
                    if (state == 1) {

                        Text(text = "Focale = $focal", Modifier.height(50.dp))
                        Text(text = "ISO = $iso", Modifier.height(50.dp))
                        Text(text = "exposure time = $expTime", Modifier.height(50.dp))
                        Text(text = "exposure index = $exp", Modifier.height(50.dp))
                        Text(text = "length= $length", Modifier.height(50.dp))
                        Text(text = "width= $width", Modifier.height(50.dp))
                    }
                    if (state == 2) {
                        Text(text = "altro", Modifier.height(50.dp))
                    }

                }
            }
        }
    }

        //imageStream.close()

}


/*
@Preview(showBackground = true)
@Composable
fun DisplayImagePreview() {
    showDetail(Uri.parse("content://media/external/images/media/31"))
}*/




