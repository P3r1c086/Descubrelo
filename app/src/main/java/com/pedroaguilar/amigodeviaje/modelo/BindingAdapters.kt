package com.pedroaguilar.amigodeviaje.common

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.pedroaguilar.amigodeviaje.modelo.loadUrl

@BindingAdapter("url")
fun ImageView.bindUrl(url: String?) {
    if (url != null) loadUrl(url)
}

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}
@BindingAdapter("enabled")
fun Button.enabled(enabled: Boolean?) {
    if (enabled == true) Button.VISIBLE else Button.INVISIBLE
}

