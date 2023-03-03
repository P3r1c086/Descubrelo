package com.pedroaguilar.amigodeviaje.modelo

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.LinearProgressIndicator

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

@BindingAdapter("setInt")
fun LinearProgressIndicator.setInt(value: Int) {
    progress = value
}
@BindingAdapter("setString")
fun TextView.setString(value: String?) {
    text = value
}
