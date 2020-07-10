package com.reactnativesketch

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.widget.LinearLayout
import com.reactnativesketch.utils.sendLoadEvent
import java.io.File
import java.io.FileOutputStream
import java.util.*


class SketchViewContainer(context: Context?) : LinearLayout(context) {
  var sketchView = SketchView(context).also { addView(it) }
  var filePath: String? = null

  private fun Bitmap.maybeScale(width: Int, height: Int): Bitmap {
    if (width == 0 || height == 0) return this
    if (width == this.width && height == this.height) return this
    return Bitmap.createScaledBitmap(this, width, height, false)
  }

  fun saveFile(width: Int, height: Int): SketchFile {
    val bitmap = Bitmap
      .createBitmap(sketchView.width, sketchView.height, Bitmap.Config.ARGB_8888)
      .also { draw(Canvas(it)) }
      .maybeScale(width, height)

    val cacheFile = filePath?.let { File(it) }
      ?: File.createTempFile("sketch_", UUID.randomUUID().toString() + ".png")
    FileOutputStream(cacheFile).use { output ->
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
    }

    return SketchFile(cacheFile.absolutePath, bitmap.width, bitmap.height)
  }

  fun loadFile(filePath: String? = this.filePath): SketchFile? {
    if (filePath === null || width == 0 || height == 0) return null
    val bitmap = BitmapFactory
      .decodeFile(filePath, BitmapFactory.Options())
      ?.maybeScale(sketchView.width, sketchView.height)
      ?: return null
    sketchView.setViewImage(bitmap)
    return SketchFile(filePath, bitmap.width, bitmap.height)
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    loadFile()?.also { this.sendLoadEvent(it) }
  }
}
