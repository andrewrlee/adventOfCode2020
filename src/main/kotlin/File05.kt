import java.io.File
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*


private class Challenge05 {
    data class Seat(val row: Int, val column: Int) {
        constructor(pass: String) : this(
            row = findRow(pass.substring(0, 7)),
            column = findColumn(pass.substring(7))
        )

        val id: Int get() = this.row * 8 + this.column

        companion object {
            fun findHalf(list: List<Int>, first: Boolean) =
                list.chunked(list.size / 2).let { if (first) it.first() else it.last() }

            fun createSearcher(lowChar: Char, list: List<Int>) = { partOfPass: String ->
                partOfPass.fold(list) { result, c -> findHalf(result, c == lowChar) }.first()
            }

            val findColumn = createSearcher('L', (0..7).toList())
            val findRow = createSearcher('F', (0..127).toList())
        }
    }

    fun readSeats() = File("src/main/resources/05-input.txt").readLines(UTF_8).map(::Seat)

    val myRow = readSeats()
        .groupByTo(TreeMap()) { it.row }.values
        .dropWhile { it.size < 8 }
        .find { it.size != 8 } ?: emptyList()

    fun findMySeat(): Seat {
        val availableColumns = myRow.map { it.column }
        val myColumn = (0..7).find { !availableColumns.contains(it) }!!
        return myRow.first().copy(column = myColumn)
    }

    fun findAnswer1v1() = readSeats().map { it.id }.maxOrNull()
    fun findAnswer2v1() = findMySeat().id

    fun solve() {
        println(findAnswer1v1())
        println(findAnswer2v1())
    }
}

fun main() = Challenge05().solve()