package project.side.ikdaman.feature.barcode

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import project.side.ikdaman.core.navigation.BOOK_EDIT_ROUTE
import project.side.ikdaman.core.ui.AppTheme

private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

@Composable
fun BarcodeScreen(navController: NavController) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var isPermissionGranted by remember { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isPermissionGranted = granted
    }

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
        cameraProvider = cameraProvider
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

@Composable
fun BarcodeScreenUI(
    onBack: () -> Unit = {},
    onNavigateToEditScreen: () -> Unit = {},
    isPermissionGranted: Boolean? = null,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraProvider: ProcessCameraProvider? = null
) {
    Scaffold(
        topBar = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    ) { innerPadding ->
        if (isPermissionGranted == null) return@Scaffold

        if (isPermissionGranted == true) {
            CameraScreen(
                lifecycleOwner = lifecycleOwner,
                cameraProvider = cameraProvider
            )
        } else {
            NoCameraScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun CameraScreen(
    cameraProvider: ProcessCameraProvider? = null,
    lifecycleOwner: LifecycleOwner
) {
    if (cameraProvider == null) return

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun NoCameraScreen(modifier: Modifier = Modifier) {
    Text(
        text = "바코드 스캔을 위해 카메라 권한을 허용해 주세요",
        modifier = modifier
    )
}

private fun checkPermission(context: Context): Boolean =
    context.checkSelfPermission(CAMERA_PERMISSION) == PERMISSION_GRANTED

@Composable
@Preview(showBackground = true)
fun BarcodeScreenUIPreview() {
    AppTheme { BarcodeScreenUI() }
}