package projects.stream

import munit.FunSuite

class StreamSuite extends FunSuite {
  test("pure produces expected value") {
    assertEquals(Stream.pure(1).toList, List(1))
  }

  test("map transforms value as expected") {
    assertEquals(Stream.pure(1).map(_ + 3).toList, List(4))
  }

  test("fromIterator produces all the values from the iterator") {
    assertEquals(
      Stream.fromIterator(List(1, 2, 3, 4).toIterator).toList,
      List(1, 2, 3, 4)
    )
  }

  test("pure is substitutable") {
    assertEquals(
      Stream.pure(1).product(Stream.pure(1)).toList,
      List((1, 1))
    )

    val one = Stream.pure(1)
    assertEquals(
      one.product(one).toList,
      List((1, 1))
    )
  }
}
