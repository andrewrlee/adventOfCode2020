import Challenge08.Operation.*
import Challenge08.Result.Fail
import Challenge08.Result.Success
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

private class Challenge08 {

    enum class Operation { acc, jmp, nop }
    enum class Mode { ALLOW_MODIFY, NO_MODIFY }

    data class Instruction(val index: Int, val op: Operation, val value: Int, val changed: Boolean = false) {
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

    open class Result(open val seen: List<Instruction> = emptyList()) {
        data class Fail(override val seen: List<Instruction> = emptyList()) : Result(seen)
        data class Success(override val seen: List<Instruction> = emptyList()) : Result(seen)
    }

    private fun findModifiedSolution(current: Instruction, seen: List<Instruction>, mode: Mode) =
        if (mode == Mode.NO_MODIFY) Fail() else when (current.op) {
            acc -> Fail()
            jmp -> current.copy(op = nop, changed = true).let { findWorking(it, Mode.NO_MODIFY, seen) }
            nop -> current.copy(op = jmp, changed = true).let { findWorking(it, Mode.NO_MODIFY, seen) }
        }

    fun findWorking(current: Instruction, mode: Mode, seen: List<Instruction> = listOf(current)): Result {
        findModifiedSolution(current, seen, mode).let { if (it is Success) return it }
        if (instructions.size <= current.nextIndex) return Success(seen)
        val next = instructions[current.nextIndex]
        return if (seen.contains(next)) return Fail() else findWorking(next, mode, seen + next)
    }

    fun findAnswer2v1(): Int = findWorking(instructions[0], Mode.ALLOW_MODIFY).seen.map { it.accContr }.sum()

    fun solve() {
        println(findAnswer1v1())
        println(findAnswer2v1())
    }
}

fun main() = Challenge08().solve()