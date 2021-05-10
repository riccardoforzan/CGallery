package com.epse.gallery

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetNavigation()
        }
    }

    @Composable
    fun SetNavigation(){
        //Surface(color= MaterialTheme.colors.primary)
        val navController= rememberNavController()

        NavHost(navController = navController, startDestination = "imagesGrid" ) {
            composable("imagesGrid"){ImagesGrid(this@MainActivity, navController).ShowGridAllImages(navController)}
            //composable("imagesGrid"){ImagesGrid(this@MainActivity, navController).ShowGridRandomImages(navController)}
            /**
             * TODO:
             * Find a way to pass a URI
             */
            composable("displayImage"){DisplayImage().MovingImage(Uri.parse("content://media/external/images/media/31"))}
        }
    }
}