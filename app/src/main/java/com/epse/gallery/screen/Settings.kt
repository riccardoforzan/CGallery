package com.epse.gallery.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.SemanticsProperties.Role
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.epse.gallery.R
import com.epse.gallery.SPUtils
import com.epse.gallery.ui.theme.GalleryTheme
import com.google.accompanist.coil.rememberCoilPainter

class Settings(private val ctx: Context, private val navController: NavHostController) {

    @Composable
    fun ShowSettings() {
        GalleryTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Settings") },
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    //ChangeGalleryName()
                    //ChangeImageSize()
                    ChangeDefaultOrder()
                }
            }
        }
    }

    @Composable
    fun ChangeDefaultOrder(){
        val radioOptions = listOf("ASC", "DESC")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

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
                                Log.d("DEBUG Checked",selectedOption)
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

    @Composable
    fun ChangeImageSize(){

        //Constants for the size of the pictures
        val default = 120F
        val maxSize = 240.0F
        val range = 1.00f..maxSize

        val sp = ctx.getSharedPreferences(SPUtils.preferences, Context.MODE_PRIVATE)
        val actualSize:Float = sp.getFloat(SPUtils.image_size_on_grid, default)
        var sliderPosition by remember { mutableStateOf(actualSize) }
        val size = sliderPosition

        Text(text="Move the slider to see a preview of how big will the image be")
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
                val sp = ctx.getSharedPreferences(SPUtils.preferences, Context.MODE_PRIVATE)
                with(sp.edit()) {
                    putFloat(SPUtils.image_size_on_grid, size)
                    apply()
                }
            }
        )
    }

    @Composable
    fun ChangeGalleryName() {
        Text(text = "Set your gallery name")

        //Set a placeholder with the value saved on shared preferences
        val sp = ctx.getSharedPreferences(SPUtils.preferences, Context.MODE_PRIVATE)
        val actual = sp.getString(SPUtils.gallery_title, "")!!

        var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(actual))
        }

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                //Update on shared preferences
                val sp = ctx.getSharedPreferences(SPUtils.preferences, Context.MODE_PRIVATE)
                with(sp.edit()) {
                    putString(SPUtils.gallery_title, text.text)
                    apply()
                }
            },
            label = { Text("Set your gallery name") },
        )

        Text("New gallery name: ${text.text}")
    }


}