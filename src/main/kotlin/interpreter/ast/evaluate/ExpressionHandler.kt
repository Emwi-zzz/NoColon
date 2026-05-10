package org.example.interpreter.ast.evaluate

import org.example.interpreter.ast.Expression
import org.example.interpreter.environment.Environment
import org.example.interpreter.log.*

object ExpressionHandler {
    fun evaluate(expression: Expression, env: Environment): Int {
        return when (expression) {
            is Expression.Literal -> evaluateLiteral(expression, env)

            is Expression.Variable -> evaluateVariable(expression, env)

            is Expression.UnaryOp -> evaluateUnaryOp(expression, env)

            is Expression.BinaryOp -> evaluateBinaryOp(expression, env)

            is Expression.Call -> evaluateCall(expression, env)
        }
    }

    private fun evaluateLiteral(literal: Expression.Literal, env: Environment): Int = literal.value

    private fun evaluateVariable(variable: Expression.Variable, env: Environment): Int {
        return env.getVariable(variable.name)?: NCLog(UnknownVariableError(variable.name))
    }

    private fun evaluateUnaryOp(unaryOp: Expression.UnaryOp, env: Environment): Int {
        return when(unaryOp.op){
            "!" -> if (evaluate(unaryOp.right, env) == 0) 1 else 0
            else -> NCLog(UnknownUnaryOperatorError(unaryOp.op))
        }
    }

    private fun evaluateBinaryOp(binaryOp: Expression.BinaryOp, env: Environment): Int {
        return when(binaryOp.op){
            "+" -> evaluate(binaryOp.left, env) + evaluate(binaryOp.right, env)
            "-" -> evaluate(binaryOp.left, env) - evaluate(binaryOp.right, env)
            "*" -> evaluate(binaryOp.left, env) * evaluate(binaryOp.right, env)
            "/" -> evaluate(binaryOp.left, env) / evaluate(binaryOp.right, env)
            "%" -> evaluate(binaryOp.left, env) % evaluate(binaryOp.right, env)

            "<" -> if (evaluate(binaryOp.left, env) < evaluate(binaryOp.right, env)) 1 else 0
            ">" -> if (evaluate(binaryOp.left, env) > evaluate(binaryOp.right, env)) 1 else 0
            "<=" -> if (evaluate(binaryOp.left, env) <= evaluate(binaryOp.right, env)) 1 else 0
            ">=" -> if (evaluate(binaryOp.left, env) >= evaluate(binaryOp.right, env)) 1 else 0

            "==" -> if (evaluate(binaryOp.left, env) == evaluate(binaryOp.right, env)) 1 else 0
            "!=" -> if (evaluate(binaryOp.left, env) != evaluate(binaryOp.right, env)) 1 else 0

            "&&" -> if (evaluate(binaryOp.left, env) != 0 && evaluate(binaryOp.right, env) != 0) 1 else 0
            "||" -> if (evaluate(binaryOp.left, env) != 0 || evaluate(binaryOp.right, env) != 0) 1 else 0

            else -> NCLog(UnknownBinaryOperatorError(binaryOp.op))
        }
    }

    private fun evaluateCall(call: Expression.Call, env: Environment): Int {
        val functionDecl = env.getFunction(call.functionName) ?: NCLog(UnknownFunctionError(call.functionName))

        val functionEnv = Environment(env)
        functionDecl.parameters.forEachIndexed { index, parameter ->
            functionEnv.setVariable(parameter, evaluate(call.arguments[index], env))
        }

        return try {
            functionDecl.body.forEach { statement ->
                StatementHandler.execute(statement, functionEnv)
            }
            0
        } catch (signal: ReturnSignal) {
            signal.value
        }
    }
}