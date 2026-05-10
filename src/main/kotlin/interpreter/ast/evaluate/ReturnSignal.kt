package org.example.interpreter.ast.evaluate

class ReturnSignal(val value: Int) : RuntimeException(null, null, false, false)