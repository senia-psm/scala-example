import scala.concurrent.Future
import scala.util.Random

case class Person(
                   name: String,
                   lastName: String,
                   patronymic: Option[String],
                   age: Int)

case class Company(founder: Person,
                   head: Option[Person])

val person1 = Person("John", "Galt", None, 33)
val person2 = Person("Иван",
  "Иванович",
  Some("Иванов"), 40)

// Seq
def companiesFoundedBy(person: Person): Seq[Company] = {
  Seq(
    Company(person, Some(person)),
    Company(person, None))
}

def employeesOf(company: Company): Seq[Person] = {
  company.head.toSeq :+ Person(Random.nextString(5),
    Random.nextString(5), None, Random.nextInt(100))
}

val founders = Seq(person1, person2)

founders
  .flatMap(founder => companiesFoundedBy(founder)
    .flatMap(company => employeesOf(company)
      .map(employee => (founder, company, employee))
    ))

for {
  founder <- founders
  company <- companiesFoundedBy(founder)
  employee <- employeesOf(company)
} println((founder, company, employee))



// Option
def worksFor(person: Person): Option[Company] = {
  if (Random.nextBoolean()) Some(Company(person2, Some(person1)))
  else None
}

def headOf(company: Company): Option[Person] = company.head

def op: Option[Person] =
  if (Random.nextBoolean()) Some(person1)
  else None

val head: Option[Person] = op.flatMap(worksFor).flatMap(headOf)

for {
  p <- op
  c <- worksFor(p)
  h <- headOf(c)
} yield h


// Future
def getCompanyById(id: Int): Future[Company] =
Future.successful(Company(person1, None))
def getFounder(company: Company): Future[Person] =
  Future.successful(company.founder)
def worksFor2(person: Person): Future[Company] =
  Future.successful(Company(person2, Some(person1)))

val c2: Future[Company] = getCompanyById(1)
  .flatMap(getFounder)
  .flatMap(worksFor2)
c2

val c2_2: Future[Company] = for {
  c <- getCompanyById(1)
  f <- getFounder(c)
  c2 <- worksFor2(f)
} yield c2
c2_2

// map: (M[T], T -> R) -> M[R]
// flatMap: (M[T], T -> M[R]) -> M[R]
// apply: T -> M[T]

val oCompany = Option(Company(person1, None))

oCompany.flatMap(c => Option(c.founder))