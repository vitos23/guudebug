package com.vitos23.guudebug.lang

import com.vitos23.guudebug.runtime.Environment

abstract class Instruction(line: Int) : Statement(line) {

    abstract fun execute(env: Environment)
}