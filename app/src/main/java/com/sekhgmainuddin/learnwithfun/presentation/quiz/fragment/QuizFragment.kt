package com.sekhgmainuddin.learnwithfun.presentation.quiz.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions.ALL_CONTOURS
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions.FAST
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.enums.CheatingStatus
import com.sekhgmainuddin.learnwithfun.common.utils.CameraUtility
import com.sekhgmainuddin.learnwithfun.databinding.FragmentQuizBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.quiz.QuizViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QuizFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentQuizBinding? = null
    private val binding: FragmentQuizBinding
        get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private var cameraExecutor: ExecutorService? = null
    private val viewModel by activityViewModels<QuizViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz, container, false
        )

        return _binding!!.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()
        cameraExecutor = Executors.newSingleThreadExecutor()

        val displayMetrics = requireContext().resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels
        var dX = 0.0f
        var dY = 0.0f
        binding.cameraPreview.setOnTouchListener { dragView, event ->
            val newX: Float
            val newY: Float
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dX = dragView.x - event.rawX
                    dY = dragView.y - event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    newX = event.rawX + dX
                    newY = event.rawY + dY
                    if (newX <= 0 || newX >= screenWidth - dragView.width || newY <= 0 || newY >= screenHeight - dragView.height) {
                        return@setOnTouchListener true
                    }
                    dragView.x = newX
                    dragView.y = newY
                }

                else -> {
                    return@setOnTouchListener false
                }
            }
            true
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("firebaseMLKIT", "onPause: APP CHANGED")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cameraExecutor?.shutdown()
        cameraExecutor = null
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setFlashMode(ImageCapture.FLASH_MODE_OFF)
                .setJpegQuality(25)
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { imageAnalysis ->
                    Log.d("firebaseMLKIT", "startCamera: YES")
                    cameraExecutor?.let {
                        imageAnalysis.setAnalyzer(
                            it,
                            FaceCheatAnalyzer(),
                        )
                    }
                }
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("cameraXLog", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))

    }

    private class FaceCheatAnalyzer : ImageAnalysis.Analyzer {
        fun degreesToFirebaseRotation(degrees: Int): Int = when (degrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
        }

        @OptIn(ExperimentalGetImage::class)
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            val imageRotation =
                degreesToFirebaseRotation(imageProxy.imageInfo.rotationDegrees)
            if (mediaImage != null) {
                val image = FirebaseVisionImage.fromMediaImage(
                    mediaImage,
                    imageRotation
                )
                val options = FirebaseVisionFaceDetectorOptions.Builder()
                    .setPerformanceMode(FAST)
                    .setLandmarkMode(ALL_LANDMARKS)
                    .setContourMode(ALL_CONTOURS)
                    .setClassificationMode(ALL_CLASSIFICATIONS)
                    .build()
                val detector =
                    FirebaseVision.getInstance()
                        .getVisionFaceDetector(options)
                detector.detectInImage(image)
                    .addOnSuccessListener { faces ->
                        val cheatingStatus: CheatingStatus
                        if (faces == null || faces.isEmpty()) {
                            Log.d("firebaseMLKIT", "NO FACE DETECTED")
                            cheatingStatus = CheatingStatus.NO_FACE_DETECTED
                        } else if (faces.size > 1) {
                            Log.d(
                                "firebaseMLKIT",
                                "MULTIPLE FACE DETECTED"
                            )
                            cheatingStatus =
                                CheatingStatus.MULTIPLE_FACE_DETECTED
                        } else {
                            val rotY = faces[0].headEulerAngleY
                            cheatingStatus = if (rotY > 30f || rotY < -30f) {
                                Log.d(
                                    "firebaseMLKIT",
                                    "CHEATING DETECTED"
                                )
                                CheatingStatus.CHEATING_DETECTED
                            } else {
                                CheatingStatus.NO_CHEATING
                            }
                        }
//                        if (cheatingStatus != CheatingStatus.NO_CHEATING) {
//                            viewModel.triggerExamViolation(cheatingStatus)
//                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(500)
                            imageProxy.close()
                        }
                    }
                    .addOnFailureListener {
                        imageProxy.close()
                    }
            }
        }
    }

    private fun requestPermission() {

        if (CameraUtility.hasCameraPermissions(requireContext())) {
            startCamera()
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept the camera permission to use this app",
                141,
                Manifest.permission.CAMERA
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept the camera permission to use this app",
                141,
                Manifest.permission.CAMERA

            )
        }

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startCamera()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}