package com.epse.gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Created a sort of image shower following
 * https://developer.android.com/jetpack/compose/tutorial
 *
 * TODO:
 * - Make it zoomable
 * - Make black borders
 * - Retrieve info to show from metadata
 */

class ImageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * The code inside this block is rendered on the activity
         */
        setContent {
            ImageElement()
        }
    }

}

@Composable
fun ImageElement(){
    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.forest),
            contentDescription = null,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Text(text = "Name: Stuff1")
        Text(text = "Description: Stuff2")
        Text(text = "Tags: Stuff3")
    }
}

/**
 * Dev stuff only
 * Allows previewing the setContent block of the onCreate() function
 * on the right of the screen
 */
@Preview(showBackground = true)
@Composable
fun ImageActivityPreview() {
    ImageElement()
}