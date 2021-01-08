package com.example

object Chapter01_01_functions {

    def factorial(n: Int): Int = (1 to n).product

    def is_prime(n: Int): Boolean = {
        (2 until n).forall(k ⇒ n % k != 0)
    }

    def count_even(s: Set[Int]): Int = {
        def is_even(k: Int): Int = if (k % 2 == 0) 1 else 0

        s.toSeq.map(k ⇒ is_even(k)).sum
    }

    def count_even_using_val(s: Set[Int]): Int = {
        val is_even = (k: Int) ⇒ if (k % 2 == 0) 1 else 0

        // Need to convert a `Set[Int]` to a sequence (`Seq[Int]`), or else `map` does not work correctly!
        s.toSeq.map(k ⇒ is_even(k)).sum
    }
}

object Chapter01_02_examples {
    def ex01(x: Int): Int = x + 20

    val ex01a: Int ⇒ Int = x ⇒ x + 20

    def ex02(x: Int): (Int ⇒ Int) = y ⇒ y + x

    val ex02a: Int ⇒ (Int ⇒ Int) = x ⇒ (y ⇒ y + x)

    def ex03(x: Int): Boolean = !Chapter01_01_functions.is_prime(x)

    val ex03a: Int ⇒ Boolean = x ⇒ !Chapter01_01_functions.is_prime(x)

    def ex04(s: Seq[Double]): Double = s.sum / s.size

    def ex05(n: Int): Double = (1 to n)
        .map { i ⇒ (2 * i).toDouble / (2 * i - 1) * (2 * i) / (2 * i + 1) }
        .product

    def ex06(s: Seq[Set[Int]]): Seq[Set[Int]] = s.filter(t ⇒ t.size >= 3)
}

object App {

    def main(args: Array[String]): Unit = {


        val x = (for (i <- 1 to 5) yield i * 2).toList

        println(x)

//        (333 to 393).filter(_ % 2 == 0).foreach(println(_))
//
//        println((1 to 200).map(_ => "-").reduce(_ + _))
//
//        for (a <- 333 to 353) println(a)
//
//        val t = (1, 2, List(3, 4, 5)): (Int, Int, List[Int])
//
//        t match {
//            case (x, y, z :: l) => l.foreach(i => println(s"$i"))
//            case _ => println("No case")
//        }
//
//        val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
//
//        println(
//            numbers.foldLeft(List[Int]())((xs, y) => xs :+ y)
//        )
//
//        println(
//            numbers.foldRight(List[Int]())((y, xs) => xs :+ y)
//        )
    }
}
