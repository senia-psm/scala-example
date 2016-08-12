//def myWhyle(cond: => Boolean)(body: => Unit) = {
//  while (cond) {
//    body
//  }
//}
//
//var i = 0
//
//myWhyle(i < 6) {
//  i += 1
//  println(i)
//}

val os = Option("a")

os.getOrElse{
  throw new RuntimeException("Unexpected")
}

os match {
  case Some(s) => s
  case None => sys.error("should not get here")
}

class B(_a: => A) {
  lazy val a: A = _a
}

class A(_b: => B) {
  lazy val b: B = _b
}

object Test {
  val a: A = new A(b)
  val b: B = new B(a)
}

Test.a.b eq Test.b.a.b
Test.b

