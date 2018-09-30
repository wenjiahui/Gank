package org.wen.gank.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.v4.content.ContextCompat

fun Context.safeStartActivity(intent: Intent, failedCallback: () -> Unit = {}) {
  val resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
  if (!resolveInfos.isEmpty()) {
    startActivity(intent)
  } else {
    failedCallback()
  }
}

fun Context.getCompatColor(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.getDimen(@DimenRes dimenRes: Int) = resources.getDimensionPixelOffset(dimenRes)