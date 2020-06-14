package nicestring

fun String.isNice(): Boolean {
    val condition = arrayOf(this.first(), this.second(), this.third())
    return condition.filter { it }.size > 1
}

fun String.first(): Boolean {
    val includeString = listOf("bu", "ba", "be")
    return includeString.none { this.contains(it) }
}

fun String.second(): Boolean {
    val vowels = listOf('a', 'e', 'i', 'o', 'u')
    return this.toCharArray().filter { vowels.contains(it) }.size > 2
}

fun String.third(): Boolean {
    return this.toCharArray().asList().zipWithNext().any { (current, next) ->
        current == next
    }
}
