package com.roborox.sketch

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class SketchViewPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext) = emptyList<NativeModule?>()

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> = listOf(SketchViewManager())
}
