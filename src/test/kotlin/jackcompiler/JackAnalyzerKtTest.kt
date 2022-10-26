package jackcompiler

import org.junit.jupiter.api.Test

import java.io.File
import org.junit.jupiter.api.Assertions.*

internal class JackAnalyzerKtTest {


    @Test
    fun testMain() {
        val expected = File("src/Project10/ArrayTest/MainT.xml").readText()
        assertEquals(expected, main(arrayOf("src/Project10/ArrayTest/Main.jack")))
    }
}