package com.epse.gallery.screen

import android.content.Context
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

class Settings(private val ctx: Context, private val navController: NavHostController) {

    @Composable
    fun ShowSettings() {
        Text("Settings")
    }

}