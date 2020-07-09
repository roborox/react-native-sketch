package com.reactnativesketch.tools

import android.graphics.Path
import android.view.MotionEvent

import android.view.View


abstract class PathTrackingSketchTool internal constructor(touchView: View?) : SketchTool(touchView!!) {
  var path: Path = Path()
  override fun clear() {
    path.reset()
  }

  override fun onTouchDown(event: MotionEvent?) {
    path.moveTo(event!!.x, event.y)
  }

  override fun onTouchMove(event: MotionEvent?) {
    path.lineTo(event!!.x, event.y)
    touchView.invalidate()
  }

  override fun onTouchUp(event: MotionEvent?) {
    path.lineTo(event!!.x, event.y)
    touchView.invalidate()
  }

  override fun onTouchCancel(event: MotionEvent?) {
    onTouchUp(event)
  }
}
