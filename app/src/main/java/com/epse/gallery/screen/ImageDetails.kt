package com.epse.gallery.screen

import android.content.Context
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.epse.gallery.MainActivity.Companion.isPortrait
import com.epse.gallery.StorageUtils
import com.epse.gallery.ui.theme.GalleryTheme
import com.google.accompanist.coil.rememberCoilPainter
import kotlin.math.pow
import kotlin.math.round


class ImageDetails(private val ctx: Context, private val navController: NavHostController,imageURI: Uri) {

    private var imageName: String?
    private var imagePath: String?
    private var imageStorage: String?
    private var imageSize: String?

    private val length: String?
    private val width: String?
    private val date: String?
    private val focal: String?
    private val iso: String?
    private val expTime: Double?
    private val exp: String?
    private val model: String?
    private val GPSlatitude: String?
    private val GPSlongitude: String?


    init {
        val fileAttributes: Map<String, String> = StorageUtils.getFileData(ctx, imageURI)
        val attributesIterator = fileAttributes.iterator()
        imageName = null
        imagePath = null
        imageStorage = null
        imageSize = null
        while (attributesIterator.hasNext()) {
            val E = attributesIterator.next()
            when (E.key) {
                "name" -> imageName = E.value
                "path" -> imagePath = E.value
                "size" -> {
                    val sizeMB = round(E.value.toDouble() / 2.0.pow(20.0) * 100) / 100
                    imageSize = sizeMB.toString()
                }
                "storage" -> imageStorage = E.value
            }
        }


        val imageStream = ctx.contentResolver.openInputStream(imageURI)
        val imageEI = ExifInterface(imageStream!!)

        length = imageEI.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
        width = imageEI.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
        date = imageEI.getAttribute(ExifInterface.TAG_DATETIME)
        focal = imageEI.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
        iso = imageEI.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)
        expTime =
            round(imageEI.getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME, 0.0) * 1000) / 1000
        exp = imageEI.getAttribute(ExifInterface.TAG_EXPOSURE_INDEX)
        model = imageEI.getAttribute(ExifInterface.TAG_MODEL)
        GPSlatitude = imageEI.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
        GPSlongitude = imageEI.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)

    }


    @ExperimentalFoundationApi
    @Composable
    fun ShowDetail(imageURI: Uri) {
        GalleryTheme() {

            var addedToFavorite by remember { mutableStateOf(false) }
            val paint = rememberCoilPainter(imageURI)

            var selectedTab by rememberSaveable { mutableStateOf(0) }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(imageName.toString()) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = null)
                            }
                        },

                        actions = {
                            //IconButton that does nothing
                            IconButton(onClick = { addedToFavorite = !addedToFavorite }) {
                                if (addedToFavorite) Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = null
                                )
                                else Icon(
                                    Icons.Filled.FavoriteBorder,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            )
            {

                if (isPortrait) {

                    Column(
                        modifier = Modifier
                            //.verticalScroll(scrollState)
                            .padding(5.dp)
                    ) {

                        Image(
                            painter = paint,
                            contentDescription = null,
                            modifier = Modifier
                                .height(200.dp)
                                .clip(shape = RoundedCornerShape(5.dp))
                                .align(Alignment.CenterHorizontally),
                            //.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.height(10.dp))

                        TabRow(selectedTabIndex = selectedTab) {
                            Tab(
                                text = { Text("General") },
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 }
                            )
                            Tab(
                                text = { Text("Shooting") },
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 }
                            )
                            Tab(
                                text = { Text("Other") },
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 }
                            )
                        }
                        ShowTabRowText(selectedTab)
                    }
                }

                else {

                    Row(
                        modifier = Modifier
                            //.verticalScroll(scrollState)
                            .padding(5.dp)
                    ) {

                        Image(
                            painter = paint,
                            contentDescription = null,
                            modifier = Modifier
                                .width(150.dp)
                                .clip(shape = RoundedCornerShape(5.dp)),
                            //.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(20.dp))

                        Column() {
                            TabRow(selectedTabIndex = selectedTab) {
                                Tab(
                                    text = { Text("General") },
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 }
                                )
                                Tab(
                                    text = { Text("Shooting") },
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 }
                                )
                                Tab(
                                    text = { Text("Other") },
                                    selected = selectedTab == 2,
                                    onClick = { selectedTab = 2 }
                                )
                            }
                            ShowTabRowText(selectedTab)

                        }
                    }
                }
            }
        }
    }


    @Composable
    fun ShowTabRowText(selected:Int) {


            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                //.padding(20.dp)
            ) {

                if (selected== 0) {

                    Text(text = "Name= $imageName", Modifier.height(50.dp))
                    Text(text = "Path: $imagePath", Modifier.height(50.dp))
                    Text(text = "Date= $date", Modifier.height(50.dp))
                    Text(text = "Size= $imageSize MB", Modifier.height(50.dp))
                    Text(text = "Storage= $imageStorage", Modifier.height(50.dp))
                    Text(text = "Stuff to scroll", Modifier.height(50.dp))
                    Text(text = "Stuff to scroll", Modifier.height(50.dp))
                    Text(text = "Stuff to scroll", Modifier.height(50.dp))
                    Text(text = "Stuff to scroll", Modifier.height(50.dp))
                }
                if (selected == 1) {

                    if (focal != null) Text(
                        text = "Focal Length = $focal",
                        Modifier.height(50.dp)
                    )
                    if (iso != null) Text(
                        text = "ISO = $iso",
                        Modifier.height(50.dp)
                    )
                    if (expTime != 0.0) Text(
                        text = "exposure time = $expTime",
                        Modifier.height(50.dp)
                    )
                    if (exp != null) Text(
                        text = "exposure index = $exp",
                        Modifier.height(50.dp)
                    )
                    if (model != null) Text(
                        text = "Camera model= $model",
                        Modifier.height(50.dp)
                    )
                    if (focal == null && iso == null && expTime == 0.0 && exp == null && model == null) Text(
                        text = "Shooting data not available",
                        Modifier.height(50.dp)
                    )
                }
                if (selected == 2) {
                    if (GPSlongitude != null) Text(
                        text = "longitude= $GPSlongitude",
                        Modifier.height(50.dp)
                    )
                    if (GPSlatitude != null) Text(
                        text = "latitude= $GPSlatitude",
                        Modifier.height(50.dp)
                    )
                    if (length != null) Text(
                        text = "length= $length",
                        Modifier.height(50.dp)
                    )
                    if (width != null) Text(
                        text = "width= $width",
                        Modifier.height(50.dp)
                    )
                }
            }
    }
}



/*
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DisplayImagePreview() {
    ShowDetail(Uri.parse("content://media/external/images/media/31"))
}*/


