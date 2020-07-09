package com.reactnativesketch.tools

import android.R.attr.path
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.reactnativesketch.utils.ToolUtils.ConvertDPToPixels


class PenSketchTool(touchView: View?) : PathTrackingSketchTool(touchView), ToolThickness, ToolColor {
  override var toolThickness = 0f
    set(toolThickness) {
      field = toolThickness
      paint.strokeWidth = ConvertDPToPixels(touchView.getContext(), toolThickness)
    }
  override var toolColor = 0
    set(toolColor) {
      field = toolColor
      paint.color = toolColor
    }
  private val paint = Paint()

  init {
    toolColor = DEFAULT_COLOR
    toolThickness = DEFAULT_THICKNESS
    paint.style = Paint.Style.STROKE
    paint.isAntiAlias = true
    paint.strokeJoin = Paint.Join.ROUND
    paint.strokeCap = Paint.Cap.ROUND
  }

  companion object {
    private const val DEFAULT_THICKNESS = 5f
    private const val DEFAULT_COLOR = Color.BLACK
  }

  override fun render(canvas: Canvas?) {
    canvas?.drawPath(path, paint)
  }
}
