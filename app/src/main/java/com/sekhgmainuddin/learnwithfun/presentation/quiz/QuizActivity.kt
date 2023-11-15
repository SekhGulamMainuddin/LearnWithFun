package com.sekhgmainuddin.learnwithfun.presentation.quiz

import android.Manifest
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.activity.viewModels
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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import com.sekhgmainuddin.learnwithfun.data.db.entities.CheatFlagEntity
import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.ContentDto
import com.sekhgmainuddin.learnwithfun.databinding.ActivityQuizBinding
import com.sekhgmainuddin.learnwithfun.databinding.SubmittingQuizLayoutBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity
import com.sekhgmainuddin.learnwithfun.presentation.quiz.uiStates.QuizState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QuizActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityQuizBinding
    private var imageCapture: ImageCapture? = null
    private var cameraExecutor: ExecutorService? = null
    private val viewModel by viewModels<QuizViewModel>()
    private var contentDetailDto: ContentDto? = null
    private var examId: String? = null
    private var quizCompleted = false
    private lateinit var quizDialog: Dialog
    private lateinit var dialogBinding: SubmittingQuizLayoutBinding
    private lateinit var cheatingAlertDialog: MaterialAlertDialogBuilder
    private var isCheatDialogShowing = false
    private var cheatingAttempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)

        contentDetailDto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("content", ContentDto::class.java)
        } else {
            intent.getParcelableExtra<ContentDto>("content")
        }
        examId = intent.getStringExtra("examId")
        viewModel.examId = examId!!
        viewModel.courseId = intent.getStringExtra("courseId")!!

        if (contentDetailDto != null) {
            viewModel.setQuestions(contentDetailDto!!)
        } else {
            setResult(-1)
            finish()
        }

        cheatingAlertDialog = MaterialAlertDialogBuilder(
            this
        )
            .setTitle(R.string.cheating_detected)
            .setMessage(R.string.please_dont_violate_exam_rules)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                cheatingAttempts = 0
                isCheatDialogShowing = false
                dialog.dismiss()
            }
            .setCancelable(false)

        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.submitting_quiz_layout,
            null,
            false
        )
        dialogBinding.lifecycleOwner = this
        dialogBinding.viewModel = viewModel
        quizDialog =
            Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen).apply {
                setContentView(dialogBinding.root)
                setCancelable(false)
            }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        requestPermission()
        cameraExecutor = Executors.newSingleThreadExecutor()

        val displayMetrics = resources.displayMetrics
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
                    dragView.performClick()
                }

                MotionEvent.ACTION_MOVE -> {
                    newX = event.rawX + dX
                    newY = event.rawY + dY
                    if (newX <= 0 || newX >= screenWidth - dragView.width || newY <= 0 || newY >= screenHeight - dragView.height) {
                        return@setOnTouchListener true
                    }
                    dragView.performClick()
                    dragView.x = newX
                    dragView.y = newY
                }

                else -> {
                    return@setOnTouchListener false
                }
            }
            true
        }
        bindObservers()
    }

    private fun bindObservers() {
        lifecycleScope.launch {
            launch {
                viewModel.quizStates.collect {
                    when (it) {
                        QuizState.Initial -> {}

                        QuizState.Completed -> {
                            quizDialog.show()
                            showToast(R.string.quiz_submitted_successfully)
                            delay(1500)
                            quizDialog.dismiss()
                            finish()
                        }

                        is QuizState.Error -> {
                            quizDialog.dismiss()
                            if (it.message.isEmpty()) {
                                showToast(getString(it.messageRes))
                            } else {
                                showToast(it.message)
                            }
                        }

                        QuizState.NextQuestion -> {
                            quizDialog.dismiss()
                        }

                        QuizState.SubmittingAnswer -> {
                            quizDialog.show()
                        }
                    }
                }
            }
            launch {
                viewModel.cheatingStates.collectLatest {
                    cheatingAttempts++
                    if (it != CheatingStatus.NO_CHEATING && cheatingAttempts != 3) {
                        cheatingAlertDialog.setTitle(it.name.replace('_', ' ').lowercase().replaceFirstChar(Char::uppercase))
                        if (!isCheatDialogShowing) {
                            isCheatDialogShowing = true
                            cheatingAlertDialog.show()
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (!quizCompleted) {
            viewModel.triggerExamViolation(
                CheatFlagEntity(
                    System.currentTimeMillis(),
                    examId!!,
                    CheatingStatus.EXAM_WINDOW_CHANGED_DURING_TEST.name,
                    null,
                    null
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor?.shutdown()
        cameraExecutor = null
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
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
        }, ContextCompat.getMainExecutor(this))

    }

    private inner class FaceCheatAnalyzer : ImageAnalysis.Analyzer {
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
            val bitmapImage = imageProxy.toBitmap()
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
                        if (cheatingStatus != CheatingStatus.NO_CHEATING) {
                            viewModel.triggerExamViolation(
                                CheatFlagEntity(
                                    System.currentTimeMillis(),
                                    examId!!,
                                    cheatingStatus.name,
                                    bitmapImage,
                                    null
                                )
                            )
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1000)
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

        if (CameraUtility.hasCameraPermissions(this)) {
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