package com.epse.gallery.screen

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.google.accompanist.coil.rememberCoilPainter
import kotlin.math.roundToInt

@ExperimentalFoundationApi
class DisplayImage(private val ctx: Context, private val navController: NavHostController){

    @Composable
    fun MovingImage(imageURI:Uri) {

        val paint = rememberCoilPainter(imageURI)

        var allowedRotation by remember { mutableStateOf(false) }
        var angle by remember { mutableStateOf(0f) }
        var zoom by remember { mutableStateOf(1f) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Scaffold(
            bottomBar = {
                BottomAppBar(
                    backgroundColor = Color.Transparent,
                    //contentColor = Color.Transparent

                )
                {
                    TextButton(
                        onClick = {
                            allowedRotation = !allowedRotation
                            angle = 0F
                        },
                        colors = ButtonDefaults.textButtonColors(backgroundColor= Color.Blue, contentColor= Color.White)
                    ) {
                        if (allowedRotation) Text("disable rotation")
                        else Text("enable rotation")
                    }

                    TextButton(
                        onClick = {
                            navController.navigate(
                                route = Screens.ImageDetails_ShowDetail+"/${imageURI}")
                        },
                        colors = ButtonDefaults.textButtonColors(backgroundColor= Color.Blue, contentColor= Color.White)
                    ) { Text("Details") }



                }
            },
            content= {

                Box(
                    //modifier = Modifier.fillMaxSize()


                )
                {

                    Image(
                        painter = paint,
                        contentDescription = null,
                        modifier = Modifier
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .graphicsLayer(
                                scaleX = zoom,
                                scaleY = zoom,
                                rotationZ = angle
                            )
                            .pointerInput(Unit) {
                                detectTransformGestures(
                                    onGesture = { _, pan, gestureZoom, gestureRotate ->
                                        if (allowedRotation) angle += gestureRotate
                                        val newZoom = zoom * gestureZoom
                                        zoom = maxOf(1.0F, minOf(3.0F, newZoom))

                                        val newOffSetX = offsetX + pan.x * zoom
                                        val maxOffSetX = (paint.intrinsicSize.width / 2) * (zoom - 1)
                                        offsetX = maxOf(maxOffSetX * (-1), minOf(maxOffSetX, newOffSetX))

                                        val newOffSetY = offsetY + pan.y * zoom
                                        val maxOffSetY = (paint.intrinsicSize.height / 2) * (zoom - 1)
                                        offsetY = maxOf(maxOffSetY * (-1), minOf(maxOffSetY, newOffSetY))

                                    }
                                )
                            }
                            .fillMaxSize()

                    )
                    // Text aggiunti solo per debug
                    Column() {
                        Text("Zoom:$zoom" + " x:$offsetX" + " y:$offsetY")
                        /**
                         * Throws an exception
                         */
                        //Text("heigth:${paint.intrinsicSize.height}  width:${paint.intrinsicSize.width}")
                    }

                }

            }
        )

    }

/*
    @Preview(showBackground = true)
    @Composable
    fun DisplayImagePreview() {
        MovingImage(Uri.parse("content://media/external/images/media/31"), )
    }
*/
}