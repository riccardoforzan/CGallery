package com.epse.gallery

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

class DisplayImage{

    @Composable
    fun MovingImage(paint: Painter) {
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
                        Text("heigth:${paint.intrinsicSize.height}  width:${paint.intrinsicSize.width}")
                    }

                }

            }
        )

    }




    @Preview(showBackground = true)
    @Composable
    fun DisplayImagePreview() {
        MovingImage(painterResource(R.drawable.forest))
    }

}