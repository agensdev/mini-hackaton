package no.agens.uib.hackaton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeFirebase()

        // if you don't want to use Compose.
        //setContentView(R.layout.main_activty)

        setContent {
            val viewModel = remember { MainViewModel() }

            val state by viewModel.getState().collectAsState()

            Crossfade(targetState = state.isSignedIn) { isSignedIn ->
                if(isSignedIn) {
                    MainScreen()
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
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
    }

    private fun initializeFirebase() {
        FirebaseApp.initializeApp(this)
        val settings = firestoreSettings {
            isPersistenceEnabled = false
        }
        Firebase.firestore.firestoreSettings = settings
    }
}
