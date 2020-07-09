import * as React from "react"
import { StyleSheet, View, Text } from "react-native"
import { SketchView, SketchToolType } from "react-native-sketch"
import { TemporaryDirectoryPath } from "react-native-fs"

export default function App() {
  return (
    <View style={styles.container}>
      <Text>Bah</Text>
      <SketchView
        selectedTool={SketchToolType.pen}
        localSourceImagePath={TemporaryDirectoryPath + "/draw"}
      />
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
})
