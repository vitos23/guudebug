package com.vitos23.guudebug.runtime

import java.util.*

class CallStack(private val stack: Stack<CallFrame> = Stack<CallFrame>()) {

    fun push(callFrame: CallFrame) {
        stack.push(callFrame)
    }

    fun pop(): CallFrame = stack.pop()

    fun peek(): CallFrame = stack.peek()

    fun isEmpty(): Boolean = stack.empty()

    fun size(): Int = stack.size

    fun forEach(action: (CallFrame) -> Unit) {
        for (frame in stack) {
            action(frame)
        }
    }

    fun print() {
        forEach { frame -> println(frame) }
    }

    fun copy(): CallStack = CallStack(stack.clone() as Stack<CallFrame>)

    fun asList(): List<CallFrame> = stack.toList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return (other as CallStack).stack == stack
    }
}
