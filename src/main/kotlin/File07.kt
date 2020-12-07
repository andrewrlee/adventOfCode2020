import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

private class Challenge07 {

    data class Bag(val description: String, val quantity: Int, val children: List<Bag> = emptyList()) {
        constructor(args: List<String>) : this(args[2], args[1].toInt())

        companion object {
            val fullLineRegex = "^(.*?) bags contain (.*?).$".toRegex()
            val contentRegex = "^(\\d)+ (.*) bag[s]*$".toRegex()
            fun extract(regex: Regex, value: String) = regex.find(value)?.groupValues

            fun extractContents(line: String) =
                line.split(',').mapNotNull { extract(contentRegex, it.trim())?.let(::Bag) }

            fun create(line: String) = extract(fullLineRegex, line)!!.let { Bag(it[1], 1, extractContents(it[2])) }
        }
    }

    val bags = File("src/main/resources/07-input.txt").readLines(UTF_8).map(Bag::create)

    val containingBags: Map<String, List<String>> = bags
        .flatMap { bag -> bag.children.map { Pair(it.description, bag.description) } }
        .groupBy { it.first }
        .mapValues { it.value.map { it.second } }

    fun findContainingBags(bagName: String, seen: Set<String> = emptySet()): Set<String> {
        val containers = containingBags[bagName] ?: return seen
        return containers.flatMap { findContainingBags(it, seen + containers) }.toSet()
    }

    fun findAnswer1v1() = findContainingBags("shiny gold").count()

    val containedBags = bags.groupBy { it.description }.mapValues { it.value[0] }

    fun countContainedBags(bagName: String): Int =
        1 + containedBags[bagName]!!.children.map { it.quantity * countContainedBags(it.description) }.sum()

    fun findAnswer2v1() = countContainedBags("shiny gold") - 1 // don't count the shiny gold bag!

    fun solve() {
        println(findAnswer1v1())
        println(findAnswer2v1())
    }
}

fun main() = Challenge07().solve()