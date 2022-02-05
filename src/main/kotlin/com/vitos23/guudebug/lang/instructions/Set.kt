package com.vitos23.guudebug.lang.instructions

import com.vitos23.guudebug.runtime.Environment
import com.vitos23.guudebug.lang.Instruction

class Set(line: Int, val varName: String, val value: Int) : Instruction(line) {
    override fun execute(env: Environment) {
        env.setVar(varName, value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Set

        return line == other.line && varName == other.varName && value == other.value
    }

    override fun toString(): String = "$line: set $varName $value"
}