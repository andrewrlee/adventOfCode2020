import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

private class Challenge06 {

    val groups = File("src/main/resources/06-input.txt").readText(UTF_8)
        .splitToSequence("\n\n")
        .map { it.split('\n').map { it.toSet() } }

    fun findAnswer1v1() = groups.map { it.flatten().distinct().count() }.sum()

    fun findAnswer2v1() = groups.map { it.reduce { acc, i -> acc.intersect(i) }.count() }.sum()

    fun solve() {
        println(findAnswer1v1())
        println(findAnswer2v1())
    }
}

fun main() = Challenge06().solve()