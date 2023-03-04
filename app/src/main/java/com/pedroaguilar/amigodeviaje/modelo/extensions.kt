package com.pedroaguilar.amigodeviaje.modelo

import android.widget.ImageView
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.pedroaguilar.amigodeviaje.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun ImageView.loadUrl(url: String?) {
    Glide.with(context).load(url).error(R.mipmap.ic_launcher).into(this)
}

fun <T> Lifecycle.launchAndCollect(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.CREATED,
    body: (T) -> Unit
) {
    coroutineScope.launch {
        this@launchAndCollect.repeatOnLifecycle(state) {
            flow.collect(body)
        }
    }
}

fun <T> LifecycleOwner.launchAndCollect(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.CREATED,
    body: (T) -> Unit
) {
    lifecycleScope.launch {
        this@launchAndCollect.repeatOnLifecycle(state) {
            flow.collect(body)
        }
    }
}
