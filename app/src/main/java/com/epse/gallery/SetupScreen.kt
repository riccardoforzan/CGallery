package com.epse.gallery

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.coil.rememberCoilPainter

/**
 * This composable UI is shown when the app is started but the permissions are not granted to it.
 * The purpose of this UI is asking for permissions respecting the policy of Android.
 * https://developer.android.com/training/permissions/requesting
 */

class SetupScreen() {

    /**
     * Take inspiration from
     * https://github.com/AppIntro/AppIntro
     */
    @Composable
    fun askForReadStorage(){

        Image(
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            painter = rememberCoilPainter(
                request = Uri.parse("content://media/external/images/media/31")
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.background,
                )
            ) {

            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                    text = "Descrizione dell'applicazione")

            TextButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                Text("Dai i permessi")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SetupScreenPreview() {
        askForReadStorage()
    }

}