package project.side.ikdaman.feature.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.USERINFO_ROUTE
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun MyPageTab(
    navController: NavController,
    onPermissionCheck: () -> Unit = {},
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    MyPageTabUI(
        nickname = uiState.nickname,
        isChecked = uiState.isChecked,
        selectedTime = uiState.selectedTime,
        navigateToEditProfile = { navController.navigate(USERINFO_ROUTE) },
        onCheckedChanged = {
            viewModel.toggleAlarm()
            onPermissionCheck()
        },
        onTimeSelected = { viewModel.updateSelectedTime(it) }
    )
}

@Composable
fun MyPageTabUI(
    nickname: String,
    isChecked: Boolean,
    selectedTime: String = "09:00",
    navigateToEditProfile: () -> Unit = {},
    onCheckedChanged: (Boolean) -> Unit = {},
    onTimeSelected: (String) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            "${nickname}님,\n안녕하세요!",
            style = MyPageTextStyle.TitleText,
            modifier = Modifier.padding(top = 85.dp, start = 23.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 34.dp, bottom = 30.dp, end = 21.dp)
                .clickable { navigateToEditProfile() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("내 정보 관리", style = MyPageTextStyle.MenuText)
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(R.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Color(0xFFF9F9F9))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 22.dp, top = 27.dp, bottom = 21.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("푸시 메시지 설정", style = MyPageTextStyle.MenuText)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChanged,
                modifier = Modifier
                    .scale(0.9f)
                    .size(49.dp, 26.dp)
                    .indication(interactionSource, null),
                interactionSource = interactionSource,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF444444),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFBBBBBB),
                    uncheckedBorderColor = Color(0xFFBBBBBB),
                ),
            )
        }

        val isDropdownExpanded = remember { mutableStateOf(false) }

        if (isChecked) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("시간", style = MyPageTextStyle.MenuText)
                Spacer(modifier = Modifier.weight(1f))
                Box {
                    Row(
                        modifier = Modifier
                            .clickable { isDropdownExpanded.value = true }
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedTime,
                            style = MyPageTextStyle.MenuText
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Image(
                            painter = painterResource(id = R.drawable.arrow_right), // Replace with dropdown icon
                            contentDescription = "Select time",
                            modifier = Modifier
                                .size(14.dp)
                                .padding(start = 4.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = isDropdownExpanded.value,
                        modifier = Modifier
                            .background(Color.White)
                            .height(300.dp),
                        properties = PopupProperties(
                            focusable = true,
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true,
                        ),
                        onDismissRequest = { isDropdownExpanded.value = false }
                    ) {
                        // 24 hours dropdown items
                        listOf(
                            "01:08", "01:09", "00:00", "01:00", "02:00", "03:00", "04:00",
                            "05:00", "06:00", "07:00", "08:00", "09:00",
                            "10:00", "11:00", "12:00", "13:00", "14:00",
                            "15:00", "16:00", "17:00", "18:00", "19:00",
                            "20:00", "21:00", "22:00", "23:00"
                        ).forEach { time ->
                            DropdownMenuItem(
                                modifier = Modifier.background(Color.White),
                                colors = MenuDefaults.itemColors(textColor = Color.Black),
                                text = { Text(time) },
                                onClick = {
                                    onTimeSelected(time)
                                    isDropdownExpanded.value = false
                                }
                            )
                        }
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Color(0xFFF9F9F9))
        )
        Spacer(modifier = Modifier.height(27.dp))
        MyPageMenuItem("공지사항")
        MyPageMenuItem("서비스 이용약관")
        MyPageMenuItem("개인정보 처리방침")
        MyPageMenuItem("1:1 문의")
    }
}

@Composable
fun MyPageMenuItem(
    text: String = "",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.height(26.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text, style = MyPageTextStyle.SubMenuText)
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
@Preview(showBackground = true)
fun MyPageTabUIPreview() {
    AppTheme { MyPageTabUI("닉네임", true) }
}