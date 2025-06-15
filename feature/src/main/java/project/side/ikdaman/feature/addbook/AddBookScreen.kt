package project.side.ikdaman.feature.addbook

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.ui.PretendardFontFamily
import project.side.ikdaman.core.view.GradientBox
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookItem

@Composable
fun AddBookScreen(
    isbn: String,
    viewModel: AddBookViewModel = hiltViewModel(),
    navController: NavController
) {
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()
    val selectedColor by viewModel.selectedColor.collectAsStateWithLifecycle()
    val initialImpression by viewModel.initialImpression.collectAsStateWithLifecycle()
    val addBookSuccess by viewModel.addBookSuccess.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.searchBookWithIsbn(isbn)
    }

    LaunchedEffect(addBookSuccess) {
        when (val result = addBookSuccess) {
            is ApiResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            is ApiResult.Success -> {
                navController.navigate(MAIN_ROUTE)
                Toast.makeText(context, "책이 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }

            ApiResult.Loading -> Unit
            null -> Unit
        }
    }

    AddBookScreenUI(
        selectedColor = selectedColor,
        initialImpression = initialImpression,
        onInitialImpressionChange = { viewModel.updateInitialImpression(it) },
        bookItem = searchResult,
        addBook = { viewModel.addBook() }
    )
}

@SuppressLint("InvalidColorHexValue")
@Composable
private fun AddBookScreenUI(
    selectedColor: Color = Palette.first,
    initialImpression: String = "",
    onInitialImpressionChange: (String) -> Unit = {},
    bookItem: BookItem? = null,
    addBook: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = {},
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Back",
                        Modifier.size(26.dp)
                    )
                }
                Text(
                    text = "나의 새로운 책",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 15.dp),
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
            }
        },
    ) { paddingValues ->
        GradientBox(
            Modifier
                .fillMaxSize(),
            gradient = Brush.verticalGradient(
                colors = listOf(
                    selectedColor,
                    selectedColor.copy(alpha = 0.2f),
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                if (bookItem == null) return@Column

                val labelTextStyle = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )

                val contentTextStyle = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color(0xff777777)
                )

                Spacer(Modifier.height(24.dp))

                AsyncImage(
                    model = bookItem.cover,
                    contentDescription = "Book Cover",
                    modifier = Modifier
                        .size(width = 80.dp, height = 114.dp)
                        .align(Alignment.CenterHorizontally),
                )

                Spacer(Modifier.height(24.dp))

                Row {
                    Column {
                        Text(
                            "책 제목",
                            style = labelTextStyle,
                        )
                        Text(
                            "작가",
                            style = labelTextStyle,
                        )
                        Text(
                            "출판사",
                            style = labelTextStyle,
                        )
                        Text(
                            "총 페이지",
                            style = labelTextStyle,
                        )
                    }
                    Spacer(Modifier.width(30.dp))
                    Column {
                        Text(
                            bookItem.title,
                            style = contentTextStyle
                        )
                        Text(
                            bookItem.author,
                            style = contentTextStyle
                        )
                        Text(
                            bookItem.publisher,
                            style = contentTextStyle
                        )
                        Text(
                            bookItem.subInfo?.itemPage ?: "0",
                            style = contentTextStyle
                        )
                    }
                }
                Spacer(Modifier.height(30.dp))
                Row {
                    Text(
                        "도서 정보 알라딘 제공",
                        style = TextStyle(
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                        )
                    )
                    Spacer(Modifier.width(9.dp))
                    Text(
                        "알라딘에서 보기",
                        style = TextStyle(
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline
                        ),
                    )
                }
                Spacer(Modifier.height(30.dp))
                Text(
                    "책의 첫인상",
                    style = labelTextStyle,
                )
                Spacer(Modifier.height(10.dp))
                InitialImpressionTextField(
                    initialImpression = initialImpression,
                    onInitialImpressionChange = onInitialImpressionChange
                )
                Spacer(modifier = Modifier.weight(1f))
                AddBookButton(modifier = Modifier.fillMaxWidth(), addBook = addBook)
            }
        }
    }
}

@Composable
private fun InitialImpressionTextField(
    initialImpression: String,
    onInitialImpressionChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .heightIn(min = 48.dp)
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(5.dp),
                color = Color.White
            )
            .padding(
                top = 17.dp,
                start = 15.dp,
                end = 15.dp,
                bottom = 15.dp
            )
    ) {
        BasicTextField(
            value = initialImpression,
            onValueChange = {
                if (it.length <= 50)
                    onInitialImpressionChange(it)
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp
            ),
            decorationBox = { innerTextField ->
                if (initialImpression.isEmpty()) {
                    Text(
                        text = "처음 책을 보고 들었던 생각을 짧게 적어보세요.\n" +
                                "독서가 마음처럼 잘되지 않을 때, 나에게 힘을 줄 거예요!",
                        color = Color(0xff626262),
                        style = TextStyle(
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp
                        ),
                    )
                }
                innerTextField()
            }
        )
        Spacer(Modifier.height(15.dp))
        Text(
            "${initialImpression.length}/50",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            ),
            color = Color(0xff333333)
        )
    }
}

@Composable
private fun AddBookButton(modifier: Modifier, addBook: () -> Unit) {
    Button(
        onClick = { addBook() },
        modifier = modifier.padding(bottom = 78.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        contentPadding = PaddingValues(vertical = 11.dp)
    ) {
        Text(
            text = "책 추가하기",
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        )
    }
}

@Composable
@Preview
private fun AddBookScreenUIPreview() {
    AppTheme {
        AddBookScreenUI()
    }
}