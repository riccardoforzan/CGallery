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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.epse.gallery.R
import com.epse.gallery.SPUtils
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
            BottomNavigation(imagesList)
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

    @Composable
    fun BottomNavigation(photos: List<Uri>) {
        val fabShape = CircleShape
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        GalleryTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    //Getting title of the gallery from shared preferences
                    val name = SPUtils.preferences
                    val sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE)
                    val title:String = sp.getString(SPUtils.gallery_title,"DEFAULT")!!
                    TopAppBar(title = { Text(text=title) })
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
                        Icon(Icons.Filled.Add, "Add a new photo to this library")
                    }
                },
                bottomBar = {
                    BottomAppBar(cutoutShape = fabShape) {
                        IconButton(
                            onClick = { navController.navigate(route = Screens.Settings_ShowSettings) }
                        ){
                            Icon(Icons.Filled.Settings,"")
                        }
                    }
                },
                content = {
                    CreateGrid(photos = photos)
                })
        }
    }

}

