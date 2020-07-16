package com.roborox.sketch.tools

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.roborox.sketch.SketchView


abstract class SketchTool(val touchView: SketchView) : OnTouchListener {
  companion object {
    const val TYPE_PEN = 0
    const val TYPE_ERASE = 1
  }

  abstract val type: Int

  abstract fun render(canvas: Canvas)

  abstract fun clear()

  override fun onTouch(v: View, event: MotionEvent): Boolean {
    when (event.action) {
      MotionEvent.ACTION_DOWN -> onTouchDown(event)
      MotionEvent.ACTION_MOVE -> onTouchMove(event)
      MotionEvent.ACTION_UP -> onTouchUp(event)
      MotionEvent.ACTION_CANCEL -> onTouchCancel(event)
    }
    return true
  }
  abstract fun onTouchDown(event: MotionEvent?)
  abstract fun onTouchMove(event: MotionEvent?)
  abstract fun onTouchUp(event: MotionEvent?)
  abstract fun onTouchCancel(event: MotionEvent?)
}
