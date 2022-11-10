package no.agens.uib.hackaton

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainScreenState(
    val isSignedIn: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val state = MutableStateFlow(MainScreenState())

    fun getState(): StateFlow<MainScreenState> = state.asStateFlow()

    init {
        FirebaseAuth
            .getInstance()
            .signInAnonymously()
            .addOnSuccessListener {
                state.update {
                    it.copy(isSignedIn = true)
                }
                CharacterHelper.setInitialValues()
            }.addOnFailureListener {
                Log.e("uibhackaton", "Failed to sign in anonymously.", it)
            }
    }


}