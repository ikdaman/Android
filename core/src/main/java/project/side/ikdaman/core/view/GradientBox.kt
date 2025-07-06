package project.side.ikdaman.core.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

@Composable
fun GradientBox(
    modifier: Modifier = Modifier,
    gradient: Brush,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier.background(brush = gradient),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}