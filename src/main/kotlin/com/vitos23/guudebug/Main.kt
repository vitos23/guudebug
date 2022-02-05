package com.vitos23.guudebug

import com.vitos23.guudebug.debug.CmdInterface

fun main(args: Array<String>) {
    try {
        CmdInterface(args).run()
    } catch (_: GuuException) {}
}