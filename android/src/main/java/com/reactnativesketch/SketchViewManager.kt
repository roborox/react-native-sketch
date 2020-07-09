package com.reactnativesketch

import android.graphics.Color
import android.view.View
import com.facebook.infer.annotation.Assertions
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import java.io.IOException
import java.util.*


class SketchViewManager : SimpleViewManager<SketchViewContainer?>() {
  override fun getName(): String = RN_PACKAGE

  override fun createViewInstance(reactContext: ThemedReactContext) = SketchViewContainer(reactContext)

  @ReactProp(name = PROPS_SELECTED_TOOL)
  fun setSelectedTool(viewContainer: SketchViewContainer, toolId: Int) {
    viewContainer.sketchView.setToolType(toolId)
  }

  @ReactProp(name = PROPS_TOOL_COLOR, defaultInt = Color.BLACK, customType = "Color")
  fun setToolColor(viewContainer: SketchViewContainer, color: Int) {
    viewContainer.sketchView.setToolColor(color)
  }

  @ReactProp(name = PROPS_LOCAL_SOURCE_IMAGE_PATH)
  fun setLocalSourceImagePath(viewContainer: SketchViewContainer, localSourceImagePath: String) {
    viewContainer.openSketchFile(localSourceImagePath)
  }

  override fun receiveCommand(root: SketchViewContainer, commandId: String, args: ReadableArray?) {
    Assertions.assertNotNull<Any>(root)
    when (commandId) {
      COMMAND_CLEAR_SKETCH -> {
        root.sketchView.clear()
        return
      }
      COMMAND_CHANGE_TOOL -> {
        Assertions.assertNotNull(args)
        val toolId = args!!.getInt(0)
        root.sketchView.setToolType(toolId)
        return
      }
      COMMAND_SAVE_SKETCH -> {
        try {
          val sketchFile: SketchFile = root.saveToLocalCache()
          onSaveSketch(root, sketchFile)
          return
        } catch (e: IOException) {
          e.printStackTrace()
        }
        throw IllegalArgumentException(String.format(Locale.ENGLISH, "Unsupported command %d.", commandId))
      }
      else -> throw IllegalArgumentException(String.format(Locale.ENGLISH, "Unsupported command %d.", commandId))
    }
  }

  private fun onSaveSketch(root: SketchViewContainer, sketchFile: SketchFile) {
    val event = Arguments.createMap()
    event.putString("localFilePath", sketchFile.localFilePath)
    event.putInt("imageWidth", sketchFile.width)
    event.putInt("imageHeight", sketchFile.height)
    sendEvent(root, "onSaveSketch", event)
  }

  private fun sendEvent(view: View, eventType: String, event: WritableMap) {
    val nativeEvent = Arguments.createMap()
    nativeEvent.putString("type", eventType)
    nativeEvent.putMap("event", event)
    val reactContext = view.context as ReactContext
    reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(view.id, "topChange", nativeEvent)
  }

  companion object {
    private const val RN_PACKAGE = "RNSketchView"
    private const val PROPS_TOOL_COLOR = "toolColor"
    private const val PROPS_SELECTED_TOOL = "selectedTool"
    private const val PROPS_LOCAL_SOURCE_IMAGE_PATH = "localSourceImagePath"
    private const val COMMAND_CLEAR_SKETCH = "clearSketch"
    private const val COMMAND_SAVE_SKETCH = "saveSketch"
    private const val COMMAND_CHANGE_TOOL = "changeTool"
  }
}
