package org.example.interpreter.parser

import org.example.interpreter.ast.Statement

fun NoColonParser.ProgramContext.toAst(): List<Statement>{
    return statement().map { it.toAst() }
}