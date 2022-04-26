package no.agens.uib.hackaton

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object CharacterHelper {

    private val uuid = FirebaseAuth.getInstance().currentUser!!.uid
    private val playerRef = Firebase.firestore.collection("players").document(uuid)


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
            "name" to "vikingman-${(1..300).random()}",
            "x" to (0..16).random(),
            "y" to (0..16).random(),
            "coins" to 0,
            "color" to listOf("blue", "red", "green").random(),
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