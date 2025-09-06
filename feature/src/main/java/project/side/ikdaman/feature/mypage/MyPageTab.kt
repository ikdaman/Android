@file:OptIn(ExperimentalMaterial3Api::class)

package project.side.ikdaman.feature.mypage

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import project.side.ikdaman.app.core.R.string
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.NOTICE_ROUTE
import project.side.ikdaman.core.navigation.USERINFO_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.utils.oneClick
import java.util.Locale

@Composable
fun MyPageTab(
    navController: NavController,
    onPermissionCheck: () -> Unit = {},
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    MyPageTabUI(
        nickname = uiState.nickname,
        isChecked = uiState.isChecked,
        selectedTime = uiState.selectedTime,
        navigateToEditProfile = { navController.navigate(USERINFO_ROUTE) },
        onCheckedChanged = {
            viewModel.toggleAlarm()
            onPermissionCheck()
        },
        onTimeSelected = { viewModel.updateSelectedTime(it) },
        navigateToNotice = { navController.navigate(NOTICE_ROUTE) },
        navigateToLink = { resId ->
            context.startActivity(
                Intent(Intent.ACTION_VIEW, context.getString(resId).toUri())
            )
        }
    )
}

@Composable
fun MyPageTabUI(
    nickname: String,
    isChecked: Boolean,
    selectedTime: String = "21:00",
    isTimeSelectOpened: MutableState<Boolean> = remember { mutableStateOf(false) },
    navigateToEditProfile: () -> Unit = {},
    onCheckedChanged: (Boolean) -> Unit = {},
    onTimeSelected: (String) -> Unit = {},
    navigateToNotice: () -> Unit = {},
    navigateToLink: (Int) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = 68.dp)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
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
                    .oneClick { navigateToEditProfile() },
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
                    Row(
                        modifier = Modifier
                            .clickable { isTimeSelectOpened.value = true }
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF5F5F5))
                            .padding(vertical = 5.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = selectedTime,
                            style = MyPageTextStyle.MenuText,
                        )
                        Spacer(Modifier.width(10.dp))
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.arrow_down),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
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
            MyPageMenuItem("공지사항", navigateToNotice)
            MyPageMenuItem("서비스 이용약관") { navigateToLink(string.url_terms_of_service) }
            MyPageMenuItem("개인정보 처리방침") { navigateToLink(string.url_privacy_policy) }
        }

        // BottomSheet 추가
        if (isTimeSelectOpened.value) {
            ModalBottomSheet(
                onDismissRequest = { isTimeSelectOpened.value = false },
                containerColor = Color.White,
                tonalElevation = 0.dp,
                modifier = Modifier.padding(bottom = 56.dp)
            ) {
                TimeSelectionBottomSheet(
                    selectedTime = selectedTime,
                    onTimeSelected = { time ->
                        onTimeSelected(time)
                        isTimeSelectOpened.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun TimeSelectionBottomSheet(
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    val times = (0..23).map { hour -> String.format(Locale.KOREA, "%02d:00", hour) }
    LazyColumn(contentPadding = PaddingValues(bottom = 20.dp, start = 12.dp, end = 12.dp)) {
        items(times.chunked(4)) { rowTimes ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowTimes.forEach { time ->
                    val isSelected = time == selectedTime
                    Box(
                        modifier = Modifier
                            .size(75.dp, 38.dp)
                            .clickable { onTimeSelected(time) }
                            .clip(RoundedCornerShape(5.dp))
                            .background(if (isSelected) Color.Black else Color.White)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.Black else Color(0xFFD3D3D3),
                                shape = RoundedCornerShape(5.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = time,
                            style = MyPageTextStyle.MenuText,
                            color = if (isSelected) Color.White else Color(0xFF444444)
                        )
                    }
                }
            }
        }
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
            .oneClick { onClick() },
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

@Composable
@Preview(showBackground = true)
fun TimeSelectionBottomSheetPreview() {
    AppTheme {
        TimeSelectionBottomSheet(
            selectedTime = "21:00",
            onTimeSelected = {}
        )
    }
}