package no.agens.uib.hackaton

import android.util.Log
import androidx.annotation.DrawableRes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

sealed class VikingDirection(
    @DrawableRes val drawableRes: Int,
) {
    object RedRight : VikingDirection(drawableRes = R.drawable.red_viking_right)
}

data class VikingState(
    val direction: VikingDirection = VikingDirection.RedRight,
    val name: String,
    val coins: Long,
    val xPos: Long,
    val yPos: Long,
)

object CharacterHelper {

    private val uuid = FirebaseAuth.getInstance().currentUser!!.uid
    private val playerRef = Firebase.firestore.collection("players").document(uuid)

    val vikingState = playerReferenceFlow()

    init {

    }

    /**
     * The values for your viking as stored in Firebase
     */
    private fun playerReferenceFlow(): Flow<VikingState?> = callbackFlow {
        val snapshotListener = playerRef.addSnapshotListener { snapshot, error ->
            val vikingData = if (error == null) {
                VikingState(
                    name = snapshot?.getString("name") ?: "",
                    coins = snapshot?.getLong("coins") ?: 0L,
                    xPos = snapshot?.getLong("x") ?: 0L,
                    yPos = snapshot?.getLong("y") ?: 0L,
                    direction = when (snapshot?.getString("direction")) {
                        "right" -> VikingDirection.RedRight
                        else -> VikingDirection.RedRight
                    }
                )
            } else {
                null
            }
            trySend(vikingData)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    fun setInitialValues() {
        playerRef.get().addOnSuccessListener {
            if (!it.exists()) {
                playerRef.set(
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
                ).addOnFailureListener {
                    Log.e("uibhackaton", "failed to set initial data", it)
                }
            }
        }.addOnFailureListener {
            Log.e("uibhackaton", "Failed to get player data", it)
        }
    }

    fun updateName(name: String) =
        playerRef.update(
            mapOf("name" to name)
        ).addOnFailureListener {
            Log.e("uibhackaton", "failed to update name", it)
        }

    fun moveTo(x: Int, y: Int) {
        playerRef.update(
            "x", x,
            "y", y,
            "updatedAt",
            FieldValue.serverTimestamp()
        )
    }

    fun moveCharacter(direction: Direction) {
        val field = when (direction) {
            Direction.RIGHT, Direction.LEFT -> "x"
            Direction.UP, Direction.DOWN -> "y"
        }
        val valueChange = when (direction) {
            Direction.RIGHT, Direction.DOWN -> 1L
            Direction.UP, Direction.LEFT -> -1L
        }
        val task = when (direction) {
            Direction.UP,
            Direction.DOWN -> {
                playerRef.update(
                    field, FieldValue.increment(valueChange),
                    "updatedAt", FieldValue.serverTimestamp(),
                )
            }
            Direction.RIGHT,
            Direction.LEFT -> {
                playerRef.update(
                    field, FieldValue.increment(valueChange),
                    "updatedAt", FieldValue.serverTimestamp(),
                    "direction", direction.valueAsString // Only set direction for left/right movement
                )
            }
        }
        task.addOnFailureListener {
            Log.e("uibhackaton", "failed to move player", it)
        }
    }

    enum class Direction(val valueAsString: String) {
        RIGHT("right"), LEFT("left"), UP("up"), DOWN("down")
    }
}