package com.epse.gallery.screen

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.epse.gallery.MainActivity
import com.epse.gallery.R
import com.google.accompanist.coil.rememberCoilPainter

class PermissionScreen(val act:ComponentActivity) {

    @ExperimentalFoundationApi
    @Composable
    fun RationateUI(){
        MaterialTheme {
            val typography = MaterialTheme.typography
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    /**
                     * TODO: Insert application logo as request parameter
                     */
                    painter = rememberCoilPainter(request = null),
                    contentDescription = null,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(16.dp))

                Text(
                    text=act.getString(R.string.permission_read_external_storage_description),
                    style = typography.h6
                )

                Button(
                    modifier = Modifier.padding(30.dp),
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
                            act.requestPermissions(arrayOf(permission),MainActivity.storagePermissionCode)
                        }
                    }
                ){
                    Text(
                        text = act.getString(R.string.btn_manage_permissions),
                    )
                }

            }
        }

    }

    /**
     * Screen to show when read on external storage permission has not been granted
     */
    @Composable
    fun ReadStorageDenied(){
        MaterialTheme{
            val typography = MaterialTheme.typography
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text=act.getString(R.string.permission_read_external_storage_not_granted),
                    style = typography.h6
                )
            }
        }
    }

}