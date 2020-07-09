package com.reactnativesketch.utils

import android.content.Context
import android.util.TypedValue

object ToolUtils {
  fun ConvertDPToPixels(context: Context, dp: Float): Float {
    val dm = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm)
  }
}
