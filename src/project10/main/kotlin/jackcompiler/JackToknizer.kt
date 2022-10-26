package jackcompiler

import java.io.File
import java.lang.Exception
import java.util.*

val whiteSpaces = Regex("""\s*""")

enum class Token(val regex: Regex) {
    KEYWORD(Regex("class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return")),
    SYMBOL(Regex("""}|\(|\)|\[|\]|\.|,|;|\+|-|\*|/|&|\||<|>|=|~""" + "|\\{")),
    INTEGER_CONSTANT(Regex("""\d+""")),
    STRING_CONSTANT(Regex("\"[^\\n\"]*\"")),
    IDENTIFIER(Regex("""[a-zA-Z_]\w*"""))
}


class JackToknizer {

    private var charIndex = 0
    var currentToken = ""
    var currentTokenType = ""
    private var sourceCode: String = ""
    private var currentCode: String = ""

    constructor(_path: String) {
        sourceCode = File(_path).readText().replace(Regex("""/\*.*?\*/|/\*\*.*?\*/|//.*|\r\n|\n|\r"""), "").trimIndent()
        currentCode = sourceCode
    }

    fun hasMoreTokens() = sourceCode.length > charIndex

    private fun incrementIndex(delta: Int) {
        charIndex += delta
        currentCode = sourceCode.takeLast(sourceCode.length - charIndex)
    }

    fun advance() {
        whiteSpaces.find(currentCode)?.let { this.incrementIndex(it.value.length) }
        println(currentCode)
        for (token in Token.values()) {
            token.regex.matchAt(currentCode, 0)?.let {
                println("${it.value} $token")
                currentToken = it.value
                currentTokenType = token.name.lowercase(Locale.getDefault())
                this.incrementIndex(it.value.length)
                return
            }
        }
        throw Exception("Syntax Error at: $charIndex, $currentCode")
    }
}