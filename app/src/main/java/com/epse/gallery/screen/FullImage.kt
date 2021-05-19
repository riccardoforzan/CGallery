package com.epse.gallery.screen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.epse.gallery.MainActivity
import com.google.accompanist.coil.rememberCoilPainter
import kotlin.math.roundToInt

@ExperimentalFoundationApi
class FullImage(private val ctx: Context, private val navController: NavHostController) {

    private var backgroundColor = Color.Black
    private lateinit var myURI: Uri
    private var showButton by mutableStateOf(false)
    private var expandedState by mutableStateOf(false)
    private var zoom by mutableStateOf(1f)
    private var offsetX by mutableStateOf(0f)
    private var offsetY by mutableStateOf(0f)

    @Composable
    fun ShowFullImage(imageURI: Uri) {
        myURI = imageURI
        val paint = rememberCoilPainter(myURI)
        Box(modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit){
                detectTapGestures(
                    onDoubleTap = {
                        if(zoom==1f)
                            zoom=2.5f
                        else{
                            zoom=1f
                            offsetX=0f
                            offsetY=0f
                        }
                    },
                    onTap = {showButton=!showButton}
                )
            }
        ) {
            Image(painter = paint, contentDescription = null, modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .align(Alignment.Center)
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .graphicsLayer(
                    scaleX = zoom,
                    scaleY = zoom
                )
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { _, pan, gestureZoom, _ ->
                            val newZoom = zoom * gestureZoom
                            zoom = maxOf(1.0F, minOf(3.0F, newZoom))

                            val newOffSetX = offsetX + pan.x * zoom
                            val maxOffSetX = (paint.intrinsicSize.width / 2) * (zoom - 1)
                            offsetX = maxOf(maxOffSetX * (-1), minOf(maxOffSetX, newOffSetX))

                            val newOffSetY = offsetY + pan.y * zoom
                            val maxOffSetY = (paint.intrinsicSize.height / 2) * (zoom - 1)
                            offsetY = maxOf(maxOffSetY * (-1), minOf(maxOffSetY, newOffSetY))

                            showButton=false
                            expandedState=false
                        }
                    )


                }
                /*.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) {
                    showButton = !showButton
                }*/)

            if (showButton) {
                Icon(Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(30.dp)
                        .align(Alignment.TopStart)
                        .clickable {
                            navController.navigate(route = Screens.ImagesGrid_ShowGrid)
                        }
                )
                Column(modifier=Modifier.align(Alignment.BottomEnd)){
                    MultiActionButton()
                }
            }
        }
    }
    @Composable
    private fun MultiActionButton() {
        val transition = updateTransition(targetState = expandedState)
        val rotation: Float by transition.animateFloat { state ->
            if (state) 90f else 0f
        }
        val scale: Dp by transition.animateDp() { state ->
            if (state) 40.dp else 0.dp
        }
        FloatingActionButton(
            backgroundColor = Color.White,
            modifier = Modifier
                .padding(bottom=10.dp,start=25.dp)
                .size(scale),
            onClick = {
            }
        ) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = null
            )
        }
        FloatingActionButton(
            backgroundColor = Color.White,
            modifier = Modifier
                .padding(bottom=10.dp,start=25.dp)
                .size(scale),
            onClick = {
                navController.navigate(
                    route = Screens.ImageDetails_ShowDetail + "/${myURI}"
                )
            }
        ) {
            Icon(
                Icons.Filled.Info,
                contentDescription = null
            )
        }
        FloatingActionButton(
            backgroundColor = Color.White,
            modifier = Modifier
                .padding(10.dp),
            onClick = {
                expandedState = !expandedState
            }
        ) {
            Icon(
                Icons.Filled.List,
                contentDescription = null,
                modifier = Modifier
                    .rotate(rotation)
            )
        }
    }
}