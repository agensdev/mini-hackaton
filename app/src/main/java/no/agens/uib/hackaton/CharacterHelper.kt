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

sealed class VikingDirection {
    object Right : VikingDirection()
    object Left : VikingDirection()
}

sealed class VikingColor(
    @DrawableRes val drawableRes: Int,
    @DrawableRes val idleDrawableRes: Int,
) {
    object Eric : VikingColor(
        drawableRes = R.drawable.viking_2_walking,
        idleDrawableRes = R.drawable.viking_2_idle
    )

    object Baelog : VikingColor(
        drawableRes = R.drawable.viking_1_walking,
        idleDrawableRes = R.drawable.viking_1_idle
    )

    object Olaf : VikingColor(
        drawableRes = R.drawable.viking_3_walking,
        idleDrawableRes = R.drawable.viking_3_idle
    )
}

data class VikingState(
    val direction: VikingDirection = VikingDirection.Right,
    val name: String,
    val coins: Long,
    val xPos: Long,
    val yPos: Long,
    val color: VikingColor
)


object CharacterHelper {

    private val uuid = FirebaseAuth.getInstance().currentUser!!.uid
    private val playerRef = Firebase.firestore.collection("players").document(uuid)

    val vikingState = playerReferenceFlow()

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
                        "right" -> VikingDirection.Right
                        else -> VikingDirection.Left
                    },
                    color = when (snapshot?.getString("color")) {
                        "red" -> VikingColor.Eric
                        "green" -> VikingColor.Baelog
                        else -> VikingColor.Olaf
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
                        "name" to "${(1..70).random()}-viking",
                        "x" to (0..16).random(),
                        "y" to (0..16).random(),
                        "coins" to 0,
                        "color" to listOf("red", "blue", "green").random(),
                        "direction" to "left",
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).addOnFailureListener {
                    Log.e("hackaton", "failed to set initial data", it)
                }
            }
        }.addOnFailureListener {
            Log.e("hackaton", "Failed to get player data", it)
        }
    }

    fun updateName(name: String) =
        playerRef.update(
            mapOf("name" to name)
        ).addOnFailureListener {
            Log.e("hackaton", "failed to update name", it)
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
                    field,
                    FieldValue.increment(valueChange),
                    "updatedAt",
                    FieldValue.serverTimestamp(),
                    "direction",
                    direction.valueAsString // Only set direction for left/right movement
                )
            }
        }
        task.addOnFailureListener {
            Log.e("hackaton", "failed to move player", it)
        }
    }

    enum class Direction(val valueAsString: String) {
        RIGHT("right"), LEFT("left"), UP("up"), DOWN("down")
    }
}