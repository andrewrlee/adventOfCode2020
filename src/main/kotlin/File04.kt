import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

typealias Passport = Map<String, String>

private class Challenge04 {
    val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid") // "cid"

    fun rawPasswordData() = File("src/main/resources/04-input.txt").readText(UTF_8).split("\n\n")

    fun readPassport(raw: String): Passport = raw
        .splitToSequence(' ', '\n')
        .map { it.split(':').let { Pair(it[0], it[1]) } }
        .filter { (key, _) -> requiredFields.contains(key) }.toMap()

    fun Passport.yearIn(field: String, range: IntRange) = this[field]!!.toInt() in range
    fun Passport.fieldMatches(field: String, regex: String) = this[field]!!.matches(regex.toRegex())
    fun Passport.isAllPresent(): Boolean = this.size == requiredFields.size

    fun Passport.heightIsValid(): Boolean {
        val (_, measure, type) = "^(\\d+)(cm|in)$".toRegex().find(this["hgt"]!!)?.groupValues ?: return false
        val range = if (type == "cm") 150..193 else 59..76
        return measure.toInt() in range
    }

    fun findAnswer1v1() = rawPasswordData().map(this::readPassport).filter { it.isAllPresent() }.count()

    fun findAnswer2v1() = rawPasswordData().map(this::readPassport)
        .filter {
            it.isAllPresent()
                    && it.yearIn("byr", 1920..2002)
                    && it.yearIn("iyr", 2010..2020)
                    && it.yearIn("eyr", 2020..2030)
                    && it.heightIsValid()
                    && it.fieldMatches("hcl", "^#[0-9a-f]{6}$")
                    && it.fieldMatches("ecl", "^amb|blu|brn|gry|grn|hzl|oth$")
                    && it.fieldMatches("pid", "^[0-9]{9}$")
        }
        .count()

    fun solve() {
        println(findAnswer1v1())
        println(findAnswer2v1())
    }
}

fun main() = Challenge04().solve()