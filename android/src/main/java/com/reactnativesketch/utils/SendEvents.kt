package com.roborox.sketch.utils

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.roborox.sketch.SketchFile
import com.roborox.sketch.SketchViewContainer

internal fun SketchViewContainer.sendSavedEvent(sketchFile: SketchFile, requestId: Int? = null) {
  this.sendSavedLoadEvent("onSaved", sketchFile, requestId)
}
internal fun SketchViewContainer.sendLoadEvent(sketchFile: SketchFile, requestId: Int? = null) {
  this.sendSavedLoadEvent("onLoad", sketchFile, requestId)
}
internal fun SketchViewContainer.sendSavedLoadEvent(eventName: String, sketchFile: SketchFile, requestId: Int? = null) {
  val event = Arguments.createMap()
  event.putString("filePath", sketchFile.filePath)
  val size = Arguments.createMap()
  size.putInt("width", sketchFile.width)
  size.putInt("height", sketchFile.height)
  event.putMap("size", size)
  this.sendResponse(eventName, event, requestId)
}
internal fun SketchViewContainer.sendResponse(eventName: String, event: WritableMap?, requestId: Int? = null) {
  val response = Arguments.createMap()
  if (requestId !== null) {
    response.putInt("requestId", requestId)
  }
  response.putMap("event", event)
  val reactContext = this.context as ReactContext
  reactContext
    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
    .emit(eventName, response)
}
