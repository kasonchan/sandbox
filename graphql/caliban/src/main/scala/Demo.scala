import caliban.GraphQL.graphQL
import caliban.{CalibanError, GraphQL, RootResolver}
import zio.console.{Console, putStrLn}
import zio.{App, ExitCode, URIO, ZIO}

object GQL {
  // Schema
  case class User(userid: String,
                  firstname: String,
                  lastname: String,
                  username: String,
                  email: String,
                  password: String)
  case class Username(username: String)
  case class Queries(users: List[User], user: Username => Option[User])

  // Actual data
  val data = List(
    User("12345",
         "Tony",
         "Stark",
         "Ironman",
         "ironman@superhero.org",
         "ironmansecret"))

  // Helper functions
  def getUsers: List[User] = data
  def getUser(username: String): Option[User] =
    data.find(user => user.username == username)

  // Resolver
  val queries: Queries =
    Queries(getUsers, args => getUser(args.username))

  // API
  val api: GraphQL[Any] = graphQL(RootResolver(queries))
}

object Demo extends App {
  val queryEmailPassword = """{ users { email password } }"""
  val queryUsername = """{ user(username: "Ironman") { firstname lastname } }"""

  val myAppLogic: ZIO[Console, CalibanError.ValidationError, Unit] = {
    for {
      _ <- putStrLn(GQL.api.render)
      _ <- putStrLn(GQL.api.toDocument.toString)
      i <- GQL.api.interpreter
      rEP <- i.execute(queryEmailPassword)
      _ <- putStrLn("Querying for users email password")
      _ <- putStrLn(rEP.data.toString)
      rUsername <- i.execute(queryUsername)
      _ <- putStrLn("Querying for firstname lastname by username")
      _ <- putStrLn(rUsername.data.toString)
    } yield ()
  }

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = myAppLogic.exitCode
}
