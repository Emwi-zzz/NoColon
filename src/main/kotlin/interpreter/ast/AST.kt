package org.example.interpreter.ast

import org.example.interpreter.ast.evaluate.*
import org.example.interpreter.environment.Environment


sealed interface Node

sealed class Expression : Node {
    fun evaluate(env: Environment): Int {
        return ExpressionHandler.evaluate(this, env)
    }
    data class Literal(val value: Int) : Expression()

    data class Variable(val name: String) : Expression()

    data class UnaryOp(val op: String, val right: Expression) : Expression()

    data class BinaryOp(val left: Expression, val op: String, val right: Expression) : Expression()

    data class Call(val functionName: String, val arguments: List<Expression>) : Expression()
}

sealed class Statement : Node {
    fun execute(env: Environment) {
        StatementHandler.execute(this, env)
    }
    data class Assignment(val name: String, val expression: Expression) : Statement()

    data class If(
        val condition: Expression,
        val thenBranch: List<Statement>,
        val elseBranch: List<Statement>?
    ) : Statement()

    data class While(val condition: Expression, val body: List<Statement>) : Statement()

    data class FunctionDecl(
        val name: String,
        val parameters: List<String>,
        val body: List<Statement>
    ) : Statement()

    data class Return(val expression: Expression) : Statement()
}