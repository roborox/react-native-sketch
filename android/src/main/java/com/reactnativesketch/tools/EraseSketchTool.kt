package com.reactnativesketch.tools

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.View
import com.reactnativesketch.utils.ToolUtils.ConvertDPToPixels


class EraseSketchTool(touchView: View?) : PathTrackingSketchTool(touchView), ToolThickness {
  private val paint = Paint()
  override var toolThickness = 0f
    set(thickness) {
      field = thickness
      paint.strokeWidth = ConvertDPToPixels(touchView.context, toolThickness)
    }

  override fun render(canvas: Canvas?) {
    touchView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    canvas!!.drawPath(path, paint)
  }

  companion object {
    private const val DEFAULT_THICKNESS = 10f
  }

  init {
    toolThickness = DEFAULT_THICKNESS
    val porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    paint.xfermode = porterDuffXfermode
    paint.style = Paint.Style.STROKE
  }
}
