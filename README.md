# UiB Hackathon

The goal is to make a way to move your Mango Ipa Viking around and gather as many Mango Ipas as
possible. 

During the development phase, you may move around and try out different approaches. When announced,
the scores will be reset and the competition begins.

Creativity is awarded.

This sample app already contains the code you need to authenticate anonymously, and initialize a 
Character in the game.

## Getting started

Download and install Android Studio: https://developer.android.com/studio

Clone the project: `git clone git@github.com:agensdev/mini-hackaton.git`

Open in Android Studio and do a gradle sync (elephant with down left arrow icon)

Connect your device or emulator and press Run.

Open `MainScreen.kt` and find the todo: `// TODO: Make your UI here!`. That's where to begin!

## Useful files

These files might be relevant to you:
- `app/build.gradle` - Where you define dependencies
- `MainActivity` - App entry point
- `MainScreen` - Where the main screen is put together
- `CharacterHelper` - Helper methods for moving and changing your character
- `ui/*` - Directory for defining theme and components

## CharacterHelper

On app startup, you are signed in automatically and given a character. See `setInitialValues`.

Your character stored in the database can be observed with the `CharacterHelper::vikingState` flow.
 
To move character, invoke `CharacterHelper::moveCharacter` and provide a `Direction`.
Note that movement is restricted to once per second. Also, to be able to move, the field updatedAt 
needs to be present.

The character can also be renamed using the `CharacterHelper::updateName` method.

## Useful links

- Basic Compose layouts: https://developer.android.com/jetpack/compose/layouts/basics
- Guide to animations in compose: https://developer.android.com/jetpack/compose/animation
- State in compose: https://developer.android.com/jetpack/compose/state
