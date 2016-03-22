package byContext.valueContainers.references

import byContext.rawInputHandling.{DataSetItem, DataSetItemConverter}
import byContext.valueContainers.ValueRefMarker
import byContext.valueContainers.stringInterpolation.InterpolatedStringValueMarker

object VerifyNoRefMarkersConfigured extends DataSetItemConverter{
  override def convert: PartialFunction[DataSetItem, Any] = {
    case DataSetItem(_,_,marker:InterpolatedStringValueMarker) =>
      throw new RuntimeException(s"string interpolation is supported only for globals")
    case DataSetItem(_,_,marker:ValueRefMarker) =>
      throw new RuntimeException(s"value references is supported only for globals")
  }
}
