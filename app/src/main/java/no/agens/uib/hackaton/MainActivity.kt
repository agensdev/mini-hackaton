@file:OptIn(ExperimentalFoundationApi::class)

package no.agens.uib.hackaton

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import no.agens.uib.hackaton.ui.theme.UiBHackatonTheme


@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // if you don't want to use Compose.
        //setContentView(R.layout.main_activty)

        FirebaseApp.initializeApp(this)
        val settings = firestoreSettings {
            isPersistenceEnabled = false
        }
        Firebase.firestore.firestoreSettings = settings
        FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener {
            CharacterHelper.setInitialValues()
            CharacterHelper.updateName("mangokongen")

            setContent {
                Controls()
            }

            /*

            Firebase.firestore.collection("beers").whereEqualTo("beer", 3).addSnapshotListener { value, error ->




                value?.documents?.forEach {
                    val x = it.id.split("x").first()
                    val y = it.id.split("x").last()
                    CharacterHelper.moveTo(x.toInt(),y.toInt())
                    Toast.makeText(this, "tried to move", Toast.LENGTH_SHORT).show()

                }
            }*/

            // do logic with loggedIn user
        }.addOnFailureListener {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show()
        }

    }


}


@Composable
fun Controls() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(Modifier) {
            val viking by CharacterHelper.vikingState.collectAsState(initial = null)

            Text(viking?.name?:"no op")
            if(viking?.direction?.drawableRes != null){
                GifImage(drawableRes = viking?.direction?.drawableRes!!)
            }

            Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                Button(onClick = { CharacterHelper.moveUp() }) {
                    Text("UP")
                }
            }
            Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                Button(onClick = { CharacterHelper.moveLeft() }) {
                    Text("LEFT")
                }

                Box(Modifier.width(42.dp))

                Button(onClick = { CharacterHelper.moveRight() }) {
                    Text("RIGHT")
                }
            }
            Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                Button(onClick = { CharacterHelper.moveDown() }) {
                    Text("DOWN")
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UiBHackatonTheme {
        Controls()
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes : Int
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = drawableRes).apply(block = {

            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth().height(24.dp),
    )

}