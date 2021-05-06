package com.epse.gallery

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

/**
 * Created a sort of image shower following
 * https://developer.android.com/jetpack/compose/tutorial
 *
 * TODO:
 * - Make it zoomable
 * - Make black borders
 * - Retrieve info to show from metadata
 */

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowAllImages()
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowAllImages(){

    /**
     * Experimental: grab a reference to a navigation controller
     */
    val navController = rememberNavController()

    /**
     * Creates a dummy layout with 20 elements having the
     * same image to display
     */
    val numbers = (0..20).toList()

    LazyVerticalGrid(
        cells = GridCells.Fixed(4)
    ) {
        items(numbers.size) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.forest),
                    contentDescription = null,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

/**
 * Dev stuff only
 * Allows previewing the setContent block of the onCreate() function
 * on the right of the screen
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShowAllImages()
}