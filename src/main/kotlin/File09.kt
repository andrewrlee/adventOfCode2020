import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

private class Challenge09 {

    val numbers =
        File("src/main/resources/09-input.txt").readLines(UTF_8).map { it.toLong() }

    fun <A, B> cartesianProduct(listA: Iterable<A>, listB: Iterable<B>): Sequence<Pair<A, B>> =
        sequence { listA.forEach { a -> listB.forEach { b -> yield(a to b) } } }

    fun findAnswer1v1() = numbers.toList()
        .windowed(26, 1)
        .map { it.take(25).toSet() to it.last() }
        .find { (previous, number) ->
            cartesianProduct(previous, previous).filter { (i, j) -> i != j }.find { (i, j) -> i + j == number } == null
        }?.second

    fun List<Long>.findContiguousRangeThatSumsTo(resultToFind: Long): List<Long>? {
        var sum = 0L
        val trimmedList =
            this.reversed().takeWhile { ((sum + it) <= resultToFind).also { _ -> sum += it } }.sorted()
        return if (trimmedList.sum() == resultToFind) trimmedList else null
    }

    fun findContiguousRange(resultToFind: Long): List<Long> = (0..numbers.size).mapNotNull { i ->
        (0..i).map(numbers::get).findContiguousRangeThatSumsTo(resultToFind)?.let { return it }
    }

    fun findAnswer2v1() = findContiguousRange(findAnswer1v1()!!).let { it.first() + it.last() }

    fun solve() {
        println(findAnswer1v1())
        println(findAnswer2v1())
    }
}

fun main() = Challenge09().solve()