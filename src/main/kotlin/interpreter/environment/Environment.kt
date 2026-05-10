package org.example.interpreter.environment

import org.example.interpreter.ast.Statement
import org.example.interpreter.log.IllegalFunctionOverriding
import org.example.interpreter.log.NCLog

data class Variable(val name: String, var value: Int)
data class Environment(val parent: Environment? = null) {

    private val values = mutableMapOf<String, Int>()
    private val functions = mutableMapOf<String, Statement.FunctionDecl>()

    fun getAllVariables(): List<Variable>{
        val variables = mutableListOf<Variable>()
        values.forEach { (name, value) -> variables.add(Variable(name, value)) }
        return variables
    }

    fun getVariable(name: String): Int?{
        var current: Environment? = this
        while(current != null){
            if(current.values.containsKey(name)) return current.values[name]
            current = current.parent
        }
        return null
    }

    fun setVariable(name: String, value: Int) {
        var current: Environment? = this
        while(current != null){
            if(current.values.containsKey(name)){
                current.values[name] = value
                return
            }
            current = current.parent
        }
        this.values[name] = value
    }

    fun declareFunction(function: Statement.FunctionDecl) {
        if(getFunction(function.name) != null) NCLog(IllegalFunctionOverriding(function.name))
        functions[function.name] = function
    }

    fun getFunction(name: String): Statement.FunctionDecl?{
        var current: Environment? = this
        while(current != null){
            if(current.functions.containsKey(name)) return current.functions[name]
            current = current.parent
        }
        return null
    }
}