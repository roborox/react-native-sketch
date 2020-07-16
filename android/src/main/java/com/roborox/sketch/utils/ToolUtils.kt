package com.roborox.sketch.utils

import android.content.Context
import android.util.TypedValue

object ToolUtils {
  fun convertDPToPixels(context: Context, dp: Float): Float {
    val dm = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm)
  }
}
