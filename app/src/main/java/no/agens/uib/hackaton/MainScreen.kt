package no.agens.uib.hackaton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.agens.uib.hackaton.CharacterHelper.Direction
import no.agens.uib.hackaton.CharacterHelper.moveCharacter
import no.agens.uib.hackaton.ui.BasicButton
import no.agens.uib.hackaton.ui.TextBody
import no.agens.uib.hackaton.ui.TextTitle
import no.agens.uib.hackaton.ui.theme.UiBHackatonTheme


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UiBHackatonTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val viking by CharacterHelper.vikingState.collectAsState(initial = null)

                TextTitle(text = viking?.name ?: "No name")
                Spacer(modifier = Modifier.height(16.dp))
                val vikingVector = viking?.direction?.drawableRes
                if (vikingVector != null) {
                    GifImage(drawableRes = vikingVector)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextBody(text = "(${viking?.xPos},${viking?.yPos})")
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BasicButton(
                    onClick = { moveCharacter(direction = Direction.UP) },
                    text = "UP"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    BasicButton(
                        onClick = { moveCharacter(direction = Direction.LEFT) },
                        text = "LEFT"
                    )

                    Spacer(Modifier.width(42.dp))

                    BasicButton(
                        onClick = { moveCharacter(direction = Direction.RIGHT) },
                        text = "RIGHT"
                    )
                }
                BasicButton(
                    onClick = { moveCharacter(direction = Direction.DOWN) },
                    text = "DOWN"
                )
            }
        }
    }
}
