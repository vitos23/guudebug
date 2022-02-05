package com.vitos23.guudebug.runtime

interface Environment {

    fun print(s: String)

    fun println(s: String)

    fun setVar(varName: String, value: Int)

    fun getVar(varName: String): Int

    fun getVariables(): List<Variable>

    fun runProcedure(procName: String)

    fun getCallStack(): CallStack

    //fun loadProcedure(proc: Procedure)
}