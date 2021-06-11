package com.epse.gallery.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.epse.gallery.R
import com.epse.gallery.SPStrings
import com.epse.gallery.StorageUtils
import com.epse.gallery.ui.theme.GalleryTheme
import com.google.accompanist.coil.rememberCoilPainter

class Settings(private val ctx: Context, private val navController: NavHostController) {

    @Composable
    fun ShowSettings() {
        GalleryTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.settings )) },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = null)
                            }
                        },
                    )
                }
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    ChangeGalleryName()
                    Divider()
                    ChangeImageSize()
                    Divider()
                    ChangeDefaultOrder()
                }
            }
        }
    }

    @Composable
    fun ChangeDefaultOrder(){

        Text(text= stringResource(id = R.string.default_image_order))

        /**
         * The order of those two options must be DESCENDING, ASCENDING  because in the function
         * saveToSharedPreferences 0 is mapped to DESC and 1 to ASC and those values are used in
         * the query
         */
        val radioOptions = listOf(
            stringResource(id = R.string.date_descending),
            stringResource(id = R.string.date_ascending)
        )

        val sp = ctx.getSharedPreferences(SPStrings.preferences, Context.MODE_PRIVATE)
        val saved = sp.getString(SPStrings.default_order,"DESC")!!
        val default = if(saved == "DESC") 0 else 1

        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[default]) }

        Column(Modifier.selectableGroup()) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                //Update shared preferences
                                updateSPOrder(radioOptions.indexOf(text))
                            },
                            role = androidx.compose.ui.semantics.Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }

    private fun updateSPOrder(option:Int){
        val sp = ctx.getSharedPreferences(SPStrings.preferences, Context.MODE_PRIVATE)
        val order = if(option==0) "DESC" else "ASC"
        with(sp.edit()) {
            putString(SPStrings.default_order, order)
            apply()
        }
        StorageUtils.setQueryOrder(order = order,ctx)
    }

    @Composable
    fun ChangeImageSize(){

        //Screen attributes
        val width = LocalConfiguration.current.screenWidthDp
        val height = LocalConfiguration.current.screenHeightDp
        val maxSize = (kotlin.math.min(width, height)).toFloat()

        //Size for the pictures
        val minSize:Float = ((kotlin.math.min(width,height))/10).toFloat()
        val default:Float = maxSize/3
        val range = minSize..maxSize

        val sp = ctx.getSharedPreferences(SPStrings.preferences, Context.MODE_PRIVATE)
        val actualSize:Float = sp.getFloat(SPStrings.image_size_on_grid, default)
        var sliderPosition by remember { mutableStateOf(actualSize) }
        val size = sliderPosition

        Text(text= stringResource(id = R.string.move_slider))
        Box(
            modifier = Modifier.size(size.dp)
        ) {
            Image(
                painter = rememberCoilPainter(request = R.mipmap.app_icon),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        Slider(
            value = sliderPosition,
            valueRange = range,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = {
                //Update value on shared preferences
                val sp = ctx.getSharedPreferences(SPStrings.preferences, Context.MODE_PRIVATE)
                with(sp.edit()) {
                    putFloat(SPStrings.image_size_on_grid, size)
                    apply()
                }
            }
        )
    }

    @Composable
    fun ChangeGalleryName() {
        Text(text = stringResource(id = R.string.set_gallery_name))

        //Set a placeholder with the value saved on shared preferences
        val sp = ctx.getSharedPreferences(SPStrings.preferences, Context.MODE_PRIVATE)
        val actual = sp.getString(SPStrings.gallery_title, "")!!

        var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(actual))
        }

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                //Update on shared preferences
                val sp = ctx.getSharedPreferences(SPStrings.preferences, Context.MODE_PRIVATE)
                with(sp.edit()) {
                    putString(SPStrings.gallery_title, text.text)
                    apply()
                }
            },
            label = { Text(text = stringResource(id = R.string.set_gallery_name))},
        )

        Text(stringResource(id = R.string.new_gallery_name) + ": " + text.text)
    }


}