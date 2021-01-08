package com.example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec


class Chapter03_01_examplesSpec extends AnyFlatSpec with Matchers {

  def id[T]: T ⇒ T = x ⇒ x

  def const[C, X]: (C ⇒ X ⇒ C) = c ⇒ x ⇒ c

  behavior of "curried functions"

  it should "define fully parametric function values" in {
    def hypothenuse = (x: Double, y: Double) ⇒ math.sqrt(x * x + y * y)

    hypothenuse(3, 4) shouldEqual 5

    def swap[X, Y]: ((X, Y)) ⇒ (Y, X) = {
      case ((x, y)) ⇒ (y, x)
    }

    swap(('a, "true")) shouldEqual (("true", 'a))

    def shift3[A, B, C]: ((A, B, C)) ⇒ (B, C, A) = {
      case ((x, y, z)) ⇒ (y, z, x)
    }

    shift3((1, true, "x")) shouldEqual ((true, "x", 1))

    id(123) shouldEqual 123

    def always[T]: (T ⇒ String) = const("always")

    always(123) shouldEqual "always"
    always(true) shouldEqual "always"
    always((1, "xyz")) shouldEqual "always"

    const(123)("xyz") shouldEqual 123

    def compose[X, Y, Z](f: X ⇒ Y, g: Y ⇒ Z): X ⇒ Z = x ⇒ g(f(x))

    val f = compose((x: Int) ⇒ x % 2 == 0, (y: Boolean) ⇒ y.toString)
    f(23) shouldEqual "false"

    val f1 = compose[Int, Boolean, String](_ % 2 == 0, _.toString)
    f1(23) shouldEqual "false"
    f1(22) shouldEqual "true"

  }

  behavior of "worked examples"

  it should "compute const(id)" in {

    // const[C, X](id[T]): X ⇒ T ⇒ T  works when C = T ⇒ T

    def ex01: Any ⇒ Nothing ⇒ Nothing = const(id)

    def ex01a[T]: Any ⇒ T ⇒ T = const(id[T])

    def ex01b[X, T] = const[T ⇒ T, X](id[T])

    //def ex01c[X, T]: X ⇒ T ⇒ T = const(id)

    //def c[T] = ex01c[Int, T](12345) // "c" is the identity function

    //c("abc") shouldEqual "abc"
  }

  // not the same as twice(f: T ⇒ T, x: T): T
  def twice0[T](f: T ⇒ T)(x: T): T = f(f(x))

  def twice[T](f: T ⇒ T): T ⇒ T =
    x ⇒ f(f(x))

  def twice_v[T]: (T ⇒ T) ⇒ T ⇒ T =
    f ⇒ x ⇒ f(f(x))

  it should "define `twice`" in {

    val g = twice((x: Int) ⇒ x + 3)

    g(10) shouldEqual 16

    val g1 = twice[Int](x ⇒ x + 3)

    g1(10) shouldEqual 16

    twice((x: Int) ⇒ x + 3)(10) shouldEqual 16

    val h = twice_v[Int](_ + 3)

    h(10) shouldEqual 16

    twice_v((x: Int) ⇒ x + 3)(10) shouldEqual 16

    twice_v[Int](_ + 3)(10) shouldEqual 16

    twice_v { x: Int ⇒ x + 3 }(10) shouldEqual 16

    twice { x: Int ⇒ x + 3 }(10) shouldEqual 16
  }

  // twice[T]: (T ⇒ T) ⇒ (T ⇒ T)
  // twice[A]: (A ⇒ A) ⇒ A ⇒ A
  // we must have A = T ⇒ T
  // twice[A](twice[T]): A ⇒ A = (T ⇒ T) ⇒ (T ⇒ T)

  it should "compute twice(twice)" in {
    //def twicetwice0 = twice(twice)

    //def twicetwice0a[T] = twice(twice)

    def twicetwice1[T] = twice[T ⇒ T](twice)

    def twicetwice2[T] = twice(twice[T])

    def twicetwice3[T]: (T ⇒ T) ⇒ T ⇒ T = twice(twice)

    "twicetwice0 { x: Int ⇒ x + 3 }(10)" shouldNot compile
    "twicetwice0a { x: Int ⇒ x + 3 }(10)" shouldNot compile

    twicetwice1 { x: Int ⇒ x + 3 }(10) shouldEqual 22
    twicetwice2 { x: Int ⇒ x + 3 }(10) shouldEqual 22
    twicetwice3 { x: Int ⇒ x + 3 }(10) shouldEqual 22

    twicetwice1[Int](_ + 3)(10) shouldEqual 22
    twicetwice2[Int](_ + 3)(10) shouldEqual 22
    twicetwice3[Int](_ + 3)(10) shouldEqual 22
  }

  it should "fix an argument of a function" in {
    def firstArg[X, Y, Z](f: (X, Y) ⇒ Z, x0: X): Y ⇒ Z = {
      y ⇒ f(x0, y)
    }

    def firstArg1[X, Y, Z]: ((X, Y) ⇒ Z, X) ⇒ Y ⇒ Z =
      (f, x0) ⇒ y ⇒ f(x0, y)

    def try1(x: Int, y: Boolean): String = s"have $x and $y"

    val try2 = firstArg(try1, 123)

    try2(true) shouldEqual "have 123 and true"

    firstArg(try1, 123)(true) shouldEqual "have 123 and true"

    firstArg1(try1, 123)(true) shouldEqual "have 123 and true"
  }

  it should "implement `converge`" in {
    def converge[T](f: T ⇒ T, x0: T, cond: T ⇒ Boolean): T = {
      Stream.iterate(x0)(f)
        .find(cond)
        .get
    }

    @tailrec
    def converge_rec[X](f: X ⇒ X, x0: X, cond: X ⇒ Boolean): X = {
      if (cond(x0)) x0 else converge_rec(f, f(x0), cond)
    }

    // Compute approximate square roots.

    def approx_sqrt(x: Double, precision: Double): Double = {
      def cond(y: Double): Boolean = math.abs(y * y - x) <= precision

      def iterate_sqrt(y: Double): Double = (y + x / y) / 2

      converge(iterate_sqrt, x / 2, cond)
    }

    def approx_sqrt_rec(x: Double, precision: Double): Double = {
      def cond(y: Double): Boolean = math.abs(y * y - x) < precision

      def iterate_sqrt(y: Double): Double = (y + x / y) / 2

      converge_rec(iterate_sqrt, x / 2, cond)
    }

    val precision = 1e-8

    approx_sqrt(25, precision) shouldEqual 5.0 +- precision
    approx_sqrt_rec(25, precision) shouldEqual 5.0 +- precision
  }

  // ((Int ⇒ A) ⇒ A) ((Int ⇒ B) ⇒ B) works only if
  // Int ⇒ A = (Int ⇒ A) ⇒ A cannot work!

  it should "infer missing types 1" in {
    def p[T]: (Int ⇒ T) ⇒ T = (f: Int ⇒ T) ⇒ f(2)

    val f: Int ⇒ Boolean = x ⇒ x % 2 == 0

    p(f) shouldEqual true

    "p(p)" shouldNot compile
  }

  it should "infer missing types 2" in {

    def q1[F, T](f: F)(g: F ⇒ T): T = g(f)

    def q[F, T] =
      (f: F) ⇒ (g: F ⇒ T) ⇒ g(f)

    def q2[F, T]: F ⇒ (F ⇒ T) ⇒ T =
      f ⇒ g ⇒ g(f)

    val a = q[Int, Boolean](10) // a: (Int ⇒ Boolean) ⇒ Boolean
    val a2 = q2[Int, Boolean](10) // a: (Int ⇒ Boolean) ⇒ Boolean

    a(x ⇒ x % 2 == 0) shouldEqual true
    a(_ % 2 == 0) shouldEqual true // shorter syntax

    a2(x ⇒ x % 2 == 0) shouldEqual true

    val b = q(10) // b : (Int ⇒ Nothing) ⇒ Nothing
    "b(x ⇒ x % 2 == 0)" shouldNot compile

    val b2 = q2(10) // b : (Int ⇒ Nothing) ⇒ Nothing
    "b2(x ⇒ x % 2 == 0)" shouldNot compile

    val c = q(10) { x ⇒ x % 2 == 0 } // OK
    c shouldEqual true

    val c2 = q2(10) { x ⇒ x % 2 == 0 } // OK
    c2 shouldEqual true

    // q(q): (A ⇒ (A ⇒ B) ⇒ B) (C ⇒ (C ⇒ D) ⇒ D) = (A ⇒ B) ⇒ B
    // = ((C ⇒ (C ⇒ D) ⇒ D) ⇒ B) ⇒ B
    // A = C ⇒ (C ⇒ D) ⇒ D

    def qq[A, B, C]: ((A ⇒ (A ⇒ B) ⇒ B) ⇒ C) ⇒ C =
      q[A ⇒ (A ⇒ B) ⇒ B, C](q[A, B])

    // q(q(q))
    def qqq[A, B, C, D]: ((((A ⇒ (A ⇒ B) ⇒ B) ⇒ C) ⇒ C) ⇒ D) ⇒ D = q[((A ⇒ (A ⇒ B) ⇒ B) ⇒ C) ⇒ C, D](q[(A ⇒ (A ⇒ B) ⇒ B), C](q))

    // q(q)(q) does not work?
    "def qqq0[A, B] = q(q)(q[A, B])" shouldNot compile

    // q(q)(q) works when correct type annotations are given:
    def qqq1[A, B, C] =
      qq[A, B, ((A ⇒ (A ⇒ B) ⇒ B) ⇒ C) ⇒ C](q[A ⇒ (A ⇒ B) ⇒ B, C])

    // qq[A, B, C](q[F, T]: F ⇒ (F ⇒ T) ⇒ T)
    // (A ⇒ (A ⇒ B) ⇒ B) ⇒ C must equal F ⇒ ((F ⇒ T) ⇒ T)
    // F must equal A ⇒ (A ⇒ B) ⇒ B
    // C must equal ((A ⇒ (A ⇒ B) ⇒ B) ⇒ T) ⇒ T
  }
}
