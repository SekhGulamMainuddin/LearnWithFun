package com.sekhgmainuddin.learnwithfun.common.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.LinearGradient
import android.graphics.Shader
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.MediaColumns.*
import android.text.format.DateUtils
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.util.regex.Matcher
import java.util.regex.Pattern

object Utils {

    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS
    private const val WEEK_MILLIS = 7 * DAY_MILLIS
    private const val MONTH_MILLIS = 30L * DAY_MILLIS
    private const val YEAR_MILLIS = 365L * DAY_MILLIS

    fun Long.getTimeAgo(): String? {
        val now: Long = System.currentTimeMillis()
        if (this > now || this <= 0) {
            return null
        }

        val diff = now - this
        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " minutes ago"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hours ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else if (diff < WEEK_MILLIS) {
            (diff / DAY_MILLIS).toString() + " days ago"
        } else if (diff < MONTH_MILLIS) {
            (diff / WEEK_MILLIS).toString() + " weeks ago"
        } else if (diff < 2L * YEAR_MILLIS) {
            "1 year ago"
        } else {
            (diff / YEAR_MILLIS).toString() + " years ago"
        }
    }

    fun Long.getMessageTIme(context: Context): String? {
        val time = DateUtils.formatDateTime(context, this, DateUtils.FORMAT_SHOW_TIME)
        val date = DateUtils.formatDateTime(context, this, DateUtils.FORMAT_SHOW_DATE)
        return time
    }


    fun View.slideVisibility(visibility: Boolean, durationTime: Long = 300) {
        val transition = Slide(Gravity.START)
        transition.apply {
            duration = durationTime
            addTarget(this@slideVisibility)
        }
        TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
        this.isVisible = visibility
    }

    fun TextView.changeTextColorGradient(colorArray: IntArray) {
        this.apply {
            val context = this.context
            this.setTextColor(context.getColor(colorArray[0]))
            val textShader: Shader = LinearGradient(
                0f,
                0f,
                paint.measureText(text.toString()),
                textSize,
                colorArray,
                floatArrayOf(0f, 1f, 2f),
                Shader.TileMode.REPEAT
            )
            paint.shader = textShader
        }
    }

    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun String.isValidPassword(): Boolean {
        val PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!/'|*.,<>;:()_`~?])(?=\\S+$).{8,}$"
        val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher? = this.let { pattern.matcher(it) }
        return matcher?.matches() == true
    }

    fun getFileExtension(uri: Uri, context: Context): String? {
        val contentResolver: ContentResolver = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    fun Uri.getBitmap(contentResolver: ContentResolver): Bitmap? = this.let {
        if (SDK_INT < 28) {
            return MediaStore.Images.Media.getBitmap(
                contentResolver,
                this
            )
        } else {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, this))
        }
    }

    fun Bitmap.saveAsJPG(filename: String, applicationContext: Context) =
        "$filename.jpg".let { name ->
            if (SDK_INT < Q)
                @Suppress("DEPRECATION")
                FileOutputStream(
                    File(
                        applicationContext.getExternalFilesDir(DIRECTORY_PICTURES),
                        name
                    )
                )
                    .use { compress(Bitmap.CompressFormat.JPEG, 90, it) }
            else {
                val values = ContentValues().apply {
                    put(DISPLAY_NAME, name)
                    put(MIME_TYPE, "image/jpg")
                    put(RELATIVE_PATH, DIRECTORY_PICTURES)
                    put(IS_PENDING, 1)
                }

                val resolver = applicationContext.contentResolver
                val uri = resolver.insert(EXTERNAL_CONTENT_URI, values)
                uri?.let { resolver.openOutputStream(it) }
                    ?.use { compress(Bitmap.CompressFormat.JPEG, 70, it) }

                values.clear()
                values.put(IS_PENDING, 0)
                uri?.also {
                    resolver.update(it, values, null, null)
                }
            }
        }

    private val imageExtensionList = arrayListOf(
        "jpg",
        "jpeg",
        "jpe",
        "jif",
        "jfif",
        "jfi",
        "gif",
        "webp",
        "tiff",
        "tif",
        "psd",
        "bmp",
        "svg",
        "svgz",
        "pdf"
    )
    private val videoExtensionList = arrayListOf("mp4")

    fun String.isImageOrVideo(): Int {
        if (this in imageExtensionList)
            return 0
        else if (this in videoExtensionList)
            return 1
        else
            return -1
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @Throws(Throwable::class)
    fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(
                videoPath,
                HashMap()
            )
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }

}