package com.epse.gallery.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.epse.gallery.R
import com.epse.gallery.SPStrings
import com.epse.gallery.StorageUtils
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * This UI show in full screen the image selected with the possibility of deleting the image.
 * Scroll left or right to show next or previous image.
 * Double tap or pinch-to-zoom to perform a zoom action.
 * @param ctx context of the calling activity
 * @param navController navController registered for the application
 */
@ExperimentalFoundationApi
class FullImage(private val ctx: Context, private val navController: NavHostController) {
    private var backgroundColor = Color.Black
    private var lastChange = System.currentTimeMillis()
    private var showButton by mutableStateOf(false)
    private var expandedState by mutableStateOf(false)
    private var zoom by mutableStateOf(1f)
    private var offsetX by mutableStateOf(0f)
    private var offsetY by mutableStateOf(0f)
    private var images = StorageUtils.getImageURIs()
    private lateinit var defaultImage: String

    /**
     * After receiving an uri, this function starts building the image referenced.
     * @param imageURI uri of the image in full screen
     */
    @ExperimentalMaterialApi
    @Composable
    fun ShowFullImage(imageURI: Uri) {
        defaultImage = imageURI.toString()
        BackDrop()
    }

    /**
     * Build a backdrop scaffold.
     * The back layer is built with the image in full screen.
     * The front layer contains delete confirmation button.
     */
    @ExperimentalMaterialApi
    @Composable
    @SuppressLint("RestrictedApi")
    private fun BackDrop() {
        val coroutineScope = rememberCoroutineScope()
        val configuration = LocalConfiguration.current
        val height:Dp = if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
            (configuration.screenHeightDp - configuration.screenHeightDp/5).dp
        else
            (configuration.screenHeightDp - configuration.screenHeightDp/3).dp
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
                                            coroutineScope.launch {
                                                val uri = Uri.parse(defaultImage)
                                                /**
                                                 * The way Android 10 handles deletion is a bit
                                                 * tricky: the image is deleted after two calls.
                                                 * This parameter on shared preferences is used to
                                                 * address this specific behaviour of API 29
                                                 */
                                                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                                                    val name = SPStrings.preferences
                                                    val sp = ctx.getSharedPreferences(
                                                        name,
                                                        Context.MODE_PRIVATE
                                                    )
                                                    val spKey = SPStrings.API29_delete
                                                    with(sp.edit()) {
                                                        putString(spKey, uri.toString())
                                                        apply()
                                                    }
                                                }
                                                StorageUtils.delete(ctx, uri)
                                                navController.backStack.clear()
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


    /**
     * Show the image in full screen mode.
     * In addition performs every type of gestures usable (like double tap, pinch-to-zoom, scrolling..)
     * @param backDropState the state of the back drop scaffold: used to show or hide deleting option
     */
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
                    //on double tap apply or reset zoom
                    onDoubleTap = {
                        if (zoom == 1f)
                            zoom = 2.5f
                        else {
                            zoom = 1f
                            offsetX = 0f
                            offsetY = 0f
                        }
                    },
                    //on single tap show or hide multi action button
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
                    //detect gesture like scrolling or pinch-to-zoom
                    detectTransformGestures(
                        onGesture = { _, pan, gestureZoom, _ ->
                            if (backDropState.isRevealed) {
                                val newZoom = zoom * gestureZoom
                                zoom = maxOf(1.0F, minOf(3.0F, newZoom))
                                val currentTime = System.currentTimeMillis()
                                if (zoom == 1f) {
                                    // if the image isn't zoomed , the scroll is good perform a recomposition with the previous or the next image
                                    // currentTime and lastChange are necessary to filter multiple scrolling action
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
                                }
                                // this branch works when the image is zoomed and the user perform a panning gesture.
                                else {
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
                            navController.popBackStack()
                        }
                )
                Column(modifier = Modifier.align(Alignment.BottomEnd)) {
                    MultiActionButton(backDropState)
                }
            }
        }
    }

    /**
     * Build a multi action button.
     * First button (list button) trigger the other three with an animation: delete button, share button and info button.
     * @param backDropState the state of the back drop scaffold: used to show or hide deleting option
     */
    @Composable
    @ExperimentalMaterialApi
    private fun MultiActionButton(backDropState: BackdropScaffoldState) {
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
                if(!StorageUtils.hasWriteStoragePermission(ctx) && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                    //Launch a toast with error message
                    errorMessage()
                } else {
                    //Start deleting
                    coroutineScope.launch {
                        backDropState.conceal()
                        expandedState = false
                        showButton = false
                    }
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
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "image/*"
                sharingIntent.putExtra(Intent.EXTRA_STREAM, defaultImage)
                val title = ctx.getString(R.string.share_with)
                ctx.startActivities(
                    arrayOf(
                        Intent.createChooser(
                            sharingIntent,
                            title
                        )
                    )
                )
            }
        ) {
            Icon(
                Icons.Filled.Share,
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
                    route = Screens.ImageDetails_ShowDetail + "/${defaultImage}"
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

    /**
     * Perform a toast error message for absence of write permission
     */
    private fun errorMessage(){
        Toast.makeText(
            ctx,
            ctx.getString(R.string.permission_write_external_storage_not_granted),
            Toast.LENGTH_SHORT
        ).show()
    }

}