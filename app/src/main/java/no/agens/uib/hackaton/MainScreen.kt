package no.agens.uib.hackaton

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
        var showMangoIpaRow by remember { mutableStateOf(false) }
        val viking by CharacterHelper.vikingState.collectAsState(initial = null)

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextTitle(text = viking?.name ?: "No name")
                val vikingColor = when (viking?.color) {
                    "red" -> "Erik (red)"
                    "green" -> "Baleog (green)"
                    "blue" -> "Olaf (blue)"
                    else -> ""
                }
                TextBody(text = vikingColor)
                TextBody(text = "(${viking?.xPos},${viking?.yPos})")
            }



            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Sample button (can be removed):
                BasicButton(
                    text = "This is a button",
                    onClick = {
                        // This is run on click.
                        showMangoIpaRow = !showMangoIpaRow
                    }
                )
                // TODO: Make your UI here!
            }



            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                visible = showMangoIpaRow,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
            ) {
                MangoIpaRow()
            }
        }
    }
}

@Composable
fun MangoIpaRow(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(5) {
            AsyncImage(
                model = R.drawable.mango_ipa,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
fun AnimationsDemo() {
    MaterialTheme {
        var isEnabled by remember { mutableStateOf(false) }
        val backgroundColor by animateColorAsState(if (isEnabled) Color.Green else Color.Red)
        val cornerRadius by animateDpAsState(if (isEnabled) 4.dp else 32.dp)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(all = 24.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .clickable {
                    isEnabled = !isEnabled
                }
                .size(64.dp)
        ) {
            Crossfade(
                targetState = isEnabled,
                animationSpec = tween(500)
            ) { isOn ->
                Text(text = if (isOn) "På" else "Av")
            }
        }
    }
}
