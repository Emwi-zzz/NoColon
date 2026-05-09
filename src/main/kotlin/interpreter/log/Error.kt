package org.example.interpreter.log

sealed class Error: Log{
    abstract val message: String
}

data class UnknownStatementError(val name: String? = null): Error(){
    override val message = "Unknown statement: $name"
}

data class UnknownExpressionError(val name: String? = null): Error(){
    override val message = "Unknown expression: $name"
}

data class UnknownPrimaryExpressionError(val name: String? = null): Error(){
    override val message = "Unknown primary expression: $name"
}