import caliban.GraphQL.graphQL
import caliban.RootResolver

object GraphQL {

  case class User(uid: Long, firstName: String,
                  lastName: String,
                  username: String,
                  email: String,
                  password: String)

  // Actual data
  val data = List(User(12345L, "Tony", "Stark", "Ironman", "ironman@superhero.org", "ironmansecret"))

  // Helper functions
  def getUsers: List[User] = data

  def getUser(username: String): Option[User] = data.filter(user => user.username == username).headOption

  def getUser(uid: Long): Option[User] = data.filter(user => user.uid == uid).headOption

  // Schema
  case class Queries(users: List[User], user: String => Option[User], uid: Long => Option[User])

  // Resolver
  val queries = Queries(getUsers, args => getUser(args), args => getUser(args))

  // API
  val api = graphQL(RootResolver(queries))

}

object Demo extends App {
    println("Starting GraphQL API")
    println(GraphQL.api.render)
    println("Ending GraphQL API")
    println()

    println("Starting Query")
    val query = """{ getUsers }"""
    for {
      interpreter <- GraphQL.api.interpreter
      result <- interpreter.execute(query)
      _ <- zio.console.putStrLn(result.data.toString)
    } yield ()
    println("Ending Query")
}
