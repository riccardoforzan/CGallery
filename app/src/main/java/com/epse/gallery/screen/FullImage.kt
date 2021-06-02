package com.epse.gallery.screen

import android.content.Context
import android.net.Uri
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.epse.gallery.MainActivity
import com.epse.gallery.R
import com.epse.gallery.StorageUtils
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.launch
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import kotlin.math.roundToInt

@ExperimentalFoundationApi
class FullImage(private val ctx: Context, private val navController: NavHostController) {
    /*
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
                                offsetX = maxOf(maxOffSetX *(-1), minOf(maxOffSetX, newOffSetX))
                                if(zoom==1f){
                                    val images=StorageUtils.getImageURIs()
                                    if(pan.x>=50){
                                        var index=images.indexOf(myURI)-1
                                        if(index<=-1)
                                            index=0
                                        navController.navigate(
                                            route = Screens.FullImage_ShowFullImage + "/${StorageUtils.getImageURIs()[index]}"
                                        )
                                    }
                                    if(pan.x<=-50){
                                        var index=images.indexOf(myURI)+1
                                        if(index>=images.size)
                                            index=images.size-1
                                        navController.navigate(
                                            route = Screens.FullImage_ShowFullImage + "/${StorageUtils.getImageURIs()[index]}"
                                        )
                                    }

                                }

                                val newOffSetY = offsetY + pan.y * zoom
                                val maxOffSetY = (paint.intrinsicSize.height / 2) * (zoom - 1)
                                offsetY = maxOf(maxOffSetY *(-1), minOf(maxOffSetY, newOffSetY))

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
                    .padding(bottom = 10.dp, start = 25.dp)
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
                    .padding(bottom = 10.dp, start = 25.dp)
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
        }*/

    private var backgroundColor = Color.Black
    private var lastChange = System.currentTimeMillis()
    private var showButton by mutableStateOf(false)
    private var expandedState by mutableStateOf(false)
    private var zoom by mutableStateOf(1f)
    private var offsetX by mutableStateOf(0f)
    private var offsetY by mutableStateOf(0f)
    private var images = StorageUtils.getImageURIs()
    private lateinit var defaultImage: String

    @ExperimentalMaterialApi
    @Composable
    fun ShowFullImage(imageURI: Uri) {
        defaultImage = imageURI.toString()
        //BuildFullImage()
        BackDrop()
    }

    @ExperimentalMaterialApi
    @Composable
    private fun BackDrop() {
        val coroutineScope = rememberCoroutineScope()
        val height:Dp
        if(MainActivity.isPortrait)
            height=550.dp
        else
            height=250.dp
        val backDropState = rememberBackdropScaffoldState(BackdropValue.Revealed)
        BackdropScaffold(
            scaffoldState = backDropState,
            appBar = {},
            backLayerContent = { BuildFullImage(backDropState) },
            frontLayerContent = {
                                Column(modifier=Modifier.fillMaxSize()){
                                    Text(
                                        text= stringResource(id = R.string.delete_confirmation),
                                        modifier=Modifier.padding(10.dp),
                                        color=Color.Gray,
                                        fontStyle=FontStyle.Italic
                                    )
                                    Row(modifier= Modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {
                                            coroutineScope.launch{
                                                val uri=Uri.parse(defaultImage)
                                                MainActivity.deletedImageUri=uri
                                                StorageUtils.delete(ctx, uri)
                                                navController.navigate(route = Screens.ImagesGrid_ShowGrid)
                                            }
                                        }
                                    ){
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = stringResource(id = R.string.delete)
                                        )
                                        Text(text=stringResource(id = R.string.delete),
                                            modifier=Modifier.padding(start=15.dp))
                                    }
                                }
            },
            peekHeight = height,
            headerHeight = 0.dp
        )
    }


    @ExperimentalMaterialApi
    @Composable
    private fun BuildFullImage(backDropState: BackdropScaffoldState) {
        val coroutineScope = rememberCoroutineScope()
        var myURI by rememberSaveable { mutableStateOf(defaultImage) }
        defaultImage=myURI
        var uri = Uri.parse(myURI)
        val paint = rememberCoilPainter(uri)
        Box(modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (zoom == 1f)
                            zoom = 2.5f
                        else {
                            zoom = 1f
                            offsetX = 0f
                            offsetY = 0f
                        }
                    },
                    onTap = {
                        showButton = !showButton
                        expandedState = false
                        coroutineScope.launch {
                            backDropState.reveal()
                        }
                    }
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
                            if (backDropState.isRevealed) {
                                val newZoom = zoom * gestureZoom
                                zoom = maxOf(1.0F, minOf(3.0F, newZoom))
                                val currentTime = System.currentTimeMillis()
                                if (zoom == 1f) {
                                    if (pan.x >= 50 && currentTime - lastChange > 200) {
                                        uri = Uri.parse(myURI)
                                        val index = images.indexOf(uri)
                                        var newIndex = index - 1
                                        if (newIndex <= -1)
                                            newIndex = 0
                                        lastChange = System.currentTimeMillis()
                                        myURI = images[newIndex].toString()
                                    }
                                    if (pan.x <= -50 && currentTime - lastChange > 200) {
                                        uri = Uri.parse(myURI)
                                        val index = images.indexOf(uri)
                                        var newIndex = index + 1
                                        if (newIndex >= images.size)
                                            newIndex = images.size - 1
                                        lastChange = System.currentTimeMillis()
                                        myURI = images[newIndex].toString()
                                    }
                                } else {
                                    val newOffSetX = offsetX + pan.x * zoom
                                    val maxOffSetX = (paint.intrinsicSize.width / 2) * (zoom - 1)
                                    offsetX =
                                        maxOf(maxOffSetX * (-1), minOf(maxOffSetX, newOffSetX))

                                    val newOffSetY = offsetY + pan.y * zoom
                                    val maxOffSetY = (paint.intrinsicSize.height / 2) * (zoom - 1)
                                    offsetY =
                                        maxOf(maxOffSetY * (-1), minOf(maxOffSetY, newOffSetY))
                                }
                                showButton = false
                                expandedState = false

                            }
                        }
                    )
                })

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
                Column(modifier = Modifier.align(Alignment.BottomEnd)) {
                    MultiActionButton(Uri.parse(myURI), backDropState)
                }
            }
        }
    }

    @Composable
    @ExperimentalMaterialApi
    private fun MultiActionButton(myURI: Uri, backDropState: BackdropScaffoldState) {
        val coroutineScope = rememberCoroutineScope()
        val transition = updateTransition(targetState = expandedState)
        val rotation: Float by transition.animateFloat { state ->
            if (state) 90f else 0f
        }
        val scale: Dp by transition.animateDp { state ->
            if (state) 40.dp else 0.dp
        }
        FloatingActionButton(
            backgroundColor = Color.White,
            modifier = Modifier
                .padding(bottom = 10.dp, start = 25.dp)
                .size(scale),
            onClick = {
                coroutineScope.launch {
                    backDropState.conceal()
                    expandedState = false
                    showButton = false
                }
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
                .padding(bottom = 10.dp, start = 25.dp)
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