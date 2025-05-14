import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import project.side.ikdaman.feature.home.HomeTextStyles

@Composable
fun ExpandableInlineText(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 3,
    isExpanded: MutableState<Boolean> = remember { mutableStateOf(false) },
) {
    var shouldShowMore by remember { mutableStateOf(false) }
    var finalText by remember { mutableStateOf(AnnotatedString(text)) }
    var rememberedText by remember { mutableStateOf(text) }

    LaunchedEffect(text) {
        if (text != rememberedText) {
            finalText = AnnotatedString(text)
            isExpanded.value = false
            shouldShowMore = false
            rememberedText = text
        }
    }


    Box(
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
        ) {
            isExpanded.value = !isExpanded.value
        })
    {
        Log.i("TAG", "isExpanded: ${isExpanded.value}")
        AnimatedContent(targetState = isExpanded) { isExpanded ->
            Text(
                text = if (!isExpanded.value) finalText else AnnotatedString(text),
                maxLines = if (isExpanded.value) Int.MAX_VALUE else maxLines,
                onTextLayout = { layoutResult ->
                    if (!isExpanded.value) {
                        try {
                            if (layoutResult.hasVisualOverflow || shouldShowMore) {
                                shouldShowMore = true
                                val visibleText = text
                                    .substring(0, layoutResult.getLineEnd(maxLines - 1))
                                    .dropLast("더보기".length + 4) // 공간 확보
                                    .trimEnd()
                                finalText = buildAnnotatedString {
                                    append(visibleText)
                                    append("... ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("더보기")
                                    }
                                }
                            } else {
                                finalText = AnnotatedString(text)
                            }
                        } catch (_: Exception) {
                        }
                    }
                },
                style = HomeTextStyles.bottomDescription.copy(
                    color = Color(0xFF666666)
                ),
            )
        }
    }
}