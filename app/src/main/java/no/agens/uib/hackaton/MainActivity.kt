@file:OptIn(ExperimentalFoundationApi::class)

package no.agens.uib.hackaton

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
            //CharacterHelper.updateName("some name")

            lifecycleScope.launch {
                // sample of how to move character, moves the character in a circle three times
                repeat(3){
                    CharacterHelper.moveRight()
                    delay(1500)
                    CharacterHelper.moveDown()
                    delay(1500)
                    CharacterHelper.moveLeft()
                    delay(1500)
                    CharacterHelper.moveUp()
                    delay(1500)
                }

            }
            // do logic with loggedIn user
        }.addOnFailureListener {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show()
        }

    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UiBHackatonTheme {
        Text("hello UiB")
    }
}