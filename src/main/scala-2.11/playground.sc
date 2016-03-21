

val x = Seq(1,2,3)

x.foldLeft(0){
  (agg,current)=>
  println(s"agg: $agg cur: $current")
    agg + current
}