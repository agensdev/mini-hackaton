# UiB-Hackaton

Simple starting point to interact with Firebase Firestore.

The sample app already contains the code you need to authenticate anonymously, and initialize a Character in the game.
It should appear on screen and move in a circle three times.

# Characterhelper

## Creating a character

All fields needs to be set, otherwise Firestore will reject your request.

```kotlin 
private val uuid = FirebaseAuth.getInstance().currentUser!!.uid
private fun setInitialValue() = playerRef.set(
        mapOf(
            "id" to uuid,
            "name" to "vikingman-${(1..300).random()}", // random generic viking name
            "x" to (0..16).random(),
            "y" to (0..16).random(),
            "coins" to 0,
            "color" to listOf("blue", "red", "green").random(), // blue/red/green are the only valid colors
            "direction" to "left",
            "updatedAt" to FieldValue.serverTimestamp()
        )
    )

```

## Moving a character

```kotlin

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

```

## Renaming a character

```kotlin

    fun updateName(name: String) =
        playerRef.update(
            mapOf("name" to name)
        ).addOnFailureListener {
            Log.e("uibhackaton", "failed to update name", it)
        }
```



