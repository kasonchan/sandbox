/**
 * @author kasonchan
 * @since 2020-09
 */
fun main() {
    println("First line of Kotlin code!")
    val name = "Kason"
    println("My name is $name.")

    when (true) {
        true -> println("It is checked.")
        else -> println("It is not checked.")
    }

    when (5 in 1..10) {
        true -> println("5 is in the range of 1 to 10.")
        else -> println("5 is not in the range of 1 to 10.")
    }

    val user1 = User("username", "firstname", "lastname", "passw", 1)
    val user2 = User("username", "firstname", "lastname", "password", 2)
    val users = arrayOf(user1, user2)

    users.forEach { user -> println(user.hiddenPassword()) }
    for (user in users) println(user.hiddenPassword())

    println(user1.password)
    println(user1.code)
    println(user1.hiddenPassword())

    val price = """${'$'}9.99"""
    println(price)

    when(user1) {
        User("","","","", 3) -> println()
        else -> println()
    }
}

class User(val username: String, val firstname: String, val lastname: String, val password: String, val code: Long) {
    init {
        println("$username, $firstname, $lastname")
    }

    fun hiddenPassword(): String = "*".repeat(password.length)
}
