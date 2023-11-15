package com.shpp.application.level_4.presentation.utils.extensions

import android.service.notification.Condition
import android.view.View

fun View.visible() {
    this.visibility = View.VISIBLE
}
fun View.invisible() {
    this.visibility = View.INVISIBLE
}
fun View.gone() {
    this.visibility = View.GONE
}

fun View.visibleIf(
    condition: Boolean
) {
    this.visibility = if(condition) View.VISIBLE else View.GONE
}