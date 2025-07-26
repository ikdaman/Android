package project.side.ikdaman.feature.barcode

import android.graphics.Rect
import android.util.Log
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_ISBN
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private val TAG = "BarcodeScanner"

@ExperimentalCamera2Interop
@ExperimentalGetImage
class BarcodeScanner {
    private val _isbnFlow = MutableSharedFlow<String?>(1)
    val isbnFlow = _isbnFlow.asSharedFlow()

    private val barcodeScanner = BarcodeScanning.getClient()
    val executor: ExecutorService = Executors.newSingleThreadExecutor()

    val imageAnalysisBuilder = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)

    fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        if (barcode.valueType == TYPE_ISBN) {
                            barcode.rawValue?.let { value ->
                                Log.d(TAG, "processImageProxy: $value")
                                _isbnFlow.tryEmit(value)
                            }
//                            val boundingBox = barcode.boundingBox
//                            if (boundingBox != null) {
//                                // 바코드의 bounding box를 화면 크기 기준으로 변환
//                                val scaledBoundingBox = scaleBoundingBoxToScreen(
//                                    boundingBox,
//                                    imageWidth,
//                                    imageHeight,
//                                    canvasWidth,
//                                    canvasHeight
//                                )
//
//                                Log.d("hkhk", "scaledBoundingBox: $scaledBoundingBox")
//                                Log.d("hkhk", "focusRect: $focusRect")
//                                if (isBoundingBoxWithinFocusRect(scaledBoundingBox, focusRect)) {
//                                    barcode.rawValue?.let { value ->
//                                        _barcodeFlow.tryEmit(value)
//                                    }
//                                }
//                            }
                        }
                    }
                    imageProxy.close()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "processImageProxy: $e")
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun scaleBoundingBoxToScreen(
        boundingBox: Rect,
        imageWidth: Float,
        imageHeight: Float,
        canvasWidth: Float,
        canvasHeight: Float
    ): Rect {
        val scaleX = canvasWidth / imageWidth
        val scaleY = canvasHeight / imageHeight

        val scaledLeft = boundingBox.left * scaleX
        val scaledTop = boundingBox.top * scaleY
        val scaledRight = boundingBox.right * scaleX
        val scaledBottom = boundingBox.bottom * scaleY

        return Rect(scaledLeft.toInt(), scaledTop.toInt(), scaledRight.toInt(), scaledBottom.toInt())
    }

    private fun isBoundingBoxWithinFocusRect(boundingBox: Rect, focusRect: Rect): Boolean {
        return boundingBox.left >= focusRect.left &&
                boundingBox.top >= focusRect.top &&
                boundingBox.right <= focusRect.right &&
                boundingBox.bottom <= focusRect.bottom
    }
}
