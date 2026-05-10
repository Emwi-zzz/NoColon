package org.example.interpreter.ast.evaluate

import org.example.interpreter.ast.Statement
import org.example.interpreter.environment.Environment

object StatementHandler {
    fun execute(statement: Statement, environment: Environment) {
        when (statement) {
            is Statement.If -> executeIf(statement, environment)
            is Statement.While -> executeWhile(statement, environment)
            is Statement.Return -> executeReturn(statement, environment)
            is Statement.Assignment -> executeAssignment(statement, environment)
            is Statement.FunctionDecl -> executeFunctionDecl(statement, environment)
        }
    }
}

fun StatementHandler.executeIf(statement: Statement.If, environment: Environment) {
    if(ExpressionHandler.evaluate(statement.condition, environment) != 0){
        val localEnv = Environment(environment)
        statement.thenBranch.forEach { execute(it, localEnv) }
    } else {
        val localEnv = Environment(environment)
        statement.elseBranch?.forEach { execute(it, localEnv) }
    }
}

fun StatementHandler.executeWhile(statement: Statement.While, environment: Environment) {
    while(ExpressionHandler.evaluate(statement.condition, environment) != 0){
        val localEnv = Environment(environment)
        statement.body.forEach { execute(it, localEnv) }
    }
}

fun StatementHandler.executeReturn(statement: Statement.Return, environment: Environment) {
    val result = ExpressionHandler.evaluate(statement.expression, environment)
    throw ReturnSignal(result)
}

fun StatementHandler.executeAssignment(statement: Statement.Assignment, environment: Environment) {
    environment.setVariable(statement.name, ExpressionHandler.evaluate(statement.expression, environment))
}

fun StatementHandler.executeFunctionDecl(statement: Statement.FunctionDecl, environment: Environment) {
    environment.declareFunction(statement)
}