

val re = """<<.*>>""".r
val txt ="before <<hh22>> after "
val x =re.findAllMatchIn(txt)
x.map{m=>
  val path = m.source.subSequence(m.start+2,m.end-2)
  (path, m.)
}.foreach(println)

