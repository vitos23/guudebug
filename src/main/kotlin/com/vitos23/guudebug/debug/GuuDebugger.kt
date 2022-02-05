package com.vitos23.guudebug.debug

import com.vitos23.guudebug.runtime.CallStack
import com.vitos23.guudebug.runtime.Executor
import com.vitos23.guudebug.runtime.Variable

open class GuuDebugger(private val executor: Executor) : Debugger {

    private val env = executor.getEnv()

    override fun finished(): Boolean = !executor.hasNext()

    override fun stepInto() = executor.executeNext()

    override fun stepOver() = executor.executeNextUnit()

    override fun executeAll() = executor.executeAll()

    override fun getStackTrace(): CallStack = env.getCallStack()

    override fun getVariables(): List<Variable> = env.getVariables()

    override fun getNextLine(): Int = executor.getNextLine()
}