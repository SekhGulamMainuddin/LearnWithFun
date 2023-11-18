package com.sekhgmainuddin.learnwithfun.common.binding_adapters

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.ITALIC
import android.graphics.Typeface.NORMAL
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.GlideImageLoader
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getCurrentPrice
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getViewsOrLikesCount
import com.sekhgmainuddin.learnwithfun.presentation.payment.uiStates.PaymentState
import kotlin.random.Random


@BindingAdapter(
    "app:imageUrl",
    "app:progressBar",
    "app:placeholder",
    "app:errorImage",
    requireAll = false
)
fun setImageUrl(
    view: ImageView,
    imageUrl: String?,
    progressBar: ProgressBar?,
    placeholder: Int = R.drawable.placeholder_image,
    errorImage: Int = R.drawable.image_error
) {
    val options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(placeholder)
        .error(errorImage)
        .priority(Priority.HIGH)

    GlideImageLoader(
        view,
        progressBar
    ).load(imageUrl, options)
}

@BindingAdapter(
    "app:full_text",
    "app:span_text",
    "app:span_color",
    "app:non_span_style",
    requireAll = false
)
fun formatText(
    textView: TextView,
    fullText: String,
    spanText: String,
    spanColor: Int,
    nonSpanStyle: String? = null
) {
    val firstMatchingIndex = fullText.indexOf(spanText)
    val lastMatchingIndex = firstMatchingIndex + spanText.length
    val spannable = SpannableString(fullText)
    if (nonSpanStyle != null) {
        spannable.setSpan(
            StyleSpan(if (nonSpanStyle == "bold") BOLD else if (nonSpanStyle == "italic") ITALIC else NORMAL),
            lastMatchingIndex + 1,
            fullText.length - 1,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }
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
    val paddingFiveDp =
        view.context.resources.getDimensionPixelOffset(R.dimen.padding_if_icon_present)
    val paddingTenDp =
        view.context.resources.getDimensionPixelOffset(R.dimen.padding_if_icon_not_present)
    if (drawable != null) {
        view.updatePadding(left = paddingFiveDp, right = paddingFiveDp)
    } else {
        view.updatePadding(left = paddingTenDp, right = paddingTenDp)
    }
}

@BindingAdapter("app:imageDrawable")
fun setImageDrawable(view: ImageView, imageDrawable: Int) {
    Glide.with(view.context).load(AppCompatResources.getDrawable(view.context, imageDrawable))
        .placeholder(R.color.white).into(view)
}

@BindingAdapter("app:customRawRes", "app:customLoop")
fun setLottieAnimation(view: LottieAnimationView, customRawRes: Int, customLoop: Boolean) {
    view.setAnimation(customRawRes)
    if (customLoop) {
        view.repeatCount = LottieDrawable.INFINITE
    }
    view.playAnimation()
}

@BindingAdapter("app:makeSelectedAutomatically")
fun makeSelectedAutomatically(view: TextView, makeSelectedAutomatically: Boolean) {
    view.isSelected = makeSelectedAutomatically
}

@BindingAdapter("app:viewsCount")
fun setViewsCount(view: TextView, views: Int) {
    view.text =
        "${getViewsOrLikesCount(views)} ${view.context.getString(if (views > 1) R.string.views else R.string.view)}"
}

@BindingAdapter("app:likesCount")
fun setLikesCount(view: TextView, likes: Int) {
    view.text =
        "${getViewsOrLikesCount(likes)} ${view.context.getString(if (likes > 1) R.string.likes else R.string.like)}"
}

@BindingAdapter("app:strikeThrough")
fun setStrikeThrough(view: TextView, strikeThrough: Boolean) {
    view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

@BindingAdapter("app:studentsEnrolled")
fun setStudentsEnrolled(view: TextView, studentsEnrolled: Int) {
    val studentOrStudents =
        view.context.getString(if (studentsEnrolled > 1) R.string.students else R.string.student)
    view.text =
        "${getViewsOrLikesCount(studentsEnrolled)} $studentOrStudents ${view.context.getString(R.string.enrolled)}"
    formatText(
        view,
        view.text.toString(),
        getViewsOrLikesCount(studentsEnrolled),
        view.context.getColor(R.color.black),
        "normal"
    )
}

@BindingAdapter("app:actualPriceOfCourse", "app:givenDiscount")
fun setCurrentPrice(view: TextView, actualPriceOfCourse: Double, givenDiscount: Double) {
    view.text =
        view.context.getString(R.string.price, getCurrentPrice(actualPriceOfCourse, givenDiscount))
}

@BindingAdapter("app:animationForPaymentState")
fun setAnimationForPaymentState(
    view: LottieAnimationView,
    animationForPaymentState: PaymentState?
) {
    val animationRes = when (animationForPaymentState) {
        PaymentState.Initial -> {
            R.raw.initiating_payment
        }

        PaymentState.FetchingToken -> {
            R.raw.initiating_payment
        }

        PaymentState.DoingPayment -> {
            R.raw.doing_payment
        }

        is PaymentState.Error -> {
            R.raw.payment_failed
        }

        is PaymentState.PaymentTokenFetched -> {
            R.raw.doing_payment
        }

        PaymentState.PaymentVerified -> {
            R.raw.payment_successful_animation
        }

        PaymentState.VerifyingPayment -> {
            R.raw.verifying_your_payment
        }

        else -> {
            R.raw.loading_animation
        }
    }
    setLottieAnimation(view, animationRes, true)
}

@BindingAdapter("app:textForPaymentState")
fun setTextForPaymentState(view: TextView, textForPaymentState: PaymentState?) {
    val text = when (textForPaymentState) {
        PaymentState.Initial -> {
            R.string.initiating_payment
        }

        PaymentState.FetchingToken -> {
            R.string.fetching_payment_token
        }

        PaymentState.DoingPayment -> {
            R.string.starting_payment
        }

        is PaymentState.Error -> {
            R.string.payment_failed
        }

        is PaymentState.PaymentTokenFetched -> {
            R.string.payment_token_fetched
        }

        PaymentState.PaymentVerified -> {
            R.string.payment_done
        }

        PaymentState.VerifyingPayment -> {
            R.string.verifying_payment
        }

        else -> {
            R.string.loading
        }
    }
    view.text = view.context.getString(text)
}

@BindingAdapter("app:progressText")
fun setProgressText(view: TextView, progressText: Int) {
    if (progressText != 0) {
        val valueAnimator = ValueAnimator.ofInt(0, progressText)
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener {
            view.text = it.animatedValue.toString()
        }
        valueAnimator.start()
    }
}

@BindingAdapter("app:animatedProgress", "app:animationDuration", requireAll = false)
fun setAnimatedProgress(
    view: CircularProgressIndicator,
    animatedProgress: Int,
    animationDuration: Long = 1000
) {
    if (animatedProgress != 0) {
        ObjectAnimator.ofInt(view, "progress", animatedProgress)
            .setDuration(animationDuration)
            .start()
    }
}

@BindingAdapter("app:customBackgroundTint")
fun setCustomBackgroundTint(view: FloatingActionButton, color: Int) {
    view.backgroundTintList = ColorStateList.valueOf(view.context.getColor(color))
}