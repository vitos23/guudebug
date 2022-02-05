package com.vitos23.guudebug.runtime

internal class CaptureIO : IOInterface {

    // first: 1 - print, 2 - println
    val trace = ArrayList<Pair<Int, String>>()

    override fun print(s: String) {
        trace.add(Pair(1, s))
    }

    override fun println(s: String) {
        trace.add(Pair(2, s))
    }
}