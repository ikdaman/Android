package project.side.ikdaman.feature.bookshelf

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.ui.PretendardFontFamily
import project.side.ikdaman.feature.search.SearchTextField

@Composable
fun BookShelfTab(navController: NavController, viewModel: BookShelfViewModel = hiltViewModel()) {
    val selectedColor = viewModel.selectedColor.collectAsState().value

    BookShelfTabUI(
        onNavigateTo = {
            navController.navigate(it)
        },
        selectedColor = selectedColor
    )
}

@Composable
fun BookShelfTabUI(
    onNavigateTo: (String) -> Unit = {},
    selectedColor: Color = Palette.first
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedColor)
    ) {
        Column {
            SearchTextField(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 35.dp, bottom = 40.dp),
                searchText = "",
                onSearchTextChanged = {}
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 17.dp),
                horizontalArrangement = Arrangement.End
            ) {
                FilterText(text = "전체", isSelected = true)
                FilterText(text = "\uD83C\uDFB5 완독한 책", isSelected = false)
                FilterText(text = "\uD83D\uDCD6 독서중인 책", isSelected = false)
            }
            HorizontalPager(state = pagerState) { page ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BookItem()
                        BookItem(true)
                        BookItem()
                    }
                    BookShelf(selectedColor = selectedColor)
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BookItem()
                        BookItem(true)
                        BookItem()
                    }
                    BookShelf(selectedColor = selectedColor)
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BookItem()
                        BookItem(true)
                        BookItem()
                    }
                    BookShelf(selectedColor = selectedColor)
                }
            }
        }
    }
}

@Composable
fun FilterText(text: String, isSelected: Boolean) {
    val style = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily
    )

    Text(
        text = text,
        style = if (isSelected) style.copy(fontWeight = FontWeight.Bold) else
            style.copy(color = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier.padding(start = 10.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun BookShelf(modifier: Modifier = Modifier, selectedColor: Color = Palette.first) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(23.dp)
            .offset(y = (-8.5).dp)
//            .dropShadow(
//                color = Color.Black.copy(alpha = 0.1f),
//                blurRadius = 10.dp
//            )
            .innerShadow(
                shape = RectangleShape,
                color = selectedColor,
                blur = 8.dp
            )
            .background(color = Color.White.copy(alpha = 0.3f))
    )
}

@Preview(showBackground = true)
@Composable
fun BookItem(
    isCompleted: Boolean = false,
    bookImage: String = ""
) {
    Box {
        if (isCompleted) {
            Image(
                painter = painterResource(R.drawable.book_mark),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .offset(y = 8.5.dp)
            )
        }
        Box(
            modifier = Modifier
                .dropShadow(
                    color = Color.Black.copy(alpha = 0.1f),
                    offsetX = (-5).dp,
                    offsetY = 0.dp,
                    blurRadius = 4.dp
                )
        ) {
            if (bookImage.isEmpty()) {
                Image(
                    painter = painterResource(R.drawable.no_image),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp, 160.dp)
                )
            } else {
                AsyncImage(
                    model = "https://image.aladin.co.kr/product/4086/97/cover500/8936434128_2.jpg",
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp, 160.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BookShelfTabUIPreview() {
    AppTheme {
        BookShelfTabUI()
    }
}