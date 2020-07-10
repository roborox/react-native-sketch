import React, { useRef, useState, useMemo } from "react"
import { StyleSheet, View, Text, Button } from "react-native"
import {
  SketchView,
  SketchToolType,
  SketchViewRef,
  PenSketchTool,
  EraseSketchTool,
} from "@roborox/react-native-sketch"
import { ExternalDirectoryPath } from "react-native-fs"

export default function App() {
  const sketch = useRef<SketchViewRef>(null)

  const [penTool, setPenTool] = useState<PenSketchTool>({
    color: "blue",
    thickness: 30,
  })
  const [eraseTool, setEraseTool] = useState<EraseSketchTool>({
    thickness: 20,
  })
  const tools = useMemo(() => {
    return {
      [SketchToolType.pen]: penTool,
      [SketchToolType.erase]: eraseTool,
    }
  }, [eraseTool, penTool])

  const [tool, setTool] = useState<SketchToolType>(SketchToolType.pen)

  return (
    <View style={styles.container}>
      <Text>Bah</Text>
      <View style={styles.row}>
        <Button
          title="Save"
          onPress={() =>
            sketch.current?.saveSketch({ width: 512, height: 512 })
          }
        />
        <Button title="Clear" onPress={() => sketch.current?.clearSketch()} />
        <Button title="Pen" onPress={() => setTool(SketchToolType.pen)} />
        <Button title="Erase" onPress={() => setTool(SketchToolType.erase)} />
      </View>
      {tool === SketchToolType.pen ? (
        <View style={styles.row}>
          <Button
            title="Red"
            onPress={() => setPenTool((t) => ({ ...t, color: "red" }))}
          />
          <Button
            title="Green"
            onPress={() => setPenTool((t) => ({ ...t, color: "green" }))}
          />
          <Button
            title="Blue"
            onPress={() => setPenTool((t) => ({ ...t, color: "blue" }))}
          />
        </View>
      ) : null}
      {tool === SketchToolType.pen ? (
        <View style={styles.row}>
          <Button
            title="30"
            onPress={() => setPenTool((t) => ({ ...t, thickness: 30 }))}
          />
          <Button
            title="20"
            onPress={() => setPenTool((t) => ({ ...t, thickness: 20 }))}
          />
          <Button
            title="10"
            onPress={() => setPenTool((t) => ({ ...t, thickness: 10 }))}
          />
        </View>
      ) : null}
      {tool === SketchToolType.erase ? (
        <View style={styles.row}>
          <Button
            title="30"
            onPress={() => setEraseTool((t) => ({ ...t, thickness: 30 }))}
          />
          <Button
            title="20"
            onPress={() => setEraseTool((t) => ({ ...t, thickness: 20 }))}
          />
          <Button
            title="10"
            onPress={() => setEraseTool((t) => ({ ...t, thickness: 10 }))}
          />
        </View>
      ) : null}
      <View style={styles.sketchContainer}>
        <SketchView
          ref={sketch}
          toolType={tool}
          tool={tools[tool]}
          filePath={ExternalDirectoryPath + "/draw"}
          style={styles.sketch}
          onLoad={(e) => console.log("loaded:", e)}
          onSaved={(e) => console.log("saved:", e)}
          onChange={() => console.log("changed")}
        />
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  row: {
    flexDirection: "row",
  },
  sketchContainer: {
    flex: 1,
    flexDirection: "row",
  },
  sketch: {
    flex: 1,
    opacity: 0.5,
  },
})
