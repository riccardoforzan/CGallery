package com.epse.gallery.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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
    private val focal: Double
    private val iso: String?
    private val fAperture: Double
    private val fMaxAperture: Double
    private val expTime: Double
    private val model: String?
    private val length: Int
    private val width: Int
    private val gpslatitude: String?
    private val gpslongitude: String?
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
            val entry = attributesIterator.next()
            when (entry.key) {
                "name" -> imageName = entry.value
                "path" -> imagePath = entry.value
                "size" -> {
                    val sizeMB = round(entry.value.toDouble() / 2.0.pow(20.0) * 100) / 100
                    imageSize = sizeMB.toString()
                }
                "storage" -> imageStorage = entry.value
            }
        }


        val imageStream = ctx.contentResolver.openInputStream(imageURI)
        val imageEI = ExifInterface(imageStream!!)

        val exifDate = imageEI.getAttribute(ExifInterface.TAG_DATETIME)
        if(exifDate!= null) {
            date = SimpleDateFormat("yyyy:MM:dd hh:mm:ss", Locale.US).parse(exifDate)!!
            formattedDate = SimpleDateFormat("dd/MM/yyy hh:mm", Locale.US).format(date)
        }
        else{
            date = null
            formattedDate = null
        }

        val latlong = imageEI.getLatLong()
        if (latlong != null){
            gpslatitude = "${(round(latlong[0] * 1000) / 1000)} ${imageEI.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)}"
            gpslongitude = "${(round(latlong[1] * 1000) / 1000)} ${imageEI.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)}"
        }
        else {
            gpslatitude = null
            gpslongitude = null
        }

        length = imageEI.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1)
        width = imageEI.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1)
        focal = imageEI.getAttributeDouble(ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM, -1.0)
        iso = imageEI.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY)
        expTime = round(imageEI.getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME, -1.0) * 1000) / 1000
        fAperture =  imageEI.getAttributeDouble(ExifInterface.TAG_APERTURE_VALUE, -1.0)
        fMaxAperture =  imageEI.getAttributeDouble(ExifInterface.TAG_MAX_APERTURE_VALUE, -1.0)
        model = imageEI.getAttribute(ExifInterface.TAG_MODEL)
        xResolution  = imageEI.getAttributeDouble(ExifInterface.TAG_X_RESOLUTION, -1.0).toInt()
        yResolution  = imageEI.getAttributeDouble(ExifInterface.TAG_Y_RESOLUTION,-1.0).toInt()
        colorSpace  = imageEI.getAttribute(ExifInterface.TAG_COLOR_SPACE)
    }


    @ExperimentalFoundationApi
    @Composable
    fun ShowDetail(imageURI: Uri) {
        GalleryTheme {

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

                            IconButton(onClick = {
                                val sharingIntent = Intent(Intent.ACTION_SEND)
                                sharingIntent.type = "image/*"
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, imageURI)
                                val title = ctx.getString(R.string.share_with)
                                ctx.startActivities(
                                    arrayOf(
                                        Intent.createChooser(
                                            sharingIntent,
                                            title
                                        )
                                    )
                                )
                            }) {
                                Icon(
                                    Icons.Filled.Share,
                                    contentDescription = stringResource(id = R.string.share_with)
                                )
                            }
                        }
                    )
                }
            )
            {

                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {

                    Column(
                        modifier = Modifier.padding(5.dp)
                    ) {

                        Image(
                            painter = paint,
                            contentDescription = null,
                            modifier = Modifier
                                .height(200.dp)
                                .clip(shape = RoundedCornerShape(5.dp))
                                .align(Alignment.CenterHorizontally)
                                .clickable {
                                    //Clicking the image pops back
                                    navController.popBackStack()
                                },
                            contentScale = ContentScale.Crop,
                        )

                        Spacer(Modifier.height(10.dp))

                        Surface (
                            elevation = 5.dp,
                            modifier = Modifier.clip(
                                shape = RoundedCornerShape(
                                    topStart = 5.dp,
                                    topEnd = 5.dp
                                )
                            )
                        )
                        {
                            Column {
                                TabRow(
                                    selectedTabIndex = selectedTab,
                                    )
                                {
                                    Tab(
                                        text = { Text(stringResource(id = R.string.generalTab)) },
                                        selected = selectedTab == 0,
                                        onClick = { selectedTab = 0 }
                                    )
                                    Tab(
                                        text = { Text(stringResource(id = R.string.shootingTab)) },
                                        selected = selectedTab == 1,
                                        onClick = { selectedTab = 1 }
                                    )
                                    Tab(
                                        text = { Text(stringResource(id = R.string.otherTab)) },
                                        selected = selectedTab == 2,
                                        onClick = { selectedTab = 2 }
                                    )
                                }
                                ShowTabRowText(selectedTab)
                            }
                        }
                    }
                }

                else {


                    Row(
                        modifier = Modifier.padding(5.dp)
                    ) {

                        Image(
                            painter = paint,
                            contentDescription = null,
                            modifier = Modifier
                                .width(150.dp)
                                .clip(shape = RoundedCornerShape(5.dp))
                                .clickable {
                                    //Clicking the image pops back
                                    navController.popBackStack()
                                },

                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(10.dp))

                        Surface (
                            elevation = 5.dp,
                            modifier = Modifier.clip(
                                shape = RoundedCornerShape(
                                    topStart = 5.dp,
                                    topEnd = 5.dp
                                )
                            )
                        )
                        {
                            Column {
                                TabRow(
                                    selectedTabIndex = selectedTab,
                                )
                                {
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
    }


    @Composable
    fun ShowTabRowText(selected:Int) {

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(scrollState)
                .padding(10.dp)

        ) {

            if (selected== 0) {

                ShowDetailText(stringResource(R.string.imageDName), imageName)
                ShowDetailText(stringResource(R.string.imageDDate), formattedDate)
                ShowDetailText(stringResource(R.string.imageDSizeOnDisk), imageSize ,"MB")
                ShowDetailText(stringResource(R.string.imageDStorage), imageStorage)
                ShowDetailText(stringResource(R.string.imageDPath), imagePath)


                if (imageName==null && formattedDate==null && imagePath==null && imageStorage==null && imageSize ==null) {
                    Text(
                        text= stringResource(R.string.DataNotAvailable),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            if (selected == 1) {

                if(fAperture> 0.0)  ShowDetailText(stringResource(R.string.tagFAperture), fAperture.toString())
                if(fMaxAperture> 0.0)  ShowDetailText(stringResource(R.string.tagMaxFAperture), fMaxAperture.toString())
                if(focal> 0.0) ShowDetailText(stringResource(R.string.tagFocal), focal.toString(), "mm")
                ShowDetailText(stringResource(R.string.tagIso), iso)
                if(expTime> 0.0) ShowDetailText(stringResource(R.string.tagExpTime), expTime.toString(), "s")
                ShowDetailText(stringResource(R.string.tagModel), model)

                if (fAperture< 0 && fMaxAperture < 0 && focal< 0 && iso== null && expTime <0 && model==null) {
                    Text(
                        text= stringResource(R.string.DataNotAvailable),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            if (selected == 2) {

                ShowDetailText(stringResource(R.string.tagLongitude), gpslongitude)
                ShowDetailText(stringResource(R.string.tagLatitude), gpslatitude)
                if(length>0 && width>0 ) ShowDetailText(stringResource(R.string.imageSizes), "$width x $length")
                if(xResolution> 0) ShowDetailText(stringResource(R.string.tagYRes), "$xResolution dpi" )
                if(xResolution> 0) ShowDetailText(stringResource(R.string.tagXRes), "$yResolution dpi")

                if (gpslongitude == null && gpslatitude == null && width <=0  && length <=0 && xResolution <=0 && yResolution <=0) {
                    Text(
                        text= stringResource(R.string.DataNotAvailable),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
        }

    @Composable
    fun ShowDetailText(name: String, value: String?, other: String = ""){
        if(value !=null){
           Spacer(Modifier.height(10.dp))
            Row{
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

            Spacer(Modifier.height(5.dp))
            Divider(color= Color.Gray)
        }
    }
}
