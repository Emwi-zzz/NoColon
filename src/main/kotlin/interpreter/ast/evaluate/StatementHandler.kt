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
            is Statement.Block -> executeBlock(statement, environment)
        }
    }
}


fun StatementHandler.executeIf(statement: Statement.If, environment: Environment) {
    if(ExpressionHandler.evaluate(statement.condition, environment) != 0){
        statement.thenBranch.forEach { execute(it, environment) }
    } else {
        statement.elseBranch?.forEach { execute(it, environment) }
    }
}

fun StatementHandler.executeWhile(statement: Statement.While, environment: Environment) {
    while(ExpressionHandler.evaluate(statement.condition, environment) != 0){
        statement.body.forEach { execute(it, environment) }
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

fun StatementHandler.executeBlock(statement: Statement.Block, environment: Environment) {
    statement.statements.forEach { execute(it, environment) }
}