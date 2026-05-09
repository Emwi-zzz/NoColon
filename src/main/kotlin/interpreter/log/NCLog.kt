package org.example.interpreter.log

sealed interface Log

object NCLog {
    operator fun invoke(body: Error): Nothing {
        error(body.message)
    }

    operator fun invoke(body: Log) {
        println(body.toString())
    }
}