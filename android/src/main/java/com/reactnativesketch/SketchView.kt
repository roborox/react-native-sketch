package com.roborox.sketch

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import com.facebook.react.bridge.ReadableMap
import com.roborox.sketch.tools.EraseSketchTool
import com.roborox.sketch.tools.PenSketchTool
import com.roborox.sketch.tools.SketchTool


class SketchView(context: Context?) : View(context) {
  private val penTool = PenSketchTool(this)
  private val eraseTool = EraseSketchTool(this)
  var currentTool: SketchTool? = null
  var incrementalImage: Bitmap? = null

  init {
    setTool(SketchTool.TYPE_PEN)
    setBackgroundColor(Color.TRANSPARENT)
  }

  private inline fun <T>maybe(props: ReadableMap?, name: String, default: T, block: ReadableMap.() -> T): T {
    return if (props !== null && props.hasKey(name)) { props.block() } else { default }
  }

  fun setTool(toolType: Int, props: ReadableMap? = null) {
    currentTool = when (toolType) {
      SketchTool.TYPE_PEN -> {
        penTool.toolColor = maybe(props, "color", PenSketchTool.DEFAULT_COLOR) { getInt("color") }
        penTool.toolThickness = maybe(props, "thickness", PenSketchTool.DEFAULT_THICKNESS) { getDouble("thickness").toFloat() }
        penTool
      }
      SketchTool.TYPE_ERASE -> {
        eraseTool.toolThickness = maybe(props, "thickness", EraseSketchTool.DEFAULT_THICKNESS) { getDouble("thickness").toFloat() }
        eraseTool
      }
      else -> penTool
    }
  }

  fun setViewImage(bitmap: Bitmap?) {
    incrementalImage = bitmap
    invalidate()
  }

  private fun drawBitmap(): Bitmap {
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
    incrementalImage?.also {
      canvas.drawBitmap(incrementalImage, left.toFloat(), top.toFloat(), null)
    }
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
