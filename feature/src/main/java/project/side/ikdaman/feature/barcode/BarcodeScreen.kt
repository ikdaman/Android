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
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.BOOK_EDIT_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.PretendardFontFamily

private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

@OptIn(ExperimentalGetImage::class)
@Composable
fun BarcodeScreen(navController: NavController) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var isPermissionGranted by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> isPermissionGranted = granted }

    LaunchedEffect(Unit) {
        isPermissionGranted = checkPermission(context = context)
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

        if (isPermissionGranted == true) {
            CameraScreen(
                lifecycleOwner = lifecycleOwner,
                cameraProvider = cameraProvider,
            )
        } else {
            NoCameraScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
private fun CameraScreen(
    cameraProvider: ProcessCameraProvider? = null,
    lifecycleOwner: LifecycleOwner,
) {
    if (cameraProvider == null) return

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
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

        Log.d("hkhk", "Compose focusRect: $focusRect")

        val barcodeScanner = remember {
            BarcodeScanner(
                focusRect = android.graphics.Rect(
                    leftPx.toInt(),
                    topPx.toInt(),
                    (leftPx + focusWidthPx).toInt(),
                    (topPx + focusHeightPx).toInt()
                ),
                screenWidth = canvasWidth,
                screenHeight = canvasHeight
            )
        }

        LaunchedEffect(Unit) {
            Log.d("hkhk", "CameraScreen: 초기 실행")
            barcodeScanner.barcodeFlow.collect { value ->
                Log.d("hkhk", "CameraScreen: $value")
            }
        }

        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                val preview = androidx.camera.core.Preview.Builder().build().also {
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