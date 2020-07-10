package com.reactnativesketch.tools

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.View
import com.reactnativesketch.SketchView
import com.reactnativesketch.utils.ToolUtils.convertDPToPixels


class EraseSketchTool(touchView: SketchView) : PathTrackingSketchTool(touchView), ToolThickness {
  companion object {
    const val DEFAULT_THICKNESS = 10f
  }

  override val type = TYPE_ERASE

  private val paint = Paint().apply {
    xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    style = Paint.Style.STROKE
  }
  override var toolThickness = DEFAULT_THICKNESS
    set(thickness) {
      field = thickness
      paint.strokeWidth = convertDPToPixels(touchView.context, toolThickness)
    }

  override fun render(canvas: Canvas) {
    touchView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    canvas.drawPath(path, paint)
  }
}
