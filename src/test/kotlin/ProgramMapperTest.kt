import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.example.interpreter.ast.Expression
import org.example.interpreter.ast.Statement
import org.example.interpreter.parser.toAst
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ProgramMapperTest {

    private fun parseProgram(source: String): NoColonParser.ProgramContext {
        val lexer = NoColonLexer(CharStreams.fromString(source))
        val parser = NoColonParser(CommonTokenStream(lexer))

        val errorListener = object : BaseErrorListener() {
            override fun syntaxError(
                recognizer: Recognizer<*, *>?,
                offendingSymbol: Any?,
                line: Int,
                charPositionInLine: Int,
                msg: String?,
                e: RecognitionException?
            ) {
                fail("Syntax error at $line:$charPositionInLine: $msg")
            }
        }

        lexer.removeErrorListeners()
        parser.removeErrorListeners()
        lexer.addErrorListener(errorListener)
        parser.addErrorListener(errorListener)

        return parser.program()
    }

    private fun astOf(source: String): List<Statement> {
        return parseProgram(source).toAst()
    }

    @Test
    fun `empty program maps to empty AST`() {
        val source = ""

        val expected = emptyList<Statement>()

        assertEquals(expected, astOf(source))
    }

    @Test
    fun `assignment with arithmetic expression maps to AST`() {
        val source = """
            x = 1 + 2 * 3
        """.trimIndent()

        val expected = listOf(
            Statement.Assignment(
                name = "x",
                expression = Expression.BinaryOp(
                    left = Expression.Literal(1),
                    op = "+",
                    right = Expression.BinaryOp(
                        left = Expression.Literal(2),
                        op = "*",
                        right = Expression.Literal(3)
                    )
                )
            )
        )

        assertEquals(expected, astOf(source))
    }

    @Test
    fun `if else statement maps to AST`() {
        val source = """
            if x < 10 then y = 1, return y else y = 2
        """.trimIndent()

        val expected = listOf(
            Statement.If(
                condition = Expression.BinaryOp(
                    left = Expression.Variable("x"),
                    op = "<",
                    right = Expression.Literal(10)
                ),
                thenBranch = listOf(
                    Statement.Assignment(
                        name = "y",
                        expression = Expression.Literal(1)
                    ),
                    Statement.Return(
                        expression = Expression.Variable("y")
                    )
                ),
                elseBranch = listOf(
                    Statement.Assignment(
                        name = "y",
                        expression = Expression.Literal(2)
                    )
                )
            )
        )

        assertEquals(expected, astOf(source))
    }

    @Test
    fun `while statement maps to AST`() {
        val source = """
            while x > 0 do x = x - 1
        """.trimIndent()

        val expected = listOf(
            Statement.While(
                condition = Expression.BinaryOp(
                    left = Expression.Variable("x"),
                    op = ">",
                    right = Expression.Literal(0)
                ),
                body = listOf(
                    Statement.Assignment(
                        name = "x",
                        expression = Expression.BinaryOp(
                            left = Expression.Variable("x"),
                            op = "-",
                            right = Expression.Literal(1)
                        )
                    )
                )
            )
        )

        assertEquals(expected, astOf(source))
    }

    @Test
    fun `function declaration maps to AST`() {
        val source = """
            fun add(a, b) { result = a + b, return result }
        """.trimIndent()

        val expected = listOf(
            Statement.FunctionDecl(
                name = "add",
                parameters = listOf("a", "b"),
                body = listOf(
                    Statement.Assignment(
                        name = "result",
                        expression = Expression.BinaryOp(
                            left = Expression.Variable("a"),
                            op = "+",
                            right = Expression.Variable("b")
                        )
                    ),
                    Statement.Return(
                        expression = Expression.Variable("result")
                    )
                )
            )
        )

        assertEquals(expected, astOf(source))
    }

    @Test
    fun `function call expression maps to AST`() {
        val source = """
            result = add(1, x)
        """.trimIndent()

        val expected = listOf(
            Statement.Assignment(
                name = "result",
                expression = Expression.Call(
                    functionName = "add",
                    arguments = listOf(
                        Expression.Literal(1),
                        Expression.Variable("x")
                    )
                )
            )
        )

        assertEquals(expected, astOf(source))
    }

    @Test
    fun `boolean and logical expressions map to AST`() {
        val source = """
            valid = true && !false
        """.trimIndent()

        val expected = listOf(
            Statement.Assignment(
                name = "valid",
                expression = Expression.BinaryOp(
                    left = Expression.Literal(1),
                    op = "&&",
                    right = Expression.UnaryOp(
                        op = "!",
                        right = Expression.Literal(0)
                    )
                )
            )
        )

        assertEquals(expected, astOf(source))
    }

    @Test
    fun `multiple top level statements map to AST in order`() {
        val source = """
            x = 1
            y = x + 2
            return y
        """.trimIndent()

        val expected = listOf(
            Statement.Assignment(
                name = "x",
                expression = Expression.Literal(1)
            ),
            Statement.Assignment(
                name = "y",
                expression = Expression.BinaryOp(
                    left = Expression.Variable("x"),
                    op = "+",
                    right = Expression.Literal(2)
                )
            ),
            Statement.Return(
                expression = Expression.Variable("y")
            )
        )

        assertEquals(expected, astOf(source))
    }
}