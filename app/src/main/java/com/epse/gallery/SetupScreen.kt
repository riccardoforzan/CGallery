package com.epse.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * This composable UI is shown when the app is started but the permissions are not granted to it.
 * The purpose of this UI is asking for permissions respecting the policy of Android.
 * https://developer.android.com/training/permissions/requesting
 */
class SetupScreen {

    @Composable
    fun askForReadStorage(){
        MaterialTheme {
            Column(
                modifier = Modifier.background(Color.Green)
            ) {
                Text(
                    text = "Hello theming",
                    color = MaterialTheme.colors.primary
                )
            }
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun SetupScreenPreview() {
        askForReadStorage()
    }

}