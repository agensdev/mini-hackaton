package no.agens.uib.hackaton

import android.util.Log
import androidx.annotation.DrawableRes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

sealed class VikingDirection(
    @DrawableRes val drawableRes : Int,) {
    object RedRight : VikingDirection(drawableRes = R.drawable.red_viking_right)
}

data class VikingState(
    val direction: VikingDirection = VikingDirection.RedRight,
    val name: String,
    val score: Long,
)

object CharacterHelper {

    private val uuid = FirebaseAuth.getInstance().currentUser!!.uid
    private val playerRef = Firebase.firestore.collection("players").document(uuid)

    val vikingState = listenForChanges()

    init {

    }

    private fun listenForChanges() = callbackFlow {

        val snapshotListener = playerRef.addSnapshotListener { value, error ->
            val stuff = if(error != null){
                VikingState(
                    name = value?.getString("name")?:"",
                    score = value?.getLong("score")?:0L,
                    direction = when(value?.getString("direction")){
                        else -> VikingDirection.RedRight
                    }
                )
            }else {
                null
            }
            trySend(stuff)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    fun setInitialValues() {

        playerRef.get().addOnSuccessListener {
            if (!it.exists()) {
                setInitialValue().addOnFailureListener {
                    Log.e("uibhackaton", "failed to set initial data", it)
                }
            }

        }.addOnFailureListener {
            setInitialValue().addOnFailureListener {
                Log.e("uibhackaton", "failed to set initial data", it)
            }
        }


    }

    /**
     * generates a random spawned viking with a generic name
     * Choses a random sprite based on color enum, Erik/Baelog/Olaf from Lost Vikings
     */
    private fun setInitialValue() = playerRef.set(
        mapOf(
            "id" to uuid,
            "name" to "vikingman-${(1..30).random()}",
            "x" to (0..16).random(),
            "y" to (0..16).random(),
            "coins" to 0,
            "color" to "blue",
            "direction" to "left",
            "updatedAt" to FieldValue.serverTimestamp()
        )
    )

    fun updateName(name: String) =
        playerRef.update(
            mapOf("name" to name)
        ).addOnFailureListener {
            Log.e("uibhackaton", "failed to update name", it)
        }

    fun getCurrentPos() {

    }

    fun moveLeft() {
        moveCharacter(playerRef, "x", -1L, "left")
    }

    fun moveRight() {
        moveCharacter(playerRef, "x", 1L, "right")
    }

    fun moveUp() {
        moveCharacter(playerRef, "y", -1L)
    }

    fun moveDown() {
        moveCharacter(playerRef, "y", 1L)
    }

    fun moveTo(x: Int, y: Int) {
        playerRef.update(
            "x", x,
            "y", y,
            "updatedAt",
            FieldValue.serverTimestamp()
        )
    }

    private fun moveCharacter(
        userRef: DocumentReference?,
        field: String,
        value: Long,
        direction: String? = null
    ) {

        if (direction != null) {
            userRef?.update(
                field,
                FieldValue.increment(value),
                "updatedAt",
                FieldValue.serverTimestamp(),
                "direction",
                direction
            )?.addOnFailureListener {
                Log.e("uibhackaton", "failed to move", it)
            }
        } else {
            userRef?.update(
                field,
                FieldValue.increment(value),
                "updatedAt",
                FieldValue.serverTimestamp()
            )
                ?.addOnFailureListener {
                    Log.e("uibhackaton", "failed to move", it)
                }
        }

    }
}