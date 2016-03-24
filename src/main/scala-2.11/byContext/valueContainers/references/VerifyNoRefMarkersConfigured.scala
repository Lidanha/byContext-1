package byContext.valueContainers.references

import byContext.index.{VisitContext, TreeNodeConverter}
import byContext.valueContainers.ValueRefMarker
import byContext.valueContainers.stringInterpolation.InterpolatedStringValueMarker

object VerifyNoRefMarkersConfigured extends TreeNodeConverter{
  override val convert: PartialFunction[(VisitContext, Any), Any] = {
    case (_,m:InterpolatedStringValueMarker)=>
      throw new RuntimeException(s"string interpolation is supported only for globals")
    case (_,m:ValueRefMarker)=>
      throw new RuntimeException(s"value references is supported only for globals")
  }
}
