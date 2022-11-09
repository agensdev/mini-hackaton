# Mini-Hackaton

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

Moving is restricted to once per second. 
To be able to move, the field updatedAt needs to be present.

To move character, invoke `CharacterHelper::moveCharacter` and provide a `Direction`.

## Renaming a character

```kotlin

    fun updateName(name: String) =
        playerRef.update(
            mapOf("name" to name)
        ).addOnFailureListener {
            Log.e("uibhackaton", "failed to update name", it)
        }
```



