package com.vitos23.guudebug.runtime

import com.vitos23.guudebug.lang.Procedure

data class CallFrame(val instructionIndex: Int, val procedure: Procedure, val line: Int)