package com.sekhgmainuddin.learnwithfun.common.binding_adapters

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.GlideImageLoader

@BindingAdapter("app:imageResource", "app:progressBar")
fun setImageResource(view: ImageView, imageResource: String, progressBar: ProgressBar) {
    val options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.image_error)
        .priority(Priority.HIGH)

    GlideImageLoader(
        view,
        progressBar
    ).load(imageResource, options)
}

@BindingAdapter("app:full_text", "app:span_text", "app:span_color")
fun formatText(textView: TextView, fullText: String, spanText: String, spanColor: Int) {
    val firstMatchingIndex = fullText.indexOf(spanText)
    val lastMatchingIndex = firstMatchingIndex + spanText.length
    val spannable = SpannableString(fullText)
    spannable.setSpan(
        ForegroundColorSpan(spanColor),
        firstMatchingIndex,
        lastMatchingIndex,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    textView.text = spannable
}

@BindingAdapter("app:customLLPadding")
fun setCustomLLPadding(view: LinearLayout, drawable: Drawable?) {
    val paddingFiveDp = view.context.resources.getDimensionPixelOffset(R.dimen.padding_if_icon_present)
    val paddingTenDp = view.context.resources.getDimensionPixelOffset(R.dimen.padding_if_icon_not_present)
    if (drawable != null) {
        view.updatePadding(left = paddingFiveDp, right = paddingFiveDp)
    } else {
        view.updatePadding(left = paddingTenDp, right = paddingTenDp)
    }
}