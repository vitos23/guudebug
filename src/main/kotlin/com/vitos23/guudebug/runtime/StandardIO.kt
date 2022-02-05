package com.vitos23.guudebug.runtime

class StandardIO : IOInterface {

    override fun print(s: String) {
        kotlin.io.print(s)
    }

    override fun println(s: String) {
        kotlin.io.println(s)
    }
}