package com.vitos23.guudebug.debug

import com.vitos23.guudebug.runtime.CallStack
import com.vitos23.guudebug.runtime.Variable

interface Debugger {

    fun finished(): Boolean

    fun stepInto()

    fun stepOver()

    fun executeAll() {
        while (!finished()) {
            stepOver()
        }
    }

    fun getStackTrace(): CallStack

    fun getVariables(): List<Variable>

    fun getNextLine(): Int
}