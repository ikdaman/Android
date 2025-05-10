package project.side.ikdaman.feature.barcode

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.BOOK_EDIT_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.PretendardFontFamily
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.model.BookSearch

private val TAG = "BarcodeScreen"
private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

@OptIn(ExperimentalGetImage::class)
@Composable
fun BarcodeScreen(
    navController: NavController,
    viewModel: BarcodeViewModel = hiltViewModel(
        navController.getBackStackEntry(MAIN_ROUTE)
    )
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var isPermissionGranted by remember { mutableStateOf(false) }
    val isbn = viewModel.isbn.collectAsStateWithLifecycle()
    val searchResult = viewModel.searchResult.collectAsStateWithLifecycle()

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> isPermissionGranted = granted }

    val barcodeScanner = remember {
        BarcodeScanner()
    }

    LaunchedEffect(Unit) {
        isPermissionGranted = checkPermission(context = context)
    }

    LaunchedEffect(isbn.value) {
        viewModel.searchBookWithIsbn(isbn.value)
    }

    LaunchedEffect(isPermissionGranted) {
        if (isPermissionGranted) {
            initCameraProvider(
                context = context,
                onCameraProviderInit = { cameraProvider = it }
            )
        } else {
            cameraPermissionLauncher.launch(CAMERA_PERMISSION)
        }
    }

    LaunchedEffect(Unit) {
        barcodeScanner.isbnFlow.collect { value ->
            if (value != null) {
                viewModel.updateIsbn(value)
            }
        }
    }

    BarcodeScreenUI(
        onBack = {
            navController.popBackStack()
        },
        onNavigateToEditScreen = {
            navController.navigate(BOOK_EDIT_ROUTE)
        },
        isPermissionGranted = isPermissionGranted,
        lifecycleOwner = lifecycleOwner,
        cameraProvider = cameraProvider,
        searchResult = searchResult.value,
        onDismissDialog = {
            Log.d(TAG, "Dismiss Dialog")
            viewModel.resetIsbn()
        },
        barcodeScanner = barcodeScanner
    )
}

private fun initCameraProvider(
    context: Context,
    onCameraProviderInit: (ProcessCameraProvider) -> Unit,
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener(
        { onCameraProviderInit(cameraProviderFuture.get()) },
        ContextCompat.getMainExecutor(context)
    )
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun BarcodeScreenUI(
    onBack: () -> Unit = {},
    onNavigateToEditScreen: () -> Unit = {},
    isPermissionGranted: Boolean? = null,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraProvider: ProcessCameraProvider? = null,
    searchResult: BookSearch? = null,
    onDismissDialog: () -> Unit = {},
    barcodeScanner: BarcodeScanner
) {
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Back",
                        modifier = Modifier.size(26.dp),
                        tint = Color.White
                    )
                }
                Text(
                    text = "바코드 스캔하기",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 15.dp),
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                )
            }
        }
    ) { innerPadding ->
        if (isPermissionGranted == null) return@Scaffold

        searchResult?.let { result ->
            if (result.books.isNotEmpty()) {
                BookBottomSheetDialog(
                    bottomPaddingValues = innerPadding,
                    bookItem = result.books[0],
                    onAddBookClick = {},
                    onDismiss = onDismissDialog
                )
            }

        }

        if (isPermissionGranted == true) {
            CameraScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                lifecycleOwner = lifecycleOwner,
                cameraProvider = cameraProvider,
                barcodeScanner = barcodeScanner
            )
        } else {
            NoCameraScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
private fun CameraScreen(
    modifier: Modifier = Modifier,
    cameraProvider: ProcessCameraProvider? = null,
    lifecycleOwner: LifecycleOwner,
    barcodeScanner: BarcodeScanner
) {
    if (cameraProvider == null) return

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                try {
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        barcodeScanner.imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.e("BarcodeScreen", "bindToLifecycle failed: ${e.message}", e)
                }


                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        val canvasWidth = constraints.maxWidth
        val canvasHeight = constraints.maxHeight

        val focusWidthPx = with(LocalDensity.current) { 362.dp.toPx() }
        val focusHeightPx = with(LocalDensity.current) { 245.dp.toPx() }

        val leftPx = (canvasWidth - focusWidthPx) / 2
        val topPx = (canvasHeight - focusHeightPx) / 2

        val focusRect = Rect(
            left = leftPx,
            top = topPx,
            right = (leftPx + focusWidthPx),
            bottom = (topPx + focusHeightPx)
        )

        BarcodeOverlay(
            modifier = Modifier.fillMaxSize(),
            focusRect = focusRect
        )
    }

}

@Composable
private fun NoCameraScreen(modifier: Modifier = Modifier) {
    Text(
        text = "바코드 스캔을 위해 카메라 권한을 허용해 주세요",
        modifier = modifier
    )
}

@Composable
fun BarcodeOverlay(
    modifier: Modifier = Modifier,
    focusRect: Rect
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
    ) {

        // 중앙 포커스 영역만 투명하게
        Canvas(
            modifier = modifier
                .matchParentSize()
        ) {
            //중앙 부분을 지우기
            drawRect(
                color = Color.Transparent,
                topLeft = Offset(focusRect.left, focusRect.top),
                size = Size(focusRect.width, focusRect.height),
                blendMode = BlendMode.Clear // <<< 이게 핵심
            )

            // 테두리
            drawRect(
                color = Color(0xFFFFD900),
                topLeft = Offset(focusRect.left, focusRect.top),
                size = Size(focusRect.width, focusRect.height),
                style = Stroke(width = 2f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 190.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "바코드를 영역에 맞춰 보세요",
                color = Color.White,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "원하는 책을 빠르게 찾을 수 있어요",
                color = Color.White,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            )
        }
    }

}

private fun checkPermission(context: Context): Boolean =
    context.checkSelfPermission(CAMERA_PERMISSION) == PERMISSION_GRANTED

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookBottomSheetDialog(
    bottomPaddingValues: PaddingValues,
    bookItem: BookItem,
    onAddBookClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 책 썸네일
            AsyncImage(
                model = bookItem.cover,
                contentDescription = "책 썸네일",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 책 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = bookItem.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = bookItem.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // "이 책 추가" 버튼
            Button(
                onClick = onAddBookClick,
                modifier = Modifier.height(36.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text(text = "이 책 추가 +")
            }
        }
    }
}
//@OptIn(ExperimentalGetImage::class)
//@Composable
//@Preview(showBackground = true)
//fun BarcodeScreenUIPreview() {
//    AppTheme {
//        BarcodeScreenUI(
//            onBack = {},
//            onNavigateToEditScreen = {},
//            isPermissionGranted = true,
//            lifecycleOwner = LocalLifecycleOwner.current,
//            barcodeScanner = BarcodeScanner()
//        )
//    }
//}

//@Composable
//@Preview(showBackground = true, apiLevel = 31)
//fun BarcodeOverlayPreview() {
//    AppTheme {
//        Box(modifier = Modifier.fillMaxSize()) {
//            Image(
//                painter = painterResource(R.drawable.sample_book_cover2),
//                contentDescription = null,
//                modifier = Modifier.fillMaxSize()
//            )
//            BarcodeOverlay(Modifier.fillMaxSize())
//        }
//    }
//}