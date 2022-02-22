package com.vitos23.guudebug.runtime

class CallStack(private val stack: ArrayList<CallFrame> = ArrayList()) {

    fun push(callFrame: CallFrame) {
        stack.add(callFrame)
    }

    fun pop(): CallFrame = stack.removeLast()

    fun peek(): CallFrame = stack.last()

    fun isEmpty(): Boolean = stack.isEmpty()

    fun size(): Int = stack.size

    fun forEach(action: (CallFrame) -> Unit): Unit = stack.forEach(action)

    fun print(): Unit = forEach { frame -> println(frame) }

    fun copy(): CallStack = CallStack(ArrayList(stack))

    fun asList(): List<CallFrame> = stack.toList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return (other as CallStack).stack == stack
    }

    override fun hashCode(): Int = stack.hashCode()
}
