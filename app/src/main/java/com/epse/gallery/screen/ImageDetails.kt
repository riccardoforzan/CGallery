package com.epse.gallery.screen

import android.content.Context
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.google.accompanist.coil.rememberCoilPainter


class ImageDetails(private val ctx: Context, private val navController: NavHostController){

    @Composable
    fun ShowDetail(imageURI: Uri){

      //  Log.d("DEB Passed URI:",imageURI.toString())
        val imageStream = ctx.contentResolver.openInputStream(imageURI)
        val imageEI = ExifInterface(imageStream!!)
        val paint = rememberCoilPainter(imageURI)
        var addedToFavorite by remember{ mutableStateOf(false) }

        Scaffold(
            topBar= {
                TopAppBar(
                    title = { Text("Image Name") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    },

                    actions = {
                        //IconButton that does nothing
                        IconButton(onClick = { addedToFavorite  = !addedToFavorite }) {
                            if(addedToFavorite) Icon(Icons.Filled.Favorite, contentDescription = null)
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
                    .verticalScroll(scrollState)
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

                //stampa alcuni metadati
                val length= imageEI.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
                val width= imageEI.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
                val imageName= imageEI.getAttribute(ExifInterface.TAG_FILE_SOURCE)
                val date = imageEI.getAttribute(ExifInterface.TAG_DATETIME)
                val focal= imageEI.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
                val iso= imageEI.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
                val expTime= imageEI.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)
                val exp= imageEI.getAttribute(ExifInterface.TAG_EXPOSURE_INDEX)
                Text(text = "NOME= $imageName", Modifier.height(50.dp))
                Text(text = "length= $length", Modifier.height(50.dp))
                Text(text = "width= $width", Modifier.height(50.dp))
                Text(text = "data= $date", Modifier.height(50.dp))
                Text(text = "Focale = $focal", Modifier.height(50.dp))
                Text(text = "ISO = $iso", Modifier.height(50.dp))
                Text(text = "Tempo Esposizione = $expTime", Modifier.height(50.dp))
                Text(text = "Esposizione = $exp", Modifier.height(50.dp))

            }
        }

        imageStream.close()

    }


/*
@Preview(showBackground = true)
@Composable
fun DisplayImagePreview() {
    showDetail(Uri.parse("content://media/external/images/media/31"))
}*/

}


