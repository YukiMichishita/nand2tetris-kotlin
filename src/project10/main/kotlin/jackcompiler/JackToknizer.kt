package jackcompiler

import java.io.File
import java.lang.Exception
import java.util.*

val whiteSpaces = Regex("""\s*""")

enum class Token(val type: String, val regex: Regex) {
    KEYWORD(
        "keyword",
        Regex("class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return")
    ),
    SYMBOL("symbol", Regex("""}|\(|\)|\[|\]|\.|,|;|\+|-|\*|/|&|\||<|>|=|~""" + "|\\{")),
    INTEGER_CONSTANT("integerConstant", Regex("""\d+""")),
    STRING_CONSTANT("stringConstant", Regex("\"[^\\n\"]*\"")),
    IDENTIFIER("identifier", Regex("""[a-zA-Z_]\w*"""));

    fun matches(source: String): String? {
        return regex.matchAt(source, 0)?.value
    }
}

class JackToknizer(_path: String) {

    private var charIndex = 0
    var currentToken = ""
    var currentTokenType = ""
    private val sourceCode =
        File(_path).readText().replace(Regex("""/\*.*?\*/|/\*\*.*?\*/|//.*|\r\n|\n|\r"""), "").trimIndent()
    private var currentCode = sourceCode

    fun hasMoreTokens() = sourceCode.length > charIndex

    private fun incrementIndex(delta: Int) {
        charIndex += delta
        currentCode = sourceCode.takeLast(sourceCode.length - charIndex)
    }

    fun advance() {
        whiteSpaces.find(currentCode)?.let { this.incrementIndex(it.value.length) }

        for (token in Token.values()) {
            token.matches(currentCode)?.let {
                currentToken = it
                if (token == Token.STRING_CONSTANT) {
                    currentToken = currentToken.replace("\"", "")
                }
                currentTokenType = token.type
                this.incrementIndex(it.length)
                return
            }
        }
        throw Exception("Syntax Error at: $charIndex, $currentCode")
    }
}