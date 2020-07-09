import React, {
  useRef,
  useCallback,
  useEffect,
  forwardRef,
  MutableRefObject,
  useImperativeHandle,
} from "react"
import {
  DeviceEventEmitter,
  EmitterSubscription,
  UIManager,
  findNodeHandle,
  requireNativeComponent,
  ViewProps,
} from "react-native"

type SketchViewNativeProps = ViewProps & {
  toolColor?: string
  selectedTool: SketchToolType
  localSourceImagePath: string
  onChange?: (event: SaveEvent) => void
}

const RNSketchView = requireNativeComponent<SketchViewNativeProps>(
  "RNSketchView"
)

export type SaveEvent = {
  localFilePath: string
  imageWidth: number
  imageHeight: number
}

export enum SketchToolType {
  pen = 0,
  erase = 1,
}

export type SketchViewProps = Omit<SketchViewNativeProps, "onChange"> & {
  onSaveSketch?: (event: SaveEvent) => void
}

export type SketchViewRef = {
  clearSketch(): void
  saveSketch(): void
  changeTool(tool: SketchToolType): void
}
type ForwardedRef<T> =
  | ((instance: T | null) => void)
  | MutableRefObject<T | null>
  | null

function SketchView(
  { onSaveSketch, ...props }: SketchViewProps,
  ref: ForwardedRef<SketchViewRef>
) {
  const subscriptions = useRef<EmitterSubscription[]>([])

  useEffect(() => {
    if (typeof onSaveSketch !== "function") return
    const sub = DeviceEventEmitter.addListener("onSaveSketch", onSaveSketch)
    subscriptions.current.push(sub)
    return () => {
      sub.remove()
      subscriptions.current = subscriptions.current.filter((s) => s === sub)
    }
  }, [onSaveSketch])

  useEffect(() => {
    return () => {
      subscriptions.current.forEach((s) => s.remove())
      subscriptions.current = []
    }
  }, [])

  const onChange = useCallback(
    (event: any) => {
      if (event.nativeEvent.type === "onSaveSketch") {
        onSaveSketch?.({
          localFilePath: event.nativeEvent.event.localFilePath,
          imageWidth: event.nativeEvent.event.imageWidth,
          imageHeight: event.nativeEvent.event.imageHeight,
        })
      }
    },
    [onSaveSketch]
  )

  const rnRef = useRef(null)
  useImperativeHandle(ref, () => ({
    clearSketch() {
      UIManager.dispatchViewManagerCommand(
        findNodeHandle(rnRef.current),
        "clearSketch" as any
      )
    },
    saveSketch() {
      UIManager.dispatchViewManagerCommand(
        findNodeHandle(rnRef.current),
        "saveSketch" as any
      )
    },
    changeTool(toolId) {
      UIManager.dispatchViewManagerCommand(
        findNodeHandle(rnRef.current),
        "changeTool" as any,
        [toolId]
      )
    },
  }))

  return <RNSketchView ref={rnRef} {...props} onChange={onChange} />
}
const ForwardedSketchView = forwardRef(SketchView)
export { ForwardedSketchView as SketchView }
export default ForwardedSketchView
