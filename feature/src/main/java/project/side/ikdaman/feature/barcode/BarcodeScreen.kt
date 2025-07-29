@file:kotlin.OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package project.side.ikdaman.feature.barcode

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import project.side.ikdaman.core.navigation.ADD_BOOK_ROUTE
import project.side.ikdaman.core.navigation.FromWhere
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.PretendardFontFamily
import project.side.ikdaman.core.utils.oneClick
import project.side.ikdaman.core.view.AddBookButton
import project.side.ikdaman.core.view.CustomModalBottomSheet
import project.side.ikdaman.domain.model.BookItem

private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun BarcodeScreen(
    navController: NavController,
    fromWhere: String = FromWhere.FROM_MAIN,
    viewModel: BarcodeViewModel = hiltViewModel(
        navController.getBackStackEntry(MAIN_ROUTE)
    )
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var isPermissionGranted by remember { mutableStateOf(false) }
    val isbn by viewModel.isbn.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> isPermissionGranted = granted }

    val barcodeScanner = remember {
        BarcodeScanner()
    }

    LaunchedEffect(Unit) {
        isPermissionGranted = checkPermission(context = context)
    }

    LaunchedEffect(isbn) {
        Log.d("BarcodeScreen", "isbn: $isbn")
        viewModel.searchBookWithIsbn(isbn)
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
        withContext(Dispatchers.Default) {
            barcodeScanner.isbnFlow.collect { value ->
                if (value != null) {
                    viewModel.updateIsbn(value)
                }
            }
        }
    }

    BarcodeScreenUI(
        onBack = {
            navController.popBackStack()
        },
        isPermissionGranted = isPermissionGranted,
        lifecycleOwner = lifecycleOwner,
        cameraProvider = cameraProvider,
        bookItem = searchResult,
        barcodeScanner = barcodeScanner,
        onNavigateToAddBookScreen = {
            navController.navigate("$ADD_BOOK_ROUTE/$it")
        },
        onAddBook = {
            viewModel.addBook {
                if (fromWhere == FromWhere.FROM_SEARCH) {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "navigateToHome",
                        true
                    )
                }
                navController.popBackStack()
            }
        },
        onDismissDialog = {
            viewModel.resetIsbn()
            viewModel.resetSearchResult()
        },
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

@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun BarcodeScreenUI(
    onBack: () -> Unit = {},
    isPermissionGranted: Boolean? = null,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraProvider: ProcessCameraProvider? = null,
    bookItem: BookItem? = null,
    barcodeScanner: BarcodeScanner,
    onNavigateToAddBookScreen: (String) -> Unit = {},
    onAddBook: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .statusBarsPadding()
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
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
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        if (isPermissionGranted == null) return@Scaffold

        bookItem?.let {
            BarcodeResultBottomSheet(
                onDismissDialog = onDismissDialog,
                bookItem = it,
                onNavigateToAddBookScreen = onNavigateToAddBookScreen,
                onAddBook = onAddBook
            )
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


@OptIn(ExperimentalCamera2Interop::class)
@Composable
private fun CameraScreen(
    modifier: Modifier = Modifier,
    cameraProvider: ProcessCameraProvider? = null,
    lifecycleOwner: LifecycleOwner,
    barcodeScanner: BarcodeScanner,
) {
    if (cameraProvider == null) return

    // DisposableEffect를 사용하여 화면 이탈 시 카메라 해제
    DisposableEffect(cameraProvider, lifecycleOwner) {
        onDispose {
            try {
                cameraProvider.unbindAll()
            } catch (e: Exception) {
                Log.e("BarcodeScreen", "Camera unbind failed: ${e.message}")
            }
        }
    }

    Box(modifier) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                bindCamera(previewView, cameraProvider, lifecycleOwner, barcodeScanner)
                previewView
            },
            update = { view ->
                bindCamera(view, cameraProvider, lifecycleOwner, barcodeScanner)
            },
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, Color(0xFFFFD900))
                .background(Color.Black)
        )
    }

}

@OptIn(ExperimentalCamera2Interop::class)
private fun bindCamera(
    previewView: PreviewView,
    cameraProvider: ProcessCameraProvider,
    lifecycleOwner: LifecycleOwner,
    barcodeScanner: BarcodeScanner
) {
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
            barcodeScanner.imageAnalysisBuilder.build()
                .also { analysis ->
                    analysis.setAnalyzer(barcodeScanner.executor) { imageProxy ->
                        barcodeScanner.processImageProxy(imageProxy)
                    }
                }
        )
    } catch (e: Exception) {
        Log.e("BarcodeScreen", "bindToLifecycle failed: ${e.message}", e)
    }
}

@Composable
private fun NoCameraScreen(modifier: Modifier = Modifier) {
    Text(
        text = "바코드 스캔을 위해 카메라 권한을 허용해 주세요",
        modifier = modifier
    )
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeResultBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismissDialog: () -> Unit,
    bookItem: BookItem,
    onNavigateToAddBookScreen: (String) -> Unit,
    onAddBook: () -> Unit = {}
) {
    CustomModalBottomSheet(
        sheetState = sheetState,
        onDismiss = onDismissDialog,
        content = { modifier ->
            Column(
                modifier
                    .padding(horizontal = 20.dp, vertical = 25.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier.size(26.dp),
                        onClick = {
                            onDismissDialog()
                        }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "닫기")
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .oneClick {
                            onNavigateToAddBookScreen(bookItem.isbn)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 책 썸네일
                    AsyncImage(
                        model = bookItem.cover,
                        contentDescription = "책 썸네일",
                        modifier = Modifier
                            .size(width = 80.dp, height = 114.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier
                            .height(114.dp)
                            .fillMaxWidth()
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
                        Spacer(Modifier.weight(1f))
                        AddBookButton(
                            modifier = Modifier.align(Alignment.End),
                            onClick = onAddBook
                        )
                    }
                }
            }
        }
    )
}

private fun checkPermission(context: Context): Boolean =
    context.checkSelfPermission(CAMERA_PERMISSION) == PERMISSION_GRANTED

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun BarcodeResultBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    LaunchedEffect(Unit) {
        sheetState.show()
    }
    BarcodeResultBottomSheet(
        sheetState = sheetState,
        onDismissDialog = {},
        bookItem = BookItem(
            title = "test",
            author = "test",
            cover = "https://image.aladin.co.kr/product/4086/97/coversum/8936434128_2.jpg",
            isbn = "test",
            itemId = 0,
            link = "test",
            publisher = "test",
            subInfo = null
        ),
        onNavigateToAddBookScreen = {}
    )
}