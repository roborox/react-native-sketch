package com.roborox.sketch

import com.facebook.react.bridge.*
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.roborox.sketch.utils.sendLoadEvent
import com.roborox.sketch.utils.sendSavedEvent
import java.util.*


class SketchViewManager : SimpleViewManager<SketchViewContainer?>() {
  override fun getName(): String = RN_PACKAGE

  override fun createViewInstance(reactContext: ThemedReactContext) = SketchViewContainer(reactContext)

  @ReactProp(name = PROPS_FILE_PATH)
  fun setFilePath(root: SketchViewContainer, filePath: String) {
    root.filePath = filePath
    root.loadFile()?.also { root.sendLoadEvent(it) }
  }

  override fun receiveCommand(root: SketchViewContainer, commandId: String, args: ReadableArray?) {
    when (commandId) {
      COMMAND_CLEAR_SKETCH -> root.sketchView.clear()
      COMMAND_LOAD_SKETCH -> {
        args!!
        val requestId = args.getInt(0)
        val filePath = args.getString(1)!!
        root.filePath = filePath
        root.loadFile(filePath)?.also {
          root.sendLoadEvent(it, requestId)
        }
      }
      COMMAND_RELOAD_SKETCH -> {
        args!!
        val requestId = args.getInt(0)
        root.loadFile()?.also { root.sendLoadEvent(it, requestId) }
      }
      COMMAND_SAVE_SKETCH -> {
        args!!
        val requestId = args.getInt(0)
        val width = args.getInt(1)
        val height = args.getInt(2)
        val sketchFile = root.saveFile(width, height)
        root.sendSavedEvent(sketchFile, requestId)
      }
      COMMAND_SET_TOOL -> {
        args!!
        val toolType = args.getInt(0)
        val toolProps = args.getMap(1)!!
        root.sketchView.setTool(toolType, toolProps)
      }
      else -> throw IllegalArgumentException(String.format(Locale.ENGLISH, "Unsupported command %d.", commandId))
    }
  }

  companion object {
    private const val RN_PACKAGE = "RNSketchView"
    private const val PROPS_FILE_PATH = "filePath"
    private const val COMMAND_CLEAR_SKETCH = "clearSketch"
    private const val COMMAND_LOAD_SKETCH = "loadSketch"
    private const val COMMAND_RELOAD_SKETCH = "reloadSketch"
    private const val COMMAND_SAVE_SKETCH = "saveSketch"
    private const val COMMAND_SET_TOOL = "setTool"
  }
}
