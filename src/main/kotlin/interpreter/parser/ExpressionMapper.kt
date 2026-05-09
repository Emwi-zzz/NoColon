package org.example.interpreter.parser

import org.example.interpreter.ast.Expression
import org.example.interpreter.log.NCLog
import org.example.interpreter.log.*

fun NoColonParser.ExpressionContext.toAst(): Expression {
    return when (this) {
        is NoColonParser.FunctionCallExprContext -> Expression.Call(
            functionName = IDENTIFIER().text,
            arguments = argumentList()
                ?.expression()
                ?.map { it.toAst() }
                ?: emptyList()
        )

        is NoColonParser.PrimaryExprContext -> primary().toAst()

        is NoColonParser.NotExprContext -> Expression.UnaryOp(
            op = NOT().text,
            right = expression().toAst()
        )

        is NoColonParser.MulDivExprContext -> Expression.BinaryOp(
            left = expression(0).toAst(),
            op = when {
                MULT() != null -> MULT().text
                DIV() != null -> DIV().text
                else -> MOD().text
            },
            right = expression(1).toAst()
        )

        is NoColonParser.AddSubExprContext -> Expression.BinaryOp(
            left = expression(0).toAst(),
            op = when {
                PLUS() != null -> PLUS().text
                else -> MINUS().text
            },
            right = expression(1).toAst()
        )

        is NoColonParser.ComparisonExprContext -> Expression.BinaryOp(
            left = expression(0).toAst(),
            op = when {
                LT() != null -> LT().text
                GT() != null -> GT().text
                LE() != null -> LE().text
                else -> GE().text
            },
            right = expression(1).toAst()
        )

        is NoColonParser.EqualityExprContext -> Expression.BinaryOp(
            left = expression(0).toAst(),
            op = when {
                EQ() != null -> EQ().text
                else -> NEQ().text
            },
            right = expression(1).toAst()
        )

        is NoColonParser.AndExprContext -> Expression.BinaryOp(
            left = expression(0).toAst(),
            op = AND().text,
            right = expression(1).toAst()
        )

        is NoColonParser.OrExprContext -> Expression.BinaryOp(
            left = expression(0).toAst(),
            op = OR().text,
            right = expression(1).toAst()
        )

        else -> NCLog(UnknownExpressionError(text))
    }
}

fun NoColonParser.PrimaryContext.toAst(): Expression {
    return when (this) {
        is NoColonParser.ParenExprContext -> expression().toAst()

        is NoColonParser.IntLiteralContext -> Expression.Literal(
            value = INTEGER_LITERAL().text.toInt()
        )

        is NoColonParser.TrueLiteralContext -> Expression.Literal(1)

        is NoColonParser.FalseLiteralContext -> Expression.Literal(0)

        is NoColonParser.IdentifierExprContext -> Expression.Variable(
            name = IDENTIFIER().text
        )

        else -> NCLog(UnknownPrimaryExpressionError(text))
    }
}

