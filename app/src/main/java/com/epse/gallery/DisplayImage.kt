package com.epse.gallery

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
        var angle by remember { mutableStateOf(0f) }
        var zoom by remember { mutableStateOf(1f) }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
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
                            angle += gestureRotate
                            if (zoom * gestureZoom >= 1.0) zoom *= gestureZoom else zoom = 1.0F
                            offsetX += pan.x*zoom
                            offsetY += pan.y*zoom

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


    @Preview(showBackground = true)
    @Composable
    fun DisplayImagePreview() {
        MovingImage(painterResource(R.drawable.forest))
    }

}