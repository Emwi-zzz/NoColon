package org.example.interpreter.parser

import org.example.interpreter.ast.Statement
import org.example.interpreter.log.NCLog
import org.example.interpreter.log.NCLog.invoke
import org.example.interpreter.log.UnknownStatementError

fun NoColonParser.StatementContext.toAst(): Statement {
    return when {
        variableAssignment() != null -> variableAssignment().toAst()
        ifStatement() != null -> ifStatement().toAst()
        whileStatement() != null -> whileStatement().toAst()
        functionDeclaration() != null -> functionDeclaration().toAst()
        returnStatement() != null -> returnStatement().toAst()
        else -> NCLog(UnknownStatementError(text))
    }
}

fun NoColonParser.VariableAssignmentContext.toAst(): Statement.Assignment {
    return Statement.Assignment(
        name = IDENTIFIER().text,
        expression = expression().toAst()
    )
}

fun NoColonParser.IfStatementContext.toAst(): Statement.If {
    val blocks = block()

    return Statement.If(
        condition = expression().toAst(),
        thenBranch = blocks[0].toAst(),
        elseBranch = blocks.getOrNull(1)?.toAst()
    )
}

fun NoColonParser.WhileStatementContext.toAst(): Statement.While {
    return Statement.While(
        condition = expression().toAst(),
        body = block().toAst()
    )
}

fun NoColonParser.FunctionDeclarationContext.toAst(): Statement.FunctionDecl {
    return Statement.FunctionDecl(
        name = IDENTIFIER().text,
        parameters = parameterList()
            ?.parameter()
            ?.map { it.IDENTIFIER().text }
            ?: emptyList(),
        body = block().toAst()
    )
}

fun NoColonParser.ReturnStatementContext.toAst(): Statement.Return {
    return Statement.Return(
        expression = expression().toAst()
    )
}

fun NoColonParser.BlockContext.toAst(): List<Statement> {
    return statement().map { it.toAst() }
}