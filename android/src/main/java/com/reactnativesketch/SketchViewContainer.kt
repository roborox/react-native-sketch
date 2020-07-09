package com.reactnativesketch

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.widget.LinearLayout
import java.io.File
import java.io.FileOutputStream
import java.util.*


class SketchViewContainer(context: Context?) : LinearLayout(context) {
  var sketchView = SketchView(context)

  init {
    addView(sketchView)
  }

  fun saveToLocalCache(): SketchFile {
    val viewBitmap = Bitmap.createBitmap(sketchView.getWidth(), sketchView.getHeight(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(viewBitmap)
    draw(canvas)
    val cacheFile = File.createTempFile("sketch_", UUID.randomUUID().toString() + ".png")
    val imageOutput = FileOutputStream(cacheFile)
    viewBitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutput)
    val sketchFile = SketchFile()
    sketchFile.localFilePath = cacheFile.absolutePath
    sketchFile.width = viewBitmap.width
    sketchFile.height = viewBitmap.height
    return sketchFile
  }

  fun openSketchFile(localFilePath: String?): Boolean {
    val bitmapOptions = BitmapFactory.Options()
    bitmapOptions.outWidth = sketchView.getWidth()
    val bitmap = BitmapFactory.decodeFile(localFilePath, bitmapOptions)
    if (bitmap != null) {
      sketchView.setViewImage(bitmap)
      return true
    }
    return false
  }
}
