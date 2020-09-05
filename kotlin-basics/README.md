# kotlin-basics

## Notes

### Basic Types

#### Number Constant

You can use underscores to make number constants more readable

```
val oneMillion = 1_000_000
val creditCardNumber = 1234_5678_9012_3456L
```

### Control Flow

```
for (i in 6 downTo 0 step 2) {
    println(i)
}
for (user in users) println(user.hiddenPassword())
```

### Conditional Expressions

#### When expression
A when expression controls the flow of code by evaluating the value of a variable in order to determine what code gets 
executed.

```
val grade = "A"

when(grade) {
  "A" -> println("Excellent job!")
  "B" -> println("Very well done!")
  "C" -> println("You passed!")
  else -> println("Close! Make sure to perpare more next time!")
}
```

#### The Range Operator
The range operator (..) is used to create a succession of number or character values.

```
var height = 46 // inches

if (height in 1..53) {
  println("Sorry, you must be at least 54 inches to ride the rollercoaster.")
}
```

#### Equality Operators
Equality operators are symbols that are used to compare the equivalence of two values in order to return true or false. 
Equality operators include == and !=.

### Functions 

#### Default Arguments
We can give arguments a default value which provides an argument an automatic value if no value is passed into 
the function when it’s invoked.

```
fun favoriteLanguage(language = "Kotlin") {
  println("My favorite programming language is $language")  
}
```

#### Return Statements
In Kotlin, in order to return a value from a function, we must add a return statement to our function using the return 
keyword. This value is then passed to where the function was invoked.

If we plan to return a value from a function, we must declare the return type in the function header.

```
// Return type is declared outside the parentheses
fun getArea(length: Int, width: Int): Int {
  var area = length * width

  // return statement
  return area
}
```

### Classes

#### Primary Constructor

A primary constructor defines each property value within a class header and allows us to then set unique values when 
the object is instantiated.

To create a primary constructor using the shorthand syntax, we can follow the name of the class with a pair of 
parentheses () inside of which each property is defined and assigned a data type.

#### Init Blocks

The init block gets invoked with every instance that’s created and is used to add logic to the class. 
The init keyword precedes any member functions and is followed by a pair of curly braces.

## References

- https://www.codecademy.com/learn/learn-kotlin/modules/learn-kotlin-introduction-to-kotlin/cheatsheet
- https://kotlinlang.org/docs/reference/
- https://kotlinlang.org/docs/reference/returns.html
