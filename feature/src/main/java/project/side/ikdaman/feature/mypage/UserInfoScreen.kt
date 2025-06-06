package project.side.ikdaman.feature.mypage

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.domain.model.UserInfo

enum class Gender { MALE, FEMALE, NONE }

@Composable
fun UserInfoScreen(navController: NavController, viewModel: UserInfoViewModel = hiltViewModel()) {
    // TODO 생년월일 yyyy-MM-dd 형식인지, 날짜 유효한지 확인
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    UserInfoScreenUI(
        isLoading = uiState.isLoading,
        userInfo = uiState.userInfo,
        nickState = uiState.nickState,
        updateNickState = viewModel::updateNickState,
        checkNickname = viewModel::checkNickname
    ) {
        navController.popBackStack()
    }
}

@Composable
fun UserInfoScreenUI(
    isLoading: Boolean = false,
    userInfo: UserInfo,
    nickState: NickState = NickState.INIT,
    updateNickState: (NickState) -> Unit = {},
    checkNickname: (String) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    val nickname = remember { mutableStateOf(TextFieldValue()) }
    val birthdate = remember { mutableStateOf(TextFieldValue()) }
    val gender = remember { mutableStateOf(Gender.NONE) }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(userInfo) {
        nickname.value = TextFieldValue(userInfo.nickname)
        birthdate.value = TextFieldValue(userInfo.birthdate)
        gender.value = when (userInfo.gender) {
            "male" -> Gender.MALE
            "female" -> Gender.FEMALE
            else -> Gender.NONE
        }
    }

    LaunchedEffect(nickState) {
        when (nickState) {
            NickState.VALID -> {
                Toast.makeText(context, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
            }

            NickState.INVALID -> {
                Toast.makeText(context, "사용할 수 없는 닉네임입니다.", Toast.LENGTH_SHORT).show()
                updateNickState(NickState.CHECK)
            }

            NickState.ERROR -> {
                Toast.makeText(context, "오류가 발생했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                updateNickState(NickState.CHECK)
            }

            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            Image(
                painterResource(R.drawable.arrow_left),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 22.dp, start = 12.dp)
                    .size(26.dp)
                    .clickable { navigateBack() }
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "내 정보 관리",
                style = MyPageTextStyle.TitleText,
                modifier = Modifier.padding(top = 25.dp, start = 3.dp, bottom = 37.dp)
            )
            UserInfoLabel("* 닉네임")
            Row(modifier = Modifier.padding(top = 10.dp)) {
                UserInfoTextField(
                    modifier = Modifier.weight(1f),
                    bringIntoViewRequester = bringIntoViewRequester,
                    coroutineScope = coroutineScope,
                    value = nickname.value
                ) {
                    if (it.text == userInfo.nickname) {   // 기존에 사용하던 닉네임
                        updateNickState(NickState.INIT)
                    } else if (nickname.value.text != it.text && nickState != NickState.CHECK) {  // 닉네임이 변경된 경우에만 중복확인하도록
                        updateNickState(NickState.CHECK)
                    }
                    nickname.value = it
                }
                Spacer(modifier = Modifier.width(10.dp))
                UserInfoButton(
                    text = "중복 확인",
                    onClick = {
                        checkNickname(nickname.value.text)
                    },
                    enabled = nickname.value.text.isNotBlank() && nickState == NickState.CHECK,
                    style = MyPageTextStyle.CheckButtonText,
                    containerColor = Color(0xFF858585)
                )
            }
            UserInfoLabel("생년월일")
            UserInfoTextField(
                modifier = Modifier.fillMaxWidth(),
                bringIntoViewRequester = bringIntoViewRequester,
                coroutineScope = coroutineScope,
                value = birthdate.value
            ) {
                birthdate.value = it
            }
            UserInfoLabel("성별")
            Row {
                UserInfoButton(
                    text = "남",
                    onClick = {
                        gender.value =
                            if (gender.value == Gender.MALE) Gender.NONE else Gender.MALE
                    },
                    style = MyPageTextStyle.GenderButtonText,
                    containerColor = if (gender.value == Gender.MALE)
                        Color(0xFF858585) else Color(0xFFF5F5F5)
                )
                Spacer(modifier = Modifier.width(10.dp))
                UserInfoButton(
                    text = "여",
                    onClick = {
                        gender.value =
                            if (gender.value == Gender.FEMALE) Gender.NONE else Gender.FEMALE
                    },
                    style = MyPageTextStyle.GenderButtonText,
                    containerColor = if (gender.value == Gender.FEMALE)
                        Color(0xFF858585) else Color(0xFFF5F5F5),
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            UserInfoButton(
                modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester),
                text = "저장하기",
                onClick = {
                    //실제 저장 API 호출
                },
                enabled = nickState == NickState.VALID || nickState == NickState.INIT,
                style = MyPageTextStyle.ButtonText,
                containerColor = Color.Black,
                fillMaxWidth = true
            )
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .height(26.dp)
                    .clickable { },
                contentAlignment = Alignment.CenterStart
            ) {
                Text("로그아웃", style = MyPageTextStyle.SubMenuText)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .height(26.dp)
                    .clickable { },
                contentAlignment = Alignment.CenterStart
            ) {
                Text("회원탈퇴", style = MyPageTextStyle.SubMenuText)
            }
        }
    }
}

@Composable
fun UserInfoLabel(label: String) {
    Text(
        label,
        style = MyPageTextStyle.LabelText,
        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
    )
}

@Composable
fun UserInfoButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    style: TextStyle,
    containerColor: Color,
    disabledContainerColor: Color = Color.Unspecified,
    fillMaxWidth: Boolean = false,
) {
    Button(
        modifier = modifier
            .then(
                if (fillMaxWidth) Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                else Modifier.size(80.dp, 54.dp)
            ),
        onClick = onClick,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 0.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = disabledContainerColor,
        )
    ) {
        Text(text, style = style)
    }
}

@Composable
fun UserInfoTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    bringIntoViewRequester: BringIntoViewRequester,
    coroutineScope: CoroutineScope,
    onValueChanged: (TextFieldValue) -> Unit
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = Color.Transparent,
            backgroundColor = Color.Transparent
        )
    ) {
        TextField(
            value = value,
            onValueChange = onValueChanged,
            modifier = modifier
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            delay(300)
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color(0xFF626262),
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                cursorColor = Color(0xFF626262),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = MyPageTextStyle.TextFieldText
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoScreenPreview() {
    UserInfoScreenUI(userInfo = UserInfo("닉네임", "1999-01-01", "male"))
}