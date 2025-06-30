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
import androidx.compose.ui.text.TextRange
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
import project.side.ikdaman.core.navigation.LOGIN_ROUTE
import project.side.ikdaman.core.view.AppDialog
import project.side.ikdaman.core.view.WithdrawDialog
import project.side.ikdaman.domain.model.UserInfo

@Composable
fun UserInfoScreen(
    navController: NavController,
    viewModel: UserInfoViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val accountUiState = accountViewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val showLogoutDialog = remember { mutableStateOf(false) }
    val showWithdrawDialog = remember { mutableStateOf(false) }
    val showRealWithdrawDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(accountUiState) {
        val message = getMessage(accountUiState)
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        when (accountUiState) {
            is AccountState.Success -> navigateToLoginScreen(navController)
            is AccountState.NeedToLogin -> accountViewModel.reAuth(context)
            is AccountState.Error -> {
                if (accountUiState.navigateToLogin) {
                    navigateToLoginScreen(navController)
                }
                accountViewModel.initAccountState()
            }

            else -> Unit
        }
    }

    if (showLogoutDialog.value) {
        AppDialog(
            title = "읽다만에서\n로그아웃하시겠어요?",
            visible = showLogoutDialog.value,
            onDismissRequest = { showLogoutDialog.value = false },
            onConfirmClicked = { accountViewModel.logout(context) },
        )
    }

    if (showWithdrawDialog.value) {
        AppDialog(
            title = "읽다만에서\n탈퇴하시겠어요?",
            visible = showWithdrawDialog.value,
            onDismissRequest = { showWithdrawDialog.value = false },
            onConfirmClicked = { showRealWithdrawDialog.value = true }
        )
    }

    if (showRealWithdrawDialog.value) {
        WithdrawDialog(
            showDialog = showRealWithdrawDialog.value,
            onConfirmClicked = accountViewModel::withdraw,
            onDismissRequest = { showRealWithdrawDialog.value = false }
        )
    }

    UserInfoScreenUI(
        isLoading = uiState.isLoading || accountUiState == AccountState.Loading,
        userInfo = uiState.userInfo,
        nicknameIsValid = uiState.nicknameIsValid,
        birthdateIsValid = uiState.birthdateIsValid,
        updateNicknameIsValid = viewModel::updateNicknameIsValid,
        updateBirthdateIsValid = viewModel::updateBirthdateIsValid,
        updateUserInfo = viewModel::updateUserInfo,
        onLogoutClicked = { showLogoutDialog.value = true },
        onWithdrawClicked = { showWithdrawDialog.value = true },
        checkNickname = viewModel::checkNickname
    ) {
        navController.popBackStack()
    }
}

private fun navigateToLoginScreen(navController: NavController) {
    navController.navigate(LOGIN_ROUTE) {
        popUpTo(0) {
            inclusive = true
        }
    }
}

@Composable
fun UserInfoScreenUI(
    isLoading: Boolean = false,
    userInfo: UserInfo,
    nicknameIsValid: Boolean = true,
    birthdateIsValid: Boolean = true,
    updateNicknameIsValid: (String) -> Unit = {},
    updateBirthdateIsValid: (String) -> Unit = {},
    checkNickname: (String) -> Unit = {},
    updateUserInfo: (String, String, String) -> Unit = { _, _, _ -> },
    onLogoutClicked: () -> Unit = {},
    onWithdrawClicked: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    val nickname = remember { mutableStateOf(TextFieldValue()) }
    val birthdate = remember { mutableStateOf(TextFieldValue()) }
    val gender = remember { mutableStateOf(Gender.NONE) }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userInfo) {
        nickname.value = TextFieldValue(userInfo.nickname)
        birthdate.value = TextFieldValue(userInfo.birthdate ?: "")
        gender.value = Gender.toGender(userInfo.gender ?: "")
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
                    updateNicknameIsValid(it.text)
                    nickname.value = it
                }
                Spacer(modifier = Modifier.width(10.dp))
                UserInfoButton(
                    text = "중복 확인",
                    onClick = {
                        checkNickname(nickname.value.text)
                    },
                    enabled = nickname.value.text.isNotBlank() && !nicknameIsValid,
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
                val (formatted, correctedCursor) = onDateChanged(birthdate.value, it)

                birthdate.value = TextFieldValue(
                    text = formatted,
                    selection = TextRange(correctedCursor)
                )

                updateBirthdateIsValid(formatted)
            }
            UserInfoLabel("성별")
            Row {
                UserInfoButton(
                    text = "남",
                    onClick = {
                        gender.value =
                            if (gender.value == Gender.MALE) Gender.NONE else Gender.MALE
                    },
                    isSelected = gender.value == Gender.MALE,
                    style = MyPageTextStyle.GenderButtonText,
                    containerColor = Color(0xFFF5F5F5),
                    selectedContainerColor = Color.Black
                )
                Spacer(modifier = Modifier.width(10.dp))
                UserInfoButton(
                    text = "여",
                    onClick = {
                        gender.value =
                            if (gender.value == Gender.FEMALE) Gender.NONE else Gender.FEMALE
                    },
                    isSelected = gender.value == Gender.FEMALE,
                    style = MyPageTextStyle.GenderButtonText,
                    containerColor = Color(0xFFF5F5F5),
                    selectedContainerColor = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            UserInfoButton(
                modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester),
                text = "저장하기",
                onClick = {
                    updateUserInfo(
                        nickname.value.text,
                        birthdate.value.text,
                        gender.value.genderToString()
                    )
                },
                enabled = nicknameIsValid && birthdateIsValid,
                style = MyPageTextStyle.ButtonText,
                containerColor = Color.Black,
                fillMaxWidth = true
            )
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .height(26.dp)
                    .clickable { onLogoutClicked() },
                contentAlignment = Alignment.CenterStart
            ) {
                Text("로그아웃", style = MyPageTextStyle.SubMenuText)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .height(26.dp)
                    .clickable { onWithdrawClicked() },
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
    isSelected: Boolean = false,
    style: TextStyle,
    containerColor: Color,
    selectedContainerColor: Color = Color.Unspecified,
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
            containerColor = if (isSelected) selectedContainerColor else containerColor,
            disabledContainerColor = disabledContainerColor,
        )
    ) {
        Text(text, style = if (isSelected) style.copy(Color.White) else style)
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

private fun getMessage(accountUiState: AccountState): String? {
    return when (accountUiState) {
        is AccountState.Success -> when (accountUiState.type) {
            SuccessType.LOGOUT -> "로그아웃 되었습니다."
            SuccessType.WITHDRAW -> "회원탈퇴 되었습니다."
        }

        is AccountState.NeedToLogin -> when (accountUiState.type) {
            WarningType.NEED_RE_AUTH -> "인증 문제로 다시 로그인이 필요합니다."
        }

        is AccountState.Error -> when (accountUiState.type) {
            ErrorType.LOGOUT_FAILED -> "로그아웃에 실패했습니다."
            ErrorType.WITHDRAW_FAILED -> "회원 탈퇴에 실패했습니다."
            ErrorType.UNLINK_FAILED -> "회원 탈퇴는 완료되었으나 소셜 연결 해제에 실패했습니다."
            ErrorType.RE_AUTH_FAILED -> "소셜로그인에 실패했습니다."
            ErrorType.UNKNOWN -> "오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
        }

        else -> null
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoScreenPreview() {
    UserInfoScreenUI(userInfo = UserInfo("닉네임", "1999-01-01", "male"))
}