package com.vitos23.guudebug.runtime

import com.vitos23.guudebug.lang.Procedure
import com.vitos23.guudebug.parser.GuuParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.collections.HashSet

internal class GuuVMTest {

    @Test
    fun test1() {
        val source = """
            sub main
                set a 1
                print a
                set a 2
                call foo
                print a
                
            sub foo
                print a
                set a 3
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val io = CaptureIO()
        val vm = GuuVM(prog, ioInterface = io)

        assertEquals(2, vm.getNextLine())
        vm.executeNext()
        assertEquals(1, vm.getVar("a"))
        assertTrue(io.trace.isEmpty())

        assertTrue(vm.hasNext())
        vm.executeNext()
        assertEquals(Pair(2, "1"), io.trace.last())

        vm.executeNext()
        vm.executeNext()

        assertEquals(9, vm.getNextLine())
        assertEquals(1, io.trace.size)

        vm.executeNext()
        assertEquals(Pair(2, "2"), io.trace.last())

        vm.executeNext()
        vm.executeNext()

        assertEquals(Pair(2, "3"), io.trace.last())
        assertFalse(vm.hasNext())


        expectError { vm.executeNext() }
    }

    @Test
    fun testNoMain() {
        val source = """
            sub foo
                print a
                set a 3
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val io = CaptureIO()

        expectError { GuuVM(prog, ioInterface = io) }
    }

    @Test
    fun testHeapLimit() {
        val source = """
            sub main
                set a 1
                set b 2
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val io = CaptureIO()
        val vm = GuuVM(prog, 1, 2, io)

        vm.executeNext()

        expectError { vm.executeNext() }
    }

    @Test
    fun testCallStackLimit() {
        val source = """
            sub main
                call foo
            
            sub foo
                call main
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val io = CaptureIO()
        val vm = GuuVM(prog, 1, 2, io)

        vm.executeNext()
        vm.executeNext()

        expectError { vm.executeNext() }
    }

    @Test
    fun testUseOfUndeclaredProcedures() {
        val source = """
            sub main
                set a 1
                call foo
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val io = CaptureIO()
        val vm = GuuVM(prog, ioInterface = io)

        vm.executeNext()

        expectError { vm.executeNext() }
    }

    @Test
    fun testUseOfUndeclaredVars() {
        val source = """
            sub main
                print a
                set a 1
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val io = CaptureIO()
        val vm = GuuVM(prog, ioInterface = io)

        expectError { vm.executeNext() }
    }

    @Test
    fun testExecuteNextUnit() {
        val source = """
            sub main
                set a 1
                call foo
                print a
                call func
                print a
                call foo
                
            sub foo
                print a
                set a 3
            
            sub func
                set a 2
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val io = CaptureIO()
        val vm = GuuVM(prog, ioInterface = io)

        vm.executeNextUnit()
        assertEquals(3, vm.getNextLine())

        vm.executeNextUnit()
        assertEquals(1, io.trace.size)
        assertEquals(Pair(2, "1"), io.trace.last())
        assertEquals(3, vm.getVar("a"))
        assertEquals(4, vm.getNextLine())

        vm.executeNextUnit()
        assertEquals(Pair(2, "3"), io.trace.last())
        assertEquals(5, vm.getNextLine())

        vm.executeNextUnit()
        vm.executeNextUnit() // 6: print a
        assertEquals(3, io.trace.size)
        assertEquals(Pair(2, "2"), io.trace.last())
        assertEquals(7, vm.getNextLine())

        vm.executeNextUnit()
        assertEquals(4, io.trace.size)
        assertEquals(Pair(2, "2"), io.trace.last())
        assertEquals(-1, vm.getNextLine())
        assertFalse(vm.hasNext())
    }

    @Test
    fun testGetVariables() {
        val source = """
            sub main
                set a 1
                call foo
                
            sub foo
                set b 2
                call func
                set d 4
            
            sub func
                set c 3
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val io = CaptureIO()
        val vm = GuuVM(prog, ioInterface = io)

        vm.executeNext()
        vm.executeNext()
        vm.executeNext()

        val expected1 = HashSet(listOf(
            Variable("a", 1),
            Variable("b", 2)
        ))
        assertEquals(expected1, HashSet(vm.getVariables()))

        vm.executeNextUnit()
        vm.executeNext()

        val expected2 = HashSet(listOf(
            Variable("a", 1),
            Variable("b", 2),
            Variable("c", 3),
            Variable("d", 4)
        ))
        assertEquals(expected2, HashSet(vm.getVariables()))
    }

    @Test
    fun testGetCallStack() {
        val source = """
            sub main
                call foo
                call func
                
            sub foo
                call func
            
            sub func
                set a 1
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val io = CaptureIO()
        val vm = GuuVM(prog, ioInterface = io)

        vm.executeNext()

        val expected1 = listOf(
            CallFrame(0, prog[0] as Procedure, 2),
            CallFrame(0, prog[1] as Procedure, 6)
        )
        assertEquals(expected1, vm.getCallStack().asList())

        vm.executeNext()

        val expected2 = listOf(
            CallFrame(0, prog[0] as Procedure, 2),
            CallFrame(0, prog[1] as Procedure, 6),
            CallFrame(0, prog[2] as Procedure, 9)
        )
        assertEquals(expected2, vm.getCallStack().asList())

        vm.executeNext()
        vm.executeNext()

        val expected3 = listOf(
            CallFrame(1, prog[0] as Procedure, 3),
            CallFrame(0, prog[2] as Procedure, 9)
        )
        assertEquals(expected3, vm.getCallStack().asList())

        vm.executeNext()
        assertFalse(vm.hasNext())
    }

    private inline fun expectError(action: () -> Unit) {
        try {
            action()
            fail("Expected GuuRuntimeException")
        } catch(_: GuuRuntimeException) {}
    }
}