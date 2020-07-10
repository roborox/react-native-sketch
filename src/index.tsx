import React, {
  useRef,
  useEffect,
  forwardRef,
  MutableRefObject,
  useImperativeHandle,
  Component,
} from "react"
import {
  UIManager,
  findNodeHandle,
  requireNativeComponent,
  NativeMethods,
  NativeEventEmitter,
  NativeModules,
  processColor,
  ViewProps,
} from "react-native"

const RNSketchView = requireNativeComponent<SketchViewNativeProps>(
  "RNSketchView"
)
const sketchViewEventEmitter = new NativeEventEmitter(
  NativeModules.RNSketchView
)

function SketchView(
  { toolType, tool, onChange, onSaved, onLoad, ...props }: SketchViewProps,
  ref: ForwardedRef<SketchViewRef>
) {
  const requests = useRef({
    byId: [] as Record<
      number,
      {
        resolve(): void
        reject(): void
      }
    >,
    nextId: 0,
  })
  function handleResponse(handler: (event: any) => void) {
    return ({ requestId, event }: any) => {
      handler(event)
      if (requestId !== undefined) {
        const request = requests.current.byId[requestId]
        request?.resolve()
      }
    }
  }

  useEffect(() => {
    if (typeof onChange !== "function") return
    const sub = sketchViewEventEmitter.addListener("onChange", onChange)
    return () => sub.remove()
  }, [onChange])
  useEffect(() => {
    if (typeof onSaved !== "function") return
    const sub = sketchViewEventEmitter.addListener(
      "onSaved",
      handleResponse(onSaved)
    )
    return () => sub.remove()
  }, [onSaved])
  useEffect(() => {
    if (typeof onLoad !== "function") return
    const sub = sketchViewEventEmitter.addListener(
      "onLoad",
      handleResponse(onLoad)
    )
    return () => sub.remove()
  }, [onLoad])

  const rnRef = useRef<
    Component<SketchViewNativeProps, {}, any> & Readonly<NativeMethods>
  >(null)
  function sendRequest(method: string, args: any[]) {
    const requestId = requests.current.nextId++
    return new Promise<void>((resolve, reject) => {
      requests.current.byId[requestId] = { resolve, reject }
      UIManager.dispatchViewManagerCommand(
        findNodeHandle(rnRef.current),
        method as any,
        [requestId, ...args]
      )
    })
  }

  useImperativeHandle(ref, () => ({
    clearSketch() {
      UIManager.dispatchViewManagerCommand(
        findNodeHandle(rnRef.current),
        "clearSketch" as any,
        []
      )
    },
    loadSketch(filePath: string) {
      return sendRequest("loadSketch", [filePath])
    },
    reloadSketch() {
      return sendRequest("reloadSketch", [])
    },
    saveSketch(size?: Size) {
      return sendRequest("saveSketch", [size?.width ?? 0, size?.height ?? 0])
    },
    setNativeProps(nativeProps: SketchViewNativeProps) {
      rnRef.current?.setNativeProps(nativeProps)
    },
  }))

  useEffect(() => {
    const color = "color" in tool ? processColor(tool.color) : undefined
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(rnRef.current),
      "setTool" as any,
      [toolType, { ...tool, color }]
    )
  }, [toolType, tool])

  return <RNSketchView ref={rnRef} {...props} />
}
const ForwardedSketchView = forwardRef(SketchView)
export { ForwardedSketchView as SketchView }
export default ForwardedSketchView

export type SketchViewRef = {
  clearSketch(): void
  loadSketch(filePath: string): Promise<void>
  reloadSketch(): Promise<void>
  saveSketch(size?: Size): Promise<void>
  setNativeProps(props: SketchViewNativeProps): void
}

export type SketchViewProps = ViewProps & {
  toolType: SketchToolType
  tool: AnySketchTool
  filePath?: string
  onChange?: (event: AnyChangeEvent) => void
  onSaved?: (event: SavedLoadEvent) => void
  onLoad?: (event: SavedLoadEvent) => void
}

export type SketchViewNativeProps = {
  filePath?: string
}

export enum SketchToolType {
  pen = 0,
  erase = 1,
}

export type PenSketchTool = {
  color: string
  thickness: number
}
export type EraseSketchTool = {
  thickness: number
}
export type AnySketchTool = PenSketchTool | EraseSketchTool

export type SavedLoadEvent = {
  filePath: string
  size: Size
}

export type PenChangeEvent = {
  type: SketchToolType.pen
}
export type EraseChangeEvent = {
  type: SketchToolType.erase
}
export type AnyChangeEvent = PenChangeEvent | EraseChangeEvent

export type Size = {
  width: number
  height: number
}

type ForwardedRef<T> =
  | ((instance: T | null) => void)
  | MutableRefObject<T | null>
  | null
