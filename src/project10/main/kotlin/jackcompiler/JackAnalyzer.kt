package jackcompiler

import java.io.File

fun main(args: Array<String>) {
    val fileName = readln()
    val toknizer = JackToknizer(fileName)

    val xmlify = { token: String, tokenType: String ->
        "<$tokenType> ${
            token.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        } </$tokenType>\n"
    }

    val outputXml = mutableListOf("<tokens>\n")

    while (toknizer.hasMoreTokens()) {
        toknizer.advance()
        val token = toknizer.currentToken
        val tokenType = toknizer.currentTokenType
        outputXml.add(xmlify(token, tokenType))
    }
    outputXml.add("</tokens>")
    val outFile = File(fileName.replace("jack", "xml_"))
    outFile.writeText(outputXml.joinToString(""))
}

