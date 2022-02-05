package com.vitos23.guudebug.lang.instructions

import com.vitos23.guudebug.runtime.Environment
import com.vitos23.guudebug.lang.Instruction

class Call(line: Int, val procName: String) : Instruction(line) {

    override fun execute(env: Environment) {
        env.runProcedure(procName)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Call

        return line == other.line && procName == other.procName
    }

    override fun toString(): String = "$line: call $procName"

}