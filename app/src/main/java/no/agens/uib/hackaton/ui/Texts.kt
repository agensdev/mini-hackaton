package no.agens.uib.hackaton.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun TextTitle(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        style = MaterialTheme.typography.h1
    )
}

@Composable
fun TextBody(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.body1
    )
}