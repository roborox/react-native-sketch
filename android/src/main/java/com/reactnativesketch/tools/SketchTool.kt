package com.reactnativesketch.tools

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener


abstract class SketchTool internal constructor(val touchView: View) : OnTouchListener {
  abstract fun render(canvas: Canvas?)
  abstract fun clear()

  override fun onTouch(v: View, event: MotionEvent): Boolean {
    val action = event.action
    when (action) {
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

  companion object {
    const val TYPE_PEN = 0
    const val TYPE_ERASE = 1
  }
}
