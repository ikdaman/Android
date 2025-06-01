package project.side.ikdaman.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import project.side.ikdaman.core.navigation.BARCODE_ROUTE
import project.side.ikdaman.core.navigation.SEARCH_ROUTE
import project.side.ikdaman.core.ui.AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookModalBottomSheet(
    mainNavController: NavController,
    appNavController: NavController,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        tonalElevation = 5.dp,
        containerColor = Color.White,
        dragHandle = null,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 25.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "책 등록하기",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 26.sp
                    )
                )
                IconButton(
                    modifier = Modifier.size(26.dp),
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Icon(Icons.Default.Close, contentDescription = "닫기")
                }
            }

            Spacer(Modifier.height(15.dp))

            AppText(
                text = "검색해서 등록",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 26.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        mainNavController.navigate(SEARCH_ROUTE)
                        onDismiss()
                    }
            )
            Spacer(Modifier.height(15.dp))

            AppText(
                text = "바코드로 등록",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 26.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        appNavController.navigate(BARCODE_ROUTE)
                        onDismiss()
                    }
            )
        }
    }
}

@Composable
@Preview
private fun AddBookModalBottomSheetPreview() {
    val navController = rememberNavController()
    AddBookModalBottomSheet(
        navController,
        navController
    ) {}
}
