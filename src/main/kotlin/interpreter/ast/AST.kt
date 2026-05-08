package org.example.interpreter.ast


sealed interface Node

sealed class Expression : Node {
    data class Literal(val value: Any) : Expression()

    data class Variable(val name: String) : Expression()

    data class UnaryOp(val op: String, val right: Expression) : Expression()

    data class BinaryOp(val left: Expression, val op: String, val right: Expression) : Expression()

    data class Call(val functionName: String, val arguments: List<Expression>) : Expression()
}

sealed class Statement : Node {
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

    data class ExpressionStatement(val expression: Expression) : Statement()
}