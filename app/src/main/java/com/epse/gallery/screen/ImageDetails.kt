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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.epse.gallery.MainActivity.Companion.isPortrait
import com.epse.gallery.R
import com.epse.gallery.StorageUtils
import com.epse.gallery.ui.theme.GalleryTheme
import com.google.accompanist.coil.rememberCoilPainter
import java.text.SimpleDateFormat
import java.util.*


import kotlin.math.pow
import kotlin.math.round


@ExperimentalMaterialApi
class ImageDetails(private val ctx: Context,
                   private val navController: NavHostController, imageURI: Uri) {

    private var imageName: String?
    private var imagePath: String?
    private var imageStorage: String?
    private var imageSize: String?


    private val date: Date?
    private val formattedDate : String?

    private val focal: String?
    private val iso: String?
    //private val flashFired: Int?

    private val fAperture: Double
    private val fMaxAperture: Double
    private val expTime: Double
    private val model: String?

    private val length: String?
    private val width: String?
    private val GPSlatitude: String?
    private val GPSlongitude: String?
    private val xResolution: Int
    private val yResolution: Int
    private val colorSpace: String?



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
        val exifDate = imageEI.getAttribute(ExifInterface.TAG_DATETIME)


        if(exifDate!= null) {
            date = SimpleDateFormat("yyyy:MM:dd hh:mm:ss", Locale.US).parse(exifDate)
            formattedDate = SimpleDateFormat("dd/MM/yyy hh:mm", Locale.US).format(date)
        }
        else{
            date = null
            formattedDate = null
        }

        focal = imageEI.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
        iso = imageEI.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)
        expTime = round(imageEI.getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME, -1.0) * 1000) / 1000
        fAperture =  imageEI.getAttributeDouble(ExifInterface.TAG_APERTURE_VALUE, -1.0)
        fMaxAperture =  imageEI.getAttributeDouble(ExifInterface.TAG_MAX_APERTURE_VALUE, -1.0)
        model = imageEI.getAttribute(ExifInterface.TAG_MODEL)
        GPSlatitude = imageEI.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
        GPSlongitude = imageEI.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
        xResolution  = imageEI.getAttributeDouble(ExifInterface.TAG_X_RESOLUTION, -1.0).toInt()
        yResolution  = imageEI.getAttributeDouble(ExifInterface.TAG_Y_RESOLUTION,-1.0).toInt()
        colorSpace  = imageEI.getAttribute(ExifInterface.TAG_COLOR_SPACE)

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
                                    text = { Text(stringResource(R.string.generalTab)) },
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 }
                                )
                                Tab(
                                    text = { Text(stringResource(R.string.shootingTab)) },
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 }
                                )
                                Tab(
                                    text = { Text(stringResource(R.string.otherTab)) },
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
                .padding(10.dp)
        ) {

            if (selected== 0) {

                //Text("test data : $date")
                ShowDetailText(stringResource(R.string.imageDName), imageName)
                ShowDetailText(stringResource(R.string.imageDDate), formattedDate)
                ShowDetailText(stringResource(R.string.imageDPath), imagePath)
                ShowDetailText(stringResource(R.string.imageDStorage), imageStorage)
                ShowDetailText(stringResource(R.string.imageDSizeOnDisk), imageSize ,"MB")

            }
            if (selected == 1) {

                if(fAperture> 0.0)  ShowDetailText(stringResource(R.string.tagFAperture), fAperture.toString())
                if(fMaxAperture> 0.0)  ShowDetailText(stringResource(R.string.tagMaxFAperture), fMaxAperture.toString())
                ShowDetailText(stringResource(R.string.tagFocal), focal)
                ShowDetailText(stringResource(R.string.tagIso), iso)
               if(expTime> 0.0) ShowDetailText(stringResource(R.string.tagExpTime), expTime.toString())
                ShowDetailText(stringResource(R.string.tagModel), model)


                if (focal == null && iso == null && expTime == 0.0  && model == null) {
                    Text(
                        text= "${stringResource(R.string.shootingTab)} ${stringResource(R.string.DataNotAvailable)}",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }

            if (selected == 2) {

                ShowDetailText(stringResource(R.string.tagLongitude), GPSlongitude)
                ShowDetailText(stringResource(R.string.tagLatitude), GPSlatitude)
                ShowDetailText(stringResource(R.string.imageSizes), "$width x $length")
                if(xResolution> 0) ShowDetailText(stringResource(R.string.tagYRes), "$xResolution dpi" )
                if(xResolution> 0) ShowDetailText(stringResource(R.string.tagXRes), "$yResolution dpi")

                if (GPSlongitude == null && GPSlatitude == null && width == null  && length == null) {
                    Text(
                        text= "${stringResource(R.string.otherTab)} ${stringResource(R.string.DataNotAvailable)}",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        }
        }

    @Composable
    fun ShowDetailText(name: String, value: String?, other: String = ""  ){
        if(value !=null){

            Row(){
                Text(
                    text = "$name :",
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .weight(2f)
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    text = "$value $other",
                    modifier = Modifier
                        .wrapContentWidth(Alignment.End)
                        .weight(3f)

                )
            }
            Spacer(Modifier.height(15.dp))
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


