package com.epse.gallery

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.epse.gallery.ui.theme.GalleryTheme
import com.google.accompanist.coil.rememberCoilPainter

@ExperimentalMaterialApi
@ExperimentalFoundationApi

/**
 * This UI is shown only the first time the application is opened, for this reason we decided to
 * make it as an Activity and keeping it outside the navigation graph of our application.
 * The purpose of this activity is to present what's implemented in the application and redirect
 * the user to the screen to manage permissions
 */
class FirstTimeActivity : ComponentActivity() {

    override fun onStart() {
        super.onStart()
        //Saving in shared preferences that this screen has been displayed
        val sp = this.getSharedPreferences(SPStrings.preferences, Context.MODE_PRIVATE)
        with(sp.edit()) {
            putBoolean(SPStrings.first_time, false)
            apply()
        }
        setContent{
            PresentationScreen()
        }
    }

    /**
     * Close the application on back pressed
     */
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
    }

    @Composable
    fun PresentationScreen(){
        GalleryTheme{
            Column(
                modifier = Modifier.padding(16.dp),
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
                Presentation()
                Spacer(Modifier.height(16.dp))
                StartButton()
            }

        }

    }

    @Composable
    fun Presentation() {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_description),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colors.onBackground
            )
        }
    }

    @Composable
    fun StartButton(){
        Button(
            modifier = Modifier.padding(30.dp),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            ),
            onClick = {
                setContent {
                    /**
                     * This piece of code is used to show the UI to manage permission on the
                     * first start of the application.
                     */
                    val ctx = LocalContext.current
                    val int = Intent(ctx, PermissionActivity::class.java)
                    int.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    int.putExtra(SPStrings.first_time, true)
                    ctx.startActivity(int)
                }
            }
        ) {
            Text(
                text = getString(R.string.start_using),
            )
        }
    }


}
