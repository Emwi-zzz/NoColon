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

data class UnknownFunctionError(val name: String? = null): Error(){
    override val message = "Unknown function: $name"
}

data class UnknownVariableError(val name: String? = null): Error(){
    override val message = "Unknown variable: $name"
}

data class IllegalFunctionOverriding(val name: String? = null): Error(){
    override val message = "Illegal function overriding: $name"
}

data class UnknownUnaryOperatorError(val name: String? = null): Error(){
    override val message = "Unknown unary operator: $name"
}

data class UnknownBinaryOperatorError(val name: String? = null): Error(){
    override val message = "Unknown binary operator: $name"
}