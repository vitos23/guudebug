package com.vitos23.guudebug.runtime

import com.vitos23.guudebug.lang.Procedure
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CallStackTest {

    @Test
    fun test1() {
        val stack = CallStack()

        assertTrue(stack.isEmpty())
        expectError { stack.pop() }
        expectError { stack.peek() }

        val frame1 = CallFrame(3, Procedure(1, "main", listOf()), 3)
        val frame2 = CallFrame(71, Procedure(40, "foo", listOf()), 160)

        stack.push(frame1)

        assertFalse(stack.isEmpty())
        assertEquals(1, stack.size())
        assertEquals(frame1, stack.peek())

        stack.push(frame2)

        assertFalse(stack.isEmpty())
        assertEquals(2, stack.size())
        assertEquals(frame2, stack.peek())
        assertNotEquals(frame1, stack.peek())

        var s = 0
        stack.forEach { s += it.instructionIndex }
        assertEquals(74, s)

        assertEquals(frame2, stack.pop())
        assertEquals(frame1, stack.pop())
        assertTrue(stack.isEmpty())
        assertEquals(0, stack.size())
    }

    private fun expectError(action: () -> Unit) {
        try {
            action()
            org.junit.jupiter.api.fail("Expected Exception!")
        } catch (e: Exception) {
            println("An expected error: ${e.message}")
        }
    }
}