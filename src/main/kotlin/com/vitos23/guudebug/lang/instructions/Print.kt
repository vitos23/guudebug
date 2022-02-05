package com.vitos23.guudebug.lang.instructions

import com.vitos23.guudebug.runtime.Environment
import com.vitos23.guudebug.lang.Instruction

class Print(line: Int, val varName: String) : Instruction(line) {

    override fun execute(env: Environment) {
        env.println(env.getVar(varName).toString())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Print

        return line == other.line && varName == other.varName
    }

    override fun toString(): String = "$line: print $varName"

}