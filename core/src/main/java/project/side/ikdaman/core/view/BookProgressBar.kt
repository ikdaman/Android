package project.side.ikdaman.core.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun BookProgressBarWithText(
    width: Int,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(modifier.height(28.dp)) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .height(3.dp)
                .background(Color.Black.copy(alpha = 0.1f))
                .fillMaxWidth()
                .align(Alignment.CenterStart)
        )
        val textOffset = when {
            progress < 0.1f -> 0f // 시작 경계
            progress > 0.9f -> 0.9f * width - 30 // 끝 경계
            else -> progress * width - 30
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .height(3.dp)
                .background(Color.Black)
                .width((progress * width).dp)
                .align(Alignment.CenterStart)
        )
        val progressText = "${(progress * 100).toInt()}%"
        AppText(
            "\uD83D\uDCD6 $progressText",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                letterSpacing = (-0.4).sp,
            ),
            modifier = Modifier
                .offset(x = (textOffset).dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black)
                .padding(vertical = 7.dp, horizontal = 10.dp)
        )
    }
}

@Composable
fun BookProgressBar(
    width: Int,
    progress: Float,
) {
    Box {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .height(3.dp)
                .background(Color.Black.copy(alpha = 0.1f))
                .fillMaxWidth()
                .align(Alignment.CenterStart)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .height(3.dp)
                .background(Color.Black)
                .width((progress * width).dp)
                .align(Alignment.CenterStart)
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 400, heightDp = 300)
fun BookProgressBarWithTextPreview() {
    AppTheme {
        Column {
            Box(
                Modifier
                    .width(400.dp)
                    .height(80.dp)
            ) {
                BookProgressBarWithText(
                    width = 400,
                    progress = 0f,
                )
            }

            Box(
                Modifier
                    .width(400.dp)
                    .height(80.dp)
            ) {
                BookProgressBarWithText(
                    width = 400,
                    progress = 0.1f,
                )
            }
            Box(
                Modifier
                    .width(400.dp)
                    .height(80.dp)
            ) {
                BookProgressBarWithText(
                    width = 400,
                    progress = 0.5f,
                )
            }
            Box(
                Modifier
                    .width(400.dp)
                    .height(80.dp)
            ) {
                BookProgressBarWithText(
                    width = 400,
                    progress = 1f,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 260)
fun BookProgressBarPreview() {
    AppTheme {
        Column {
            Box(
                Modifier.padding(vertical = 10.dp).width(260.dp)
            ) {
                BookProgressBar(
                    width = 260,
                    progress = 0.2f,
                )
            }
            Box(
                Modifier.padding(vertical = 10.dp).width(260.dp)
            ) {
                BookProgressBar(
                    width = 260,
                    progress = 0.5f,
                )
            }
            Box(
                Modifier.padding(vertical = 10.dp).width(260.dp)
            ) {
                BookProgressBar(
                    width = 260,
                    progress = 0.8f,
                )
            }
        }
    }
}