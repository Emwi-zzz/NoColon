package org.example

import NoColonLexer
import NoColonParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.TokenStream
import org.example.interpreter.environment.Environment
import org.example.interpreter.parser.toAst

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    try {
        val input = generateSequence(::readlnOrNull).joinToString("\n")

        val charStream = CharStreams.fromString(input)

        val lexer = NoColonLexer(charStream)

        val tokens = CommonTokenStream(lexer)

        val parser = NoColonParser(tokens)

        val ast = parser.program().toAst()

        val globalEnvironment = Environment()

        ast.forEach { it.execute(globalEnvironment) }

        val result = globalEnvironment.getAllVariables()

        result.forEach { println("${it.name}: ${it.value}") }
    } catch (e: Exception) {
        println(e.message)
    }

}