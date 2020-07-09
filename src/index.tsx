import { NativeModules } from 'react-native';

type SketchType = {
  multiply(a: number, b: number): Promise<number>;
};

const { Sketch } = NativeModules;

export default Sketch as SketchType;
