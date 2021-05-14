package com.epse.gallery

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.google.accompanist.coil.rememberCoilPainter

class FullImage (private val navController: NavController){
    private var backgroundColor = Color.Black
    private lateinit var myURI:Uri

    @Composable
    fun ShowFullImage(imageURI: Uri){
        myURI=imageURI
        if(isPortrait){
            Portrait()
        }
        else{
            Landscape()
        }
    }

    @Composable
    fun Portrait(){
        val paint = rememberCoilPainter(myURI)
        Column(
            modifier= Modifier
                .background(backgroundColor)
                .fillMaxHeight()
        )
        {
            Box(modifier = Modifier
                .weight(0.1f)
                .fillMaxSize()){
                Icon(Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier= Modifier
                        .padding(10.dp)
                        .size(30.dp)
                        .clickable { navController.navigate("imagesGrid") }
                )
            }
            Box(modifier = Modifier
                .weight(0.8f)
                .fillMaxSize()){
                Image(painter = paint, contentDescription = null, modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth())
            }
            Box(modifier = Modifier
                .weight(0.1f)
                .fillMaxSize()){
                FloatingActionButton(
                    backgroundColor = Color.White,
                    modifier= Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),
                    onClick = { navController.navigate("imageDetails/${myURI}")})
                {
                    Icon(Icons.Filled.Edit, contentDescription = null)
                }
            }
        }

    }

    @Composable
    fun Landscape(){
        val paint = rememberCoilPainter(myURI)
        Row(
            modifier= Modifier
                .background(backgroundColor)
                .fillMaxHeight()
        )
        {
            Box(modifier = Modifier
                .weight(0.1f)
                .fillMaxSize()){
                Icon(Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier= Modifier
                        .padding(10.dp)
                        .size(30.dp)
                        .clickable { navController.navigate("imagesGrid") }
                )
            }
            Box(modifier = Modifier
                .weight(0.8f)
                .fillMaxSize()){
                Image(painter = paint, contentDescription = null, modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth())
            }
            Box(modifier = Modifier
                .weight(0.1f)
                .fillMaxSize()){
                FloatingActionButton(
                    backgroundColor = Color.White,
                    modifier= Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),
                    onClick = { navController.navigate("imageDetails/${myURI}") })
                {
                    Icon(Icons.Filled.Edit, contentDescription = null)
                }
            }
        }
    }
}