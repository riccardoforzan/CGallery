package com.epse.gallery

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

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

    /**
     * Dummy overriding, it only launches another activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, ImageActivity::class.java)
        startActivity(intent)
    }

    /**
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
    */

}

@Composable
/**
 * TODO:
 * Show a grid layout taking the images from the external storage
 */
fun ShowAllImages(){

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