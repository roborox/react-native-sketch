package com.reactnativesketch.tools

import android.graphics.Path
import android.view.MotionEvent
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.reactnativesketch.SketchView


abstract class PathTrackingSketchTool(touchView: SketchView) : SketchTool(touchView) {
  var path = Path()

  override fun clear() {
    path.reset()
  }

  override fun onTouchDown(event: MotionEvent?) {
    path.moveTo(event!!.x, event.y)
  }

  override fun onTouchMove(event: MotionEvent?) {
    path.lineTo(event!!.x, event.y)
    touchView.invalidate()
    sendChanges()
  }

  override fun onTouchUp(event: MotionEvent?) {
    path.lineTo(event!!.x, event.y)
    touchView.invalidate()
    sendChanges()
  }

  override fun onTouchCancel(event: MotionEvent?) {
    onTouchUp(event)
  }

  private fun sendChanges() {
    val event = Arguments.createMap()
    event.putInt("type", type)
    val reactContext = touchView.context as ReactContext
    reactContext
      .getJSModule(RCTDeviceEventEmitter::class.java)
      .emit("onChange", event)
  }
}
