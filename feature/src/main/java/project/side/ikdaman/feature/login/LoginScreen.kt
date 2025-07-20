package project.side.ikdaman.feature.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.LOGIN_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val loginState = viewModel.loginState.collectAsStateWithLifecycle()

    LaunchedEffect(loginState.value) {
        when (loginState.value) {
            is LoginState.Success -> {
                // TODO 최초 1번만 튜토리얼 화면으로 이동
                navigateToHomeScreen(navController)
            }

            is LoginState.Error -> Toast.makeText(
                context,
                (loginState.value as LoginState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> {}
        }
    }

    LoginScreenUI(
        isLoading = loginState.value == LoginState.Loading,
        onGoogleLogin = {
            viewModel.googleLogin(context)
        },
        onNaverLogin = {
            viewModel.naverLogin(context)
        },
        onKakaoLogin = {
            viewModel.kakaoLogin(context)
        }
    )
}


private fun navigateToHomeScreen(navController: NavController) {
    navController.navigate(MAIN_ROUTE) {
        popUpTo(LOGIN_ROUTE) {
            inclusive = true
        }
    }
}


@Composable
fun LoginScreenUI(
    isLoading: Boolean = false,
    onGoogleLogin: () -> Unit = {},
    onNaverLogin: () -> Unit = {},
    onKakaoLogin: () -> Unit = {}
) {
    Scaffold {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(111.dp))
                Image(
                    painter = painterResource(R.drawable.book),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp)
                )
                Spacer(Modifier.height(36.dp))
                Text(
                    text = "마음가는 대로 읽는 즐거움\n읽다만.",
                    style = LoginTextStyle.MainDescText,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(19.dp))
                Text(
                    text = "읽다만에 로그인하고\n더 즐거운 독서를 시작해보세요 \u263A\uFE0F",
                    style = LoginTextStyle.SubDescText,
                    textAlign = TextAlign.Center
                )
                Box(Modifier.weight(1f))
                Row {
                    Text(text = "가입 시 ", style = LoginTextStyle.TermsRegularText)
                    TermText("이용약관")
                    Text(text = " 및 ", style = LoginTextStyle.TermsRegularText)
                    TermText("개인정보처리방침에")
                    Text(text = " 동의하게 됩니다.", style = LoginTextStyle.TermsRegularText)
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 15.dp)
                ) {
                    SocialButton(
                        text = "구글로 시작하기",
                        backgroundColor = Color(0xFFFFFFFF),
                        textColor = Color(0xFF1F1F1F),
                        borderColor = Color(0xFF747775),
                        imageResId = R.drawable.google_logo
                    ) {
                        onGoogleLogin()
                    }
                    Spacer(Modifier.height(10.dp))
                    SocialButton(
                        text = "네이버로 시작하기",
                        backgroundColor = Color(0xFF03C75A),
                        textColor = Color(0xFFFFFFFF),
                        imageResId = R.drawable.naver_logo
                    ) {
                        onNaverLogin()
                    }
                    Spacer(Modifier.height(10.dp))
                    SocialButton(
                        text = "카카오로 시작하기",
                        backgroundColor = Color(0xFFFEE500),
                        textColor = Color(0xD9000000),
                        imageResId = R.drawable.kakao_logo
                    ) {
                        onKakaoLogin()
                    }
                }
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun TermText(text: String, onClick: () -> Unit = {}) {
    Column {
        Text(
            text = text,
            style = LoginTextStyle.TermsBoldText,
            modifier = Modifier
                .drawBehind {
                    val y = size.height + 1.dp.toPx()
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .clickable {
                    onClick()
                }
        )
    }
}

@Composable
fun SocialButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color? = null,
    imageResId: Int,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .then(
                if (borderColor == null) Modifier
                else Modifier.border(1.dp, borderColor, RoundedCornerShape(10.dp))
            )
            .clickable { onClick() }
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(imageResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(text = text, style = LoginTextStyle.LoginButtonText.copy(color = textColor))
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    AppTheme {
        LoginScreenUI()
    }
}