package com.epse.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.epse.gallery.ui.theme.GalleryTheme
import com.google.accompanist.coil.rememberCoilPainter

@ExperimentalMaterialApi
@ExperimentalFoundationApi
class PermissionActivity : ComponentActivity() {

    companion object{
        private const val storagePermissionCode = 1
    }

    override fun onResume(){
        super.onResume()

        //If permission are granted while the app was not stopped
        val actualPermissions = StorageUtils.hasReadStoragePermission(this)
        if(actualPermissions){
            //Launch the MainActivity
            val ctx = this
            val int = Intent(ctx, MainActivity::class.java)
            int.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            ctx.startActivity(int)
        } else {

            /**
             * This code block is executed if the permission has been denied.
             * If the version of Android is > 6.0 then check if the application should show an UI
             * to ask for permissions.
             * If the version od Android is < 6.0 then permission must have been granted during
             * installation.
             * Using an extra in the intent allow us to show the RationaleUI the first time the user
             * starts the application. This is needed because shouldShowRequestPermissionRationale returns
             * false at the first start of the application.
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permission = Manifest.permission.READ_EXTERNAL_STORAGE
                val showRequest = this.shouldShowRequestPermissionRationale(permission)
                //Check if is the first time the application is launched
                val firstTime = intent.getBooleanExtra(SPStrings.first_time, false)
                if (showRequest || firstTime) {
                    setContent { RationaleUI() }
                } else {
                    setContent { ReadStorageDenied() }
                }
            } else {
                setContent { ReadStorageDenied() }
            }
        }
    }

    /**
     * Terminate the application on back pressed
     */
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
    }

    /**
     * UI where it's explained what permissions are needed and for what purpose.
     */
    @Composable
    fun RationaleUI(){
        GalleryTheme{
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
                    text = getString(R.string.permission_external_storage_description),
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
                            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            requestPermissions(
                                permission,
                                storagePermissionCode
                            )
                        }
                    }
                ){
                    Text(
                        text = getString(R.string.btn_got_it),
                    )
                }

            }
        }

    }

    /**
     * Managed permission following guidelines.
     * https://developer.android.com/training/permissions/requesting
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            setContent{
                val ctx = LocalContext.current
                val int = Intent(ctx, MainActivity::class.java)
                int.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                ctx.startActivity(int)
            }
        } else {
            setContent{
                ReadStorageDenied()
            }
        }
    }

    /**
     * Screen to show when read on external storage permission has not been granted.
     */
    @Composable
    fun ReadStorageDenied(){
        GalleryTheme{
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = getString(R.string.permission_read_external_storage_not_granted),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }

}