import Challenge08.Operation.*
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

private class Challenge08 {

    enum class Operation { acc, jmp, nop }
    enum class Mode { ALLOW_MODIFY, NO_MODIFY }

    data class Instruction(val index: Int, val op: Operation, val value: Int) {
        constructor(index: Int, line: String) : this(
            index,
            valueOf(line.substring(0, 3)),
            line.substring(3).trim().toInt()
        )
        val accContr: Int get() = if (op == acc) value else 0
        val nextIndex: Int get() = if (op == jmp) index + value else index + 1
    }

    val instructions =
        File("src/main/resources/08-input.txt").readLines(UTF_8).withIndex().map { Instruction(it.index, it.value) }

    fun findBeforeLoop(current: Instruction, seen: List<Instruction> = listOf(current)): List<Instruction> {
        if (instructions.size < current.nextIndex) return seen
        val next = instructions[current.nextIndex]
        return if (seen.contains(next)) seen else findBeforeLoop(next, seen + next)
    }

    fun findAnswer1v1(): Int = findBeforeLoop(instructions[0]).map { it.accContr }.sum()

    private fun findModifiedSolution(current: Instruction, seen: List<Instruction>, mode: Mode) =
        if (mode == Mode.NO_MODIFY) null else when (current.op) {
            acc -> null
            jmp -> current.copy(op = nop).let { findWorking(it, Mode.NO_MODIFY, seen) }
            nop -> current.copy(op = jmp).let { findWorking(it, Mode.NO_MODIFY, seen) }
        }

    fun findWorking(current: Instruction, mode: Mode, seen: List<Instruction> = listOf(current)): List<Instruction>? {
        findModifiedSolution(current, seen, mode)?.let { return it }
        if (instructions.size <= current.nextIndex) return seen
        val next = instructions[current.nextIndex]
        return if (seen.contains(next)) return null else findWorking(next, mode, seen + next)
    }

    fun findAnswer2v1(): Int = findWorking(instructions[0], Mode.ALLOW_MODIFY)?.map { it.accContr }?.sum() ?: 0

    fun solve() {
        println(findAnswer1v1())
        println(findAnswer2v1())
    }
}

fun main() = Challenge08().solve()