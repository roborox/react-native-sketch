package com.reactnativesketch

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import com.reactnativesketch.tools.EraseSketchTool
import com.reactnativesketch.tools.PenSketchTool
import com.reactnativesketch.tools.SketchTool


class SketchView(context: Context?) : View(context) {
  var currentTool: SketchTool? = null
  var penTool: SketchTool
  var eraseTool: SketchTool
  var incrementalImage: Bitmap? = null

  init {
    penTool = PenSketchTool(this)
    eraseTool = EraseSketchTool(this)
    setToolType(SketchTool.TYPE_PEN)
    setBackgroundColor(Color.TRANSPARENT)
  }

  fun setToolType(toolType: Int) {
    currentTool = when (toolType) {
      SketchTool.TYPE_PEN -> penTool
      SketchTool.TYPE_ERASE -> eraseTool
      else -> penTool
    }
  }

  fun setToolColor(toolColor: Int) {
    (penTool as PenSketchTool).toolColor = toolColor
  }

  fun setViewImage(bitmap: Bitmap?) {
    incrementalImage = bitmap
    invalidate()
  }

  fun drawBitmap(): Bitmap {
    val viewBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(viewBitmap)
    draw(canvas)
    return viewBitmap
  }

  fun clear() {
    incrementalImage = null
    currentTool?.clear()
    invalidate()
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    if (incrementalImage != null) canvas.drawBitmap(incrementalImage, left.toFloat(), top.toFloat(), null)
    currentTool?.render(canvas)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    val value: Boolean = currentTool?.onTouch(this, event) ?: false
    if (event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP) {
      setViewImage(drawBitmap())
      currentTool?.clear()
    }
    return value
  }
}
