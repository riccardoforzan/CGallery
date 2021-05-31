package com.epse.gallery

import android.content.Context
import android.content.Intent
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
class FirstTimeActivity : ComponentActivity() {

    companion object{
        //Used to save a value on SharedPreferences that indicates if is the first launch of the app
        const val FIRST_TIME = "firstTime"
    }

    override fun onStart() {
        super.onStart()

        Log.d("DEBUG FTA","Started")

        //Saving in shared preferences that this screen has been displayed
        val sp = this.getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        with(sp.edit()) {
            putBoolean(FIRST_TIME, false)
            apply()
        }
        setContent{
            PresentationScreen()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
    }

    @Composable
    fun PresentationScreen(){
        GalleryTheme{
            Text(text = "DA SOSTITUIRE CON UNA O PIÙ SCHERMATE INTRODUTTIVE")
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
                    text = "QUESTA È UNA SCHERMATA DI INTRODUZIONE",
                    color = MaterialTheme.colors.onBackground
                )

                Button(
                    modifier = Modifier.padding(30.dp),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
                    onClick = {
                        setContent {
                            Log.d("DEBUG FTA","Button pressed")
                            val ctx = LocalContext.current
                            val int = Intent(ctx, PermissionActivity::class.java)
                            int.putExtra("firstTime",true)
                            ctx.startActivity(int)
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
}