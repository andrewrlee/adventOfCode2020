import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

private class Challenge03 {

    fun readLines() = File("src/main/resources/03-input.txt").readLines(UTF_8)

    fun lines(down: Int): Sequence<String> {
        val lines = readLines().asSequence().iterator()
        return generateSequence(lines.next()) {
            repeat(down - 1) { if (lines.hasNext()) lines.next() }
            if (lines.hasNext()) lines.next() else null
        }
    }

    fun findTree(diff: Int) = { row: Int, line: String ->
        val nextIndex = row * diff
        val nextEncounter = generateSequence { line.toList() }.flatten().drop(nextIndex).first()
        if (nextEncounter == '#') 1L else 0L
    }

    fun calculate(right: Int, down: Int) = lines(down).mapIndexed(findTree(right)).sum()

    fun findAnswer1v1() = calculate(3, 1)

    fun findAnswer2v1() = calculate(1, 1) *
            calculate(3, 1) *
            calculate(5, 1) *
            calculate(7, 1) *
            calculate(1, 2)

    fun solve() {
        println(findAnswer1v1())
        println(findAnswer2v1())
    }
}

fun main() = Challenge03().solve()