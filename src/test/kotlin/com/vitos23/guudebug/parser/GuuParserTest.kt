package com.vitos23.guudebug.parser

import com.vitos23.guudebug.lang.Procedure
import com.vitos23.guudebug.lang.instructions.Call
import com.vitos23.guudebug.lang.instructions.Print
import com.vitos23.guudebug.lang.instructions.Set
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

internal class GuuParserTest {

    @Test
    fun test1() {
        val source = """
                sub    main
            set a 1
             print a
             
               set b -2  
            print b
                call func
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val expected = listOf(
            Procedure(
                1, "main", listOf(
                    Set(2, "a", 1),
                    Print(3, "a"),
                    Set(5, "b", -2),
                    Print(6, "b"),
                    Call(7, "func")
                )
            )
        )
        assertEquals(expected, prog)
    }

    @Test
    fun test2() {
        expectError("")
        expectError("func main")
        expectError("print b 1")
        expectError("print")
        expectError("set")
        expectError("set a")
        expectError("set a 2 sth")
        expectError("set a 10000000000")
        expectError("call")
        expectError("call b 2")
        expectError("sub")
        expectError("sub main 1234")
        expectError("sub main\nsub main")
    }

    @Test
    fun test3() {
        val source = """
            sub main
                set a 1
                call foo
                print a

            sub foo
                set a 2
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val expected = listOf(
            Procedure(
                1, "main", listOf(
                    Set(2, "a", 1),
                    Call(3, "foo"),
                    Print(4, "a")
                )
            ),
            Procedure(
                6, "foo", listOf(
                    Set(7, "a", 2)
                )
            )
        )
        assertEquals(expected, prog)
    }

    @Test
    fun test4() {
        val source = """
            sub foo
            set a 1
            print a
            
            sub main
                set a 1
                call foo
                print a
        """.trimIndent()

        val prog = GuuParser(source).parse()
        val expected = listOf(
            Procedure(1, "foo", listOf(
                Set(2, "a", 1),
                Print(3, "a")
            )),
            Procedure(
                5, "main", listOf(
                    Set(6, "a", 1),
                    Call(7, "foo"),
                    Print(8, "a")
                )
            ),
        )
        assertEquals(expected, prog)
    }

    private fun expectError(source: String) {
        try {
            GuuParser(source).parse()
            fail("Expected ParseException")
        } catch (e: ParseException) {
            println("An expected error: ${e.message}")
        }
    }
}