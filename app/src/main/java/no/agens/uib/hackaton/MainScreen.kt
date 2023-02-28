package no.agens.uib.hackaton

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import no.agens.uib.hackaton.ui.BasicButton
import no.agens.uib.hackaton.ui.TextBody
import no.agens.uib.hackaton.ui.TextTitle
import no.agens.uib.hackaton.ui.theme.HackatonTheme


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HackatonTheme {
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val vikingVector = viking?.color?.drawableRes
                val idleVikingVector = viking?.color?.idleDrawableRes
                var isWalking by remember { mutableStateOf(false) }
                var isReverse by remember { mutableStateOf(false) }

                TextTitle(text = viking?.name ?: "No name")
                Spacer(modifier = Modifier.height(16.dp))
                LaunchedEffect(viking?.xPos?.plus(viking?.yPos ?: 0)) {
                    isReverse = viking?.direction == VikingDirection.Left
                    isWalking = true
                    delay(1000)
                    isWalking = false
                }

                Crossfade(modifier = Modifier.graphicsLayer {
                    rotationY = if (isReverse) 180f else 0f
                }, targetState = isWalking) {
                    when (it) {
                        true -> {
                            GifImage(drawable = vikingVector ?: R.drawable.mango_ipa)
                        }
                        else -> {
                            GifImage(drawable = idleVikingVector ?: R.drawable.mango_ipa)
                        }
                    }
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


@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    drawable: Int,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(

        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = drawable).apply(block = {
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.size(48.dp),
    )
}