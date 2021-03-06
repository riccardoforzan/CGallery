package com.epse.gallery.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.epse.gallery.R
import com.epse.gallery.SPStrings
import com.epse.gallery.StorageUtils
import com.epse.gallery.ui.theme.GalleryTheme
import com.google.accompanist.coil.rememberCoilPainter

/**
 * This UI shows the grid with all the photos.
 * It's responsive to changes made on the settings page.
 * @param ctx context of the calling activity
 * @param navController navController registered for the application
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
            SetupUI(imagesList)
        } else {
            NoPhotos()
        }
    }

    /**
     * This function sets up the UI top bar and bottom bar and calls the function to print out the
     * grid of images.
     * @param photos list of Uri photos to feed CreateGrid function
     */
    @Composable
    fun SetupUI(photos: List<Uri>) {
        val fabShape = CircleShape
        val scaffoldState = rememberScaffoldState()
        GalleryTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    //Getting title of the gallery from shared preferences
                    val name = SPStrings.preferences
                    val sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE)

                    val defaultTitle = ctx.getString(R.string.app_name)
                    var title:String = sp.getString(SPStrings.gallery_title,defaultTitle)!!
                    //If title is a string of 0 char set it at the default value
                    if (title.isEmpty()) title = defaultTitle

                    TopAppBar(
                        title = { Text(text=title) },
                        backgroundColor = MaterialTheme.colors.primary
                    )
                },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
                floatingActionButton = {
                    FloatingActionButton(
                        shape = fabShape,
                        onClick = {
                            //Launch camera intent
                            val cameraIntent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
                            ctx.startActivity(cameraIntent)
                        }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.photo_camera), ctx.getString(R.string.start_taking_photos))
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        cutoutShape = fabShape,
                        backgroundColor = MaterialTheme.colors.primary
                    ) {
                        IconButton(
                            onClick = { navController.navigate(route = Screens.Settings_ShowSettings) }
                        ){
                            Icon(Icons.Filled.Settings,"")
                        }
                    }
                },
                content = {innerPadding ->
                        // Apply the padding globally to the whole BottomNavScreensController
                        Box(modifier = Modifier.padding(innerPadding)) {
                            //Get size of the box for the images from the shared preferences
                            val sp = ctx.getSharedPreferences(SPStrings.preferences, Context.MODE_PRIVATE)
                            val size = sp.getFloat(SPStrings.image_size_on_grid, 120.0F)
                            //Create grid with all the photos
                            CreateGrid(photos = photos,size.dp)
                        }
                })
        }
    }

    /**
     * This functions prints on screen a grid with photos and columns number passed as a parameter
     * @param photos ArrayList containing URIs
     * @param size Number of DP for each grid element
     * https://developer.android.com/jetpack/compose/lists
     */
    @Composable
    private fun CreateGrid(photos: List<Uri>,size:Dp) {
        GalleryTheme {
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
     * Screen to show when no photos are detected
     */
    @Composable
    fun NoPhotos(){
        GalleryTheme{
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(1.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ctx.getString(R.string.no_images),
                    color = MaterialTheme.colors.onBackground
                )

                Button(
                    modifier = Modifier.padding(30.dp),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
                    onClick = {
                            //Launch camera intent
                            val cameraIntent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
                            ctx.startActivity(cameraIntent)
                    }
                ) {
                    Text(
                        text = ctx.getString(R.string.start_taking_photos),
                    )
                }

            }//column
        }
    }

}

