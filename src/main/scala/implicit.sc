import scala.concurrent.Future
import scala.language.higherKinds
trait Monad[M[_]] {
  // (M[T], T -> R) -> M[R]
  def map[T, R](m: M[T], f: T => R): M[R]
  def flatMap[T, R](m: M[T], f: T => M[R]): M[R]
  def apply[T](t: T): M[T]
}

trait ToJson[T] {
  def toJson(t: T): String
}

def serialize[T: ToJson](t: T): String = {
  implicitly[ToJson[T]].toJson(t)
}

case class Test(s: String)
object Test {
  implicit val toJson: ToJson[Test] =
    new ToJson[Test] {
      override def toJson(t: Test): String =
        s"{'s': '${t.s}'}"
    }

  implicit val fromJson: FromJson[Test] =
    new FromJson[Test] {
      override def fromJson(s: String): Test = Test(s)
    }
}


serialize(Test("x"))

trait FromJson[T] {
  def fromJson(s: String): T
}

def parse[T: FromJson](s: String): T =
  implicitly[FromJson[T]].fromJson(s)

parse[Test]("s")
