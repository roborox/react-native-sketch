package com.reactnativesketch.tools

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.reactnativesketch.SketchView
import com.reactnativesketch.utils.ToolUtils.convertDPToPixels


class PenSketchTool(touchView: SketchView) : PathTrackingSketchTool(touchView), ToolThickness, ToolColor {
  companion object {
    const val DEFAULT_THICKNESS = 5f
    const val DEFAULT_COLOR = Color.BLACK
  }

  override val type = TYPE_PEN

  private val paint = Paint().apply {
    style = Paint.Style.STROKE
    isAntiAlias = true
    strokeJoin = Paint.Join.ROUND
    strokeCap = Paint.Cap.ROUND
  }
  override var toolThickness = DEFAULT_THICKNESS
    set(toolThickness) {
      field = toolThickness
      paint.strokeWidth = convertDPToPixels(touchView.getContext(), toolThickness)
    }
  override var toolColor = DEFAULT_COLOR
    set(toolColor) {
      field = toolColor
      paint.color = toolColor
    }

  override fun render(canvas: Canvas) {
    canvas.drawPath(path, paint)
  }
}
