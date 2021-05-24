package com.epse.gallery.screen

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import com.epse.gallery.ui.theme.GalleryTheme
import com.google.accompanist.coil.rememberCoilPainter

class ErrorScreen(private val act:ComponentActivity) {

    @ExperimentalFoundationApi
    @Composable
    fun RationaleUI(){
        GalleryTheme(){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberCoilPainter(request = R.mipmap.app_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(16.dp))

                Text(
                    text =act.getString(R.string.permission_read_external_storage_description),
                    color = MaterialTheme.colors.onBackground
                )

                Button(
                    modifier = Modifier.padding(30.dp),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
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
        GalleryTheme(){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = act.getString(R.string.permission_read_external_storage_not_granted),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }

}