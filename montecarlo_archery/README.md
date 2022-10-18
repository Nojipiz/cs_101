# :memo: Archery simulator using the Montecarlo method.
Homework for "Computer Simulations" class, a GUI software created with [ScalaFx](https://www.scalafx.org/), you can start installing [sbt](https://www.scala-sbt.org/).

This is almost 80% pure functional, things like the view and the pseudorandom number generation can't be performed (easily) with a functional perspective.

## How it works?
As you know, in FP we can't just put a counter and add the results of each game or round or competitor, so we run *all the games* (by default, 20000) and put them into a list of games, later we operate over the list and create the results.

### Performance?
It's just trash! :sleepy:

### Ejecution
This project has been created with Scala 3 (also called Dotty), using sbt for the dependencies. You can compile the project with this command.
```
sbt compile
```
And for run the project!.
```
sbt run
```


