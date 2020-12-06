import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

private class Challenge02 {
    data class Policy(val low: Int, val high: Int, val v: Char)

    val pattern = "(\\d+)-(\\d+) (\\w): (\\w+)".toRegex()

    fun readPoliciesAndPasswords() = File("src/main/resources/02-input.txt").readLines(UTF_8)
        .map { pattern.find(it)!!.groupValues }
        .map { Pair(Policy(it[1].toInt(), it[2].toInt(), it[3].toCharArray()[0]), it[4]) }

    fun isValidV1(policy: Policy, password: String): Boolean {
        val (low, high, v) = policy
        val occurrences = password.groupingBy { it }.eachCount()[v] ?: 0
        return occurrences in low..high
    }

    fun isValidV2(policy: Policy, password: String): Boolean {
        val (low, high, v) = policy
        val lowCharMatch = password.length < low || password[low - 1] == v
        val highCharMatch = password.length < high || password[high - 1] == v
        return (lowCharMatch || highCharMatch) && !(lowCharMatch && highCharMatch)
    }

    private fun findAnswer1v1() = readPoliciesAndPasswords().count { (policy, password) -> isValidV1(policy, password) }

    private fun findAnswer2v1() = readPoliciesAndPasswords().count { (policy, password) -> isValidV2(policy, password) }

    fun solve() {
        println(findAnswer1v1())
        println(findAnswer2v1())
    }
}

fun main() = Challenge02().solve()
