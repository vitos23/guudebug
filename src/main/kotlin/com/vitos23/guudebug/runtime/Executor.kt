package com.vitos23.guudebug.runtime

interface Executor {
    fun hasNext(): Boolean

    fun executeNext()

    /**
     * Executes next instruction as a whole, e.g. if it's "call foo" procedure "foo" will be executed
     */
    fun executeNextUnit()

    fun executeAll() {
        while (hasNext()) {
            executeNext()
        }
    }

    /**
     * @return the number (from 1) of the line to be executed or -1 if the execution is finished
     */
    fun getNextLine(): Int

    fun getEnv(): Environment
}