package de.mknblch.audiolog.common

data class ConfigLine(
    val regex: Regex,
    val soundFile: String?,
    val args: Map<String, String>,
)

class AudioConfigParser {

    companion object {

        private val audioPartRegex = Regex("([^\\[]+)\\[([^]]+)]")

        private val skip = listOf("IGNORE", "SKIP")

        fun parseLine(line: String): ConfigLine? {
            // skip empty lines
            if (line.isBlank()) return null
            // skip comments
            if (line.trim().startsWith("#")) return null
            // separator
            val indexOfSeparator = line.indexOfFirst { it == ':' }
            // check format
            if (indexOfSeparator == -1) return null
            //
            val head = line.substring(0, indexOfSeparator)
            val regex = Regex(line.substring(indexOfSeparator + 1))
            //
            val result = audioPartRegex.matchEntire(head)
            // skip empty soundfiles
            val soundFile = when {
                skip.contains(head) -> null // skip NONE, EMPTY..
                result != null -> result.groupValues[1] // file in group 1
                else -> head // just sound file, no args
            }

            if (result == null || result.groupValues.size < 2) {
                return ConfigLine(regex, soundFile, emptyMap())
            }
            return ConfigLine(regex, soundFile, parseArguments(result.groupValues[2]))
        }

        /*
         * parse: sounds/Alert.wav[volume=100, delay=100]
         */
        private fun parseArguments(line: String): Map<String, String> = line.trim {
            when (it) {
                ' ' -> true
                '[' -> true
                ']' -> true
                else -> false
            }
        }.split(',', ';').mapNotNull { args ->
            val trim = args.trim()
            val elements = trim.split('=')
            if (elements.size != 2) return@mapNotNull Pair(trim, "true") // parse flags as boolean
            elements.let { arg ->
                Pair(arg[0], arg[1])
            }
        }.toMap()
    }
}