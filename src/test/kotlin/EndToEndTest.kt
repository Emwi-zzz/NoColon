import org.example.main
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EndToEndTest {

    private fun runProgram(source: String): String {
        val originalIn = System.`in`
        val originalOut = System.out

        val input = ByteArrayInputStream(source.toByteArray())
        val output = ByteArrayOutputStream()

        try {
            System.setIn(input)
            System.setOut(PrintStream(output))

            main()

            return output.toString().trim()
        } finally {
            System.setIn(originalIn)
            System.setOut(originalOut)
        }
    }

    @Test
    fun `executes arithmetic assignments end to end`() {
        val source = """
            x = 1 + 2 * 3
            y = x - 4
            z = y / 3
            r = y % 3
        """.trimIndent()

        val output = runProgram(source)

        assertEquals(
            """
            x: 7
            y: 3
            z: 1
            r: 0
            """.trimIndent(),
            output
        )
    }

    @Test
    fun `executes if true branch end to end`() {
        val source = """
            x = 10
            if x > 5 then y = 1 else y = 2
        """.trimIndent()

        val output = runProgram(source)

        assertEquals(
            """
            x: 10
            y: 1
            """.trimIndent(),
            output
        )
    }

    @Test
    fun `executes if false branch end to end`() {
        val source = """
            x = 3
            if x > 5 then y = 1 else y = 2
        """.trimIndent()

        val output = runProgram(source)

        assertEquals(
            """
            x: 3
            y: 2
            """.trimIndent(),
            output
        )
    }

    @Test
    fun `executes while loop end to end`() {
        val source = """
            x = 3
            total = 0
            while x > 0 do {
                total = total + x
                x = x - 1
            }
        """.trimIndent()

        val output = runProgram(source)

        assertEquals(
            """
            x: 0
            total: 6
            """.trimIndent(),
            output
        )
    }

    @Test
    fun `executes function call with return value end to end`() {
        val source = """
            fun add(a, b) {
                return a + b
            }

            result = add(20, 22)
        """.trimIndent()

        val output = runProgram(source)

        assertEquals(
            """
            result: 42
            """.trimIndent(),
            output
        )
    }

    @Test
    fun `executes recursive function end to end`() {
        val source = """
            fun fact(n) {
                if n <= 1 then return 1 else return n * fact(n - 1)
            }

            result = fact(5)
        """.trimIndent()

        val output = runProgram(source)

        assertEquals(
            """
            result: 120
            """.trimIndent(),
            output
        )
    }

    @Test
    fun `executes boolean and comparison expressions end to end`() {
        val source = """
            a = true && !false
            b = 10 == 10
            c = 10 != 20
            d = 5 >= 5
            e = false || true
        """.trimIndent()

        val output = runProgram(source)

        assertEquals(
            """
            a: 1
            b: 1
            c: 1
            d: 1
            e: 1
            """.trimIndent(),
            output
        )
    }

    @Test
    fun `ignores line and block comments end to end`() {
        val source = """
            // this is ignored
            x = 1

            /* this is also ignored */
            y = x + 2
        """.trimIndent()

        val output = runProgram(source)

        assertEquals(
            """
            x: 1
            y: 3
            """.trimIndent(),
            output
        )
    }

    @Test
    fun `function local variables are not printed as globals`() {
        val source = """
            fun makeValue() {
                local = 99
                return local
            }

            result = makeValue()
        """.trimIndent()

        val output = runProgram(source)

        assertEquals(
            """
            result: 99
            """.trimIndent(),
            output
        )

        assertTrue("local:" !in output)
    }
}