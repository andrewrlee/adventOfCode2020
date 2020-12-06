import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

private class Challenge01 {
    fun readNumbers() = File("src/main/resources/01-input.txt").readLines(UTF_8).map { it.toInt() }

    private fun findAnswer1v1(): Int {
        val numbers = readNumbers().toSortedSet()
        for (i in numbers) {
            val target = 2020 - i
            if (numbers.contains(target)) {
                return i * target
            }
        }
        throw RuntimeException("Did not find match")
    }

    private fun findAnswer2(): Int {
        val numbers = readNumbers()
        for (i in numbers) {
            for (j in numbers) {
                for (k in numbers) {
                    val result = i + j + k
                    when {
                        result < 2020 -> continue
                        result == 2020 -> return (i * j * k)
                    }
                }
            }
        }
        throw RuntimeException("Did not find match")
    }

    fun solve() {
        println(findAnswer1v1())
        println(findAnswer2())
    }
}

fun main() = Challenge01().solve()